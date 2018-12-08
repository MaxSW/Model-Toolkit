package com.xwarner.model.gui;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import com.xwarner.model.Events;
import com.xwarner.model.gui.windows.AboutWindow;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;;

public class MainMenu extends MenuBar {

	public MainMenu() {
		Menu menuFile = new Menu("File");
		MenuItem fileNew = new MenuItem("New Model");
		fileNew.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				Events.newModel();
			}
		});
		MenuItem fileOpen = new MenuItem("Open Model");
		fileOpen.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				Events.openModel();
			}
		});
		MenuItem fileSave = new MenuItem("Save Model");
		fileSave.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				Events.saveModel();
			}
		});
		menuFile.getItems().addAll(fileNew, fileOpen, fileSave);

		Menu menuHelp = new Menu("Help");
		MenuItem helpGuide = new MenuItem("User Guide");
		helpGuide.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				try {
					Desktop.getDesktop().browse(new URI("https://maxwarner.xyz/model-toolkit/guide.html"));
				} catch (IOException e1) {
					e1.printStackTrace();
				} catch (URISyntaxException e1) {
					e1.printStackTrace();
				}

			}
		});
		MenuItem helpAbout = new MenuItem("About");
		helpAbout.setOnAction(new EventHandler<ActionEvent>() {

			public void handle(ActionEvent e) {
				new AboutWindow();
			}
		});
		menuHelp.getItems().addAll(helpGuide, helpAbout);
		getMenus().addAll(menuFile, menuHelp);
	}

}
