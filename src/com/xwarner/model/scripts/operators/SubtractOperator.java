package com.xwarner.model.scripts.operators;

public class SubtractOperator extends Operator {

	public SubtractOperator() {
		super("-", 2, true);
	}

	public double evaluate(double a, double b) {
		return a - b;
	}

}
