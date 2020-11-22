package others.helpers;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class CloseButton {
    public static void closeApplication(MouseEvent mouseEvent){
        if (mouseEvent.getButton().toString().toLowerCase().equals("primary"))
            Platform.exit();
    }

    public static void closeStage(MouseEvent mouseEvent){
        if (mouseEvent.getButton().toString().toLowerCase().equals("primary")){
            Stage stage = (Stage)((Node) mouseEvent.getSource()).getScene().getWindow();
            stage.close();
        }
    }

}
