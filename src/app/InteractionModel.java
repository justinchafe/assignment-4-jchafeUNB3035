package app;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

public class InteractionModel {
    private SelectionGroup selectedShapes;
    final SimpleObjectProperty<Rectangle> selectRegion = new SimpleObjectProperty<>();
    final SimpleStringProperty toggledShape = new SimpleStringProperty(); //"circle","square" or "triangle"
    private int corner = 0; //1,2,3,4 represents four corners of a square. 0 is default state.

    public InteractionModel() {
        selectedShapes = new SelectionGroup();
    }

    public void setToggledShape(String shapeType) {
        toggledShape.set(shapeType);
    }

    public String getToggledShape() {
        return toggledShape.get();
    }

    public SimpleStringProperty getToggledShapeProperty() {
        return toggledShape;
    }

    public SimpleObjectProperty<Rectangle> selectRegionProperty() {
        return selectRegion;
    }

    /**
     * This method gets the currently selected shapes.
     * @return - a SelectionGroup representing the group of shapes currently selected.
     */
    public SelectionGroup getSelectedShapes() {
        return selectedShapes;
    }

    /**
     * Defines a selection region (square).
     * @param x - the initial x coordinate
     * @param y = the initial y coordinate
     */
    public void setSelectRegion(double x, double y) {
        corner = 0; //reset the starting corner of the region (square)
        selectRegion.set(new Rectangle(x, y, 0, 0));
    }

    public Rectangle getSelectRegion() {
        return selectRegion.get();
    }

    /**
     * Clears all selected items which are visible.
     * Do not use "isVisible()/setVisible(bool)" from Node to ensure deletion in model class
     */
    public void clearVisible() {
        selectedShapes.itemsProperty().removeIf(s -> Main.model.shapeListProperty().contains(s));
    }

    /**
     * Clears selected items which are no longer in the model.
     * Do not use "isVisible()/setVisible(bool)" to ensure deletion in model class.
     */
    public void clearHidden() {
        selectedShapes.itemsProperty().removeIf(s -> !Main.model.shapeListProperty().contains(s));
    }

    /**
     * Checks a shape to see if it is visible, not enough to use
     * "isVisible()/setVisible(bool)" as we would like it removed from model
     * @param s the shape to check
     * @return true if visible, false if not visible.
     *
     *
     */
    public boolean isVisible(Shape s) {
        return Main.model.shapeListProperty.contains(s);
    }

    public void clearAll() {
        selectedShapes.itemsProperty().clear();
    }

    /**
     * Selects a region by creating a square.  Corner is locked on first use. Enables square drawing from initial
     * corner to opposite corner. From the opposite corner back to the initial corner.
     * @param x initial x coordinate
     * @param y initial y coordinate
     */
    public void updateSelectRegion(double x, double y) {
        Rectangle selectRect = selectRegionProperty().getValue();
        double newW, newH;
        switch (corner) {
            case 0:
                if (selectRect.getX() < x && selectRect.getY() < y) {
                    corner = 1;
                } else if (selectRect.getX() > x && selectRect.getY() > y) {
                    corner = 2;
                } else if (selectRect.getX() < x && selectRect.getY() > y) {
                    corner = 3;
                } else if (selectRect.getX() > x && selectRect.getY() < y) {
                    corner = 4;
                }
                break;
            case 1:
                selectRect.setWidth(x - selectRect.getX());
                selectRect.setHeight(y - selectRect.getY());
                break;
            case 2:
                newW = selectRect.getX() - x;
                newH = selectRect.getY() - y;
                selectRect.setWidth(newW + selectRect.getWidth());
                selectRect.setHeight(newH + selectRect.getHeight());
                selectRect.setX(x);
                selectRect.setY(y);
                break;
            case 3:
                newH = selectRect.getY() - y;
                selectRect.setWidth(x - selectRect.getX());
                selectRect.setHeight(newH + selectRect.getHeight());
                selectRect.setY(y);
                break;
            case 4:
                newW = selectRect.getX() - x;
                selectRect.setHeight(y - selectRect.getY());
                selectRect.setWidth(newW + selectRect.getWidth());
                selectRect.setX(x);
                break;
        }
    }
}



