package com.xwarner.model.scripts.simple;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Stack;

import com.xwarner.model.scripts.operators.AddOperator;
import com.xwarner.model.scripts.operators.DivideOperator;
import com.xwarner.model.scripts.operators.LeftBracketOperator;
import com.xwarner.model.scripts.operators.MultiplyOperator;
import com.xwarner.model.scripts.operators.Operator;
import com.xwarner.model.scripts.operators.PowerOperator;
import com.xwarner.model.scripts.operators.RightBracketOperator;
import com.xwarner.model.scripts.operators.SubtractOperator;

public class ExpressionParser {

	/**
	 * TODO - nicer error handling allow
	 * 
	 * Longer-term TODO - more defined functions - mathematical constants -
	 * user-defined functions - importing libraries, other files etc
	 */

	public static HashMap<String, Operator> operators = new HashMap<String, Operator>();

	static {
		operators.put("+", new AddOperator());
		operators.put("-", new SubtractOperator());
		operators.put("/", new DivideOperator());
		operators.put("*", new MultiplyOperator());
		operators.put("^", new PowerOperator());
		operators.put("(", new LeftBracketOperator());
		operators.put(")", new RightBracketOperator());
		// operators.put("ln", new LnOperator());
		// operators.put("exp", new ExpOperator());

	}

	// for now, no variables just numbers
	// for now, needs to be space separated
	public static double parse(String str) {
		return evaluate(rpn(str));
	}

	/* https://en.wikipedia.org/wiki/Shunting-yard_algorithm */
	public static String rpn(String str) {
		LinkedList<String> output = new LinkedList<String>();
		Stack<Operator> operatorStack = new Stack<Operator>();

		String[] split = split(str);

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
	public static double evaluate(String postfix) {
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

	/** Evaluation with variables **/
	public static Double evaluate(String postfix, HashMap<String, Double> variables) {
		String[] split = postfix.split(" ");
		Stack<String> stack = new Stack<String>();
		for (int i = 0; i < split.length; i++) {
			String token = split[i];
			if (operators.containsKey(token)) {
				double operand2 = Double.parseDouble(stack.pop());
				double operand1 = Double.parseDouble(stack.pop());
				stack.push(Double.toString(operators.get(token).evaluate(operand1, operand2)));
			} else {
				if (isNumeric(token))
					stack.push(token);
				else {
					if (variables.containsKey(token)) {
						stack.push(Double.toString(variables.get(token)));
					} else {
						stack.push("0");
						// keep track of these variables
						variables.put(token, 0.0);
					}
				}
			}
		}
		return Double.parseDouble(stack.pop());
	}

	public static String[] split(String str) {
		char[] chars = str.toCharArray();
		ArrayList<String> tokens = new ArrayList<String>();
		String currentToken = "";

		for (int i = 0; i < chars.length; i++) {
			char c = chars[i];
			if (c == ' ')
				continue;
			int t = getType(c);
			if (currentToken.length() == 0) {
				currentToken += c;
			} else {
				int t2 = getType(currentToken.charAt(currentToken.length() - 1));
				if (t2 == t && t != 3) {
					currentToken += c;
				} else {
					tokens.add(currentToken);
					currentToken = "" + c;
				}
			}
		}

		tokens.add(currentToken);

		String[] res = new String[tokens.size()];
		for (int i = 0; i < res.length; i++) {
			res[i] = tokens.get(i);
		}
		return res;
	}

	// 1 = alpha, 2 = numeric, 3 = operator
	private static int getType(char c) {
		if (Character.isAlphabetic(c))
			return 1;
		else if (Character.isDigit(c))
			return 2;
		else
			return 3;
	}

	public static boolean isNumeric(String s) {
		return s != null && s.matches("[-+]?\\d*\\.?\\d+");
	}

}
