package com.xwarner.model.scripts.operators;

public class Operator {

	public String operator;
	public int precedence;
	public boolean leftAssociativity;

	public Operator(String operator, int precedence, boolean leftAssociativity) {
		this.operator = operator;
		this.precedence = precedence;
		this.leftAssociativity = leftAssociativity;
	}

	public double evaluate(double a, double b) {
		return 0;
	}

}
