package com.xwarner.model.services;

import java.util.HashMap;

import com.xwarner.model.Events;
import com.xwarner.model.models.ModelData;
import com.xwarner.model.models.Variable;

import javafx.concurrent.Service;
import javafx.concurrent.Task;

public class ModelTickService extends Service<HashMap<String, Variable>> {

	private ModelData data;
	private HashMap<String, Variable> variables;

	public void setData(ModelData data, HashMap<String, Variable> variables) {
		this.data = data;
		this.variables = variables;
	}

	protected Task<HashMap<String, Variable>> createTask() {
		return new Task<HashMap<String, Variable>>() {
			protected HashMap<String, Variable> call() throws Exception {
				try {
					data.evaluate(variables);
				} catch (Exception e) {
					Events.parsingError();
					e.printStackTrace();
				}
				return variables;
			}
		};
	}

}
