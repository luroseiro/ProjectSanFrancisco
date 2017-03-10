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

    //botões FXML
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

    //tela para ser desenhada
    @FXML
    private Canvas canvas;

    //controle de fluxo e outros
    int turno = 0, tipoGuiche = 0, qtdeGuiche = 0;
    boolean done = false, desenhado = false;

    //explorador de arquivos
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

    //ação dos botões
    public String[] SetupButton() {

        String[] file = fileChooser();
        return file;

    }
    public String[] LineButton() {

        String[] file = fileChooser();
        return file;

    }
    public void PlayX1() {

    }
    public void PlayX2() {

    }
    public void PlayX3() {

    }

    //propriedades dos guichês e usuários
    public void contaGuiches(Guiches[] guiches, char ultimoGuiche) {

        //cria guiches
        for(int i = 0; i < guiches.length; i++) {
            guiches[i] = new Guiches();
        }

        //define ultimo tipo de guiche
        for(char j = 'A'; j <= ultimoGuiche; j++) {
            tipoGuiche++;
        }

    }
    public void setGuiches(String[] arraySetup, Guiches[] guiches, char ultimoGuiche, char ultimoAtendente) {

        //define tipo dos guiches
        int auxTipo = 3;
        //guichês
        for(int i = 0; i < guiches.length; i++) {
            guiches[i].tipo = arraySetup[1].charAt(auxTipo);
            auxTipo++;
        }

        //define custo e rótulo
        //linha do arquivo setup
        for(int i = 4; i < arraySetup.length; i++) {
            //guichês
            for(int j = 0; j < guiches.length; j++) {
                if (guiches[j].tipo == arraySetup[i].charAt(0)) {
                    guiches[j].custo = Character.getNumericValue(arraySetup[i].charAt(1));
                    guiches[j].rotulo = arraySetup[i].substring(3, arraySetup[i].length());
                }
            }
        }

        //define atendentes
        int auxAtendentes = 3;
        //guichês
        for(int i = 0; i < guiches.length; i++) {
            if(auxAtendentes < arraySetup[2].length()) {
                if (guiches[i].tipo == arraySetup[2].charAt(auxAtendentes)) {
                    guiches[i].atendente = true;
                    auxAtendentes++;
                }
            }
        }

    }
    public void setUsers(Usuarios[] usuarios, String[] arrayLine) {

        //controle de usuários
        int countUltimo = 1, countPrimeiro;

        //linha do arquivo fila (usuário)
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

    //desenha e atualiza guichês
    public void drawGuiches(Guiches[] guiches, GraphicsContext gc) {

        //propriedades iniciais do Graphics Context
        gc.setLineWidth(2);
        gc.setFont(Font.font("Arial", 20));
        gc.setTextAlign(TextAlignment.CENTER);

        //controle de posicionamento
        int posicaoXProximoCima = 75, posicaoXProximoBaixo = 75, desenhados = 0;

        //guichês
        for(int i = 0; i < guiches.length; i++) {
            //desenha em cima
            if(desenhados < 10) {
                //verifica atendente e defina pintura
                if(!guiches[i].atendente) {
                    gc.setFill(Color.BLACK);
                }
                else {
                    gc.setFill(Color.LIGHTGREEN);
                }

                //desenha guichê
                gc.fillRoundRect(posicaoXProximoCima, 40, 85, 85, 10, 10);
                gc.setFill(Color.BLACK);
                gc.fillText(guiches[i].rotulo, posicaoXProximoCima + 42.5, 20, 90);

                //controle de posicionamento superior
                desenhados++;
                posicaoXProximoCima += 100;
            }
            //desenha embaixo
            else {
                //verifica atendente e defina pintura
                if(!guiches[i].atendente) {
                    gc.setFill(Color.BLACK);
                }
                else {
                    gc.setFill(Color.LIGHTGREEN);
                }

                //desenha guichê
                gc.fillRoundRect(posicaoXProximoBaixo, 510, 85, 85, 10, 10);
                gc.setFill(Color.BLACK);
                gc.fillText(guiches[i].rotulo, posicaoXProximoBaixo + 42.5, 625, 90);

                //controle de posicionamento inferior
                posicaoXProximoBaixo += 100;
            }

        }
    }
    public void updateGuiches(Guiches[] guiches, GraphicsContext gc, int maxGuiches) {

        //tamanho do texto de turno
        gc.setFont(Font.font(40));

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

    //atualiza fila
    public void updateFila(Usuarios[] usuarios, Guiches[] guiches, String[] fileLine) {

        //atualiza usuário para fila da triagem
        for(int i = 0; i < usuarios.length; i++) {
            if (usuarios[i].chegada == turno) {
                usuarios[i].turnosNecessarios += 1 + guiches[0].custo;
                guiches[0].fila++;
            }
        }

        //atualiza usuário para outros guichês
        atendeUsuario(usuarios, guiches, fileLine);

    }
    public void atendeUsuario(Usuarios[] usuarios, Guiches[] guiches, String[] fileLine) {

    }
        /*int count = 0, proximo = 1, contaFinal = 0;
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
    }*/

    //método principal do simulador
    public void gameLoop(GraphicsContext graphicsContext) {

        //cria arquivo de setup e guiches
        String[] fileSetup = SetupButton();
        Guiches[] guiches = new Guiches[fileSetup[1].length() - 3];
        qtdeGuiche = guiches.length;

        //cria ultimo tipo de guiche e onde está o ultimo atendente
        char ultimoGuiche = fileSetup[1].charAt(fileSetup[1].length() - 1);
        char ultimoAtendente = fileSetup[2].charAt(fileSetup[2].length() - 1);

        //propriedades iniciais dos guiches
        contaGuiches(guiches, ultimoGuiche);
        setGuiches(fileSetup, guiches, ultimoGuiche, ultimoAtendente);

        //desenha guichês
        drawGuiches(guiches, graphicsContext);

        //cria arquivo fila e usuários
        String[] fileLine = LineButton();
        Usuarios[] usuarios = new Usuarios[fileLine.length];

        //propriedades iniciais dos usuarios
        setUsers(usuarios, fileLine);

        //loop de processamento de fila
        ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();
        exec.scheduleAtFixedRate(() -> {
            //atualiza guichês, fila e turno
            updateGuiches(guiches, graphicsContext, qtdeGuiche);
            updateFila(usuarios, guiches, fileLine);
            turno++;

            //encerra processamento após fim da fila
            if(done) {
                exec.shutdown();
            }
        }, 0, 1, TimeUnit.SECONDS);

    }

    //métodos básicos JAVAFX
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

        //roda simulador
        gameLoop(graphicsContext);

    }
    public static void main(String[] args) throws IOException {
        launch(args);
    }

}
