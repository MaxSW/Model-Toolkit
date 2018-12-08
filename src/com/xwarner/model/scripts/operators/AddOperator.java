package com.xwarner.model.scripts.operators;

public class AddOperator extends Operator {

	public AddOperator() {
		super("+", 2, true);
	}

	public double evaluate(double a, double b) {
		return a + b;
	}

}
