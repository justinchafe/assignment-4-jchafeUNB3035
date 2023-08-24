package app;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.scene.Group;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

public class View extends Pane {
	public static final Color FILL_COLOR = Color.GREEN;
	public static final Color SELECTED_FILL_COLOR = Color.BLUE;
	private static Group root;

	public View() {

		//Listens for changes in the model's list, along with the underlying objects in the list.
		//Adds items to group to render on screen:
		Main.model.shapeListProperty().addListener(new ListChangeListener<Shape>() {
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
							root.getChildren().remove(removedItem);
						}
						for (Shape addedItem : c.getAddedSubList()) {
							addedItem.setStroke(Color.BLACK);
							//If we paste an item, it will get added back into Main. Ensure it is colored appropriately.
							if (Main.iModel.getSelectedShapes().itemsProperty().contains(addedItem))
								addedItem.setFill(SELECTED_FILL_COLOR);
							else
								addedItem.setFill(FILL_COLOR);
							root.getChildren().add(addedItem);
							//System.out.println("Adding Shape: " + Main.model.toString()); //Uncomment for debug info.
							//System.out.println(Main.iModel.getSelectedShapes().toString()); //Uncomment for debug info.
						}
					}
				}
			}
		});

		//Listens for changes in the selected shapes. Colors selected items in the group appropriately:
		Main.iModel.getSelectedShapes().itemsProperty().addListener(new ListChangeListener<Shape>() {
			@Override
			public void onChanged(Change<? extends Shape> c) {
				deselectAll();
				for (Shape s : Main.iModel.getSelectedShapes().itemsProperty()) {
						selectShape(s);
						//System.out.println(Main.iModel.getSelectedShapes().toString()); //Uncomment for debug info.
				}
			}
		});

		//Listens for changes in the selectedRegion. Renders the rectangle (selectedRegion) on screen:
		Main.iModel.selectRegionProperty().addListener(new ChangeListener<Rectangle>() {
			@Override
			public void changed(ObservableValue<? extends Rectangle> observable, Rectangle oldValue,
								Rectangle newValue) {
				root.getChildren().remove(oldValue);

				if (newValue != null) {
					root.getChildren().add(newValue);
					newValue.setFill(new Color(0, 0, .5, .3));
					newValue.setStroke(new Color(0, 0, .5, 1));
				}
			}
		});

		//creates a Group named root to store the nodes and adds it to this Pane (view).
		root = new Group();
		getChildren().add(root);
	}

	@Override
	public void layoutChildren() {
		super.layoutChildren();
	}

	/**
	 * Deselects a Shape by resetting the Color to default (FILL_COLOR).
	 * @param s the Shape object to deselect.
	 */
	public void deselect(Shape s)
	{
		s.setFill(FILL_COLOR);
		s.setStrokeWidth(1);
	}

	/**
	 * Deselects all Shapes in the model by resetting their color to default (FILL_COLOR)
	 */
	public void deselectAll() {
		for (Shape s : Main.model.shapeListProperty())
		{
			deselect(s);
		}
	}

	/**
	 * Selects a shape by setting it's Color to the specified selected Color (SELECTED_FILL_COLOR).
	 * @param node - the Shape to select.
	 */
	public void selectShape(Shape node) {
		node.setFill(View.SELECTED_FILL_COLOR);
		node.setStrokeWidth(4);
	}



	}

