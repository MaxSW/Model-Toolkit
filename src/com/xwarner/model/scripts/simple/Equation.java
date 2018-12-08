package com.xwarner.model.scripts.simple;

import java.util.HashMap;

public class Equation {

	public String result;
	private String expressionStr;
	private String parsedExpression;

	public Equation(String source) {
		// for now just deal with var = expression
		String[] split = source.split("=");
		if (split.length > 2) {
			System.out.println("Invalid equation");
			return;
		}
		result = split[0].trim();
		expressionStr = split[1].trim();
		// cache the rpn format for faster evaluation
		parsedExpression = ExpressionParser.rpn(expressionStr);
	}

	public void evaluate(HashMap<String, Double> variables) {
		variables.put(result, ExpressionParser.evaluate(parsedExpression, variables));
	}

}
