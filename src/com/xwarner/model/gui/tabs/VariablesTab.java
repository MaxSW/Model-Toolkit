package com.xwarner.model.gui.tabs;

import com.xwarner.model.App;
import com.xwarner.model.Events;
import com.xwarner.model.models.ModelListener;
import com.xwarner.model.models.Variable;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public class VariablesTab extends Tab implements ModelListener {

	private ObservableList<Variable> exoVars;
	private ObservableList<Variable> endoVars;
	private ObservableList<Variable> paramVars;
	private ObservableList<Variable> otherVars;

	private TableView<Variable> exoTable;
	private TableView<Variable> endoTable;
	private TableView<Variable> paramTable;
	private TableView<Variable> otherTable;

	@SuppressWarnings("unchecked")
	public VariablesTab() {
		exoVars = FXCollections.observableArrayList();
		endoVars = FXCollections.observableArrayList();
		paramVars = FXCollections.observableArrayList();
		otherVars = FXCollections.observableArrayList();

		onChange("");

		HBox root = new HBox();

		VBox exoRoot = new VBox();
		VBox endoRoot = new VBox();
		VBox paramRoot = new VBox();
		VBox otherRoot = new VBox();

		Label exoLabel = new Label("Exogenous Variables");
		Label endoLabel = new Label("Endogenous Variables");
		Label paramLabel = new Label("Parameters");
		Label otherLabel = new Label("Other/Unknown Variables");

		Font font = new Font(14);
		exoLabel.setFont(font);
		endoLabel.setFont(font);
		paramLabel.setFont(font);
		otherLabel.setFont(font);

		exoRoot.getChildren().add(exoLabel);
		endoRoot.getChildren().add(endoLabel);
		paramRoot.getChildren().add(paramLabel);
		otherRoot.getChildren().add(otherLabel);

		exoTable = new TableView<Variable>();
		endoTable = new TableView<Variable>();
		paramTable = new TableView<Variable>();
		otherTable = new TableView<Variable>();

		// exoTable
		TableColumn<Variable, String> exoVarCol = new TableColumn<Variable, String>("Variable");
		exoVarCol.setCellValueFactory(new PropertyValueFactory<>("name"));

		TableColumn<Variable, String> exoValCol = new TableColumn<Variable, String>("Value");
		exoValCol.setCellValueFactory(new PropertyValueFactory<>("value"));
		exoValCol.setCellFactory(TextFieldTableCell.<Variable>forTableColumn());
		exoValCol.setOnEditCommit((CellEditEvent<Variable, String> t) -> {
			Variable v = ((Variable) t.getTableView().getItems().get(t.getTablePosition().getRow()));
			try {
				Double val = Double.parseDouble(t.getNewValue());
				change(v.name, val);
			} catch (Exception e) {
				Events.notNumber();
				change(v.name, v.value);
			}
		});

		exoTable.getColumns().addAll(exoVarCol, exoValCol);
		exoTable.setItems(exoVars);

		// endoTable
		TableColumn<Variable, String> endoVarCol = new TableColumn<Variable, String>("Variable");
		endoVarCol.setCellValueFactory(new PropertyValueFactory<>("name"));

		TableColumn<Variable, String> endoValCol = new TableColumn<Variable, String>("Value");
		endoValCol.setCellValueFactory(new PropertyValueFactory<>("value"));
		endoValCol.setCellFactory(TextFieldTableCell.<Variable>forTableColumn());
		endoValCol.setOnEditCommit((CellEditEvent<Variable, String> t) -> {
			Variable v = ((Variable) t.getTableView().getItems().get(t.getTablePosition().getRow()));
			try {
				Double val = Double.parseDouble(t.getNewValue());
				change(v.name, val);
			} catch (Exception e) {
				Events.notNumber();
				change(v.name, v.value);
			}
		});

		endoTable.getColumns().addAll(endoVarCol, endoValCol);
		endoTable.setItems(endoVars);

		// paramTable
		TableColumn<Variable, String> paramVarCol = new TableColumn<Variable, String>("Variable");
		paramVarCol.setCellValueFactory(new PropertyValueFactory<>("name"));

		TableColumn<Variable, String> paramValCol = new TableColumn<Variable, String>("Value");
		paramValCol.setCellValueFactory(new PropertyValueFactory<>("value"));
		paramValCol.setCellFactory(TextFieldTableCell.<Variable>forTableColumn());
		paramValCol.setOnEditCommit((CellEditEvent<Variable, String> t) -> {
			Variable v = ((Variable) t.getTableView().getItems().get(t.getTablePosition().getRow()));
			try {
				Double val = Double.parseDouble(t.getNewValue());
				change(v.name, val);
			} catch (Exception e) {
				Events.notNumber();
				change(v.name, v.value);
			}
		});

		paramTable.getColumns().addAll(paramVarCol, paramValCol);
		paramTable.setItems(paramVars);

		// exoTable
		TableColumn<Variable, String> otherVarCol = new TableColumn<Variable, String>("Variable");
		otherVarCol.setCellValueFactory(new PropertyValueFactory<>("name"));

		TableColumn<Variable, String> otherValCol = new TableColumn<Variable, String>("Value");
		otherValCol.setCellValueFactory(new PropertyValueFactory<>("value"));
		otherValCol.setCellFactory(TextFieldTableCell.<Variable>forTableColumn());
		otherValCol.setOnEditCommit((CellEditEvent<Variable, String> t) -> {
			Variable v = ((Variable) t.getTableView().getItems().get(t.getTablePosition().getRow()));
			try {
				Double val = Double.parseDouble(t.getNewValue());
				change(v.name, val);
			} catch (Exception e) {
				Events.notNumber();
				change(v.name, v.value);
			}
		});

		otherTable.getColumns().addAll(otherVarCol, otherValCol);
		otherTable.setItems(otherVars);

		exoRoot.getChildren().add(exoTable);
		endoRoot.getChildren().add(endoTable);
		paramRoot.getChildren().add(paramTable);
		otherRoot.getChildren().add(otherTable);

		exoTable.setEditable(true);
		endoTable.setEditable(true);
		paramTable.setEditable(true);
		otherTable.setEditable(true);

		HBox.setMargin(exoRoot, new Insets(0, 2, 0, 2));
		HBox.setMargin(endoRoot, new Insets(0, 2, 0, 2));
		HBox.setMargin(paramRoot, new Insets(0, 2, 0, 2));
		HBox.setMargin(otherRoot, new Insets(0, 2, 0, 2));

		HBox.setHgrow(exoRoot, Priority.ALWAYS);
		HBox.setHgrow(endoRoot, Priority.ALWAYS);
		HBox.setHgrow(paramRoot, Priority.ALWAYS);
		HBox.setHgrow(otherRoot, Priority.ALWAYS);

		VBox.setVgrow(exoTable, Priority.ALWAYS);
		VBox.setVgrow(endoTable, Priority.ALWAYS);
		VBox.setVgrow(paramTable, Priority.ALWAYS);
		VBox.setVgrow(otherTable, Priority.ALWAYS);

		root.setPadding(new Insets(10));
		root.getChildren().addAll(exoRoot, endoRoot, paramRoot, otherRoot);

		setText("Variables");
		setContent(root);
		setClosable(false);
	}

	// update the real variables list when there are changes to the ones here
	public void change(String name, double value) {
		App.state.model.setVariableValue(name, value);
		App.state.model.setVariableCustom(name, true);
		App.state.model.triggerUpdate();
	}

	public void onChange(String type) {
		exoVars.clear();
		endoVars.clear();
		paramVars.clear();
		otherVars.clear();
		for (Variable var : App.state.getUIVariables()) {
			if (var.type == Variable.TYPE_EXO)
				exoVars.add(var);
			else if (var.type == Variable.TYPE_ENDO)
				endoVars.add(var);
			else if (var.type == Variable.TYPE_PARAM)
				paramVars.add(var);
			else
				otherVars.add(var);
		}
	}
}
