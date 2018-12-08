package com.xwarner.model;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.xwarner.model.gui.MainMenu;
import com.xwarner.model.gui.TopRow;
import com.xwarner.model.gui.tabs.ComparativeStaticsTab;
import com.xwarner.model.gui.tabs.DetailsTab;
import com.xwarner.model.gui.tabs.ModelTab;
import com.xwarner.model.gui.tabs.VariablesGraphTab;
import com.xwarner.model.gui.tabs.VariablesTab;
import com.xwarner.model.gui.windows.UpdateWindow;
import com.xwarner.model.models.Model;
import com.xwarner.model.services.UpdateCheckerService;

import javafx.application.Application;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * The GUI manager and entry point
 */

public class App extends Application {

	public static ModelTab modelTab;
	public static VariablesTab variablesTab;
	public static ComparativeStaticsTab comparativeStaticsTab;
	public static DetailsTab detailsTab;
	public static VariablesGraphTab variablesGraphTab;

	public static TopRow topRow;
	public static MainMenu menu;

	public static Stage stage;
	public static State state;
	public static ExecutorService threadPool;

	public static TabPane tabPane;

	public static final String VERSION = "2.2"; // used for checking for updates

	public void start(Stage stage) {
		state = new State();
		state.model = new Model("Test Model");
		threadPool = Executors.newFixedThreadPool(3);

		App.stage = stage;

		topRow = new TopRow();
		menu = new MainMenu();
		// tabs
		tabPane = new TabPane();

		modelTab = new ModelTab();
		variablesTab = new VariablesTab();
		comparativeStaticsTab = new ComparativeStaticsTab();
		detailsTab = new DetailsTab();
		variablesGraphTab = new VariablesGraphTab();

		loadStatic();

		VBox root = new VBox();
		root.getChildren().add(menu);
		root.getChildren().add(topRow);
		root.getChildren().add(tabPane);

		Scene scene = new Scene(root, 1024, 600);
		// scene.getStylesheets().add(App.class.getResource("../../../styles.css").toExternalForm());

		stage.setTitle("Model Toolkit - Untitled Model");
		stage.setScene(scene);
		stage.show();

		final KeyCombination saveCombo = new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN);
		final KeyCombination openCombo = new KeyCodeCombination(KeyCode.O, KeyCombination.CONTROL_DOWN);
		final KeyCombination newCombo = new KeyCodeCombination(KeyCode.N, KeyCombination.CONTROL_DOWN);
		final KeyCombination runCombo = new KeyCodeCombination(KeyCode.R, KeyCombination.CONTROL_DOWN);

		scene.addEventHandler(KeyEvent.KEY_RELEASED, new EventHandler<KeyEvent>() {
			public void handle(KeyEvent event) {
				if (saveCombo.match(event))
					Events.saveModel();
				else if (openCombo.match(event))
					Events.openModel();
				else if (runCombo.match(event))
					Events.runButton();
				else if (newCombo.match(event))
					Events.newModel();

			}
		});

		startup();

		stage.getIcons().add(new Image(App.class.getResourceAsStream("/model-toolkit-icon.png")));
	}

	public void startup() {
		// for testing
		state.model = new Model("");
		stage.setTitle("Model Toolkit");
		state.openModel(new File("/home/max/Desktop/test.model"));

		UpdateCheckerService updateService = new UpdateCheckerService();
		updateService.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
			public void handle(WorkerStateEvent t) {
				boolean update = (boolean) t.getSource().getValue();
				if (update) {
					new UpdateWindow();
				}
			}
		});
		updateService.start();
		topRow.setVisible(false);
		tabPane.setVisible(false);

	}

	public static void loadStatic() {
		tabPane.getTabs().addAll(modelTab, variablesTab, comparativeStaticsTab, detailsTab);
	}

	public static void loadDynamic() {
		tabPane.getTabs().addAll(modelTab, variablesTab, variablesGraphTab, comparativeStaticsTab, detailsTab);
	}

	public static void reset() {
		tabPane.getTabs().clear();
	}

	public static void main(String[] args) {
		launch(args);
	}

}
