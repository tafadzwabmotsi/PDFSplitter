package uis;

import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import others.filesdirs.FilesDirsOpener;
import others.helpers.CloseButton;
import others.helpers.TextFieldValidation;
import others.helpers.WindowMovements;
import others.restartapp.RestartApp;
import pdfsplitting.PDFSplit;

import java.io.File;
import java.io.IOException;

public class RangeSelection extends Welcome{

    private static final String RANGE_SELECTION_FXML_PATH = "../fxml/range_selection.fxml";
    private final FXMLLoader fxmlLoader;
    private final Stage rangeSelectionStage;


    public VBox containerVBox;
    public TextField fromPageTextField;
    public TextField toPageTextField;
    public ImageView okButton;

    //private final PDFSplit pdfSplit;

    public RangeSelection() {
        this.rangeSelectionStage = new Stage();
        this.fxmlLoader = new FXMLLoader();
        this.fxmlLoader.setLocation(RangeSelection.class.getResource(RANGE_SELECTION_FXML_PATH));
        this.fromPageTextField = new TextField();
        this.toPageTextField = new TextField();
        this.okButton = new ImageView();
    }


    public void setSceneAttributes(Scene scene){
        this.fromPageTextField = (TextField)scene.lookup("#fromPageTextField");
        this.toPageTextField = (TextField)scene.lookup("#toPageTextField");
        this.okButton = (ImageView) (scene).lookup("#okButton");

    }

    //get the text fields
    public TextField getFromPageTextField() {
        return this.fromPageTextField;
    }

    public TextField getToPageTextField() {
        return this.toPageTextField;
    }

    public void initializeRangeSelectionStage(Stage ownerStage, File selectedFile, Welcome welcome) throws IOException {
        this.rangeSelectionStage.initOwner(ownerStage);
        this.rangeSelectionStage.initModality(Modality.APPLICATION_MODAL);
        this.rangeSelectionStage.initStyle(StageStyle.UNDECORATED);
        this.rangeSelectionStage.setScene(this.getRangeRangeSelectionScene());
        this.rangeSelectionStage.setResizable(false);
        this.rangeSelectionStage.centerOnScreen();
        this.rangeSelectionStage.show();

        this.okButtonClicked(selectedFile, welcome);
    }

    //range of pages splitting
    private void splitRange(Welcome welcome, int numberOfPages, File selectedFile, Event event) throws Exception {
        try {
            if (this.isValidatePageRanges(this.getFromPageTextField(), this.getToPageTextField(), numberOfPages)) {

                //pdf split object instantiation
                PDFSplit pdfSplit = new PDFSplit(
                        selectedFile.getAbsolutePath(),
                        FilesDirsOpener.getSelectedDirectory(event).getAbsolutePath(),
                        this.getPageNumber(this.getFromPageTextField()),
                        this.getPageNumber(this.getToPageTextField())
                );

                pdfSplit.pdfRangeSplitting(welcome);
            }
        }
        catch (Exception ex){
            this.getRangeSelectionStage().show();
        }
    }

    //handle ok button on click
    private void okButtonClicked(File selectedFile, Welcome welcome){
        //set scene attributes
        this.setSceneAttributes(this.rangeSelectionStage.getScene());
        welcome.setHyperlinksVisibility(false, false, false);


        this.okButton.setOnMouseClicked(
                event -> {
                    if (event.getButton().toString().toLowerCase().equals("primary")){
                        this.getRangeSelectionStage().close();

                        try {
                            this.splitRange(welcome,
                                    new PDFSplit(
                                    selectedFile.getAbsolutePath()).getNumberOfPages(),
                                    selectedFile, event
                            );
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                        this.getRangeSelectionStage().close();
                }
        );
    }

    public Stage getRangeSelectionStage() {
        return this.rangeSelectionStage;
    }

    public Scene getRangeRangeSelectionScene() throws IOException {
        return new Scene(this.fxmlLoader.load(), 600, 200);
    }

    public void topBarHBoxOnMouseDraggedHandler(MouseEvent mouseEvent) {
       WindowMovements.onMouseDraggedHandler(mouseEvent);
    }

    public void topBarHBoxOnMousePressedEventHandler(MouseEvent mouseEvent) {
        WindowMovements.onMousePressedEventHandler(mouseEvent);
    }

    public void closeButtonOnMouseClickedHandler(MouseEvent mouseEvent) {
        CloseButton.closeStage(mouseEvent);
    }

    public void backButtonOMouseClickedHandler(MouseEvent mouseEvent) {
        CloseButton.closeStage(mouseEvent);
    }

    public void fromPageHBoxOnMouseDraggedHandler(MouseEvent mouseEvent) {
        WindowMovements.onMouseDraggedHandler(mouseEvent);
    }

    public void fromPageHBoxOnMousePressedHandler(MouseEvent mouseEvent) {
        WindowMovements.onMousePressedEventHandler(mouseEvent);
    }

    public void toPageHBoxOnMouseDraggedHandler(MouseEvent mouseEvent) {
        WindowMovements.onMouseDraggedHandler(mouseEvent);
    }

    public void toPageHBoxOnMousePressedHandler(MouseEvent mouseEvent) {
        WindowMovements.onMousePressedEventHandler(mouseEvent);
    }

    public void bottomHBoxOnMouseDraggedHandler(MouseEvent mouseEvent) {
        WindowMovements.onMouseDraggedHandler(mouseEvent);
    }

    public void bottomHBoxOnMousePressedHandler(MouseEvent mouseEvent) {
        WindowMovements.onMousePressedEventHandler(mouseEvent);
    }

    public void containerVBoxOnMouseDraggedHandler(MouseEvent mouseEvent) {
        WindowMovements.onMouseDraggedHandler(mouseEvent);
    }

    public void containerVBoxOnMousePressedHandler(MouseEvent mouseEvent) {
        WindowMovements.onMousePressedEventHandler(mouseEvent);
    }

    //get the integer values of the input fields
    private int getPageNumber(TextField textField){
        return Integer.parseInt(textField.getText());
    }

    //validate text field
    private boolean isValidatePageRanges(TextField fromPageTextField, TextField toPageTextField, int numberOfPages) {
        if (TextFieldValidation.isTextFieldValid(fromPageTextField) && TextFieldValidation.isTextFieldValid(toPageTextField))
            return (this.getPageNumber(fromPageTextField) + this.getPageNumber(toPageTextField) ) <= (numberOfPages + 1);
        return false;
    }

}
