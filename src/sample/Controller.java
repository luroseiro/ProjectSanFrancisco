package sample;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
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

    @FXML
    private Canvas canvas;

    public void SetupButton() {
        //Explorador de arquivos
        FileChooser fileChooserSetup = new FileChooser();
        fileChooserSetup.setInitialDirectory(new File("C:\\Users\\lf_ro\\Downloads"));
        fileChooserSetup.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Arquivo de texto", "*.txt"));
        File setupFile = fileChooserSetup.showOpenDialog(null);

        //Arquivo setup
        String pathSetup = setupFile.getAbsolutePath();
        String[] arraySetup;

        //Desenhar
        GraphicsContext graphicsContext = canvas.getGraphicsContext2D();

        try {
            //Leitura de arquivo
            ReadFile file = new ReadFile(pathSetup);
            arraySetup = file.OpenFile();
            //Relação de postos
            char ultimoGuiche = arraySetup[1].charAt(arraySetup[1].length() - 1);
            Guiches[] guiches = new Guiches[arraySetup[1].length() - 4];
            int k = 0;

            //conta guiches de cada tipo
            for(char j = 'A'; j <= ultimoGuiche; j++) {
                guiches[k] = new Guiches();
                for(int i = 3; i < arraySetup[1].length(); i++) {
                    if(arraySetup[1].charAt(i) == j) {
                        guiches[k].quantidade++;
                    }
                }
                k++;
            }
        } catch (IOException e) {
            System.out.println("Erro ao ler arquivo!");
        }
    }

    public void LineButton() {
        FileChooser fileChooserLine = new FileChooser();
        fileChooserLine.setInitialDirectory(new File("C:\\Users\\lf_ro\\Downloads"));
        fileChooserLine.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Arquivo de texto", "*.txt"));
        File lineFile = fileChooserLine.showOpenDialog(null);

        String pathLine = lineFile.getAbsolutePath();

        try {
            ReadFile file = new ReadFile(pathLine);
            String[] arrayLine = file.OpenFile();
        } catch (IOException e) {
            System.out.println("Erro ao ler arquivo!");
        }
    }
}
