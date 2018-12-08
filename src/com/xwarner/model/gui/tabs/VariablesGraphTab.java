package com.xwarner.model.gui.tabs;

import java.util.ArrayList;
import java.util.HashMap;

import com.xwarner.model.App;
import com.xwarner.model.models.ModelListener;
import com.xwarner.model.models.Variable;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Tab;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;

public class VariablesGraphTab extends Tab implements ModelListener {

	private ArrayList<String> availableVariables;
	private ArrayList<String> selectedVariables;
	private ArrayList<CheckBox> checkBoxes;

	private NumberAxis xAxis, yAxis;
	private LineChart<Number, Number> lineChart;

	private Button btn;
	private HBox topRow;

	public VariablesGraphTab() {
		availableVariables = new ArrayList<String>();
		selectedVariables = new ArrayList<String>();
		checkBoxes = new ArrayList<CheckBox>();

		VBox root = new VBox();
		root.setPadding(new Insets(10));

		topRow = new HBox();
		topRow.setAlignment(Pos.CENTER_LEFT);

		btn = new Button("Update");
		btn.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				generateGraph();
			}
		});

		topRow.getChildren().addAll(btn);

		root.getChildren().add(topRow);

		xAxis = new NumberAxis();
		yAxis = new NumberAxis();
		// creating the chart
		lineChart = new LineChart<Number, Number>(xAxis, yAxis);

		root.getChildren().add(lineChart);

		VBox.setMargin(lineChart, new Insets(20));

		setText("Variables Graph");
		setContent(root);
		setClosable(false);

		onChange("");
	}

	public void generateGraph() {
		selectedVariables.clear();

		for (CheckBox c : checkBoxes) {
			if (c.isSelected())
				selectedVariables.add(c.getText());
		}

		lineChart.getData().clear();

		xAxis.setLabel("ticks");
		yAxis.setLabel("endogenous variables");

		ArrayList<HashMap<String, Double>> data = App.state.model.getHistory();

		if (data == null) // certainly possible
			return;

		// reorganise the data for parsing
		HashMap<String, ArrayList<Double>> newData = new HashMap<String, ArrayList<Double>>();

		HashMap<String, Double> td0 = data.get(0);
		for (String name : td0.keySet()) {
			if (selectedVariables.contains(name))
				newData.put(name, new ArrayList<Double>());
		}

		for (int i = 0; i < data.size(); i++) {
			HashMap<String, Double> td = data.get(i);
			for (String name : td.keySet()) {
				if (selectedVariables.contains(name)) {
					ArrayList<Double> e = newData.get(name);
					e.add(td.get(name));
					newData.put(name, e);
				}
			}
		}

		for (String name : newData.keySet()) {
			Series<Number, Number> series = new Series<Number, Number>();
			series.setName(name);
			ArrayList<Double> d = newData.get(name);
			for (int i = 0; i < d.size(); i++) {
				Data<Number, Number> dd = new XYChart.Data<Number, Number>(i, d.get(i));
				Rectangle rect = new Rectangle(0, 0);
				rect.setVisible(false);
				dd.setNode(rect);
				series.getData().add(dd);
			}
			lineChart.getData().add(series);
		}

		// lineChart.setTitle("Comparative statics: " + x + " vs. " + y);
		// lineChart.getData().add((Series<Number, Number>)
		// t.getSource().getValue());
	}

	public void onChange(String type) {
		availableVariables.clear();
		for (Variable v : App.state.getUIVariables()) {
			if (v.type == Variable.TYPE_ENDO)
				availableVariables.add(v.name);
		}
		selectedVariables.clear();
		for (CheckBox c : checkBoxes) {
			if (c.isSelected())
				selectedVariables.add(c.getText());
		}
		checkBoxes = new ArrayList<CheckBox>();
		for (String name : availableVariables) {
			checkBoxes.add(new CheckBox(name));
		}
		topRow.getChildren().clear();
		for (CheckBox c : checkBoxes) {
			if (selectedVariables.contains(c.getText()))
				c.setSelected(true);
			topRow.getChildren().add(c);
			HBox.setMargin(c, new Insets(0, 10, 0, 0));
		}
		topRow.getChildren().add(btn);
		generateGraph();
	}

}
