package others.filesdirs;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.scene.Node;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

import java.io.File;

public class FilesDirsOpener {
    //Splitted files directory chooser
    public static File getSelectedDirectory(Event event){
        DirectoryChooser selectedDirectoryChooser = new DirectoryChooser();
        selectedDirectoryChooser.setTitle("Save Splitted Files");

        //return selected directory
        return selectedDirectoryChooser.showDialog(((Node)event.getSource()).getScene().getWindow());
    }

    //PDF file chooser
    public static File getSelectedPDFFile(Event event){
        FileChooser pdfFileChooser = new FileChooser();
        pdfFileChooser.setTitle("Open PDF File");

        pdfFileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter(
                        "Acrobat PDF file", "*.pdf"
                )
        );

        //return the selected file
        return pdfFileChooser.showOpenDialog(((Node)event.getSource()).getScene().getWindow());
    }


}
