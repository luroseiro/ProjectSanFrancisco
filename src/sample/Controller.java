package sample;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.MenuItem;
import javafx.fxml.FXML;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
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
            //leitura de arquivo
            ReadFile file = new ReadFile(pathSetup);
            arraySetup = file.OpenFile();

            //prepara guiches
            Guiches[] guiches = new Guiches[arraySetup[1].length() - 3];
            char ultimoGuiche = arraySetup[1].charAt(arraySetup[1].length() - 1);
            char ultimoAtendente = arraySetup[2].charAt(arraySetup[2].length() - 1);
            int qtdeGuiche = contaGuiches(guiches, ultimoGuiche, arraySetup[1]);
            setPropertyOfGuiches(guiches, arraySetup, ultimoGuiche, ultimoAtendente);

            //desenha guiches
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
            //leitura de arquivo
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
    public void setPropertyOfGuiches(Guiches[] guiches, String[] arraySetup, char ultimoGuiche, char ultimoAtendente) {
        //define custo e rotulo
        int posGuiche = 0;
        for(int i = 4; i < arraySetup.length; i++) {
            guiches[posGuiche].custo = Character.getNumericValue(arraySetup[i].charAt(1));
            guiches[posGuiche].rotulo = arraySetup[i].substring(3, arraySetup[i].length());
            posGuiche++;
        }
        posGuiche = 0;

        //define tipo
        for(char type = 'A'; type <= ultimoGuiche; type++) {
            guiches[posGuiche].tipo = type;
            posGuiche++;
        }

        //define atendentes
        int aux = 0;
        for (int atendentes = 3; atendentes < arraySetup[2].length(); atendentes++) {
            if(guiches[aux].tipo == arraySetup[2].charAt(atendentes)) {
                guiches[aux].atendentes++;
                if((arraySetup[2].charAt(atendentes) != ultimoGuiche) && (arraySetup[2].charAt(atendentes) != ultimoAtendente)) {
                    if (guiches[aux].tipo != arraySetup[2].charAt(atendentes + 1)) {
                        aux++;
                    }
                }
            }
        }
    }
    public void drawGuiches(Guiches[] guiches, GraphicsContext gc, int maxGuiche) {
        gc.setLineWidth(2);
        gc.setFont(Font.font("Arial", 20));
        gc.setTextAlign(TextAlignment.CENTER);

        int posicaoProx = 50, posicaoProxBaixo = 50, desenhados = 0, maxWidth = 90;

        //---------------------------------desenha triagem--------------------------------------
        if(maxGuiche <= 11) {
            int posicaoTriagem = 520;
            if (guiches[0].quantidade > 1) {
                if(guiches[0].atendentes == 0)
                    gc.setFill(Color.GRAY);
                else
                    gc.setFill(Color.BLUE);

                gc.fillRoundRect(1165, posicaoTriagem - ((guiches[0].quantidade - 1) * 85), 85, guiches[0].quantidade * 85, 10, 10);
                gc.setFill(Color.BLACK);
                gc.fillText(guiches[0].rotulo, 1207.5, posicaoTriagem - ((guiches[0].quantidade - 1) * 85) - 30, maxWidth);
                gc.setFont(Font.font(13));
                gc.fillText("Nº de guichês: " + guiches[0].quantidade, 1207.5,posicaoTriagem - ((guiches[0].quantidade - 1) * 85) - 7, maxWidth);
                gc.setFont(Font.font(40));
                gc.fillText(Integer.toString(guiches[0].atendentes), 1207.5, posicaoTriagem + 15, maxWidth);
            }
            else {
                if(guiches[0].atendentes == 0)
                    gc.setFill(Color.GRAY);
                else
                    gc.setFill(Color.BLUE);

                gc.fillRoundRect(1165, posicaoTriagem, 85, 85, 10, 10);
                gc.setFill(Color.BLACK);
                gc.fillText(guiches[0].rotulo, 1207.5, posicaoTriagem - 30, maxWidth);
                gc.setFont(Font.font(13));
                gc.fillText("Nº de guichês: " + guiches[0].quantidade, 1207.5,posicaoTriagem - ((guiches[0].quantidade - 1) * 85) - 7, maxWidth);
                gc.setFont(Font.font(40));
                gc.fillText(Integer.toString(guiches[0].atendentes), 1207.5, posicaoTriagem + 55, maxWidth);
            }
        }
        else {
            int posicaoTriagem = 285;
            if (guiches[0].quantidade > 1) {
                if(guiches[0].atendentes == 0)
                    gc.setFill(Color.GRAY);
                else
                    gc.setFill(Color.BLUE);

                gc.fillRoundRect(1165, posicaoTriagem - ((guiches[0].quantidade - 1) * 42.5), 85, guiches[0].quantidade * 85, 10, 10);
                gc.setFill(Color.BLACK);
                gc.fillText(guiches[0].rotulo, 1207.5, posicaoTriagem - ((guiches[0].quantidade - 1) * 42.5) - 30, maxWidth);
                gc.setFont(Font.font(13));
                gc.fillText("Nº de guichês: " + guiches[0].quantidade, 1207.5,posicaoTriagem - ((guiches[0].quantidade - 1) * 45) - 7, maxWidth);
                gc.setFont(Font.font(40));
                gc.fillText(Integer.toString(guiches[0].atendentes), 1207.5, posicaoTriagem + 15, maxWidth);
            }
            else {
                if(guiches[0].atendentes == 0)
                    gc.setFill(Color.GRAY);
                else
                    gc.setFill(Color.BLUE);

                gc.fillRoundRect(1165, posicaoTriagem, 85, 85, 10, 10);
                gc.setFill(Color.BLACK);
                gc.fillText(guiches[0].rotulo, 1207.5, posicaoTriagem - 30, maxWidth);
                gc.setFont(Font.font(13));
                gc.fillText("Nº de guichês: " + guiches[0].quantidade, 1207.5,posicaoTriagem - 7, maxWidth);
                gc.setFont(Font.font(40));
                gc.fillText(Integer.toString(guiches[0].atendentes), 1207.5, posicaoTriagem + 55, maxWidth);
            }
        }
        //-----------------------------------------------------------------------------------

        //--------------------------------desenha outros guiches----------------------------------
        for (int grupoGuiche = 1; grupoGuiche < maxGuiche; grupoGuiche++) {
            if(desenhados < 10) {
                if (guiches[grupoGuiche].quantidade > 1) {
                    maxWidth *= guiches[0].quantidade;
                    if(guiches[grupoGuiche].atendentes == 0)
                        gc.setFill(Color.GRAY);
                    else
                        gc.setFill(Color.BLUE);

                    gc.setFont(Font.font(20));
                    gc.fillRoundRect(posicaoProx, 50, guiches[grupoGuiche].quantidade * 85, 85, 10, 10);
                    gc.setFill(Color.BLACK);
                    gc.fillText(guiches[grupoGuiche].rotulo, posicaoProx + ((guiches[grupoGuiche].quantidade - 1) * 85), 15, maxWidth);
                    gc.setFont(Font.font(13));
                    gc.fillText("Nº de guichês: " + guiches[grupoGuiche].quantidade, posicaoProx + ((guiches[grupoGuiche].quantidade - 1) * 85),38, maxWidth);
                    gc.setFont(Font.font(40));
                    gc.fillText(Integer.toString(guiches[grupoGuiche].atendentes), posicaoProx + ((guiches[grupoGuiche].quantidade - 1 )* 85), 105, maxWidth);

                    //controle
                    desenhados++;
                    posicaoProx += (guiches[grupoGuiche].quantidade * 85) + 20;
                } else {
                    if(guiches[grupoGuiche].atendentes == 0)
                        gc.setFill(Color.GRAY);
                    else
                        gc.setFill(Color.BLUE);

                    gc.fillRoundRect(posicaoProx, 50, 85, 85, 10, 10);
                    gc.setFill(Color.BLACK);
                    gc.setFont(Font.font(20));
                    gc.fillText(guiches[grupoGuiche].rotulo, posicaoProx + 42.5, 15, maxWidth);
                    gc.setFont(Font.font(13));
                    gc.fillText("Nº de guichês: " + guiches[grupoGuiche].quantidade, posicaoProx + 42.5, 38, maxWidth);
                    gc.setFont((Font.font(40)));
                    gc.fillText(Integer.toString(guiches[grupoGuiche].atendentes), posicaoProx + 42.5, 105, maxWidth);

                    //controle
                    desenhados++;
                    posicaoProx += 100;
                }
            }
            else {
                if (guiches[grupoGuiche].quantidade > 1) {
                    maxWidth *= guiches[grupoGuiche].quantidade;
                    if(guiches[grupoGuiche].atendentes == 0)
                        gc.setFill(Color.GRAY);
                    else
                        gc.setFill(Color.BLUE);

                    gc.setFont(Font.font(20));
                    gc.fillRoundRect(posicaoProxBaixo, 520, guiches[grupoGuiche].quantidade * 85, 85, 10, 10);
                    gc.setFill(Color.BLACK);
                    gc.fillText(guiches[grupoGuiche].rotulo, posicaoProxBaixo + ((guiches[grupoGuiche].quantidade - 1) * 85), 485, maxWidth);
                    gc.setFont(Font.font(13));
                    gc.fillText("Nº de guichês: " + guiches[grupoGuiche].quantidade, posicaoProxBaixo + ((guiches[grupoGuiche].quantidade - 1) * 85), 462, maxWidth);
                    gc.setFont(Font.font(40));
                    gc.fillText(Integer.toString(guiches[grupoGuiche].atendentes), posicaoProxBaixo + 85 * (guiches[grupoGuiche].quantidade - 1), 575, maxWidth);

                    //controle
                    desenhados++;
                    posicaoProxBaixo += (guiches[grupoGuiche].quantidade * 85) + 20;
                } else {
                    if(guiches[grupoGuiche].atendentes == 0)
                        gc.setFill(Color.GRAY);
                    else
                        gc.setFill(Color.BLUE);

                    gc.fillRoundRect(posicaoProxBaixo, 520, 85, 85, 10, 10);
                    gc.setFill(Color.BLACK);
                    gc.setFont(Font.font(20));
                    gc.fillText(guiches[grupoGuiche].rotulo, posicaoProxBaixo + 42.5, 485, maxWidth);
                    gc.setFont(Font.font(13));
                    gc.fillText("Nº de guichês: " + guiches[grupoGuiche].quantidade, posicaoProxBaixo + 42.5, 462, maxWidth);
                    gc.setFont((Font.font(40)));
                    gc.fillText(Integer.toString(guiches[grupoGuiche].atendentes), posicaoProxBaixo + 42.5, 575, maxWidth);

                    //controle
                    desenhados++;
                    posicaoProxBaixo += 100;
                }
            }
        }
        //-----------------------------------------------------------------------------------
    }
}
