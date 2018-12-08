package com.xwarner.model.scripts.operators;

public class MultiplyOperator extends Operator {

	public MultiplyOperator() {
		super("*", 3, true);
	}

	public double evaluate(double a, double b) {
		return a * b;
	}

}
