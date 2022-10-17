/*
Names: Blaise Willson, Vingnesh Venkat
java version: java 16
javafx version: javafx 19
 */

package SongLib.app;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;

public class SongLib extends Application{
    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) throws Exception {

        // create FXML loader
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/SongLib/view/SongLibScene1.fxml"));

        // load the parent anchor plane
        Parent root = loader.load();

        // set scene to root
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        launch(args);
    }

}
