package com.xwarner.model.scripts.interpreter;

import java.util.HashMap;

import com.xwarner.model.scripts.functions.Function;
import com.xwarner.model.scripts.functions.InbuiltFunctions;
import com.xwarner.model.scripts.parser.Node;

public class TreeEvaluator extends Evaluator {

	private Node tree;
	private Context context;

	public TreeEvaluator(Node tree) {
		this.tree = tree;
		this.context = new Context();
	}

	public void preEvaluate() {
		InbuiltFunctions.addFunctions(context);
		evaluateFunctions();
	}

	public HashMap<String, Var> evaluateTree(HashMap<String, Var> variables) {
		for (String s : variables.keySet()) {
			context.setVariable(s, variables.get(s).value);
		}
		for (Node n : tree.getChildren()) {
			if (n.type == Node.DECLARATION) {
				parseDeclaration(n, context);
			}
		}
		return context.variables;
	}

	public void evaluateFunctions() {
		for (Node n : tree.getChildren()) {
			if (n.type == Node.FUNCTION) {
				context.functions.put(n.value, new Function(n));
			}
		}
	}

	public void resetVariables() {
		context.resetVariables();
	}

}
