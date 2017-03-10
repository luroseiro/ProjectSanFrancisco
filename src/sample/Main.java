package sample;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.*;

public class Main extends Application {

    @FXML
    private MenuItem setupButton;

    @FXML
    private MenuItem lineButton;

    @FXML
    private MenuItem playX1;

    @FXML
    private MenuItem playX2;

    @FXML
    private MenuItem playX3;

    @FXML
    private Canvas canvas;

    int turno = 0, tipoGuiche = 0, qtdeGuiche = 0;
    boolean done = false, desenhado = false;

    public void PlayX1() {

    }
    public void PlayX2() {

    }
    public void PlayX3() {

    }

    public String[] fileChooser() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File("C:\\Users\\lf_ro\\Downloads"));
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Arquivo de texto", "*.txt"));
        File arquivoLido = fileChooser.showOpenDialog(null);

        String path = arquivoLido.getAbsolutePath();
        String[] arrayFile, erro;
        erro = new String[0];

        try {
            ReadFile file = new ReadFile(path);
            arrayFile = file.OpenFile();

            return arrayFile;
        } catch (IOException e) {
            System.out.println("Erro ao ler arquivo!");
            return erro;
        }
    }

    public String[] SetupButton() {
        String[] file = fileChooser();
        return file;
    }
    public String[] LineButton() {
        String[] file = fileChooser();
        return file;
    }

    public void contaGuiches(Guiches[] guiches, char ultimoGuiche, String arraySetup) {
        //conta guiches de cada tipo
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
    }
    public void setGuiches(String[] arraySetup, Guiches[] guiches, char ultimoGuiche, char ultimoAtendente) {
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
                gc.fillText("Nº de guichês: " + guiches[0].quantidade, 1207.5,posicaoTriagem - 7, maxWidth);
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
                    gc.fillText("Nº de guichês: " + guiches[grupoGuiche].quantidade, posicaoProxBaixo + ((guiches[grupoGuiche].quantidade - 1) * 85), 508, maxWidth);
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
                    gc.fillText("Nº de guichês: " + guiches[grupoGuiche].quantidade, posicaoProxBaixo + 42.5, 508, maxWidth);
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
    public void updateGuiches(Guiches[] guiches, GraphicsContext gc, int maxGuiches) {
        //atualiza texto turno
        if (!desenhado) {
            gc.clearRect(1100, 0, 250, 50);
            gc.fillText("Turno: " + Integer.toString(turno), 1200, 30);
            desenhado = true;
        } else {
            gc.clearRect(1100, 0, 250, 50);
            gc.fillText("Turno: " + Integer.toString(turno), 1200, 30);
            desenhado = false;
        }
    }

    public void setUsers(Usuarios[] usuarios, String[] arrayLine) {
        int countUltimo = 1, countPrimeiro;

        for(int linha = 0; linha < arrayLine.length; linha++) {
            //define userOrdem
            usuarios[linha] = new Usuarios();
            contagem:
            for(int i = 1; i < arrayLine[linha].length(); i++) {
                if(arrayLine[linha].charAt(i) == 'C') {
                    countUltimo = i;
                    break contagem;
                }
            }
            usuarios[linha].userOrdem = Integer.parseInt(arrayLine[linha].substring(1, countUltimo));

            //define chegada
            countPrimeiro = countUltimo + 1;
            contagem2:
            for(int i = countPrimeiro; i < arrayLine[linha].length(); i++) {
                if(arrayLine[linha].charAt(i) == 'A') {
                    countUltimo = i;
                    break contagem2;
                }
            }
            usuarios[linha].chegada = Integer.parseInt(arrayLine[linha].substring(countPrimeiro, countUltimo));

            //define precisaIr
            usuarios[linha].precisaIr = arrayLine[linha].substring(countUltimo, arrayLine[linha].length());

            //define ultimoNecessario
            usuarios[linha].ultimoNecessario = arrayLine[linha].charAt(arrayLine[linha].length() - 1);
        }
    }
    public void updateFila(Usuarios[] usuarios, Guiches[] guiches, String[] fileLine) {
        for(int i = 0; i < usuarios.length; i++) {
            if (usuarios[i].chegada == turno) {
                usuarios[i].turnosNecessarios += 1 + guiches[0].custo;
                guiches[0].fila++;
            }
        }

        atendeUsuario(usuarios, guiches, fileLine);
    }

    public void atendeUsuario(Usuarios[] usuarios, Guiches[] guiches, String[] fileLine) {
        int count = 0, proximo = 1, contaFinal = 0;
        for(int i = 0; i < tipoGuiche; i++) {
            for(int auxUsuario = 0; auxUsuario < usuarios.length; auxUsuario++) {
                if(usuarios[auxUsuario].turnosNecessarios != -2) {
                    if (guiches[i].fila > 0) {
                        if (guiches[i].usuariosSendoAtendidos < guiches[i].atendentes) {
                            if (usuarios[auxUsuario].precisaIr.charAt(0) == guiches[i].tipo) {
                                guiches[i].usuariosSendoAtendidos++;
                                guiches[i].ordemSendoAtendido = usuarios[auxUsuario].userOrdem;
                                if (usuarios[auxUsuario].turnosNecessarios >= 1) {
                                    usuarios[auxUsuario].turnosNecessarios--;
                                    usuarios[auxUsuario].sendoAtendido = true;
                                }
                            }
                        } else if (guiches[i].usuariosSendoAtendidos == guiches[i].atendentes) {
                            if (usuarios[auxUsuario].sendoAtendido && guiches[i].tipo == usuarios[auxUsuario].precisaIr.charAt(0)) {
                                if (usuarios[auxUsuario].turnosNecessarios >= 1) {
                                    usuarios[auxUsuario].turnosNecessarios--;
                                } else if (usuarios[auxUsuario].turnosNecessarios == 0) {
                                    guiches[i].usuariosSendoAtendidos--;
                                    guiches[i].fila--;
                                    usuarios[auxUsuario].sendoAtendido = false;

                                    if (usuarios[auxUsuario].precisaIr.charAt(0) != usuarios[auxUsuario].ultimoNecessario) {
                                        usuarios[auxUsuario].precisaIr = usuarios[auxUsuario].precisaIr.substring(1);
                                    } else {
                                        usuarios[auxUsuario].precisaIr = null;
                                    }

                                    if (usuarios[auxUsuario].precisaIr == null) {
                                        usuarios[auxUsuario].turnosNecessarios = -2;
                                    } else {
                                        if (usuarios[auxUsuario].precisaIr.charAt(0) == guiches[proximo].tipo) {
                                            usuarios[auxUsuario].turnosNecessarios += guiches[proximo].custo;
                                            guiches[proximo].fila++;
                                        } else {
                                            while (usuarios[auxUsuario].precisaIr.charAt(0) != guiches[proximo].tipo) {
                                                proximo++;
                                                count++;
                                            }
                                            usuarios[auxUsuario].turnosNecessarios += guiches[proximo].custo;
                                            guiches[proximo].fila++;
                                            while (count > 0) {
                                                proximo--;
                                                count--;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        for(int i = 0; i < usuarios.length; i++) {
            if(usuarios[i].turnosNecessarios == -2) {
                contaFinal++;
            }
        }
        if(contaFinal == usuarios.length) {
            done = true;
        }
    }

    public void gameLoop(GraphicsContext graphicsContext) {
        String[] fileSetup = SetupButton();
        Guiches[] guiches = new Guiches[fileSetup[1].length() - 3];
        char ultimoGuiche = fileSetup[1].charAt(fileSetup[1].length() - 1);
        char ultimoAtendente = fileSetup[2].charAt(fileSetup[2].length() - 1);
        contaGuiches(guiches, ultimoGuiche, fileSetup[1]);
        setGuiches(fileSetup, guiches, ultimoGuiche, ultimoAtendente);
        drawGuiches(guiches, graphicsContext, qtdeGuiche);

        String[] fileLine = LineButton();
        Usuarios[] usuarios = new Usuarios[fileLine.length];
        setUsers(usuarios, fileLine);

        ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();
        exec.scheduleAtFixedRate(() -> {
            updateGuiches(guiches, graphicsContext, qtdeGuiche);
            updateFila(usuarios, guiches, fileLine);
            turno++;
            if(done)
                exec.shutdown();
        }, 0, 1, TimeUnit.SECONDS);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("sample.fxml"));

        canvas = new Canvas(1300,650);
        GraphicsContext graphicsContext = canvas.getGraphicsContext2D();

        StackPane root = loader.load();
        root.getChildren().addAll(canvas);
        primaryStage.setTitle("Project San Francisco");
        primaryStage.setScene(new Scene(root, 1300, 680));
        primaryStage.setResizable(false);

        primaryStage.show();

        gameLoop(graphicsContext);
    }
    public static void main(String[] args) throws IOException {
        launch(args);
    }
}
