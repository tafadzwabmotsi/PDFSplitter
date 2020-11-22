package others.alerts;

import javafx.scene.control.Alert;
import javafx.scene.control.DialogPane;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Alerting {

    private static final String STYLE_PATH = "../styles/style.css";
    private static final String INFORMATION_ICON = "information_icon.png";

    private final Alert alert;

    public void setAlertIcon(Stage stage, String iconName){
        stage.getIcons().setAll(new Image(String.valueOf(getClass().getResource(iconName))));
    }
    public Alerting(Alert.AlertType alertType, String alertTitle, String alertContextText){
        this.alert = new Alert(alertType);
        this.alert.setTitle(alertTitle);
        this.alert.setHeaderText("");
        this.alert.setContentText(alertContextText);

        DialogPane alertDialogPane = this.alert.getDialogPane();

        //this.setAlertIcon((Stage)alertDialogPane.getScene().getWindow(), INFORMATION_ICON);

        alertDialogPane.getStylesheets().add(getClass().getResource(STYLE_PATH).toExternalForm());
        alertDialogPane.getStyleClass().addAll("grid-pane", "alert-text");
    }

    public void getAlert(){
        this.alert.showAndWait();
    }
}
