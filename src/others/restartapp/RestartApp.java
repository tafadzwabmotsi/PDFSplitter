package others.restartapp;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.stage.Stage;
import uis.Welcome;

public class RestartApp {
    public static void restartApp() throws Exception{
        Stage stage = Welcome.getStage();

        Welcome welcome = new Welcome();
        stage.setScene(welcome.welcomeScene());
        RestartApp.setHyperlinksToFalse(welcome.getScene());
        stage.show();
    }

    private static void setHyperlinksToFalse(Scene scene){
        Hyperlink splitAllPagesHyperLink = (Hyperlink)scene.lookup("#splitAllPagesHyperLink");
        Hyperlink splitRangeHyperLink = (Hyperlink)scene.lookup("#splitRangeHyperLink");
        Hyperlink dropFileHyperlink = (Hyperlink)scene.lookup("#dropFileHyperlink");


        splitAllPagesHyperLink.setVisible(false);
        splitRangeHyperLink.setVisible(false);

        Platform.runLater(() -> dropFileHyperlink.setVisible(true));
    }
}
