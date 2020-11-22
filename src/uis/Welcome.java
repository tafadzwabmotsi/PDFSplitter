package uis;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import others.filesdirs.FilesDirsOpener;
import others.helpers.CloseButton;
import others.helpers.WindowMovements;
import others.progressbars.ProcessingProgressBar;
import others.restartapp.RestartApp;
import pdfsplitting.PDFSplit;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class Welcome extends Application {

    private static final String WELCOME_SCREEN_FXML_PATH = "../fxml/welcome.fxml";

    private final FXMLLoader fxmlLoader;
    public ProgressBar splitPagesProgressBar;
    public Button launchFilesButton;
    public Label splittingProgressLabel;
    private Scene scene;
    public static Stage stage;

    public Hyperlink splitAllPagesHyperLink;
    public Hyperlink splitRangeHyperLink;
    public Hyperlink dropFileHyperlink;

    private TextField fromPageTextField;
    private TextField toPageTextField;

    public Welcome(){

        this.fxmlLoader = new FXMLLoader();
        this.fxmlLoader.setLocation(Welcome.class.getResource(WELCOME_SCREEN_FXML_PATH));
        this.splitPagesProgressBar = new ProgressBar(0);

        this.instantiateHyperlinks();
    }

    @Override
    public void init() throws Exception {
        super.init();
    }

    //set the text fields
    public void setTextFields(TextField fromPageTextField, TextField toPageTextField){
        this.fromPageTextField = fromPageTextField;
        this.toPageTextField = toPageTextField;
    }

    //public get text fields
    public TextField getFromPageTextField(){
        return this.fromPageTextField;
    }

    public TextField getToPageTextField(){
        return this.toPageTextField;
    }

    private void instantiateHyperlinks(){
        this.splitAllPagesHyperLink = new Hyperlink();
        this.splitRangeHyperLink = new Hyperlink();
        this.dropFileHyperlink = new Hyperlink();
    }

    public void start(Stage primaryStage) throws Exception {
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.setScene(this.welcomeScene());
        primaryStage.setResizable(false);
        primaryStage.centerOnScreen();
        primaryStage.show();

        this.setWelcomeSceneAttributes(this.scene);
        this.setHyperlinksVisibility(false, false, true);
        this.setProgressBarVisibility(false);
        this.setLaunchFilesButtonVisibility(false);
        this.setSplittingProgressLabelVisibility(false);

        Welcome.stage = primaryStage;
    }

    //get the scene
    public Scene welcomeScene() throws Exception{
        this.scene = new Scene(this.fxmlLoader.load(), 800, 400);
        return this.scene;
    }

    public Scene getScene() {
        return this.scene;
    }

    public static Stage getStage() {
        return Welcome.stage;
    }

    private void setWelcomeSceneAttributes(Scene scene){
        this.splitAllPagesHyperLink = (Hyperlink)scene.lookup("#splitAllPagesHyperLink");
        this.splitRangeHyperLink = (Hyperlink)scene.lookup("#splitRangeHyperLink");
        this.dropFileHyperlink = (Hyperlink)scene.lookup("#dropFileHyperlink");
        this.splitPagesProgressBar = (ProgressBar)scene.lookup("#splitPagesProgressBar");
        this.launchFilesButton = (Button)scene.lookup("#launchFilesButton");
        this.splittingProgressLabel = (Label)scene.lookup("#splittingProgressLabel");
    }

    private void setProgressBarVisibility(boolean visibility){
        this.splitPagesProgressBar.setVisible(visibility);
    }

    private void setLaunchFilesButtonVisibility(boolean visibilityValue){
        this.launchFilesButton.setVisible(visibilityValue);
    }

    private void setSplittingProgressLabelVisibility(boolean visibilityValue){
        this.splittingProgressLabel.setVisible(visibilityValue);
    }

    public void setHyperlinksVisibility(boolean allSplitValueVisibility, boolean rangeSplitValueVisibility, boolean dropValueVisibility){
        this.splitAllPagesHyperLink.setVisible(allSplitValueVisibility);
        this.splitRangeHyperLink.setVisible(rangeSplitValueVisibility);
        this.dropFileHyperlink.setVisible(dropValueVisibility);
    }

    //Split all pages Task helper
    private void splitAllPagesTaskHelper(Event event, File selectedPDFFile) throws Exception {

       try {
           String outputFilePath = FilesDirsOpener.getSelectedDirectory(event).getAbsolutePath();

           PDFSplit pdfSplit = new PDFSplit(
                   selectedPDFFile.getAbsolutePath(),
                   outputFilePath
           );
           this.setHyperlinksVisibility(false, false, false);
           pdfSplit.pfdAllPagesSplitting(this);
       }
       catch (Exception ex){
           RestartApp.restartApp();
       }
    }

    //split all pages set on action helper function
    private void splitAllPagesHelper(Event event, File selectedPDFFile){
        this.splitAllPagesHyperLink.setOnAction(
                ev -> {
                    try {
                        this.splitAllPagesTaskHelper(event, selectedPDFFile);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
        );
    }

    //split range pages set on action helper function
    private void splitRangePagesHelper(Event event, File selectedPDFFile) {
        this.splitRangeHyperLink.setOnAction(
                ev -> {
                    RangeSelection rangeSelection = new RangeSelection();
                    try {
                        rangeSelection.initializeRangeSelectionStage(Welcome.stage, selectedPDFFile, this);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
        );
    }

    public void dropFileHyperlinkOnActionHandler(ActionEvent actionEvent) {

        File selectedPDFFile = FilesDirsOpener.getSelectedPDFFile(actionEvent);
        if (selectedPDFFile != null){
            this.setHyperlinksVisibility(true, true, false);

            this.splitAllPagesHelper(actionEvent, selectedPDFFile);
            this.splitRangePagesHelper(actionEvent, selectedPDFFile);
        }
    }

    //Test file type
    private boolean isFilePDF(String fileName){
        String[] split = fileName.split("\\\\");
        return (split[split.length-1].split("\\.")[1].toLowerCase().equals("pdf"));
    }

    //Handle drag over event
    public void welcomeVBoxOnDragOverHandler(DragEvent dragEvent) {
        if (dragEvent.getDragboard().hasFiles()){
            dragEvent.acceptTransferModes(TransferMode.ANY);

            List<File> filesList = dragEvent.getDragboard().getFiles();
            if (filesList.size() > 1){
                /*Handle the of multiple PDF files*/
            }
            else{
                if(this.isFilePDF(filesList.get(0).getAbsolutePath())){
                    this.dropFileHyperlink.setText("Drop your file here");
                }
            }
        }
        dragEvent.consume();
    }

    //Handle drag dropped event
    public void welcomeVBoxOnDragDroppedHandler(DragEvent dragEvent) {

        List<File> filesList = dragEvent.getDragboard().getFiles();
        if (filesList.size() == 1){
            File pdfFile = filesList.get(0);
            if (this.isFilePDF(pdfFile.getAbsolutePath())){
                this.setHyperlinksVisibility(true, true, false);

                this.splitAllPagesHelper(dragEvent, pdfFile);
                this.splitRangePagesHelper(dragEvent, pdfFile);
            }
            else {
                System.out.println("Make sure your file is of type PDF for splitting");
            }
        }
        else{
            System.out.println("You have to split on PDF at at time or you have");
        }
    }

    public void welcomeVBoxOnDragExitedHandler() {
        this.dropFileHyperlink.setText("Drop and drop PDF file or click to open");
    }

    public void welcomeVBoxOnDragEnteredHandler(DragEvent dragEvent) {
        List<File> filesList = dragEvent.getDragboard().getFiles();
        if (filesList.size() == 1){
            File fileDragged = filesList.get(0);

            if(!this.isFilePDF(fileDragged.getAbsolutePath())){
                //((Node)dragEvent.getSource()).setCursor(Cursor.WAIT);
            }
        }
    }

    public void closeButtonOnMouseClickedHandler(MouseEvent mouseEvent) {
        CloseButton.closeApplication(mouseEvent);
    }

    //Begin
    //Window movements when mouse pressed and dragged
    public void topBarHBoxOnMouseDraggedHandler(MouseEvent mouseEvent) {
        WindowMovements.onMouseDraggedHandler(mouseEvent);
    }

    public void topBarHBoxOnMousePressedEventHandler(MouseEvent mouseEvent) {
        WindowMovements.onMousePressedEventHandler(mouseEvent);
    }

    public void welcomeVBoxOnMouseDraggedHandler(MouseEvent mouseEvent) {
        WindowMovements.onMouseDraggedHandler(mouseEvent);
    }

    public void welcomeVBoxOnMousePressedHandler(MouseEvent mouseEvent) {
        WindowMovements.onMousePressedEventHandler(mouseEvent);
    }
    //End

    public void launchFilesButtonOnActionHandler(ActionEvent actionEvent) {
        this.setProgressBarVisibility(false);
        this.setLaunchFilesButtonVisibility(false);
        this.setSplittingProgressLabelVisibility(false);
        this.getDropFileHyperlink().setVisible(true);
    }


    //getters
    public ProgressBar getSplitPagesProgressBar() {
        return this.splitPagesProgressBar;
    }

    public Button getLaunchFilesButton() {
        return this.launchFilesButton;
    }

    public Hyperlink getSplitAllPagesHyperLink() {
        return this.splitAllPagesHyperLink;
    }

    public Hyperlink getSplitRangeHyperLink() {
        return this.splitRangeHyperLink;
    }

    public Hyperlink getDropFileHyperlink() {
        return this.dropFileHyperlink;
    }

    public Label getSplittingProgressLabel() {
        return this.splittingProgressLabel;
    }
}
