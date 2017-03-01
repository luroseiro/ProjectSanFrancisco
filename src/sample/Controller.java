package sample;

import javafx.event.ActionEvent;
import javafx.scene.control.MenuItem;
import javafx.fxml.FXML;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;

public class Controller {
    @FXML
    private MenuItem setupButton;

    @FXML
    private MenuItem lineButton;

    public void SetupButton(ActionEvent event) {
        FileChooser fileChooserSetup = new FileChooser();
        fileChooserSetup.setInitialDirectory(new File("C:\\Users\\lf_ro\\Downloads"));
        fileChooserSetup.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Arquivo de texto", "*.txt"));
        File setupFile = fileChooserSetup.showOpenDialog(null);

        String pathSetup = setupFile.getAbsolutePath();

        try {
            ReadFile file = new ReadFile(pathSetup);
            String[] arraySetup = file.OpenFile();
        } catch (IOException e) {
            System.out.println("Erro ao ler arquivo!");
        }
    }

    public void LineButton(ActionEvent event) {
        FileChooser fileChooserLine = new FileChooser();
        fileChooserLine.setInitialDirectory(new File("C:\\Users\\lf_ro\\Downloads"));
        fileChooserLine.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Arquivo de texto", "*.txt"));
        File lineFile = fileChooserLine.showOpenDialog(null);

        String pathLine = lineFile.getAbsolutePath();

        try {
            ReadFile file = new ReadFile(pathLine);
            String[] arrayLine = file.OpenFile();

            System.out.println(arrayLine[0]);
        } catch (IOException e) {
            System.out.println("Erro ao ler arquivo!");
        }
    }

}
