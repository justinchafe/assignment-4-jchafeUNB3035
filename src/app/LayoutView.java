package app;

import javafx.scene.layout.BorderPane;

/**
 * Class LayoutView is used versus declaring a BorderPane in Main.  We might add or extend to this class in future which might make
 * adding and removing views from the scene easier.
 * Note:  Initially I had declared everything in one view, but I prefer the separation.
 */
public class LayoutView {
    private BorderPane bPane;

    /**
     * Constructor for the main layout for the application.  Views are added to their appropriate positions in a BorderPane.
     * @param menu - a MenuView object which represents the menu system, added to the top of the BorderPane.
     * @param centerView a View object which represents the area for adding and manipulating nodes.  Added to the center of the BorderPane.
     */
    public LayoutView(MenuView menu, View centerView) {
        bPane = new BorderPane();
        bPane.setTop(menu.getMenuView());
        bPane.setCenter(centerView);
    }

    /**
     * Gets the overall combined Layout for this application.
     * @return - a BorderPane representing the View components that make up this application.
     */
    public BorderPane getLayoutView() {
        return bPane;
    }
}
