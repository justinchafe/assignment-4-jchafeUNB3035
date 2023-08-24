package app;

import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class MenuView {
    private ToggleButton sqTb,circTb,triTb;
    private Button cutB, pasteB;
    final ToolBar tbar;

    public MenuView() {
        tbar = createToolBar();
    }

    /**
     * Returns a menu created by this class.
     * @return the ToolBar created by this class.
     */
    public ToolBar getMenuView() {
        return tbar;
    }

    /**
     * Creates a specific ToolBar menu system.
     * @return a ToolBar for use a menu.
     */
    private ToolBar createToolBar() {
        final ToggleGroup tGroup = new ToggleGroup();
        final ToolBar toolBar = new ToolBar();
        sqTb = createToggleButton(new Image(getClass().getClassLoader().getResourceAsStream("images/square.png")), tGroup);
        circTb = createToggleButton(new Image(getClass().getClassLoader().getResourceAsStream("images/circle.png")), tGroup);
        triTb = createToggleButton(new Image(getClass().getClassLoader().getResourceAsStream("images/triangle.png")), tGroup);
        sqTb.setSelected(true);
        cutB = createButton(new Image(getClass().getClassLoader().getResourceAsStream("images/cut.png")), "Cut");
        pasteB = createButton(new Image(getClass().getClassLoader().getResourceAsStream("images/paste.png")), "Paste");
        toolBar.getItems().addAll(sqTb,circTb,triTb,new Separator(), cutB, pasteB);
        return toolBar;
    }

    /**
     * Creates a ToggleButton
     * @param img the image for the ToggleButton
     * @param tGroup the group to place the ToggleButton in.
     * @return a ToggleButton
     */
    private ToggleButton createToggleButton(Image img, ToggleGroup tGroup) {
        ToggleButton tb = new ToggleButton("" ,new ImageView(img));
        tb.setToggleGroup(tGroup);
        return tb;
    }

    /**
     * Creates a Button
     * @param img the image for the button
     * @param tooltip the tooltip for the button
     * @return a Button
     */
    private Button createButton(Image img, String tooltip) {
        Button b = new Button("" ,new ImageView(img));
        b.setTooltip(new Tooltip(tooltip));
        return b;
    }

    public ToggleButton getSquareButton() {
        return sqTb;
    }

    public ToggleButton getCircleButton() {
        return circTb;
    }

    public ToggleButton getTriangleButton() {
        return triTb;
    }

    public Button getCutButton() {
        return cutB;
    }

    public Button getPasteButton() {
        return pasteB;
    }


}
