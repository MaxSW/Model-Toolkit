package com.xwarner.model.scripts.functions;

import java.util.ArrayList;

import com.xwarner.model.scripts.interpreter.Context;
import com.xwarner.model.scripts.interpreter.Evaluator;
import com.xwarner.model.scripts.parser.Node;

public class Function extends Evaluator {

	public String name;
	public ArrayList<String> arguments;

	public Node node;

	public Function(Node node) {
		this.arguments = new ArrayList<String>();
		this.node = node;
		if (node != null)
			this.arguments = node.values;
	}

	public Function() {
		this(null);
	}

	public double evaluate(Context context) {
		this.name = node.value;
		for (Node n : node.getChildren()) {
			if (n.type == Node.DECLARATION)
				parseDeclaration(n, context);
			else if (n.type == Node.INVOCATION) {
				if (!n.value.equals("return"))
					throw new Error("you can only call return in this function");
				return evaluateMaths(n, context);
			}
		}
		return 0.0;
	}

	public void init() {
		// TODO Auto-generated method stub

	}

}
