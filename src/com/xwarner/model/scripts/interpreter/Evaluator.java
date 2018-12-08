package com.xwarner.model.scripts.interpreter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Stack;

import com.xwarner.model.scripts.functions.Function;
import com.xwarner.model.scripts.operators.AddOperator;
import com.xwarner.model.scripts.operators.DivideOperator;
import com.xwarner.model.scripts.operators.LeftBracketOperator;
import com.xwarner.model.scripts.operators.MultiplyOperator;
import com.xwarner.model.scripts.operators.Operator;
import com.xwarner.model.scripts.operators.PowerOperator;
import com.xwarner.model.scripts.operators.RightBracketOperator;
import com.xwarner.model.scripts.operators.SubtractOperator;
import com.xwarner.model.scripts.parser.Node;

/**
 * Holds common methods for evaluating parts of the AST
 * 
 * @author max
 *
 */
public class Evaluator {

	private HashMap<String, Operator> operators = new HashMap<String, Operator>();

	public Evaluator() {
		operators.put("+", new AddOperator());
		operators.put("-", new SubtractOperator());
		operators.put("/", new DivideOperator());
		operators.put("*", new MultiplyOperator());
		operators.put("^", new PowerOperator());
		operators.put("(", new LeftBracketOperator());
		operators.put(")", new RightBracketOperator());
	}

	public void parseDeclaration(Node node, Context context) {
		if (node.type != Node.DECLARATION)
			throw new Error("parseDeclaration cannot parse this node (" + node.type + "," + node.value + ")");
		context.setVariable(node.value, evaluateMaths(node, context));
	}

	protected double evaluateMaths(Node node, Context context) {
		// recursively evaluate all function invocations
		evaluateInvocations(node, context);
		// replace all the variables with the number
		replaceVariables(node, context);
		// so now everything should just be numbers and operators
		StringBuilder str = new StringBuilder();
		for (Node n : node.getChildren()) {
			str.append(n.value);
			str.append(" ");
		}
		String src = str.toString();
		src = str.substring(0, src.length() - 1);
		// System.out.println(src);
		// now evaluate this string
		return evaluate(rpn(src));
	}

	private void evaluateInvocations(Node node, Context context) {
		for (Node n : node.getChildren()) {
			if (n.type == Node.INVOCATION) {
				Function function = context.getFunction(n.value);
				// System.out.println("invoking " + n.value);
				Context localContext = new Context();
				ArrayList<Node> children = n.getChildren();
				for (int i = 0; i < children.size(); i++) {
					// evaluate the arguments
					localContext.setVariable(function.arguments.get(i), evaluateMaths(children.get(i), context));
				}
				double value = function.evaluate(localContext);
				n.type = Node.NUMBER;
				n.value = Double.toString(value);
				n.clearChildren();
			} else {
				evaluateInvocations(n, context);
			}
		}
	}

	private void replaceVariables(Node node, Context context) {
		// System.out.println("*** c: " + context.getVariable("c"));
		for (Node n : node.getChildren()) {
			if (n.type == Node.VARIABLE) {
				n.type = Node.NUMBER;
				n.value = Double.toString(context.getVariable(n.value));
			}
		}
	}

	private String rpn(String str) {
		LinkedList<String> output = new LinkedList<String>();
		Stack<Operator> operatorStack = new Stack<Operator>();

		String[] split = str.split(" ");

		for (int i = 0; i < split.length; i++) {
			String token = split[i];
			if (!operators.containsKey(token)) {
				output.add(token);
			} else if (token.equals("(")) {
				operatorStack.push(operators.get(token));
			} else if (token.equals(")")) {
				while (!operatorStack.peek().operator.equals("(")) {
					output.add(operatorStack.pop().operator);
				}
				operatorStack.pop();
			} else {
				Operator op = operators.get(token);
				if (!operatorStack.isEmpty()) {
					Operator peek = operatorStack.peek();
					while (!operatorStack.isEmpty() && (((peek.precedence > op.precedence)
							|| (peek.precedence == op.precedence && peek.leftAssociativity))
							&& !peek.operator.equals("("))) {
						output.add(operatorStack.pop().operator);
						if (!operatorStack.isEmpty())
							peek = operatorStack.peek();
					}
				}
				operatorStack.push(op);
			}
		}

		while (!operatorStack.isEmpty()) {
			output.add(operatorStack.pop().operator);
		}

		StringBuilder sbuilder = new StringBuilder();
		for (int i = 0; i < output.size(); i++) {
			sbuilder.append(output.get(i));
			if (i != output.size() - 1)
				sbuilder.append(" ");
		}
		return sbuilder.toString();
	}

	/** Evaluation without variables **/
	private double evaluate(String postfix) {
		String[] split = postfix.split(" ");
		Stack<String> stack = new Stack<String>();
		for (int i = 0; i < split.length; i++) {
			String token = split[i];
			if (operators.containsKey(token)) {
				double operand2 = Double.parseDouble(stack.pop());
				double operand1 = Double.parseDouble(stack.pop());
				stack.push(Double.toString(operators.get(token).evaluate(operand1, operand2)));
			} else {
				stack.push(token);
			}
		}
		return Double.parseDouble(stack.pop());
	}

}
