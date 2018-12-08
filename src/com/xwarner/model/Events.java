package com.xwarner.model;

import java.io.File;

import com.xwarner.model.gui.windows.NewModelWindow;

/** 
 * Handles GUI events
 */

import com.xwarner.model.models.ModelIO;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.FileChooser;

public class Events {

	public static void runButton() {
		App.topRow.status.setText("");
		App.state.model.setModel(App.modelTab.editor.getText());
		App.state.model.parse();
		App.state.model.run();
	}

	public static void resetButton() {
		App.topRow.status.setText("");
		App.state.model.setModel(App.modelTab.editor.getText());
		App.state.model.parse();
		App.state.model.softReset();
		App.state.model.setHistory(null);
	}

	public static void tickButton() {
		App.topRow.status.setText("");
		App.state.model.setModel(App.modelTab.editor.getText());
		App.state.model.parse();
		App.state.model.tick();
	}

	public static void openModel() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open Model");
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Model files (*.model)", "*.model");
		fileChooser.getExtensionFilters().add(extFilter);
		File file = fileChooser.showOpenDialog(App.stage);
		if (file != null) {
			System.out.println("opening model from " + file.getAbsolutePath());
			App.state.openModel(file);
		}
	}

	public static void saveModel() {
		if (App.state.file != null) {
			// save
			System.out.println("saving model to " + App.state.file.getAbsolutePath());
			try {
				ModelIO.save(App.state.file, App.state.model);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("saving new model");
			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Save Model");
			FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Model files (*.model)", "*.model");
			fileChooser.getExtensionFilters().add(extFilter);
			File file = fileChooser.showSaveDialog(App.stage);
			if (file != null) {
				System.out.println("saving new model to " + file.getAbsolutePath());
				App.state.file = file;
				try {
					ModelIO.save(file, App.state.model);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static void newModel() {
		new NewModelWindow();
	}

	public static void notNumber() {
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle("Invalid number");
		alert.setHeaderText("Invalid number");
		alert.setContentText("Please enter a valid number");
		alert.show();
	}

	public static void runError() {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Run error");
		// alert.setHeaderText("Look, an Error Dialog");
		alert.setContentText(
				"An error occurred when running the model. Please check that its syntax is correct. More information can be found in the user guide (Help Menu>User Guide)");
		alert.showAndWait();
	}

	public static void parsingError() {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Parsing error");
		// alert.setHeaderText("Look, an Error Dialog");
		alert.setContentText(
				"An error occurred when parsing the model. Please check that its syntax is correct. More information can be found in the user guide (Help Menu>User Guide)");
		alert.showAndWait();
	}

}
