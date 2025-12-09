package org.foodapp;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.LoginController;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        User loggedUser = showLoginDialog(primaryStage);
        if (loggedUser == null) {
            Platform.exit();
            return;
        }

        FXMLLoader loader = new FXMLLoader(
                MainApp.class.getResource("/main-view.fxml")
        );
        Scene scene = new Scene(loader.load());

        org.example.MainController mainController = loader.getController();
        mainController.setLoggedInUser(loggedUser);

        primaryStage.setTitle("Maisto sistema - " + loggedUser.getUsername()
                + " (" + loggedUser.getRole() + ")");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private User showLoginDialog(Stage owner) throws Exception {
        FXMLLoader loader = new FXMLLoader(
                MainApp.class.getResource("/login-view.fxml")
        );
        Scene scene = new Scene(loader.load());

        Stage dialogStage = new Stage();
        dialogStage.setTitle("Prisijungimas");
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.initOwner(owner);
        dialogStage.setScene(scene);

        LoginController controller = loader.getController();
        controller.setDialogStage(dialogStage);

        dialogStage.showAndWait();

        return controller.getLoggedInUser();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
