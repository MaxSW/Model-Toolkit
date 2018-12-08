package com.xwarner.model.scripts.interpreter;

import java.util.HashMap;

import com.xwarner.model.models.ModelData;
import com.xwarner.model.models.Variable;
import com.xwarner.model.scripts.parser.AST;
import com.xwarner.model.scripts.parser.Parser;

public class InterpreterModelData implements ModelData {

	private AST tree;

	public void parse(String src) {
		Parser parser = new Parser(src);
		tree = parser.parse();
	}

	/* This transforms the data between the two types */
	public void evaluate(HashMap<String, Variable> variables) {
		// for now because our evaluation destroys the tree
		// TODO clone the tree
		TreeEvaluator evaluator = new TreeEvaluator(tree.clone(null));
		evaluator.preEvaluate();

		HashMap<String, Var> variables2 = new HashMap<String, Var>();
		// System.out.println("starting");

		for (String key : variables.keySet()) {
			variables2.put(key, new Var(key, variables.get(key).value));
			// System.out.println(key + ":" + variables.get(key).value);
		}

		// System.out.println("evaluating");

		variables2 = evaluator.evaluateTree(variables2);
		for (String key : variables2.keySet()) {
			if (variables.containsKey(key)) {
				variables.get(key).value = variables2.get(key).value;
			} else {
				variables.put(key, new Variable(key, Variable.TYPE_UNKNOWN, variables2.get(key).value));
			}
			// System.out.println(key + ":" + variables2.get(key).value);
		}
		// System.out.println("done");
	}

}
