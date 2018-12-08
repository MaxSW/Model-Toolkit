package com.xwarner.model.models;

import java.util.ArrayList;
import java.util.HashMap;

import com.xwarner.model.App;
import com.xwarner.model.Events;
import com.xwarner.model.scripts.interpreter.InterpreterModelData;
import com.xwarner.model.services.DynamicModelRunData;
import com.xwarner.model.services.DynamicModelRunService;
import com.xwarner.model.services.ModelTickService;

import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;

public class Model {

	public static int TYPE_UNKNOWN = 0, TYPE_STATIC = 1, TYPE_DYNAMIC = 2;

	private HashMap<String, Variable> variables;
	private ModelData data;

	private String name;
	private String model; // the model in string form
	private int type;
	
	private ArrayList<ModelListener> listeners;

	private ArrayList<HashMap<String, Double>> history; // only used for dynamic
														// models

	public Model(String name) {
		this.name = name;
		variables = new HashMap<String, Variable>();
		data = new InterpreterModelData(); // the default
		listeners = new ArrayList<ModelListener>();
	}

	public void parse() {
		data.parse(model);
	}

	public void parse(ModelData data) {
		this.data = data;
		this.parse();
	}

	/** Resets the model and doesn't keep the variables changed by the user **/
	public void hardReset() {
		for (String name : variables.keySet()) {
			Variable var = variables.get(name);
			var.value = Double.parseDouble(var.defaultValue);
		}
		ArrayList<String> toRemove = new ArrayList<String>();
		for (String name : variables.keySet()) {
			Variable var = variables.get(name);
			if (!model.contains(var.name))
				toRemove.add(name);
		}
		for (String name : toRemove) {
			variables.remove(name);
		}
		for (ModelListener l : listeners)
			l.onChange("reset");
	}

	/** Resets the model but keeps the variables changed by the user **/
	public void softReset() {
		for (String name : variables.keySet()) {
			Variable var = variables.get(name);
			if (!var.custom)
				var.value = Double.parseDouble(var.defaultValue);
		}
		ArrayList<String> toRemove = new ArrayList<String>();
		for (String name : variables.keySet()) {
			Variable var = variables.get(name);
			if (!model.contains(var.name))
				toRemove.add(name);
		}
		for (String name : toRemove) {
			variables.remove(name);
		}
		for (ModelListener l : listeners)
			l.onChange("reset");
	}

	public void run() {
		this.run(null);
	}

	public void run(Runnable callback) {
		// softReset();
		if (type == TYPE_STATIC)
			tick();
		else {
			App.topRow.progress.setVisible(true);
			System.out.println("running " + name);
			DynamicModelRunService service = new DynamicModelRunService();
			service.setData(data, variables);
			service.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
				public void handle(WorkerStateEvent t) {
					DynamicModelRunData data = (DynamicModelRunData) t.getSource().getValue();
					variables = data.variables;
					if (data.steadyState)
						App.topRow.status.setText("steady state reached in " + data.tickCount + " ticks");
					else
						App.topRow.status
								.setText("tick limit (" + data.tickCount + ") reached without reaching a steady state");

					if (history == null)
						history = data.history;
					else
						history.addAll(data.history);

					for (String name : variables.keySet()) {
						Variable var = variables.get(name);
						var.value = round(var.value, 5);
					}
					if (callback != null)
						callback.run();
					for (ModelListener l : listeners)
						l.onChange("run");
					App.topRow.progress.setVisible(false);
				}
			});
			// service.setExecutor(App.threadPool);
			service.start();

		}
	}

	public void tick() {
		System.out.println("ticking " + name);
		App.topRow.progress.setVisible(true);
		ModelTickService service = new ModelTickService();
		service.setData(data, variables);
		service.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
			@SuppressWarnings("unchecked")
			public void handle(WorkerStateEvent t) {
				variables = (HashMap<String, Variable>) t.getSource().getValue();
				// System.out.println("== output ==");
				for (String name : variables.keySet()) {
					Variable var = variables.get(name);
					var.value = round(var.value, 5);
					// System.out.println(var.name + ": " + var.value + " (" +
					// var.getType() + ")");
				}
				for (ModelListener l : listeners)
					l.onChange("run");
				App.topRow.progress.setVisible(false);
			}
		});
		// service.setExecutor(App.threadPool);
		service.start();

	}

	/**
	 * Should only be called by things that know what they are doing, since it runs
	 * on the current thread
	 **/
	public void customTick() {
		softReset();
		try {
			data.evaluate(variables);
		} catch (Exception e) {
			Events.parsingError();
			e.printStackTrace();
		}
		// System.out.println("== output ==");
		for (String name : variables.keySet()) {
			Variable var = variables.get(name);
			var.value = round(var.value, 5);
			// System.out.println(var.name + ": " + var.value + " (" +
			// var.getType() + ")");
		}
		for (ModelListener l : listeners)
			l.onChange("run");
	}

	public void customRun() {
		softReset();
		history = new ArrayList<HashMap<String, Double>>();
		int count = 0;
		while (true) {
			count++;
			data.evaluate(variables);
			if (!steadyState()) {
				HashMap<String, Double> entry = new HashMap<String, Double>();
				for (String name : variables.keySet()) {
					Variable var = variables.get(name);
					entry.put(name, var.value);
				}
				history.add(entry);
			} else {
				// System.out.println("output: " +
				// variables.get("output").value);
				// System.out.println("steady state in " + count + " ticks");
				return;
			}
			if (count >= 1000) {
				return;
			}
		}
	}

	private boolean steadyState() {
		if (history.isEmpty())
			return false;
		HashMap<String, Double> oldVariables = history.get(history.size() - 1);
		for (String name : variables.keySet())
			if (variables.get(name).type == Variable.TYPE_ENDO)
				if (round(variables.get(name).value, 6) != round(oldVariables.get(name), 6))
					return false;
		return true;

	}

	public void triggerUpdate() {
		for (ModelListener l : listeners)
			l.onChange("manual");
	}

	public void addListener(ModelListener listener) {
		listeners.add(listener);
	}

	public void addListeners(ModelListener... listener) {
		for (int i = 0; i < listener.length; i++) {
			listeners.add(listener[i]);
		}
	}

	public void removeListener(ModelListener listener) {
		listeners.remove(listener);
	}

	public Model clone() {
		Model b = new Model(name);
		for (String name : variables.keySet()) {
			b.variables.put(name, (variables.get(name).clone()));
		}
		b.model = model;
		b.type = type;
		b.parse();
		return b;
	}

	/*
	 * Variable getters and setters. To prevent repeated calling, these do not
	 * trigger a change event. If a variable is changed in this way, the
	 * triggerUpdate method must be called subsequently
	 */
	public HashMap<String, Variable> getVariables() {
		return variables;
	}

	public void addVariable(Variable var) {
		variables.put(var.name, var);
	}

	public void removeVariable(Variable var) {
		variables.remove(var.name);
	}

	public void removeVariable(String name) {
		variables.remove(name);
	}

	public Variable getVariable(String name) {
		return variables.get(name);
	}

	public int getVariableType(String name) {
		return variables.get(name).type;
	}

	public String getVariableTypeStr(String name) {
		return variables.get(name).getType();
	}

	public void setVariableType(String name, int type) {
		variables.get(name).type = type;
	}

	public void setVariableType(String name, String type) {
		variables.get(name).setType(type);
	}

	public double getVariableValue(String name) {
		return variables.get(name).value;
	}

	public void setVariableValue(String name, double value) {
		variables.get(name).value = value;
	}

	public String getVariableDefault(String name) {
		return variables.get(name).defaultValue;
	}

	public void setVariableDefault(String name, String value) {
		variables.get(name).defaultValue = value;
	}

	public void setVariableDefault(String name, double value) {
		variables.get(name).defaultValue = Double.toString(value);
	}

	public void setVariableCustom(String name, boolean custom) {
		variables.get(name).custom = custom;
	}

	public boolean getVariableCustom(String name) {
		return variables.get(name).custom;
	}

	/* Getters and setters */
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public void setType(String str) {
		if (str.equalsIgnoreCase("static"))
			type = TYPE_STATIC;
		else if (str.equalsIgnoreCase("dynamic"))
			type = TYPE_DYNAMIC;
		else
			type = TYPE_UNKNOWN;
	}

	public String getTypeStr() {
		if (type == TYPE_STATIC)
			return "Static";
		else if (type == TYPE_DYNAMIC)
			return "Dynamic";
		return "Unknown";
	}

	public ArrayList<HashMap<String, Double>> getHistory() {
		return history;
	}

	public void setHistory(ArrayList<HashMap<String, Double>> history) {
		this.history = history;
	}

	public double round(double val, int points) {
		return Math.round(val * Math.pow(10, points)) / (Math.pow(10, points));
	}

}
