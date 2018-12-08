package com.xwarner.model.scripts.interpreter;

import java.util.HashMap;

import com.xwarner.model.scripts.parser.AST;
import com.xwarner.model.scripts.parser.Node;
import com.xwarner.model.scripts.parser.Parser;

public class Test {

	public static void main(String[] args) {
		String input = "a=exp(ln(10))\nb=ln(exp(5))";
		System.out.println(input);
		System.out.println("");
		Parser parser = new Parser(input);
		AST tree = parser.parse();
		print(tree, 0);
		TreeEvaluator evaluator = new TreeEvaluator(tree);
		evaluator.preEvaluate();
		HashMap<String, Var> variables = evaluator.evaluateTree(new HashMap<String, Var>());
		for (String name : variables.keySet()) {
			System.out.println(name + ": " + variables.get(name).value);
		}
	}

	public static void print(Node n, int t) {
		if (n != null) {
			String str = space(t) + n.type + ":" + n.value;
			if (n.values.size() != 0) {
				str += " (";
				for (String s : n.values) {
					str += s + ", ";
				}
				str = str.substring(0, str.length() - 2);
				str += ")";
			}
			System.out.println(str);
			for (Node nn : n.getChildren())
				print(nn, t + 1);
		} else {
			System.out.println(space(t) + "null node");
		}

	}

	// todo function in invocation arg?

	public static String space(int t) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < t * 2; i++) {
			sb.append(" ");
		}
		return sb.toString();
	}

}
