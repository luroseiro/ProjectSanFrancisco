package sample;

import com.sun.org.apache.xpath.internal.SourceTree;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.MenuItem;
import javafx.fxml.FXML;
import javafx.scene.paint.Color;
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

            //Prepara guiches
            Guiches[] guiches = new Guiches[arraySetup[1].length() - 3];
            char ultimoGuiche = arraySetup[1].charAt(arraySetup[1].length() - 1);
            int qtdeGuiche = contaGuiches(guiches, ultimoGuiche, arraySetup[1]);
            setTypeOfGuiches(guiches, ultimoGuiche);

            //Desenha guiches
            drawGuiches(guiches, graphicsContext, qtdeGuiche);

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

    public int contaGuiches(Guiches[] guiches, char ultimoGuiche, String arraySetup) {
        //conta guiches de cada tipo
        int tipoGuiche = 0, qtdeGuiche = 0;
        for(char j = 'A'; j <= ultimoGuiche; j++) {
            guiches[tipoGuiche] = new Guiches();
            for (int i = 3; i < arraySetup.length(); i++) {
                if (arraySetup.charAt(i) == j) {
                    guiches[tipoGuiche].quantidade++;
                }
            }
            tipoGuiche++;
            qtdeGuiche++;
        }

        return qtdeGuiche;
    }
    public void setTypeOfGuiches(Guiches[] guiches, char ultimoGuiche) {
        int posicao = 0;
        for(char type = 'A'; type <= ultimoGuiche; type++) {
            guiches[posicao].tipo = type;
            posicao++;
        }
    }
    public void drawGuiches(Guiches[] guiches, GraphicsContext gc, int maxGuiche) {
        gc.setFill(Color.BLUE);
        gc.setLineWidth(2);

        int posicaoProx = 50, posicaoTriagem = 1250;

        //desenha triagem
        if(guiches[0].quantidade > 1) {
            gc.fillRoundRect(posicaoTriagem - (guiches[0].quantidade * 85), 510, guiches[0].quantidade * 85,85,10,10);
            //desenha linhas
            for(int i = 1; i < guiches[0].quantidade; i++) {
                gc.strokeLine(posicaoTriagem - (85 * i), 511, posicaoTriagem - (85 * i), 594);
            }
        }
        else {
            gc.fillRoundRect(posicaoTriagem, 510,85,85,10,10);
        }

        //desenha outros guiches
        for(int grupoGuiche = 1; grupoGuiche < maxGuiche; grupoGuiche++) {
            if(guiches[grupoGuiche].quantidade > 1) {
                gc.fillRoundRect(posicaoProx, 50, guiches[grupoGuiche].quantidade * 85, 85, 10, 10);
                //desenha linhas
                for(int i = 1; i < guiches[grupoGuiche].quantidade; i++) {
                    gc.strokeLine(posicaoProx + (85 * i), 51, posicaoProx + (85 * i), 134);
                }
                posicaoProx += (guiches[grupoGuiche].quantidade * 85) + 15;
            }
            else {
                gc.fillRoundRect(posicaoProx, 50, 85, 85, 10, 10);
                posicaoProx += 100;
            }
        }
    }
}
