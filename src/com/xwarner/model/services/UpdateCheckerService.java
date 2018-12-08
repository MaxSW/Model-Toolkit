package com.xwarner.model.services;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import com.xwarner.model.App;

import javafx.concurrent.Service;
import javafx.concurrent.Task;

public class UpdateCheckerService extends Service<Boolean> {

	protected Task<Boolean> createTask() {
		return new Task<Boolean>() {
			protected Boolean call() throws Exception {
				String latestVersion = readStringFromURL(
						"https://s3.eu-west-2.amazonaws.com/maxwarner/model-toolkit/version.txt");
				return !latestVersion.trim().equalsIgnoreCase(App.VERSION);
			}
		};
	}

	public String readStringFromURL(String requestURL) {
		try (Scanner scanner = new Scanner(new URL(requestURL).openStream(), StandardCharsets.UTF_8.toString())) {
			scanner.useDelimiter("\\A");
			String s = scanner.hasNext() ? scanner.next() : "";
			scanner.close();
			return s;
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}

}
