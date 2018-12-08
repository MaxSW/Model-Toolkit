package com.xwarner.model;

import java.io.File;
import java.util.HashMap;

import com.xwarner.model.models.Model;
import com.xwarner.model.models.ModelIO;
import com.xwarner.model.models.ModelListener;
import com.xwarner.model.models.Variable;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * the interface between the GUI (App) and models (Model)
 * 
 * handles access to the model by the GUI and changes to the current model
 */

public class State implements ModelListener {

	public Model model;
	public File file;

	private ObservableList<Variable> cache;
	public boolean changed = true;

	public void openModel(File file) {
		try {
			model = ModelIO.open(file);
			this.file = file;
			loadNewModel(model);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void loadNewModel(Model model) {
		this.model = model;
		App.stage.setTitle("Model Toolkit - " + model.getTypeStr() + " - " + model.getName());
		addModelListeners();
		App.reset();
		if (model.getType() == Model.TYPE_STATIC)
			App.loadStatic();
		else if (model.getType() == Model.TYPE_DYNAMIC)
			App.loadDynamic();
		App.modelTab.editor.setText(model.getModel());
		model.parse();
		model.softReset(); // triggers an update of all the UI
	}

	/**
	 * Add the model listeners for this and for all the GUI elements related to the
	 * current model
	 **/
	private void addModelListeners() {
		model.addListeners(this);
		model.addListeners(App.topRow, App.modelTab, App.variablesTab, App.comparativeStaticsTab, App.detailsTab,
				App.variablesGraphTab);
	}

	public void onChange(String type) {
		changed = true;
		App.topRow.setVisible(true);
		App.tabPane.setVisible(true);
		App.stage.setTitle("Model Toolkit - " + model.getTypeStr() + " - " + model.getName());
	}

	/**
	 * Return an observable list of the variables. Unfortunately, due to wanting to
	 * separate models and GUI, I cannot use the observableArrayList in the model
	 * class and then just let the GUI listen to it. This array list is cached to
	 * increase speed.
	 **/
	public ObservableList<Variable> getUIVariables() {
		if (changed) {
			cache = FXCollections.observableArrayList();
			HashMap<String, Variable> variables = model.getVariables();
			for (String name : variables.keySet()) {
				cache.add(variables.get(name).clone()); // TODO test whether
														// clone is necessary
			}
		}
		return cache;
	}

}
