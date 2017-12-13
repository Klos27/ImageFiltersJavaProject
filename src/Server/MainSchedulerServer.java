package Server;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public class MainSchedulerServer extends Application {

    //This is our PrimaryStage (It contains everything)
    private Stage primaryStage;

    //This is the BorderPane of RootLayout
    private BorderPane primaryLayout;


    @Override
    public void start(Stage primaryStage) throws Exception{

        //1) Declare a primary stage (Everything will be on this stage)
        this.primaryStage = primaryStage;

        //Set a title for primary stage
        this.primaryStage.setTitle("SchedulerServer");

        //2) Initialize primaryLayout
        initPrimaryLayout();

        //3) Display the ProcessingServer View
        showServerView();
    }

    //Initializes the root layout.
    public void initPrimaryLayout() {
        try {
            //First, load root layout from primaryLayout.fxml
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainProcessingServer.class.getResource("view/PrimaryLayout.fxml"));
            primaryLayout = (BorderPane) loader.load();

            //Second, show the scene containing the root layout.
            Scene scene = new Scene(primaryLayout); //We are sending primaryLayout to the Scene.
            primaryStage.setScene(scene); //Set the scene in primary stage.

            //Third, show the primary stage
            primaryStage.setFullScreenExitHint("SchedulerServer");
            // primaryStage.setFullScreen(true);
//            primaryStage.setAlwaysOnTop(true);
            primaryStage.show(); //Display the primary stage
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Shows ProcessingServer view in the middle of the screen
    public void showServerView() {
        try {
            //load ServerView
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainProcessingServer.class.getResource("view/SchedulerServerLayout.fxml"));
            AnchorPane ServerView = (AnchorPane) loader.load();

            // Set ServerrView into the center of root layout.
            primaryLayout.setCenter(ServerView);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}


