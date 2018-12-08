package com.xwarner.model.gui.tabs;

import com.xwarner.model.App;
import com.xwarner.model.Events;
import com.xwarner.model.models.Model;
import com.xwarner.model.models.ModelListener;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class DetailsTab extends Tab implements ModelListener {

	public TextField name;
	public ChoiceBox<String> type;

	public DetailsTab() {
		VBox root = new VBox();
		root.setPadding(new Insets(10));

		HBox nameRoot = new HBox();
		nameRoot.setAlignment(Pos.CENTER_LEFT);
		Label nameLabel = new Label("Model name: ");
		name = new TextField();
		nameRoot.getChildren().addAll(nameLabel, name);

		HBox typeRoot = new HBox();
		typeRoot.setAlignment(Pos.CENTER_LEFT);
		Label typeLabel = new Label("Model type: ");
		ObservableList<String> types = FXCollections.observableArrayList();
		types.addAll("Static", "Dynamic");
		type = new ChoiceBox<String>(types);
		typeRoot.getChildren().addAll(typeLabel, type);

		HBox saveRoot = new HBox();
		saveRoot.setAlignment(Pos.CENTER_LEFT);
		Button save = new Button("Save");
		save.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				boolean changed = type.getValue() != App.state.model.getTypeStr();
				App.state.model.setType(type.getValue());
				App.state.model.setName(name.getText());
				Events.saveModel();
				if (changed) {
					App.reset();
					if (App.state.model.getType() == Model.TYPE_STATIC)
						App.loadStatic();
					else if (App.state.model.getType() == Model.TYPE_DYNAMIC)
						App.loadDynamic();
				}
				App.state.model.triggerUpdate();
			}
		});
		saveRoot.getChildren().add(save);

		root.getChildren().addAll(nameRoot, typeRoot, saveRoot);

		VBox.setMargin(nameRoot, new Insets(0, 0, 5, 0));
		VBox.setMargin(typeRoot, new Insets(0, 0, 15, 0));

		setText("Details");
		setContent(root);
		setClosable(false);
	}

	public void onChange(String t) {
		if (!t.equals("name"))
			type.setValue(App.state.model.getTypeStr());
		if (!t.equals("type"))
			name.textProperty().set(App.state.model.getName());
	}

}
