import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Globals.primaryStage = primaryStage;
        Globals.primaryStage.setTitle("Audio Visualizer");
        Globals.primaryStage.setResizable(false);
        Group root = new Group();
        Globals.root = root;
        Parent fxml = FXMLLoader.load(getClass().getResource("View.fxml"));

        Globals.root.getChildren().add(fxml);
        Globals.primaryStage.setScene(new Scene(Globals.root, 1920/2.5 - 9, 1080/2.5 - 9));
        Globals.primaryStage.show();
    }
}
