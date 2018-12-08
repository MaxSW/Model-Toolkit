package com.xwarner.model.services;

import com.xwarner.model.models.Model;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.shape.Rectangle;

/**
 * Does the calculations for a comparative statics graph.
 * 
 * TODO use multiple threads
 * 
 * TODO consider using something like a fixed thread pool for all background
 * tasks
 */

public class ComparativeStaticsService extends Service<XYChart.Series<Number, Number>> {

	private String x, y;
	private double from, to;
	private int num;
	private Model model;

	public void setData(String x, String y, double from, double to, int num, Model model) {
		this.x = x;
		this.y = y;
		this.from = from;
		this.to = to;
		this.num = num;
		this.model = model;
	}

	protected Task<Series<Number, Number>> createTask() {
		return new Task<Series<Number, Number>>() {
			protected Series<Number, Number> call() throws Exception {
				XYChart.Series<Number, Number> series = new XYChart.Series<Number, Number>();
				series.setName(y);

				double delta = (to - from) / num;
				double xx = from;

				for (int i = 0; i <= num; i++) {
					model.setVariableValue(x, xx);
					model.setVariableCustom(x, true);

					if (model.getType() == Model.TYPE_STATIC)
						model.customTick();
					else
						model.customRun();

					double yy = model.getVariableValue(y);

					if (Double.isNaN(yy))
						continue;

					// System.out.println("(" + xx + "," + yy + ")");

					// System.out.println(yy);
					// model.softReset();
					Data<Number, Number> dd = new XYChart.Data<Number, Number>(xx, yy);
					Rectangle rect = new Rectangle(0, 0);
					rect.setVisible(false);
					dd.setNode(rect);
					series.getData().add(dd);
					xx += delta;
				}
				return series;
			}

		};
	}

}
