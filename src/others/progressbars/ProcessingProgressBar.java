package others.progressbars;

import others.alerts.Alerting;
import javafx.concurrent.Task;
import javafx.scene.control.Alert;
import javafx.scene.control.ProgressBar;
import others.restartapp.RestartApp;

public class ProcessingProgressBar {
    private static final String STYLE_PATH = "../styles/style.css";

    public static void progressBarThread(Task<Void> task){

        ProgressBar convertingDocumentsProgressBar = new ProgressBar();
        convertingDocumentsProgressBar.setPrefWidth(200);
        convertingDocumentsProgressBar.getStylesheets().add(ProcessingProgressBar.class.getResource(STYLE_PATH).toExternalForm());
        convertingDocumentsProgressBar.progressProperty().bind(task.progressProperty());

        //stackPane.getChildren().add(convertingDocumentsProgressBar);

        task.setOnSucceeded(event -> {
            //stackPane.getChildren().remove(convertingDocumentsProgressBar);
            new Alerting(Alert.AlertType.INFORMATION,
                    "Information",
                    "Conversion completed successfully").getAlert();

            try {
                RestartApp.restartApp();
            } catch (Exception e) {
                e.printStackTrace();
            }

        });

        new Thread(task).start();
    }
}
