package com.xwarner.model.services;

import java.util.ArrayList;
import java.util.HashMap;

import com.xwarner.model.models.ModelData;
import com.xwarner.model.models.Variable;

import javafx.concurrent.Service;
import javafx.concurrent.Task;

public class DynamicModelRunService extends Service<DynamicModelRunData> {

	private ModelData data;
	private HashMap<String, Variable> variables;
	private DynamicModelRunData rData;

	public void setData(ModelData data, HashMap<String, Variable> variables) {
		this.data = data;
		this.variables = variables;
	}

	protected Task<DynamicModelRunData> createTask() {
		return new Task<DynamicModelRunData>() {
			protected DynamicModelRunData call() throws Exception {
				rData = new DynamicModelRunData();
				rData.history = new ArrayList<HashMap<String, Double>>();
				int count = 0;
				while (true) {
					count++;
					data.evaluate(variables);
					if (steadyState()) {
						System.out.println("run complete (" + count + ")");
						rData.variables = variables;
						rData.tickCount = count;
						rData.steadyState = true;
						return rData;
					} else {
						HashMap<String, Double> entry = new HashMap<String, Double>();
						for (String name : variables.keySet()) {
							Variable var = variables.get(name);
							entry.put(name, var.value);
						}
						rData.history.add(entry);
					}
					if (count >= 1000) {
						rData.variables = variables;
						rData.tickCount = count;
						rData.steadyState = false;
						return rData;
					}
				}
			}
		};

	}

	public boolean steadyState() {
		if (rData.history.isEmpty())
			return false;
		HashMap<String, Double> oldVariables = rData.history.get(rData.history.size() - 1);
		for (String name : variables.keySet())
			if (variables.get(name).type == Variable.TYPE_ENDO)
				if (round(variables.get(name).value, 6) != round(oldVariables.get(name), 6))
					return false;
		return true;
	}

	public double round(double val, int points) {
		return Math.round(val * Math.pow(10, points)) / (Math.pow(10, points));
	}

}
