package com.xwarner.model.scripts.operators;

public class PowerOperator extends Operator {

	public PowerOperator() {
		super("^", 4, false);
	}

	public double evaluate(double a, double b) {
		return Math.pow(a, b);
	}

}
