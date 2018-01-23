package SchedulerServer;

import SchedulerServer.model.SchedulerServer;
import SchedulerServer.controller.SchedulerServerController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

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

        //3) Display the SchedulerServer View
        showServerView();
    }

    //Initializes the root layout.
    public void initPrimaryLayout() {
        try {
            //First, load root layout from primaryLayout.fxml
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainSchedulerServer.class.getResource("view/PrimaryLayout.fxml"));
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
            loader.setLocation(MainSchedulerServer.class.getResource("view/SchedulerServerLayout.fxml"));
            BorderPane ServerView = (BorderPane) loader.load();

            // Set Server View into the center of root layout.
            primaryLayout.setCenter(ServerView);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void stop(){
        System.out.println("App is closing");
        if(SchedulerServer.isServerRunning) {
            if (!SchedulerServerController.safeServerClose) {
                Alert exitAlert = new Alert(Alert.AlertType.CONFIRMATION);
                exitAlert.setTitle("Server Shutdown");
                exitAlert.setHeaderText("WARNING! By pressing X you delete all server connections");
                exitAlert.setContentText("Next time press \"Safe shutdown server\", except pressing X. Do you want to wait until all server connections will end? If you choose to kill all connections, that some clients possibly get error!");

                ButtonType buttonTypeOne = new ButtonType("Wait");
                ButtonType buttonTypeCancel = new ButtonType("Kill all connections", ButtonBar.ButtonData.CANCEL_CLOSE);
                exitAlert.getButtonTypes().setAll(buttonTypeOne, buttonTypeCancel);
                Optional<ButtonType> result = exitAlert.showAndWait();
                if (result.get() == buttonTypeOne) {
                    System.out.println("Wait till all threads are ended");
                    SchedulerServerController.stopServers();
                } else {
                    System.out.println("TERMINATE ALL THREADS!");
                    SchedulerServerController.stopServers();
                    System.exit(0);
                }
            }
        }
    }
    public static void main(String[] args) {
        launch(args);
    }
}


