package app;

import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.shape.Shape;

import java.util.ArrayList;


public class SelectionGroup {
	private SimpleListProperty<Shape> items = new SimpleListProperty<>();
	public SelectionGroup()
	{
		ArrayList<Shape> list = new ArrayList<>();
		ObservableList<Shape> observableList = (ObservableList<Shape>) FXCollections.observableArrayList(list);
		items = new SimpleListProperty<Shape>(observableList);

		//Unused, leaving for future use?
		itemsProperty().addListener(new ListChangeListener<Shape>() {
			@Override
			public void onChanged(Change<? extends Shape> c) {
				while (c.next()) {
					if (c.wasPermutated()) {
						for (int i = c.getFrom(); i < c.getTo(); ++i) {

						}
					} else if (c.wasUpdated()) {
						for (int i = c.getFrom(); i < c.getTo(); ++i) {
						}
					} else {
						for (Shape removedItem : c.getRemoved()) {

						}
						for (Shape addedItem : c.getAddedSubList()) {

						}
					}
				}

			}
		});
	}

	public SimpleListProperty<Shape> itemsProperty()
	{
		return items;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("********Selection Group********\n");
		for (Shape s : items) {
			sb.append(s.toString()).append('\n');
		}
		return sb.toString();
	}
}



