import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class Main extends Application{
    public static void main(String[] args) throws IOException, InterruptedException, ExecutionException {


        launch(args);

    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("GUI.fxml"));
        primaryStage.setTitle("Smart Summary");
        primaryStage.setScene(new Scene(root, 730, 500));
        primaryStage.show();
    }



}
