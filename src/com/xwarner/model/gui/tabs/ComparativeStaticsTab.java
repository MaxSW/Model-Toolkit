package com.xwarner.model.gui.tabs;

import com.xwarner.model.App;
import com.xwarner.model.Events;
import com.xwarner.model.models.Model;
import com.xwarner.model.models.ModelListener;
import com.xwarner.model.models.Variable;
import com.xwarner.model.services.ComparativeStaticsService;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class ComparativeStaticsTab extends Tab implements ModelListener {

	private ObservableList<String> variables;

	private ChoiceBox<String> choice1, choice2;
	private TextField num1, num2, num3;

	private NumberAxis xAxis, yAxis;
	private LineChart<Number, Number> lineChart;

	public ComparativeStaticsTab() {
		variables = FXCollections.observableArrayList();
		onChange("");

		VBox root = new VBox();
		root.setPadding(new Insets(10));
		HBox topRow = new HBox();
		topRow.setAlignment(Pos.CENTER_LEFT);

		Label label1 = new Label("Graph ");
		Label label2 = new Label(" as ");
		Label label3 = new Label(" changes from ");
		Label label4 = new Label(" to ");
		Label label5 = new Label(" in ");
		Label label6 = new Label(" steps  ");
		Label label7 = new Label("  ");

		choice1 = new ChoiceBox<String>(variables);
		choice2 = new ChoiceBox<String>(variables);

		num1 = new TextField();
		num1.setPrefColumnCount(5);
		num2 = new TextField();
		num2.setPrefColumnCount(5);
		num3 = new TextField();
		num3.setPrefColumnCount(5);

		Button btn = new Button("Go");
		btn.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				generateGraph();
			}
		});

		topRow.getChildren().addAll(label1, choice1, label2, choice2, label3, num1, label4, num2, label5, num3, label6,
				btn, label7);

		root.getChildren().add(topRow);

		xAxis = new NumberAxis();
		yAxis = new NumberAxis();
		// creating the chart
		lineChart = new LineChart<Number, Number>(xAxis, yAxis);

		root.getChildren().add(lineChart);

		VBox.setMargin(lineChart, new Insets(20));

		setText("Comparative Statics");
		setContent(root);
		setClosable(false);
	}

	public void generateGraph() {

		lineChart.getData().clear();

		String y = choice1.getValue();
		String x = choice2.getValue();

		double from = 0;
		double to = 0;
		int num = 0;

		try {
			from = Double.parseDouble(num1.textProperty().get());
			to = Double.parseDouble(num2.textProperty().get());
			num = Integer.parseInt(num3.textProperty().get()) + 1;
		} catch (Exception e) {
			Events.notNumber();
			return;
		}

		if (y == null || x == null) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setContentText("Please select a variable");
			alert.show();
			return;
		}

		if (from >= to) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setContentText("From must be less than to");
			alert.show();
			return;
		}

		if (num <= 1) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setContentText("The number of steps must be positive");
			alert.show();
			return;
		}
		App.topRow.progress.setVisible(true);

		Model m = App.state.model.clone();
		m.setModel(App.modelTab.editor.getText());
		m.parse();
		m.softReset();

		xAxis.setLabel(x);
		if (m.getType() == Model.TYPE_STATIC) {
			yAxis.setLabel(y);
		} else if (m.getType() == Model.TYPE_DYNAMIC) {
			yAxis.setLabel("steady state " + y);
			lineChart.setTitle("steady state " + y + " as " + x + " varies");
		}

		ComparativeStaticsService service = new ComparativeStaticsService();
		service.setData(x, y, from, to, num, m);
		service.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
			@SuppressWarnings("unchecked")
			public void handle(WorkerStateEvent t) {
				lineChart.getData().add((Series<Number, Number>) t.getSource().getValue());
				App.topRow.progress.setVisible(false);
			}
		});
		// service.setExecutor(App.threadPool);
		service.start();

	}

	public void onChange(String type) {
		String y = null, x = null;
		if (choice1 != null)
			y = choice1.getValue();
		if (choice2 != null)
			x = choice2.getValue();
		variables.clear();
		for (Variable var : App.state.getUIVariables()) {
			variables.add(var.name);
		}
		if (choice1 != null)
			choice1.setValue(y);
		if (choice2 != null)
			choice2.setValue(x);
	}
}
