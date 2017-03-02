package sample;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.MenuItem;
import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;

public class Controller {
    @FXML
    private MenuItem setupButton;

    @FXML
    private MenuItem lineButton;

    @FXML
    public Canvas canvas;

    public String[] SetupButton() {
        FileChooser fileChooserSetup = new FileChooser();
        fileChooserSetup.setInitialDirectory(new File("C:\\Users\\lf_ro\\Downloads"));
        fileChooserSetup.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Arquivo de texto", "*.txt"));
        File setupFile = fileChooserSetup.showOpenDialog(null);

        String pathSetup = setupFile.getAbsolutePath();
        String[] arraySetup = null;
        GraphicsContext graphicsContext = canvas.getGraphicsContext2D();

        try {
            ReadFile file = new ReadFile(pathSetup);
            arraySetup = file.OpenFile();
            drawGuiches(graphicsContext, modificaString(arraySetup[1]));
        } catch (IOException e) {
            System.out.println("Erro ao ler arquivo!");
        }

        return arraySetup;
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

    public String[] modificaString(String oldString) {
        String[] newString = oldString.split("");
        return newString;
    }

    public void drawGuiches(GraphicsContext gc, String[] relacaoGuiches) {
        gc.setFill(Color.RED);
        gc.setStroke(Color.BLUE);
        gc.setLineWidth(5);
        gc.strokeLine(40,10,10,40);
        gc.fillOval(10,60,30,30);
    }
}
