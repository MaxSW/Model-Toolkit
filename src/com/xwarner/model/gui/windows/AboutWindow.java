package com.xwarner.model.gui.windows;

import com.xwarner.model.App;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class AboutWindow {

	public AboutWindow() {
		Stage stage = new Stage();

		VBox root = new VBox();
		// root.setAlignment(Pos.CENTER);
		root.setPadding(new Insets(10));

		Text text = new Text();
		text.setText(
				"Model Toolkit version " + App.VERSION + "\n\nCreated by Max Warner (m@xwarner.com/maxwarner.xyz)");
		root.getChildren().add(text);

		stage.setTitle("About Model Toolkit");
		stage.setScene(new Scene(root));
		stage.setResizable(false);
		stage.getIcons().add(new Image(App.class.getResourceAsStream("/model-toolkit-icon.png")));
		stage.show();
	}
}
