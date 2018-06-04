import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.media.*;

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
        Parent fxml = FXMLLoader.load(getClass().getResource("View.fxml"));

        global.primaryStage.setScene(new Scene(fxml, 1920/2.5, 1080/2.5));
        global.primaryStage.show();
    }
}
