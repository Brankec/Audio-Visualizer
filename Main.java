import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    Globals global;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        global.primaryStage = primaryStage;
        global.primaryStage.setTitle("Audio Visualizer");
        global.primaryStage.setResizable(false);
        Group root = new Group();
        global.root = root;
        Parent fxml = FXMLLoader.load(getClass().getResource("View.fxml"));

        global.root.getChildren().add(fxml);
        global.primaryStage.setScene(new Scene(global.root, 1920/2.5, 1080/2.5));
        global.primaryStage.show();
    }
}
