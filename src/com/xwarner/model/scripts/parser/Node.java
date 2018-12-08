package com.xwarner.model.scripts.parser;

import java.util.ArrayList;

public class Node {
	public static final int FUNCTION = 1, DECLARATION = 2, INVOCATION = 3, OPERATOR = 4, VARIABLE = 5, NUMBER = 6,
			ARGUMENT = 7, AST = 0;

	private ArrayList<Node> children;
	public Node parent;

	public int type;
	public String value;
	public ArrayList<String> values;

	public Node(int type, String value) {
		children = new ArrayList<Node>();
		this.type = type;
		this.value = value;
		this.values = new ArrayList<String>();
	}

	public void addChild(Node n) {
		if (n != null)
			n.parent = this;
		children.add(n);
	}

	public ArrayList<Node> getChildren() {
		return children;
	}

	public void clearChildren() {
		children.clear();
	}

	public Node clone(Node parent) {
		Node node = new Node(type, value);
		node.parent = parent;
		// TODO this might be messing it up
		for (String s : values) {
			String ss = s;
			node.values.add(ss);
		}
		for (Node child : children) {
			node.addChild(child.clone(node));
		}
		return node;
	}

}
