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
    private int turno = 0, tipoGuiche = 0, qtdeGuiche = 0;
    private boolean done = false, desenhado = false;

    //explorador de arquivos
    private String[] fileChooser() {
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
    private void setGuiches(String[] arraySetup, Guiches[] guiches, Fila[] fila) {

        //define tipo dos guiches
        int auxTipo = 3, auxAtendentes = 3;
        for(Guiches i: guiches) {
            //define tipo dos guichês
            i.tipo = arraySetup[1].charAt(auxTipo);
            auxTipo++;

            //define atendentes
            if(auxAtendentes < arraySetup[2].length())
                if (i.tipo == arraySetup[2].charAt(auxAtendentes)) {
                    i.atendente = true;
                    auxAtendentes++;
                }
        }

        //define custo e rótulo
        //linha do arquivo setup
        for(int i = 4; i < arraySetup.length; i++) {
            //guichês
            for(Guiches j: guiches) {
                if(j.tipo == arraySetup[i].charAt(0)) {
                    j.custo = Character.getNumericValue(arraySetup[i].charAt(1));
                    j.rotulo = arraySetup[i].substring(3, arraySetup[i].length());
                }
            }
        }

        //define filas e atribui guichês
        char tipoFila = 'A';
        for (Fila i: fila) {
            //define tipo de fila
            i.tipoGuicheDaFila = tipoFila;
            tipoFila++;

            //atribui guichê
            for(Guiches j: guiches) {
                if(j.tipo == i.tipoGuicheDaFila) {
                    j.fila = i;
                    i.qtdeGuiches++;
                }
            }
        }

    }
    private void setUsers(Usuarios[] usuarios, String[] arrayLine) {

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
    private void drawGuiches(Guiches[] guiches, GraphicsContext gc, Fila[] filas) {

        //propriedades iniciais do Graphics Context
        gc.setLineWidth(2);
        gc.setFont(Font.font("Arial", 20));
        gc.setTextAlign(TextAlignment.CENTER);

        //controle de posicionamento
        int posicaoXProximoCima = 75, posicaoXProximoBaixo = 75, desenhados = 0;

        //guichês
        for(Guiches i: guiches) {
            //desenha em cima
            if(desenhados < 10) {
                //verifica atendente e defina pintura
                if(!i.atendente) {
                    gc.setFill(Color.BLACK);
                }
                else {
                    gc.setFill(Color.GREEN);
                }

                //desenha guichê e rótulo
                gc.fillRoundRect(posicaoXProximoCima, 40, 85, 85, 10, 10);
                gc.setFill(Color.BLACK);
                gc.fillText(i.rotulo, posicaoXProximoCima + 42.5, 20, 90);

                //controle de posicionamento superior
                desenhados++;
                posicaoXProximoCima += 100;
            }
            //desenha embaixo
            else {
                //verifica atendente e defina pintura
                if(!i.atendente) {
                    gc.setFill(Color.BLACK);
                }
                else {
                    gc.setFill(Color.GREEN);
                }

                //desenha guichê
                gc.fillRoundRect(posicaoXProximoBaixo, 510, 85, 85, 10, 10);
                gc.setFill(Color.BLACK);
                gc.fillText(i.rotulo, posicaoXProximoBaixo + 42.5, 625, 90);

                //controle de posicionamento inferior
                posicaoXProximoBaixo += 100;
            }

        }

        //controle de posição para filas
        posicaoXProximoCima = 75;
        posicaoXProximoBaixo = 75;
        desenhados = 0;

        //filas
        for(Fila j: filas) {
            gc.setFill(Color.BLACK);
            //desenha fila em cima
            if(filas.length <= 10) {
                if (j.qtdeGuiches > 1) {
                    gc.strokeRoundRect(posicaoXProximoCima + 67.5,135,50,175,10,10);
                    posicaoXProximoCima += 100 * j.qtdeGuiches;
                } else {
                    gc.strokeRoundRect(posicaoXProximoCima + 17.5, 135, 50, 175, 10, 10);
                    posicaoXProximoCima += 100;
                }
            }
            //verifica onde desenhar
            else {
                //desenha em cima
                if (desenhados < filas.length / 2 - 1) {
                    if (j.qtdeGuiches > 1) {
                        gc.strokeRoundRect(posicaoXProximoCima + 67.5, 135, 50, 175, 10, 10);
                        posicaoXProximoCima += 100 * j.qtdeGuiches;
                        desenhados++;
                    } else {
                        gc.strokeRoundRect(posicaoXProximoCima + 17.5, 135, 50, 175, 10, 10);
                        posicaoXProximoCima += 100;
                        desenhados++;
                    }

                }
                //desenha embaixo
                else {
                    if (j.qtdeGuiches > 1) {
                        gc.strokeRoundRect(posicaoXProximoBaixo + 67.5, 325, 50, 175, 10, 10);
                        posicaoXProximoBaixo += 100 * j.qtdeGuiches;
                    } else {
                        gc.strokeRoundRect(posicaoXProximoBaixo + 17.5, 325, 50, 175, 10, 10);
                        posicaoXProximoBaixo += 100;
                    }
                }
            }
        }
    }
    private void updateGuiches(Guiches[] guiches, Fila[] filas, GraphicsContext gc) {

        //tamanho do texto de turno
        gc.setFont(Font.font(40));

        //atualiza texto turno
        if (!desenhado) {
            gc.clearRect(1100, 0, 250, 50);
            gc.setFill(Color.BLACK);
            gc.fillText("Turno: " + Integer.toString(turno), 1200, 30);
            desenhado = true;
        } else {
            gc.clearRect(1100, 0, 250, 50);
            gc.setFill(Color.BLACK);
            gc.fillText("Turno: " + Integer.toString(turno), 1200, 30);
            desenhado = false;
        }

        //filas
        int posicaoXProximoCima = 75, posicaoXProximoBaixo = 75, desenhados = 0;

        for(Fila j: filas) {
            gc.setFill(Color.GRAY);
            //desenha fila em cima
            if (filas.length <= 10) {
                if (j.qtdeGuiches > 1) {
                    gc.fillRoundRect(posicaoXProximoCima + 72.5,140,40,165,10,10);
                    gc.setFont(Font.font(40));
                    gc.setFill(Color.BLACK);
                    gc.fillText(Integer.toString(j.tamanhoFila), posicaoXProximoCima + 92.5,180,50);
                    posicaoXProximoCima += 100 * j.qtdeGuiches;
                } else {
                    gc.fillRoundRect(posicaoXProximoCima + 22.5,140,40,165,10,10);
                    gc.setFont(Font.font(40));
                    gc.setFill(Color.BLACK);
                    gc.fillText(Integer.toString(j.tamanhoFila), posicaoXProximoCima + 42.5,180,50);
                    posicaoXProximoCima += 100;
                }
            }
            //verifica onde desenhar
            else {
                //desenha em cima
                if (desenhados < filas.length / 2 - 1) {
                    if (j.qtdeGuiches > 1) {
                        gc.fillRoundRect(posicaoXProximoCima + 72.5,140, 40,165,10,10);
                        gc.setFont(Font.font(40));
                        gc.setFill(Color.BLACK);
                        gc.fillText(Integer.toString(j.tamanhoFila), posicaoXProximoCima + 92.5,180,50);
                        posicaoXProximoCima += 100 * j.qtdeGuiches;
                        desenhados++;
                    } else {
                        gc.fillRoundRect(posicaoXProximoCima + 22.5,140,40,165,10,10);
                        gc.setFont(Font.font(40));
                        gc.setFill(Color.BLACK);
                        gc.fillText(Integer.toString(j.tamanhoFila), posicaoXProximoCima + 42.5,180,50);
                        posicaoXProximoCima += 100;
                        desenhados++;
                    }

                }
                //desenha embaixo
                else {
                    if (j.qtdeGuiches > 1) {
                        gc.fillRoundRect(posicaoXProximoBaixo + 72.5,330,40,165,10,10);
                        gc.setFont(Font.font(40));
                        gc.setFill(Color.BLACK);
                        gc.fillText(Integer.toString(j.tamanhoFila), posicaoXProximoBaixo + 92.5,480,50);
                        posicaoXProximoBaixo += 100 * j.qtdeGuiches;
                    } else {
                        gc.fillRoundRect(posicaoXProximoBaixo + 22.5,330,40,165,10,10);
                        gc.setFont(Font.font(40));
                        gc.setFill(Color.BLACK);
                        gc.fillText(Integer.toString(j.tamanhoFila), posicaoXProximoBaixo + 42.5,480,50);
                        posicaoXProximoBaixo += 100;
                    }
                }
            }
        }

    }

    //atualiza fila
    private void updateFila(Usuarios[] usuarios, Guiches[] guiches, Fila[] fila) {

        int auxGuiche = 0;
        //atualiza usuário para fila da triagem
        for(int i = 0; i < usuarios.length; i++) {
            if(usuarios[i].chegada == turno) {
                if(guiches[auxGuiche].tipo == 'A') {
                    guiches[auxGuiche].fila.tamanhoFila++;
                    usuarios[i].turnosNecessarios += guiches[auxGuiche].custo + 1;
                    auxGuiche++;
                }
            }
        }

        //atualiza usuário para outros guichês
        atendeUsuario(usuarios, guiches, fila);

    }
    private void atendeUsuario(Usuarios[] usuarios, Guiches[] guiches, Fila[] fila) {

        //controle de fluxo
        int proximoGuiche = 1, count = 0, contaFinal = 0;

        //guichês
        for(int i = 0; i < guiches.length; i++) {
            //verifica se guichê tem fila
            if(guiches[i].fila.tamanhoFila > 0) {
                //usuários
                for (Usuarios j: usuarios) {
                    //verifica se o usuário foi embora
                    if(j.turnosNecessarios != -2) {
                        //verifica se usuário está sendo atendido
                        if (!j.sendoAtendido) {
                            //verifica se o guichê está atendendo
                            if (!guiches[i].atendendo) {
                                if(guiches[i].atendente) {
                                    if (j.precisaIr.charAt(0) == guiches[i].tipo) {
                                        j.sendoAtendido = true;
                                        j.turnosNecessarios--;
                                        j.qualGuicheSendoAtendido = i;
                                        guiches[i].atendendo = true;
                                    }
                                }
                            }
                        }
                        //se usuário estiver sendo atendido
                        else {
                            //verifica se o loop está no guichê que ele está sendo atendido
                            if (j.qualGuicheSendoAtendido == i) {
                                //verifica quantos turnos faltam pra mudar de guichê
                                if (j.turnosNecessarios >= 1) {
                                    j.turnosNecessarios--;
                                }
                                //se faltar 0
                                else if (j.turnosNecessarios == 0) {
                                    guiches[i].atendendo = false;
                                    guiches[i].fila.tamanhoFila--;
                                    j.sendoAtendido = false;

                                    //se estiver no último guichê define precisaIr = null
                                    if (j.precisaIr.charAt(0) == j.ultimoNecessario) {
                                        j.precisaIr = null;
                                    }
                                    //define proximo guichê para usuário
                                    else {
                                        j.precisaIr = j.precisaIr.substring(1);
                                    }

                                    //usuário acabou
                                    if (j.precisaIr == null) {
                                        j.turnosNecessarios = -2;
                                    }
                                    //se o próximo guichê que ele precisa ir for o próximo do loop
                                    else if (j.precisaIr.charAt(0) == guiches[proximoGuiche].tipo) {
                                        j.turnosNecessarios += guiches[proximoGuiche].custo;
                                        guiches[proximoGuiche].fila.tamanhoFila++;
                                    }
                                    else {
                                        //avança próximo guichê até o que for necessário
                                        while (j.precisaIr.charAt(0) != guiches[proximoGuiche].tipo) {
                                            proximoGuiche++;
                                            count++;
                                        }
                                        j.turnosNecessarios += guiches[proximoGuiche].custo;
                                        guiches[proximoGuiche].fila.tamanhoFila++;

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
        for(Usuarios k: usuarios) {
            if(k.turnosNecessarios == -2) {
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
        String[] fileSetup = SetupButton();
        Guiches[] guiches = new Guiches[fileSetup[1].length() - 3];
        qtdeGuiche = guiches.length;

        //cria ultimo tipo de guichê
        char ultimoGuiche = fileSetup[1].charAt(fileSetup[1].length() - 1);

        //propriedades iniciais dos guiches
        contaGuiches(guiches, ultimoGuiche);

        //cria e inicia filas
        Fila[] fila = new Fila[tipoGuiche];
        for (int i = 0; i < tipoGuiche; i++) {
            fila[i] = new Fila();
        }

        //define guichês e suas filas
        setGuiches(fileSetup, guiches, fila);

        //desenha guichês
        drawGuiches(guiches, graphicsContext, fila);

        //cria arquivo fila e usuários
        String[] fileLine = LineButton();
        Usuarios[] usuarios = new Usuarios[fileLine.length];

        //propriedades iniciais dos usuarios
        setUsers(usuarios, fileLine);

        //loop de processamento de fila
        ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();
        exec.scheduleAtFixedRate(() -> {
            //atualiza guichês, fila e turno
            updateFila(usuarios, guiches, fila);
            updateGuiches(guiches, fila, graphicsContext);
            turno++;

            //encerra processamento após fim da fila
            if (done) {
                exec.shutdown();
            }
        }, 0, 100, TimeUnit.MILLISECONDS);

    }

    //métodos básicos JavaFX
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
