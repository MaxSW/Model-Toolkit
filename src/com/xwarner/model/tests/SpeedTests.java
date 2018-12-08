package com.xwarner.model.tests;

import java.io.File;

import com.xwarner.model.models.Model;
import com.xwarner.model.models.ModelData;
import com.xwarner.model.models.ModelIO;
import com.xwarner.model.scripts.interpreter.InterpreterModelData;
import com.xwarner.model.scripts.simple.SimpleModelData;

public class SpeedTests {

	/*
	 * These tests indicate that simple is worse in both dimensions compared to the
	 * parser (perhaps it is caused by some heavy string operations?). The time
	 * taken by parser was more than halved by building the tree up front and then
	 * just cloning it each time
	 */

	public static void main(String[] args) throws Exception {
		Model model1 = ModelIO.open(new File("/home/max/Desktop/solow.model"));

		test(model1, new SimpleModelData(), "simple (solow)", 1);
		test(model1, new InterpreterModelData(), "interpreter (solow)", 1);

		test(model1, new SimpleModelData(), "simple (solow)", 1000);
		test(model1, new InterpreterModelData(), "interpreter (solow)", 1000);

		Model model2 = ModelIO.open(new File("/home/max/Desktop/classical.model"));

		test(model2, new SimpleModelData(), "simple (classical)", 1);
		test(model2, new InterpreterModelData(), "interpreter (classical)", 1);

		test(model2, new SimpleModelData(), "simple (classical)", 1000);
		test(model2, new InterpreterModelData(), "interpreter (classical)", 1000);
	}

	public static void test(Model source, ModelData data, String name, int n) {
		System.out.println("* starting " + name + " test, n = " + n);
		long parseTime = 0;
		long evaluateTime = 0;

		for (int i = 0; i < n; i++) {
			Model model = source.clone();
			long before = System.currentTimeMillis();
			model.parse(data);
			long after = System.currentTimeMillis();
			parseTime += (after - before);
			if (model.getType() == Model.TYPE_DYNAMIC) {
				before = System.currentTimeMillis();
				model.customRun();
			} else if (model.getType() == Model.TYPE_STATIC) {
				before = System.currentTimeMillis();
				model.customTick();
			}

			after = System.currentTimeMillis();
			evaluateTime += (after - before);

			// System.out.println(i + ": " + (after - before));
		}
		System.out.println("parse time: " + (parseTime / n) + " ms");
		System.out.println("evaluate time: " + (evaluateTime / n) + " ms");
	}

}
