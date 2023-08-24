package app;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    public static Model model = new Model(50);
    public static InteractionModel iModel = new InteractionModel();
    public static MenuView menuView = new MenuView();
    public static View view = new View();
    public static Controller controller = new Controller();

    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage primaryStage) {
        Scene scene = new Scene(new LayoutView(menuView,view).getLayoutView(), 500, 500);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Assignment 4");
        primaryStage.show();
    }
}