package com.xwarner.model.gui.windows;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import com.xwarner.model.App;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class UpdateWindow {

	public UpdateWindow() {
		Stage stage = new Stage();

		HBox root = new HBox();
		root.setAlignment(Pos.CENTER);
		root.setPadding(new Insets(10));

		Text text = new Text("An update is available. Please visit ");
		Hyperlink link = new Hyperlink("https://maxwarner.xyz/model-toolkit");
		link.setBorder(null);
		Text text2 = new Text(" to download it.");
		link.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				try {
					Desktop.getDesktop().browse(new URI("https://maxwarner.xyz/model-toolkit"));
				} catch (IOException | URISyntaxException e1) {
					e1.printStackTrace();
				}
			}
		});

		root.getChildren().addAll(text, link, text2);

		stage.setTitle("Update Available");
		stage.setScene(new Scene(root));
		stage.setResizable(false);
		stage.getIcons().add(new Image(App.class.getResourceAsStream("/model-toolkit-icon.png")));
		stage.show();
	}

}
