package com.xwarner.model.scripts.operators;

public class DivideOperator extends Operator {

	public DivideOperator() {
		super("/", 3, true);
	}

	public double evaluate(double a, double b) {
		return a / b;
	}

}
