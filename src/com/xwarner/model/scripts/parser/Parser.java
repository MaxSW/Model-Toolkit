package com.xwarner.model.scripts.parser;

import java.util.ArrayList;

import com.xwarner.model.scripts.parser.TokenStream.Token;

public class Parser {

	private TokenStream stream;

	public Parser(TokenStream stream) {
		this.stream = stream;
	}

	public Parser(String str) {
		this.stream = new TokenStream(new InputStream(str));
	}

	public AST parse() {
		AST ast = new AST();
		while (!stream.done()) {
			Node node = next();
			if (node != null)
				ast.addChild(node);
		}
		return ast;
	}

	public Node next() {
		Token t = stream.next();
		if (t == null) {
			return null;
		}
		if (t.type == Token.KEYWORD && t.value.equals("function")) {
			return parseFunction();
		} else if (t.type == Token.VARIABLE) {
			return parseDeclaration(t.value);
		} else if (t.type == Token.KEYWORD && t.value.equals("return")) {
			return parseReturn();
		} else if (t.type == Token.NEWLINE) {
			return next(); // go to the next line
		}
		return null;
	}

	public Node parseDeclaration(String name) {
		Node node = new Node(Node.DECLARATION, name);
		Token token = stream.peek();
		if (token.type != Token.ASSIGNMENT)
			throw new Error("invalid variable declaration");

		stream.next();
		parseSubsequent(node);
		return node;
	}

	public Node parseReturn() {
		Node node = new Node(Node.INVOCATION, "return");
		parseSubsequent(node);
		return node;
	}

	/**
	 * parses the actual math bit i.e. a = _this_ or return _this_
	 **/
	public void parseSubsequent(Node node) {
		while (!stream.done() && stream.peek().type != Token.NEWLINE && !stream.peek().value.equals("}")) {
			Token token = stream.next();
			Node n = null;
			if (token.type == Token.VARIABLE) {
				if (!stream.done()) {
					if (stream.peek().value.equals("(")) {
						n = parseInvocation(token.value);
					} else
						n = new Node(Node.VARIABLE, token.value);
				} else {
					n = new Node(Node.VARIABLE, token.value);
				}
			} else if (token.type == Token.NUMBER)
				n = new Node(Node.NUMBER, token.value);
			else if (token.type == Token.OPERATOR)
				n = new Node(Node.OPERATOR, token.value);
			else if (token.value.equals("(") || token.value.equals(")"))
				n = new Node(Node.OPERATOR, token.value);
			node.addChild(n);
		}
	}

	public Node parseInvocation(String name) {
		Node node = new Node(Node.INVOCATION, name);
		stream.next(); // consume (

		Node currentArg = new Node(Node.ARGUMENT, "");

		while (!stream.peek().value.equals(")")) {
			Token token = stream.next();
			if (token.value.equals(",")) {
				node.addChild(currentArg);
				currentArg = new Node(Node.ARGUMENT, "");
			} else {
				Node n = null;
				if (token.type == Token.VARIABLE) {
					if (stream.peek().value.equals("(")) {
						n = parseInvocation(token.value);
					} else
						n = new Node(Node.VARIABLE, token.value);
				} else if (token.type == Token.NUMBER)
					n = new Node(Node.NUMBER, token.value);
				else if (token.type == Token.OPERATOR)
					n = new Node(Node.OPERATOR, token.value);
				if (n != null)
					currentArg.addChild(n);
			}
		}
		if (currentArg.getChildren().size() != 0)
			node.addChild(currentArg);

		stream.next(); // consume )

		return node;
	}

	public Node parseFunction() {
		Node node = new Node(Node.FUNCTION, getFunctionName());
		node.values = parseFunctionParams();

		if (!stream.next().value.equals("{"))
			throw new Error("missing { in function declaration");

		while (!stream.peek().value.equals("}")) {
			Node next = next();
			if (next != null) {
				node.addChild(next);
			} else {
				break;
			}
		}

		stream.next(); // consume the }
		return node;
	}

	public ArrayList<String> parseFunctionParams() {
		ArrayList<String> params = new ArrayList<String>();
		if (!stream.next().value.equals("("))
			throw new Error("missing ( in function declaration");

		boolean commaNext = false;

		Token token;

		while (!stream.done() && !stream.peek().value.equals(")")) {
			if (commaNext) {
				if (!stream.peek().value.equals(",")) {
					System.out.println("done");
					return params;
				}
				stream.next();
				commaNext = false;
			} else {
				token = stream.next();
				if (token.type != Token.VARIABLE)
					throw new Error("unexpected token argument");
				params.add(token.value);
				commaNext = true;
			}
		}
		if (!stream.done()) {
			stream.next(); // consume the )
		}
		return params;
	}

	public String getFunctionName() {
		if (stream.peek().type != Token.VARIABLE) {
			throw new Error("function name missing");
		}
		return stream.next().value;
	}

}
