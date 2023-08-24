package app;


import java.util.ArrayList;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

public class Model {
	final SimpleListProperty<Shape> shapeListProperty;
	private double length;

	public Model(int length) {
		ArrayList<Shape> list = new ArrayList<>();
		ObservableList<Shape> observableList = (ObservableList<Shape>) FXCollections.observableArrayList(list);
		shapeListProperty = new SimpleListProperty<>(observableList);
		this.length = length;
	}

	public SimpleListProperty<Shape> shapeListProperty() {
		return shapeListProperty;
	}

	/**
	 * Length represents different things depending on the Shape.  Generally it is either the Height or Diameter of the object.
	 * @return the Length variable of the Shape object.
	 */
	public double getShapeLength() {
		return length;
	}

	/**
	 * Adds a square to the model
	 * @param x x coordinate of the square at center
	 * @param y y coordinate of the square at center
	 */
	public void addSquare(double x, double y) {
		double squareX = x - length/ 2;
		double squareY = y - length / 2;
		Rectangle newSquare = new Rectangle(squareX, squareY, length,length);
		shapeListProperty.add(newSquare);
	}

	/**
	 * Adds a circle to the model
	 * @param x x coordinate of the circle at center
	 * @param y y coordinate of the circle at center
	 */
	public void addCircle(double x, double y) {
		Circle circle = new Circle(x, y, length/ 2);
		shapeListProperty.add(circle);
	}

	/**
	 * Adds a triangle (polygon) to the model
	 * @param x x coordinate of the triangle at ordinal.
	 * @param y y coordinate of the triangle at ordinal.
	 */
	public void addTriangle(double x, double y) {
		//length = height:
		double centerToBottom = length/3; //medians intersect at a point which is (1/3)rd the length of the median, measured from the respective side.
		double centerToTop = 2*length/3; //medians intersect at a point which is (2/3)rd the length of the median, measured from the respective vertex.
		double sideLength = length/(Math.sqrt(3)/2);
		Point2D peak = new Point2D(x, y - centerToTop);
		Point2D rBase = new Point2D(x + sideLength/2, y+centerToBottom); //x - medians bisect the side which they meet.
		Point2D lBase = new Point2D(x - sideLength/2, y+centerToBottom); //x - medians bisect the side which they meet.
		Polygon polygon = new Polygon();
		polygon.getPoints().addAll(
				lBase.getX(), lBase.getY(),
				peak.getX(), peak.getY(),
				rBase.getX(), rBase.getY());
		shapeListProperty.add(polygon);
	}

	/**Removes shape at specified coordinates
	 *
	 * @param x x coordinate
	 * @param y y coordinate
	 */
	public void deleteShapeAt(int x, int y) {
		Shape delShape = getShapeAt(x, y);
		shapeListProperty.remove(delShape);
	}

	/**
	 * Returns the shape at specified coordinates
	 * @param x x coordinate
	 * @param y y coordinate
	 * @return shape at specified coordinate
	 */
	private Shape getShapeAt(int x, int y) {
		Shape shape = null;

		for (Shape s : shapeListProperty) {
			if (s.contains(x, y)) {
				shape = s;
			}
		}

		return shape;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("********SHAPES********\n");
		for (Shape s : shapeListProperty) {
			if (s instanceof Rectangle) {
				Rectangle r = (Rectangle) s;
				sb.append("Rectangle at: ").append("x: ").append(r.getX()).append(", y: ").append(r.getY())
						.append(", Width: ").append(r.getWidth()).append(", Height: ").append(r.getHeight());
			} else if (s instanceof Circle) {
				Circle c = (Circle) s;
				sb.append("Circle at: ").append("centerX: ").append(c.getCenterX()).append(", centerY: ").append(c.getCenterY())
						.append(", Radius: ").append(c.getRadius());
			} else if (s instanceof Polygon) {
				Polygon p = (Polygon) s;
				sb.append("Polygon at: ").append(p.getPoints());
			}
			sb.append('\n');
		}
		return sb.toString();
	}

	/**
	 * Calculates the length of a line given coordinates.
	 * Used to double-check for equilateral triangle. Unnecessary for model.
	 * @param p1 - the start point of the line
	 * @param p2 - the end point of the line.
	 * @return - the length of the line.
	 */
	public static double lineLength(Point2D p1, Point2D p2) {
		double xCalc = Math.pow(p2.getX() - p1.getX(), 2);
		double yCalc = Math.pow(p2.getY() - p1.getY(), 2);
		return Math.sqrt(xCalc+yCalc);
	}
}






