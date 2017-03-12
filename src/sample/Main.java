package sample;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ComboBox;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.io.File;
import java.io.IOException;

public class Main extends Application {

    //botão de velocidade
    private ComboBox<Duration> comboVelocidade = new ComboBox<>();
    private ComboBox<String> comboPause = new ComboBox<>();

    //controle de fluxo e outros
    private int turno = 0, tipoGuiche = 0;
    private boolean done = false, desenhadoTurno = false;

    //explorador de arquivos
    private String[] exploradorDeArquivos() {
        FileChooser exploradorDeArquivos = new FileChooser();
        exploradorDeArquivos.setInitialDirectory(new File("C:\\Users\\lf_ro\\Downloads"));
        exploradorDeArquivos.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Arquivo de texto", "*.txt"));
        File arquivoLido = exploradorDeArquivos.showOpenDialog(null);

        String caminho = arquivoLido.getAbsolutePath();
        String[] arrayArquivo, erro;
        erro = new String[0];

        try {
            ReadFile arquivo = new ReadFile(caminho);
            arrayArquivo = arquivo.abreArquivo();
            return arrayArquivo;
        } catch (IOException e) {
            System.out.println("Erro ao ler arquivo!");
            return erro;
        }
    }

    //carregar arquivos
    private String[] carregarArquivo() {
        return exploradorDeArquivos();
    }

    //propriedades dos guichês e usuários
    private void contaGuiches(Guiches[] guiches, char ultimoGuiche) {

        //cria guiches
        for(int i = 0; i < guiches.length; i++) {
            guiches[i] = new Guiches();
        }

        //define ultimo tipo de guiche
        for(char j = 'A'; j <= ultimoGuiche; j++) {
            tipoGuiche++;
        }

    }
    private void defineGuiches(String[] arquivoSetup, Guiches[] guiches, Fila[] filas) {

        //define tipo dos guiches
        int auxTipo = 3, auxAtendentes = 3;
        for(Guiches guiche: guiches) {
            //define tipo dos guichês
            guiche.setTipo(arquivoSetup[1].charAt(auxTipo));
            auxTipo++;

            //define atendentes
            if(auxAtendentes < arquivoSetup[2].length())
                if (guiche.getTipo() == arquivoSetup[2].charAt(auxAtendentes)) {
                    guiche.setAtendente(true);
                    auxAtendentes++;
                }
        }

        //define custo e rótulo
        //linha do arquivo setup
        for(int i = 4; i < arquivoSetup.length; i++) {
            //guichês
            for(Guiches guiche: guiches) {
                if(guiche.getTipo() == arquivoSetup[i].charAt(0)) {
                    guiche.setCusto(Character.getNumericValue(arquivoSetup[i].charAt(1)));
                    guiche.setRotulo(arquivoSetup[i].substring(3, arquivoSetup[i].length()));
                }
            }
        }

        //define filas e atribui guichês
        char tipoFila = 'A';
        for (Fila fila: filas) {
            //define tipo de fila
            fila.setTipoFila(tipoFila);
            tipoFila++;

            //atribui guichê
            for(Guiches guiche: guiches) {
                if(guiche.getTipo() == fila.getTipoFila()) {
                    guiche.setFila(fila);
                    fila.aumentaQtdeGuiches();
                }
            }
        }

    }
    private void defineUsuarios(Usuarios[] usuarios, String[] arrayLine) {

        //controle de usuários
        int countUltimo = 1, countPrimeiro;

        //linha do arquivo fila (usuário)
        for(int linha = 0; linha < arrayLine.length; linha++) {
            //define userOrdem
            usuarios[linha] = new Usuarios();
            for(int i = 1; i < arrayLine[linha].length(); i++) {
                if(arrayLine[linha].charAt(i) == 'C') {
                    countUltimo = i;
                    break;
                }
            }
            usuarios[linha].setUserOrdem(Integer.parseInt(arrayLine[linha].substring(1, countUltimo)));

            //define chegada
            countPrimeiro = countUltimo + 1;
            for(int i = countPrimeiro; i < arrayLine[linha].length(); i++) {
                if(arrayLine[linha].charAt(i) == 'A') {
                    countUltimo = i;
                    break;
                }
            }
            usuarios[linha].setChegada(Integer.parseInt(arrayLine[linha].substring(countPrimeiro, countUltimo)));

            //define precisaIr
            usuarios[linha].setPrecisaIr(arrayLine[linha].substring(countUltimo, arrayLine[linha].length()));

            //define ultimoNecessario
            usuarios[linha].setUltimoNecessario(arrayLine[linha].charAt(arrayLine[linha].length() - 1));
        }

    }

    //desenha e atualiza guichês, atualiza fila
    private void desenhaGuiches(Guiches[] guiches, GraphicsContext gc, Fila[] filas) {

        //propriedades iniciais do Graphics Context
        gc.setLineWidth(2);
        gc.setFont(Font.font("Arial", 20));
        gc.setTextAlign(TextAlignment.CENTER);

        //controle de posicionamento
        int posicaoXProximoCima = 75, posicaoXProximoBaixo = 75, desenhados = 0;

        //guichês
        for(Guiches guiche: guiches) {
            //verifica atendente e defina pintura
            if(!guiche.getAtendente()) {
                gc.setFill(Color.BLACK);
            }
            else {
                gc.setFill(Color.GREEN);
            }

            //desenha em cima
            if(desenhados < 10) {
                //desenha guichê e rótulo
                gc.fillRoundRect(posicaoXProximoCima, 40, 85, 85, 10, 10);
                gc.setFill(Color.BLACK);
                gc.fillText(guiche.getRotulo(), posicaoXProximoCima + 42.5, 20, 90);

                //controle de posicionamento superior
                desenhados++;
                posicaoXProximoCima += 100;
            }
            //desenha embaixo
            else {
                //desenha guichê
                gc.fillRoundRect(posicaoXProximoBaixo, 510, 85, 85, 10, 10);
                gc.setFill(Color.BLACK);
                gc.fillText(guiche.getRotulo(), posicaoXProximoBaixo + 42.5, 625, 90);

                //controle de posicionamento inferior
                posicaoXProximoBaixo += 100;
            }

        }

        //controle de posição para filas
        posicaoXProximoCima = 75;
        posicaoXProximoBaixo = 75;
        desenhados = 0;

        //filas
        for(Fila fila: filas) {
            //desenha fila em cima
            if(filas.length <= 10) {
                if (fila.getQtdeGuiches() > 1) {
                    gc.strokeRoundRect(posicaoXProximoCima + 67.5,135,50,175,10,10);
                    posicaoXProximoCima += 100 * fila.getQtdeGuiches();
                } else {
                    gc.strokeRoundRect(posicaoXProximoCima + 17.5, 135, 50, 175, 10, 10);
                    posicaoXProximoCima += 100;
                }
            }
            //verifica onde desenhar
            else {
                //desenha em cima
                if (desenhados < filas.length / 2 - 1) {
                    if (fila.getQtdeGuiches() > 1) {
                        gc.strokeRoundRect(posicaoXProximoCima + 67.5, 135, 50, 175, 10, 10);
                        posicaoXProximoCima += 100 * fila.getQtdeGuiches();
                        desenhados++;
                    } else {
                        gc.strokeRoundRect(posicaoXProximoCima + 17.5, 135, 50, 175, 10, 10);
                        posicaoXProximoCima += 100;
                        desenhados++;
                    }

                }
                //desenha embaixo
                else {
                    if (fila.getQtdeGuiches() > 1) {
                        gc.strokeRoundRect(posicaoXProximoBaixo + 67.5, 325, 50, 175, 10, 10);
                        posicaoXProximoBaixo += 100 * fila.getQtdeGuiches();
                    } else {
                        gc.strokeRoundRect(posicaoXProximoBaixo + 17.5, 325, 50, 175, 10, 10);
                        posicaoXProximoBaixo += 100;
                    }
                }
            }
        }
    }
    private void atualizaGuiches(Fila[] filas, GraphicsContext gc) {

        //tamanho do texto de turno
        gc.setFont(Font.font(40));

        //atualiza texto turno
        if (!desenhadoTurno) {
            gc.clearRect(1100, 0, 250, 50);
            gc.setFill(Color.BLACK);
            gc.fillText("Turno: " + Integer.toString(turno), 1200, 30);
            desenhadoTurno = true;
        } else {
            gc.clearRect(1100, 0, 250, 50);
            gc.setFill(Color.BLACK);
            gc.fillText("Turno: " + Integer.toString(turno), 1200, 30);
            desenhadoTurno = false;
        }

        //filas
        int posicaoXProximoCima = 75, posicaoXProximoBaixo = 75, desenhados = 0;

        for(Fila fila: filas) {
            gc.setFill(Color.LIGHTGRAY);
            //desenha fila em cima
            if (filas.length <= 10) {
                if (fila.getQtdeGuiches() > 1) {
                    gc.fillRoundRect(posicaoXProximoCima + 69,136,47,173,10,10);
                    gc.setFont(Font.font(40));
                    gc.setFill(Color.BLACK);
                    gc.fillText(Integer.toString(fila.getTamanhoFila()), posicaoXProximoCima + 92.5,180,50);
                    posicaoXProximoCima += 100 * fila.getQtdeGuiches();
                } else {
                    gc.fillRoundRect(posicaoXProximoCima + 19,136,47,173,10,10);
                    gc.setFont(Font.font(40));
                    gc.setFill(Color.BLACK);
                    gc.fillText(Integer.toString(fila.getTamanhoFila()), posicaoXProximoCima + 42.5,180,50);
                    posicaoXProximoCima += 100;
                }
            }
            //verifica onde desenhar
            else {
                //desenha em cima
                if (desenhados < filas.length / 2 - 1) {
                    if (fila.getQtdeGuiches() > 1) {
                        gc.fillRoundRect(posicaoXProximoCima + 69,136, 47,173,10,10);
                        gc.setFont(Font.font(40));
                        gc.setFill(Color.BLACK);
                        gc.fillText(Integer.toString(fila.getTamanhoFila()), posicaoXProximoCima + 92.5,180,50);
                        posicaoXProximoCima += 100 * fila.getQtdeGuiches();
                        desenhados++;
                    } else {
                        gc.fillRoundRect(posicaoXProximoCima + 19,136,47,173,10,10);
                        gc.setFont(Font.font(40));
                        gc.setFill(Color.BLACK);
                        gc.fillText(Integer.toString(fila.getTamanhoFila()), posicaoXProximoCima + 42.5,180,50);
                        posicaoXProximoCima += 100;
                        desenhados++;
                    }

                }
                //desenha embaixo
                else {
                    if (fila.getQtdeGuiches() > 1) {
                        gc.fillRoundRect(posicaoXProximoBaixo + 69,326,47,173,10,10);
                        gc.setFont(Font.font(40));
                        gc.setFill(Color.BLACK);
                        gc.fillText(Integer.toString(fila.getTamanhoFila()), posicaoXProximoBaixo + 92.5,480,50);
                        posicaoXProximoBaixo += 100 * fila.getQtdeGuiches();
                    } else {
                        gc.fillRoundRect(posicaoXProximoBaixo + 19,326,47,173,10,10);
                        gc.setFont(Font.font(40));
                        gc.setFill(Color.BLACK);
                        gc.fillText(Integer.toString(fila.getTamanhoFila()), posicaoXProximoBaixo + 42.5,480,50);
                        posicaoXProximoBaixo += 100;
                    }
                }
            }
        }

    }
    private void atualizaFila(Usuarios[] usuarios, Guiches[] guiches) {

        int auxGuiche = 0;
        //atualiza usuário para fila da triagem
        for(Usuarios usuario: usuarios) {
            if(usuario.getChegada() == turno) {
                if(guiches[auxGuiche].getTipo() == 'A') {
                    guiches[auxGuiche].getFila().aumentaTamanhoFila();
                    usuario.setTurnosNecessarios(guiches[auxGuiche].getCusto() + 1);
                    auxGuiche++;
                }
            }
        }

        //atualiza usuário para outros guichês
        atendeUsuario(usuarios, guiches);

    }
    private void atendeUsuario(Usuarios[] usuarios, Guiches[] guiches) {

        //controle de fluxo
        int proximoGuiche = 1, count = 0, contaFinal = 0;

        //guichês
        for(int i = 0; i < guiches.length; i++) {
            //verifica se guichê tem fila
            if(guiches[i].getFila().getTamanhoFila() > 0) {
                //usuários
                for (Usuarios usuario: usuarios) {
                    //verifica se o usuário foi embora
                    if(usuario.getTurnosNecessarios() != -2) {
                        //verifica se usuário está sendo atendido
                        if (!usuario.getSendoAtendido()) {
                            //verifica se o guichê está atendendo
                            if (!guiches[i].getAtendendo() && guiches[i].getAtendente()) {
                                if (usuario.getPrecisaIr().charAt(0) == guiches[i].getTipo()) {
                                    usuario.setSendoAtendido(true);
                                    usuario.diminuiTurnosNecessarios();
                                    usuario.setQualGuicheSendoAtendido(i);
                                    guiches[i].setAtendendo(true);
                                }
                            }
                        }
                        //se usuário estiver sendo atendido
                        else {
                            //verifica se o loop está no guichê que ele está sendo atendido
                            if (usuario.getQualGuicheSendoAtendido() == i) {
                                //verifica quantos turnos faltam pra mudar de guichê
                                if (usuario.getTurnosNecessarios() >= 1) {
                                    usuario.diminuiTurnosNecessarios();
                                }
                                //se faltar 0
                                else if (usuario.getTurnosNecessarios() == 0) {
                                    guiches[i].setAtendendo(false);
                                    guiches[i].getFila().diminuiTamanhoFila();
                                    usuario.setSendoAtendido(false);

                                    //se estiver no último guichê define precisaIr = null
                                    if (usuario.getPrecisaIr().charAt(0) == usuario.getUltimoNecessario()) {
                                        usuario.setPrecisaIr(null);
                                    }
                                    //define proximo guichê para usuário
                                    else {
                                        usuario.setPrecisaIr(usuario.getPrecisaIr().substring(1));
                                    }

                                    //usuário acabou
                                    if (usuario.getPrecisaIr() == null) {
                                        usuario.setTurnosNecessarios(-2);
                                    }
                                    //se o próximo guichê que ele precisa ir for o próximo do loop
                                    else if (usuario.getPrecisaIr().charAt(0) == guiches[proximoGuiche].getTipo()) {
                                        usuario.setTurnosNecessarios(guiches[proximoGuiche].getCusto());
                                        guiches[proximoGuiche].getFila().aumentaTamanhoFila();
                                    }
                                    else {
                                        //avança próximo guichê até o que for necessário
                                        while (usuario.getPrecisaIr().charAt(0) != guiches[proximoGuiche].getTipo()) {
                                            proximoGuiche++;
                                            count++;
                                        }
                                        usuario.setTurnosNecessarios(guiches[proximoGuiche].getCusto());
                                        guiches[proximoGuiche].getFila().aumentaTamanhoFila();

                                        //volta próximo guichê para o inicial
                                        while (count > 0) {
                                            proximoGuiche--;
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

        //verifica se todos os usuários foram embora
        for(Usuarios usuario: usuarios) {
            if(usuario.getTurnosNecessarios() == -2) {
                contaFinal++;
            }
        }
        //encerra o loop
        if(contaFinal == usuarios.length) {
            done = true;
        }

    }

    //método principal do simulador
    private void gameLoop(GraphicsContext graphicsContext) {

        //cria arquivo de setup e guichês
        String[] arquivoSetup = carregarArquivo();
        Guiches[] guiches = new Guiches[arquivoSetup[1].length() - 3];

        //cria ultimo tipo de guichê
        char ultimoGuiche = arquivoSetup[1].charAt(arquivoSetup[1].length() - 1);

        //propriedades iniciais dos guiches
        contaGuiches(guiches, ultimoGuiche);

        //cria e inicia filas
        Fila[] filas = new Fila[tipoGuiche];
        for (int i = 0; i < tipoGuiche; i++) {
            filas[i] = new Fila();
        }

        //define guichês e suas filas
        defineGuiches(arquivoSetup, guiches, filas);

        //desenha guichês
        desenhaGuiches(guiches, graphicsContext, filas);

        //cria arquivo fila e usuários
        String[] arquivoFila = carregarArquivo();
        Usuarios[] usuarios = new Usuarios[arquivoFila.length];

        //propriedades iniciais dos usuarios
        defineUsuarios(usuarios, arquivoFila);

        //loop principal
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            //atualiza guichês, fila e turno
            atualizaFila(usuarios, guiches);
            atualizaGuiches(filas, graphicsContext);
            turno++;

            //encerra processamento após fim da fila
            if (done) {
                graphicsContext.fillText("Fim!",1250,100);
            }
        }));
        timeline.setCycleCount(Animation.INDEFINITE);

        //muda velocidade
        comboVelocidade.valueProperty().addListener((observable, oldValue, newValue) -> timeline.setRate(1/newValue.toSeconds()));

        //pause e play
        comboPause.valueProperty().addListener(((observable, oldValue, newValue) -> {
            switch (newValue) {
                case "Pause":
                    timeline.pause();
                    break;
                case "Play":
                    timeline.play();
                    break;
                case "Reset":

                    break;
            }
        }));

        //roda simulador
        timeline.play();

    }

    //métodos básicos JavaFX
    @Override
    public void start(Stage primaryStage) throws Exception {

        //layout
        StackPane root = new StackPane();
        VBox vBox = new VBox();
        vBox.setSpacing(10);

        //barra de menu
        MenuBar menuBar = new MenuBar();
        menuBar.setMinHeight(25);
        menuBar.setPrefWidth(1300);

        //tela para ser desenhada
        Canvas canvas = new Canvas(1300,650);
        GraphicsContext graphicsContext = canvas.getGraphicsContext2D();

        //empilhando
        vBox.getChildren().addAll(menuBar,canvas);

        //botões de controle
        Duration initial = Duration.seconds(1);
        comboVelocidade.getItems().addAll(initial, Duration.seconds(1/2d), Duration.seconds(1/3d));
        comboVelocidade.setValue(initial);
        comboPause.getItems().addAll("Play", "Pause", "Reset");
        comboPause.setPromptText("Play");

        //ajustando botões de controle
        HBox hBox = new HBox();
        hBox.setMinWidth(1300);
        hBox.setMinHeight(25);
        hBox.setSpacing(10);
        hBox.getChildren().addAll(comboPause, comboVelocidade);

        StackPane.setAlignment(hBox, Pos.TOP_LEFT);

        //adicionando a raiz e propriedades
        root.getChildren().addAll(vBox, hBox);
        primaryStage.setTitle("Project San Francisco");
        primaryStage.setScene(new Scene(root, 1300, 680));
        primaryStage.setResizable(false);

        //exibindo
        primaryStage.show();

        //aviso do carregamento de arquivos
        AlertaCarregamento.display("AVISO!", "Carregar primeiro arquivo setup, depois arquivo fila!");

        //roda simulador
        gameLoop(graphicsContext);

    }
    public static void main(String[] args) throws IOException {
        launch(args);
    }

}
