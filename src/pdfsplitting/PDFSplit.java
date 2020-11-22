package pdfsplitting;

import javafx.concurrent.Task;
import org.apache.pdfbox.multipdf.Splitter;
import org.apache.pdfbox.pdmodel.PDDocument;
import uis.Welcome;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

public class PDFSplit {

    private String inputFilePath;
    private String outputFilesPath;
    private String outputFilePath;
    private int startPageNumber;
    private int endPageNumber;
    private PDDocument pdDocument;

    public PDFSplit(String inputFilePath, String outputFilesPath) throws IOException {
        this.inputFilePath = inputFilePath;
        this.outputFilesPath = outputFilesPath;

        this.setPDDocument(this.inputFilePath);
    }

    public PDFSplit(String inputFilePath, String outputFilePath, int startPageNumber, int endPageNumber) throws IOException {
        this.inputFilePath = inputFilePath;
        this.outputFilePath = outputFilePath;
        this.startPageNumber = startPageNumber;
        this.endPageNumber = endPageNumber;

        this.setPDDocument(this.inputFilePath);
    }

    public PDFSplit(String inputFilePath) throws IOException {
        this.setPDDocument(inputFilePath);
    }

    //initialize pdDocument
    public void setPDDocument(String inputFilePath) throws IOException {
        File pdfFile = new File(inputFilePath);
        this.pdDocument = PDDocument.load(pdfFile);
    }

    //Get the name of the PDF file without the extension .PDF
    private String getPDFFileName(){
        String[] split = inputFilePath.split("\\\\");

        return split[split.length-1].split("\\.")[0];
    }


    public void pfdAllPagesSplitting(Welcome welcomeObject){
        welcomeObject.getSplitPagesProgressBar().setVisible(true);
        welcomeObject.getSplittingProgressLabel().setVisible(true);

        Task<Void> splitAllPagesTask = new Task<Void>() {
            @SuppressWarnings("StringBufferReplaceableByString")
            @Override
            protected Void call() {
                try {
                    Splitter splitter = new Splitter();
                    List<PDDocument> pagesList = splitter.split(pdDocument);
                    Iterator<PDDocument> pagesIterator = pagesList.iterator();

                    int index = 1;

                    while (pagesIterator.hasNext()){
                        PDDocument document = pagesIterator.next();
                        document.save(String.format("%s\\%s_page_%d.pdf",outputFilesPath, getPDFFileName(), index++));

                        updateProgress(index, pdDocument.getNumberOfPages());
                        updateMessage(
                                String.valueOf(new StringBuilder()
                                        .append(index - 1)
                                        .append(" of ")
                                        .append(pdDocument.getNumberOfPages())
                                )
                        );
                    }

                    pdDocument.close();
                }catch (Exception ex){
                    ex.printStackTrace();
                }

                updateMessage("Completed");
                welcomeObject.getLaunchFilesButton().setVisible(true);
                return null;
            }
        };

        welcomeObject.getSplitPagesProgressBar().progressProperty().bind(splitAllPagesTask.progressProperty());
        welcomeObject.getSplittingProgressLabel().textProperty().bind(splitAllPagesTask.messageProperty());

         new Thread(splitAllPagesTask).start();

    }
    public void pdfRangeSplitting(Welcome welcomeObject) {

        welcomeObject.getSplitPagesProgressBar().setVisible(true);
        welcomeObject.getSplittingProgressLabel().setVisible(true);

        Task<Void> pdfRangeSplittingTask = new Task<Void>() {
            @SuppressWarnings("StringBufferReplaceableByString")
            @Override
            protected Void call() {
                try {
                    PDDocument partialPdDocument = rangeSplitting();

                    List<PDDocument> pdDocumentList = new Splitter().split(partialPdDocument);
                    Iterator<PDDocument> pdDocumentIterator = pdDocumentList.iterator();

                    int index = 1;
                    while (pdDocumentIterator.hasNext()){
                        pdDocumentIterator.next();

                        updateProgress(index++, partialPdDocument.getNumberOfPages());
                        updateMessage(
                                String.valueOf(new StringBuilder()
                                        .append(index - 1)
                                        .append(" of ")
                                        .append(partialPdDocument.getNumberOfPages())
                                )
                        );
                    }

                    pdDocument.close();
                }
                catch (Exception ex){
                    ex.printStackTrace();
                }

                updateMessage("Completed");
                welcomeObject.getLaunchFilesButton().setVisible(true);
                return null;
            }
        };

        welcomeObject.getSplitPagesProgressBar().progressProperty().bind(pdfRangeSplittingTask.progressProperty());
        welcomeObject.getSplittingProgressLabel().textProperty().bind(pdfRangeSplittingTask.messageProperty());

        new Thread(pdfRangeSplittingTask).start();
    }

    private PDDocument rangeSplitting() throws IOException {

        Splitter splitter = new Splitter();

        splitter.setStartPage(startPageNumber);
        splitter.setEndPage(endPageNumber);
        splitter.setSplitAtPage(endPageNumber - startPageNumber + 1);


        List<PDDocument> pagesList = splitter.split(pdDocument);

        PDDocument partialPdDocument = pagesList.get(0);

        System.out.println(pagesList.size());

        partialPdDocument.save(new File(String.format(
                "%s\\%s_pages_%d-%d.pdf",
                outputFilePath,
                getPDFFileName(),
                startPageNumber,
                endPageNumber
                ))
        );

        return partialPdDocument;
    }

    public void setStartPageNumber(int startPageNumber) {
        this.startPageNumber = startPageNumber;
    }

    public void setEndPageNumber(int endPageNumber) {
        this.endPageNumber = endPageNumber;
    }

    public int getNumberOfPages(){
        return this.pdDocument.getNumberOfPages();
    }
    
}
