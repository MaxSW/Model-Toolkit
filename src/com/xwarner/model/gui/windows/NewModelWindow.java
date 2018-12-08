package com.xwarner.model.gui.windows;

import com.xwarner.model.App;
import com.xwarner.model.models.Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class NewModelWindow {

	public TextField name;
	public ChoiceBox<String> type;

	public NewModelWindow() {
		Stage stage = new Stage();

		VBox root = new VBox();
		root.setAlignment(Pos.CENTER);
		root.setPadding(new Insets(10));

		HBox nameRoot = new HBox();
		nameRoot.setAlignment(Pos.CENTER_LEFT);
		Label nameLabel = new Label("Model name: ");
		name = new TextField();
		nameRoot.getChildren().addAll(nameLabel, name);

		HBox typeRoot = new HBox();
		typeRoot.setAlignment(Pos.CENTER_LEFT);
		Label typeLabel = new Label("Model type:  ");
		ObservableList<String> types = FXCollections.observableArrayList();
		types.addAll("Static", "Dynamic");
		type = new ChoiceBox<String>(types);
		typeRoot.getChildren().addAll(typeLabel, type);

		HBox saveRoot = new HBox();
		saveRoot.setAlignment(Pos.CENTER_LEFT);
		Button save = new Button("Create Model");
		save.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				Model model = new Model(name.getText());
				model.setType(type.getValue());
				model.setModel("");
				App.state.file = null;
				App.state.loadNewModel(model);
				stage.close();
			}
		});
		saveRoot.getChildren().add(save);

		root.getChildren().addAll(nameRoot, typeRoot, saveRoot);

		VBox.setMargin(nameRoot, new Insets(0, 0, 5, 0));
		VBox.setMargin(typeRoot, new Insets(0, 0, 15, 0));

		stage.setTitle("New Model");
		stage.setScene(new Scene(root));
		stage.setResizable(false);
		stage.getIcons().add(new Image(App.class.getResourceAsStream("/model-toolkit-icon.png")));
		stage.show();
	}

}
