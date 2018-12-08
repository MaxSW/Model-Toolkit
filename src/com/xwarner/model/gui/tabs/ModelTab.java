package com.xwarner.model.gui.tabs;

import com.xwarner.model.App;
import com.xwarner.model.models.ModelListener;
import com.xwarner.model.models.Variable;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.ChoiceBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

public class ModelTab extends Tab implements ModelListener {

	public TextArea editor;
	public TableView<Variable> table;

	public boolean changed;

	@SuppressWarnings("unchecked")
	public ModelTab() {

		HBox root = new HBox();
		root.setPadding(new Insets(10));

		editor = new TextArea();
		editor.setPrefRowCount(50);	
		editor.textProperty().addListener(new ChangeListener<String>() {
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				changed = true;
				App.state.model.setModel(newValue);
			}
		});

		root.getChildren().add(editor);

		// variable table

		table = new TableView<Variable>();

		TableColumn<Variable, String> variableCol = new TableColumn<Variable, String>("Variable");
		variableCol.setCellValueFactory(new PropertyValueFactory<>("name"));

		TableColumn<Variable, String> typeCol = new TableColumn<Variable, String>("Type");
		typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));

		ObservableList<String> items = FXCollections.observableArrayList();
		items.addAll("exogenous", "endogenous", "parameter", "unknown", "other");

		typeCol.setCellFactory(ChoiceBoxTableCell.forTableColumn(items));
		typeCol.setOnEditCommit((CellEditEvent<Variable, String> t) -> {
			String name = ((Variable) t.getTableView().getItems().get(t.getTablePosition().getRow())).name;
			App.state.model.setVariableType(name, t.getNewValue());
		});

		TableColumn<Variable, String> defaultCol = new TableColumn<Variable, String>("Default");
		defaultCol.setCellValueFactory(new PropertyValueFactory<>("defaultValue"));

		defaultCol.setCellFactory(TextFieldTableCell.<Variable>forTableColumn());
		defaultCol.setOnEditCommit((CellEditEvent<Variable, String> t) -> {
			String name = ((Variable) t.getTableView().getItems().get(t.getTablePosition().getRow())).name;
			App.state.model.setVariableDefault(name, t.getNewValue());
		});

		table.getColumns().addAll(variableCol, typeCol, defaultCol);

		table.setItems(App.state.getUIVariables());
		table.setEditable(true);

		root.getChildren().add(table);
		HBox.setMargin(table, new Insets(0, 0, 0, 5));
		HBox.setHgrow(editor, Priority.ALWAYS);

		setText("Model");
		setContent(root);
		setClosable(false);
	}

	public void update() {
	}

	public void onChange(String type) {
		table.setItems(App.state.getUIVariables());
	}

}
