package app;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

import java.util.ArrayList;

/**
 * Interface for implementing State pattern.
 */
interface State {
	void ready(MouseEvent e); //waiting for input
	void dragItems(MouseEvent e); //dragging on a node or the view itself
	void endAction(MouseEvent e); //transition state (usually mouse released)
	void dragItemsStarted(MouseEvent e); //not really necessary.
}

public class Controller implements State {

	private double prevX = 0, prevY = 0;
	private SelectionGroup cutGroup;

	//Controller is used as the context:
	public void endAction(MouseEvent e) {
		currentState.endAction(e);
	}
	public void ready(MouseEvent e) {
		currentState.ready(e);
	}
	public void dragItemsStarted(MouseEvent e) { currentState.dragItemsStarted(e);}
	public void dragItems(MouseEvent e) {
		currentState.dragItems(e);
	}

	//The initial State:
	State ready = new State() {
		public void ready(MouseEvent e) {
			prevX = e.getSceneX();
			prevY = e.getSceneY();
			if (Shape.class.isAssignableFrom(e.getTarget().getClass())) {
				Shape node = ((Shape) e.getTarget());
				node.toFront();
				if (!Main.iModel.getSelectedShapes().itemsProperty().contains(node)) {
					if (!e.isControlDown()) {
						Main.iModel.clearVisible(); //pressed on node, clear selected nodes
					}
					Main.iModel.getSelectedShapes().itemsProperty().add(node); //select the node.
				} else if (e.isControlDown()) {
					Main.iModel.getSelectedShapes().itemsProperty().remove(node); //remove a previously selected node
				}
			}
			e.consume();
		}
		public void dragItemsStarted(MouseEvent e) {
			e.consume();
		}
		public void dragItems(MouseEvent e) {
			if (Shape.class.isAssignableFrom(e.getTarget().getClass())) {
				currentState = dragItems;

			} else if (e.getTarget().getClass() == Main.view.getClass()) {
				Main.iModel.setSelectRegion(e.getX(), e.getY());
				Main.iModel.clearVisible();
				System.out.println("Dragged, switching state to dragSelection");
				currentState = dragSelection;
			}
		}
		public void endAction(MouseEvent e) {
			if (e.getTarget().getClass() == Main.view.getClass()) {
				switch (Main.iModel.getToggledShape()) {
					case "square":
						Main.model.addSquare(e.getX(), e.getY());
						break;
					case "circle":
						Main.model.addCircle(e.getX(), e.getY());
						break;
					case "triangle":
						Main.model.addTriangle(e.getX(), e.getY());
						break;
				}
			} else if (Shape.class.isAssignableFrom(e.getTarget().getClass()) && !e.isControlDown())
				Main.iModel.clearVisible();
		}
	};

	//State used for selecting Shape(s) via square select motion on the view:
	State dragSelection = new State() {
		public void ready(MouseEvent e) {
		}
		public void dragItemsStarted(MouseEvent e) {
			e.consume();
		}
		public void dragItems(MouseEvent e) {
				Main.iModel.updateSelectRegion(e.getX(), e.getY());
		}
		public void endAction(MouseEvent e) {
			selectObjectsWithRegion();
			Main.iModel.selectRegionProperty().setValue(null);
			currentState = ready;
		}
	};

	//State used for dragging a selected Shape(s):
	State dragItems = new State() {
		public void ready(MouseEvent e) {
			currentState = ready;
		}
		public void dragItemsStarted(MouseEvent e) {
			e.consume();
		}
		public void dragItems(MouseEvent e) {
			moveSelected(e.getSceneX() - prevX, e.getSceneY() - prevY);
			prevX = e.getSceneX();
			prevY = e.getSceneY();
		}
		public void endAction(MouseEvent e) {
			currentState = ready;
		}
	};

	//State variable with initial state:
	State currentState = ready;

	/**
	 * No argument constructor adds event handlers to menu items, and the various handlers for State.
	 * Adds event handlers for cut-and-paste operation.
	 *
	 */
	public Controller() {
		handleToggleButton(); //radio button like behavior for toggle buttons.
		Main.menuView.getCutButton().setOnAction(e->cut());
		Main.menuView.getPasteButton().setOnAction(e->paste());
		Main.view.addEventHandler(MouseEvent.MOUSE_PRESSED, this::ready);
		Main.view.addEventHandler(MouseEvent.MOUSE_RELEASED, this::endAction);
		Main.view.addEventHandler(MouseEvent.DRAG_DETECTED, this::dragItemsStarted); //consume these events, not really needed.
		Main.view.addEventHandler(MouseEvent.MOUSE_DRAGGED, this::dragItems);
	}

	/**
	 * Creates ToggleButtons with appropriate handlers and gives them radio button like behavior.
	 *
	 */
	final void handleToggleButton() {
		Main.iModel.setToggledShape("square");
		Main.menuView.getCircleButton().setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				if (!Main.menuView.getCircleButton().isSelected())
					Main.menuView.getCircleButton().setSelected(true);
				Main.iModel.setToggledShape("circle");
			}
		});

		Main.menuView.getSquareButton().setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				if (!Main.menuView.getSquareButton().isSelected())
					Main.menuView.getSquareButton().setSelected(true);
				Main.iModel.setToggledShape("square");
			}
		});

		Main.menuView.getTriangleButton().setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				if (!Main.menuView.getTriangleButton().isSelected())
					Main.menuView.getTriangleButton().setSelected(true);
				Main.iModel.setToggledShape("triangle");
			}
		});
	}

	/**
	 * Gets the classname of the current State
	 * @return the classname of the current State class.
	 */
	public String getState() {
		return currentState.getClass().getName();
	}

	/**
	 * Moves selected shapes around the view by translation:
	 * @param addX the x amount to move the shape
	 * @param addY the y amount to move the shape
	 */
	private void moveSelected(double addX, double addY) {
		for (Shape s : Main.iModel.getSelectedShapes().itemsProperty()) {
			//we don't want to move objects that have been cut from scene:
			if (s != null && Main.iModel.isVisible(s)) {
				s.setTranslateX(s.getLayoutX() + s.getTranslateX() + addX);
				s.setTranslateY(s.getLayoutY() + s.getTranslateY() + addY);
			}
		}
	}

	/**
	 * Cuts selected images from model. Avoided use of "isVisible()/setVisible(bool)" to
	 * ensure later deletion of items from model (subsequent cuts/no paste)
	 */
	private void cut() {
		Main.iModel.clearHidden();

		for (Shape s : Main.iModel.getSelectedShapes().itemsProperty()) {
				Main.model.shapeListProperty().remove(s);
		}
		cutGroup = Main.iModel.getSelectedShapes();
	}

	/**
	 * Deselects all currently selected items. Copies clipboard items back to screen. Marks them as selected.
	 *
	 */
	private void paste() {
		ArrayList<Shape> selectedItems = new ArrayList<>();
		if (cutGroup != null) {
			for (Shape s : cutGroup.itemsProperty()) {
				if (!Main.model.shapeListProperty().contains(s)) {
					Main.model.shapeListProperty().add(s);
					selectedItems.add(s);
				}
			}
			Main.iModel.clearAll();
			for (Shape s : selectedItems) {
				Main.iModel.getSelectedShapes().itemsProperty().add(s);
			}
		}
	}

	/**
	 * Gets all the Shapes found within a given region,  If they lie in the bounds they are added to
	 * the currently selected shapes
	 */
	private void selectObjectsWithRegion() {
		Main.iModel.clearVisible();
		Rectangle selectRegion = Main.iModel.selectRegionProperty().getValue();

		if (selectRegion != null) {
			for (Shape s : Main.model.shapeListProperty()) {
				Point2D topLeft = new Point2D(s.getBoundsInParent().getMinX(), s.getBoundsInParent().getMinY());
				Point2D bottomRight = new Point2D(s.getBoundsInParent().getMaxX(), s.getBoundsInParent().getMaxY());
				if (selectRegion.contains(topLeft) && selectRegion.contains(bottomRight)) {
					Main.iModel.getSelectedShapes().itemsProperty().add(s);

				}
			}
		}
	}
}


