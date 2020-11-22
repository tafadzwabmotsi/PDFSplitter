package others.helpers;

import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import others.xycoordinates.XYCoordinates;


public class WindowMovements {

    private static final XYCoordinates xyCoordinates = new XYCoordinates();

    private static Stage getWindowStage(MouseEvent mouseEvent){
        return (Stage)((Node) mouseEvent.getSource()).getScene().getWindow();
    }

    public static void onMousePressedEventHandler(MouseEvent mouseEvent){
            xyCoordinates.setXYCoordinates(mouseEvent.getSceneX(), mouseEvent.getSceneY());
    }

    public static void onMouseDraggedHandler(MouseEvent mouseEvent) {
        Stage stage = WindowMovements.getWindowStage(mouseEvent);
        stage.setX(mouseEvent.getScreenX() - xyCoordinates.getXCoordinate());
        stage.setY(mouseEvent.getScreenY() - xyCoordinates.getYCoordinate());
    }
}
