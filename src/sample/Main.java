package sample;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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

    //velocidade
    private Button botaoVelocidade1 = new Button("Velocidade: x1");
    private Button botaoVelocidade2 = new Button("Velocidade: x2");
    private Button botaoVelocidade3 = new Button("Velocidade: x3");

    //play e pause
    private Button botaoPlay = new Button("Play");
    private Button botaoPause = new Button("Pause");

    //para simulação
    private Button botaoPara = new Button();

    //controle de fluxo e outros
    private int turno = 0, tipoGuiche = 0, countCombinacao = 0;
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
    private void defineGuiches(String[] arquivoSetup, Guiches[] guiches, Fila[] filas, Troca troca) {

        //define custo da troca
        troca.setCusto(Character.getNumericValue(arquivoSetup[3].charAt(6)));

        //define tipo dos guiches
        int auxTipo = 3;
        for(Guiches guiche: guiches) {
            //define tipo dos guichês
            guiche.setTipo(arquivoSetup[1].charAt(auxTipo));
            auxTipo++;

            /*//define atendentes
            if(auxAtendentes < arquivoSetup[2].length()) {
                if (guiche.getTipo() == arquivoSetup[2].charAt(auxAtendentes)) {
                    guiche.setAtendente();
                    auxAtendentes++;
                }
            }*/
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

        //define atendentes
        int auxAtendentes = 3;
        for(Guiches guiche: guiches) {
            //define atendentes para fila
            if(auxAtendentes < arquivoSetup[2].length()) {
                if (guiche.getTipo() == arquivoSetup[2].charAt(auxAtendentes)) {
                    guiche.setAtendente();
                    guiche.getFila().aumentaAtendentes();
                    auxAtendentes++;
                }
            }
        }

    }
    private void defineUsuarios(Usuarios[] usuarios, String[] arrayLine) {

        //controle de usuários
        int countUltimo = 1, countPrimeiro;

        //linha do arquivo fila (usuário)
        for(int linha = 0; linha < arrayLine.length; linha++) {
            //conta qual caractere começar a linha
            usuarios[linha] = new Usuarios();
            for(int i = 1; i < arrayLine[linha].length(); i++) {
                if(arrayLine[linha].charAt(i) == 'C') {
                    countUltimo = i;
                    break;
                }
            }
            usuarios[linha].setNumeroUsuario(Integer.parseInt(arrayLine[linha].substring(1, countUltimo)));

            //define chegada
            countPrimeiro = countUltimo + 1;
            for(int i = countPrimeiro; i < arrayLine[linha].length(); i++) {
                if(arrayLine[linha].charAt(i) == 'A') {
                    countUltimo = i;
                    break;
                }
            }
            usuarios[linha].setChegada(Integer.parseInt(arrayLine[linha].substring(countPrimeiro, countUltimo)));

            //define precisaIr e precisavaIr
            usuarios[linha].setPrecisaIr(arrayLine[linha].substring(countUltimo, arrayLine[linha].length()));

            //define ultimoNecessario
            usuarios[linha].setUltimoNecessario(arrayLine[linha].charAt(arrayLine[linha].length() - 1));
        }

    }
    private void defineCombinacoes(Usuarios[] usuarios, Combinacoes[] combinacoes) {

        //define combinações dos guichês dos usuários
        int count;
        for(Usuarios usuario: usuarios) {
            count = 0;
            //verifica se todos os usuários já estão numa combinação
            for(Usuarios auxUsuario: usuarios) {
                if (auxUsuario.getEstaNumaCombinacao()) {
                    count++;
                }
            }
            if(count < usuarios.length) {
                for (Combinacoes combinacao : combinacoes) {
                    //se a combinação do usuário for a que está no momento
                    if (usuario.getPrecisaIr().equals(combinacao.getCombinacaoGuiche())) {
                        combinacao.aumentaQtdeUsuarios();
                        usuario.setEstaNumaCombinacao();
                        usuario.setCombinacao(combinacao);
                        break;
                    } else {
                        //se nao for e a combinação atual for null
                        if (combinacao.getCombinacaoGuiche() == null) {
                            combinacao.setCombinacaoGuiche(usuario.getPrecisaIr());
                            combinacao.aumentaQtdeUsuarios();
                            usuario.setEstaNumaCombinacao();
                            usuario.setCombinacao(combinacao);
                            break;
                        }
                    }
                }
            }
        }

    }

    //desenha e atualiza guichês, atualiza fila
    private void desenhaGuiches(Guiches[] guiches, GraphicsContext gc, Fila[] filas) {

        //propriedades iniciais do Graphics Context
        gc.setLineWidth(2);
        gc.setFont(Font.font("Arial", 20));
        gc.setTextAlign(TextAlignment.CENTER);

        //controle de posicionamento
        double posicaoXProximoCima = 75, posicaoXProximoBaixo = 75, desenhados = 0;

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
        posicaoXProximoCima = 92.5;
        posicaoXProximoBaixo = 92.5;

        //filas
        for(Fila fila: filas) {
            //desenha fila em cima
            if(posicaoXProximoCima <= 992.5) {
                if (fila.getQtdeGuiches() > 1) {
                    gc.strokeRoundRect(posicaoXProximoCima + 50 * (fila.getQtdeGuiches() - 1),135,50,175,10,10);
                    posicaoXProximoCima += 100 * fila.getQtdeGuiches();
                } else {
                    gc.strokeRoundRect(posicaoXProximoCima,135,50,175,10,10);
                    posicaoXProximoCima += 100;
                }
            }
            //desenha fila embaixo
            else {
                if (fila.getQtdeGuiches() > 1) {
                    gc.strokeRoundRect(posicaoXProximoBaixo + 50 * (fila.getQtdeGuiches() - 1),325,50,175,10,10);
                    posicaoXProximoBaixo += 100 * fila.getQtdeGuiches();
                } else {
                    gc.strokeRoundRect(posicaoXProximoBaixo,325,50,175,10,10);
                    posicaoXProximoBaixo += 100;
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

        //controle de posicionamento das filas
        int posicaoXProximoCima = 94, posicaoXProximoBaixo = 94;
        int posicaoXProximoCimaTurno = 117, posicaoXProximoBaixoTurno = 117;

        //filas
        for(Fila fila: filas) {
            gc.setFill(Color.LIGHTGRAY);
            //desenha fila em cima
            if(posicaoXProximoCima <= 994) {
                if (fila.getQtdeGuiches() > 1) {
                    gc.fillRoundRect(posicaoXProximoCima + 50 * (fila.getQtdeGuiches() - 1),136,47,173,10,10);
                    gc.setFont(Font.font(40));
                    gc.setFill(Color.BLACK);
                    gc.fillText(Integer.toString(fila.getTamanhoFila()),posicaoXProximoCimaTurno + 50 * (fila.getQtdeGuiches() - 1),180,50);
                    posicaoXProximoCima += 100 * fila.getQtdeGuiches();
                    posicaoXProximoCimaTurno += 100 * fila.getQtdeGuiches();
                } else {
                    gc.fillRoundRect(posicaoXProximoCima,136,47,173,10,10);
                    gc.setFont(Font.font(40));
                    gc.setFill(Color.BLACK);
                    gc.fillText(Integer.toString(fila.getTamanhoFila()),posicaoXProximoCimaTurno,180,50);
                    posicaoXProximoCima += 100;
                    posicaoXProximoCimaTurno += 100;
                }
            }
            //desenha embaixo
            else {
                if (fila.getQtdeGuiches() > 1) {
                    gc.fillRoundRect(posicaoXProximoBaixo + 50 * (fila.getQtdeGuiches() - 1), 326, 47, 173, 10, 10);
                    gc.setFont(Font.font(40));
                    gc.setFill(Color.BLACK);
                    gc.fillText(Integer.toString(fila.getTamanhoFila()), posicaoXProximoBaixoTurno + 50 * (fila.getQtdeGuiches() - 1), 370, 50);
                    posicaoXProximoBaixo += 100 * fila.getQtdeGuiches();
                    posicaoXProximoBaixoTurno += 100 * fila.getQtdeGuiches();
                } else {
                    gc.fillRoundRect(posicaoXProximoBaixo, 326, 47, 173, 10, 10);
                    gc.setFont(Font.font(40));
                    gc.setFill(Color.BLACK);
                    gc.fillText(Integer.toString(fila.getTamanhoFila()), posicaoXProximoBaixoTurno, 370, 50);
                    posicaoXProximoBaixo += 100;
                    posicaoXProximoBaixoTurno += 100;
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
                    guiches[auxGuiche].getFila().aumentaQtdeUsuarios();
                    usuario.setTurnosNecessarios(guiches[auxGuiche].getCusto() + 1);
                    usuario.setTurnosTotais(guiches[auxGuiche].getCusto());
                    auxGuiche++;
                }
            }
        }

        //atualiza usuário para outros guichês
        atendeUsuario(usuarios, guiches);

    }
    private void atendeUsuario(Usuarios[] usuarios, Guiches[] guiches) {

        //controle de fluxo
        int proximoGuiche = 1, count = 0, contaFinal = 0, proximo;

        //guichês
        for(int i = 0; i < guiches.length; i++) {
            //verifica se guichê tem fila
            if(guiches[i].getFila().getTamanhoFila() > 0) {
                //usuários
                for (Usuarios usuario: usuarios) {
                    //verifica se o usuário foi embora
                    if(usuario.getTurnosNecessarios() != -2) {
                        //verifica se o usuário chegou
                        if(usuario.getTurnosNecessarios() != -1) {
                            //verifica se usuário está sendo atendido
                            if (guiches[i].getAtendente()) {
                                //verifica se o guichê tem atendente
                                if (!usuario.getSendoAtendido()) {
                                    //verifica se o guichê está atendendo
                                    if (!guiches[i].getAtendendo()) {
                                        if (usuario.getPrecisaIr().charAt(0) == guiches[i].getTipo()) {
                                            usuario.setSendoAtendido(true);
                                            usuario.diminuiTurnosNecessarios();
                                            usuario.setQualGuicheSendoAtendido(i);
                                            guiches[i].setAtendendo(true);
                                        }
                                    } else {
                                        //verifica se o guichê que o usuário está esperando é o que está no loop
                                        if (usuario.getPrecisaIr().charAt(0) == guiches[i].getTipo()) {
                                            //verifica a quantidade de guichês e de atendentes
                                            if (guiches[i].getFila().getQtdeGuiches() > 1 && guiches[i].getFila().getAtendentes() > 1) {
                                                proximo = i + 1;
                                                //verifica se algum guichê próximo está livre
                                                for (int j = proximo; j < guiches.length; j++) {
                                                    if (usuario.getPrecisaIr().charAt(0) != guiches[j].getTipo()) {
                                                        if (j == guiches.length - 1) {
                                                            guiches[i].getFila().aumentaTempoTotalEspera();
                                                        }
                                                    } else {
                                                        guiches[i].getFila().diminuiTempoTotalEspera();
                                                    }
                                                }
                                            } else {
                                                guiches[i].getFila().aumentaTempoTotalEspera();
                                            }
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
                                        //se não faltar nenhum turno
                                        else if (usuario.getTurnosNecessarios() == 0) {
                                            guiches[i].setAtendendo(false);
                                            guiches[i].getFila().diminuiTamanhoFila();
                                            usuario.setSendoAtendido(false);

                                            //se estiver no último guichê define precisaIr = null
                                            if (usuario.getPrecisaIr().charAt(0) == usuario.getUltimoNecessario()) {
                                                //usuário acabou
                                                usuario.setPrecisaIr(null);
                                                usuario.setTurnosNecessarios(-2);
                                                usuario.setTurnosTotais(turno + 1 - usuario.getChegada());
                                            }
                                            //define proximo guichê para usuário
                                            else {
                                                usuario.setPrecisaIr(usuario.getPrecisaIr().substring(1));
                                            }

                                            //usuário não acabou
                                            if (usuario.getPrecisaIr() != null) {
                                                if (usuario.getPrecisaIr().charAt(0) == guiches[proximoGuiche].getTipo()) {
                                                    usuario.setTurnosNecessarios(guiches[proximoGuiche].getCusto());
                                                    usuario.setTurnosTotais(guiches[proximoGuiche].getCusto());
                                                    guiches[proximoGuiche].getFila().aumentaTamanhoFila();
                                                    guiches[proximoGuiche].getFila().aumentaQtdeUsuarios();
                                                } else {
                                                    //avança próximo guichê até o que for necessário
                                                    while (usuario.getPrecisaIr().charAt(0) != guiches[proximoGuiche].getTipo()) {
                                                        proximoGuiche++;
                                                        count++;
                                                    }
                                                    usuario.setTurnosNecessarios(guiches[proximoGuiche].getCusto());
                                                    usuario.setTurnosTotais(guiches[proximoGuiche].getCusto());
                                                    guiches[proximoGuiche].getFila().aumentaTamanhoFila();
                                                    guiches[proximoGuiche].getFila().aumentaQtdeUsuarios();

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

    //registra informações para arquivo de saída
    private void infoSaida(Usuarios[] usuarios, Combinacoes[] combinacoes, Fila[] filas) throws IOException {

        //define tempo médio do usuário no sistema
        double turnosTotaisGeral = 0, turnosMedia;
        for(Usuarios usuario: usuarios) {
            turnosTotaisGeral += usuario.getTurnosTotais();
        }
        turnosMedia = turnosTotaisGeral / usuarios.length;

        //define usuário que passou mais tempo no sistema
        int maisTempo = 0;
        Usuarios usuarioComMaisTempo = new Usuarios();
        for(Usuarios usuario: usuarios) {
            if(usuario.getTurnosTotais() > maisTempo) {
                maisTempo = usuario.getTurnosTotais();
                usuarioComMaisTempo = usuario;
            }
        }

        //define tempo médio de espera por tipo de fila
        for(Fila fila: filas) {
            fila.setTempoMedioEspera();
        }

        //define tempo médio por combinação de necessidades de atendimento
        for(Combinacoes combinacao: combinacoes) {
            for(Usuarios usuario: usuarios) {
                if (usuario.getCombinacao() == combinacao) {
                    combinacao.setTotalTempo(usuario.getTurnosTotais());
                }
            }
            combinacao.setTempoMedio();
        }

        WriteFile.escreverArquivo(turnosMedia, usuarioComMaisTempo, filas, combinacoes);

    }

    //caixa de alerta para carregamento
    private static void alertaCarregamento() {

        //cria e define caixa de alerta
        Stage janela = new Stage();
        janela.setTitle("AVISO!");
        janela.setMinWidth(350);
        janela.setMinHeight(150);

        //texto para caixa de aleta e botão
        Label label = new Label();
        label.setText("Carregar primeiro arquivo setup, depois arquivo fila!");
        Button botao = new Button("Ok");
        botao.setMinWidth(50);
        botao.setOnAction(e -> janela.close());

        //empilhando
        VBox vBox = new VBox(25);
        vBox.getChildren().addAll(label, botao);
        vBox.setAlignment(Pos.CENTER);

        //Exibe alerta
        Scene cena = new Scene(vBox);
        janela.setScene(cena);
        janela.showAndWait();
    }

    //método principal do simulador
    private void gameLoop(GraphicsContext graphicsContext) {

        //cria arquivo de setup e guichês
        String[] arquivoSetup = carregarArquivo();
        Troca troca = new Troca();
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
        defineGuiches(arquivoSetup, guiches, filas, troca);

        //desenha guichês
        desenhaGuiches(guiches, graphicsContext, filas);

        //cria arquivo fila e usuários
        String[] arquivoFila = carregarArquivo();
        Usuarios[] usuarios = new Usuarios[arquivoFila.length];

        //propriedades iniciais dos usuarios
        defineUsuarios(usuarios, arquivoFila);

        //define quantidade de combinações
        String precisaIr = usuarios[0].getPrecisaIr();
        int proximo = 2;
        countCombinacao++;

        //usuários
        for(int i = 1; i < usuarios.length; i++) {
            //verifica se onde ele precisa ir é igual ao do primeiro
            if (!usuarios[i].getPrecisaIr().equals(precisaIr)) {
                //se não for soma na combinação
                countCombinacao++;
                //verifica se os próximos também são iguais
                for(int k = proximo; k < usuarios.length; k++) {
                    if(usuarios[k].getPrecisaIr().equals(usuarios[i].getPrecisaIr()) && !usuarios[k].getJaSubtraiu()) {
                        countCombinacao--;
                        usuarios[k].setJaSubtraiu();
                    }
                }
            }
            proximo++;
        }

        //cria e define combinacoes
        Combinacoes[] combinacoes = new Combinacoes[countCombinacao];
        for(int i = 0; i < combinacoes.length; i++) {
            combinacoes[i] = new Combinacoes();
        }
        defineCombinacoes(usuarios, combinacoes);

        //loop principal
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            //atualiza guichês, fila e turno
            atualizaFila(usuarios, guiches);
            atualizaGuiches(filas, graphicsContext);
            turno++;

            //encerra processamento após fim da fila
            if (done) {
                graphicsContext.fillText("Fim!",1250,100);
                try {
                    infoSaida(usuarios, combinacoes, filas);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                botaoPara.fire();
            }
        }));
        timeline.setCycleCount(Animation.INDEFINITE);

        //velocidades
        botaoVelocidade1.setOnAction(e -> timeline.setRate(1));
        botaoVelocidade2.setOnAction(e -> timeline.setRate(2));
        botaoVelocidade3.setOnAction(e -> timeline.setRate(3));

        //play e pause
        botaoPlay.setOnAction(e -> timeline.play());
        botaoPause.setOnAction(e -> timeline.pause());

        //para simulação
        botaoPara.setOnAction(e -> timeline.stop());

        //roda simulador
        timeline.play();

    }

    //métodos básicos JavaFX
    @Override
    public void start(Stage primaryStage) throws Exception {

        //layout
        StackPane root = new StackPane();
        VBox vBox = new VBox(10);

        //barra de menu
        MenuBar menuBar = new MenuBar();
        menuBar.setMinHeight(25);
        menuBar.setPrefWidth(1300);

        //tela para ser desenhada
        Canvas canvas = new Canvas(1300,650);
        GraphicsContext graphicsContext = canvas.getGraphicsContext2D();

        //empilhando
        vBox.getChildren().addAll(menuBar, canvas);

        //ajustando botões de controle de fluxo
        HBox hBox = new HBox(1);
        botaoPlay.setMinWidth(50);
        botaoPause.setMinWidth(50);
        hBox.getChildren().addAll(botaoVelocidade1, botaoVelocidade2, botaoVelocidade3, botaoPlay, botaoPause);

        //adicionando a raiz e propriedades
        root.getChildren().addAll(vBox, hBox);
        primaryStage.setTitle("Project San Francisco");
        primaryStage.setScene(new Scene(root, 1300, 680));
        primaryStage.setResizable(false);

        //exibindo
        primaryStage.show();

        //aviso do carregamento de arquivos
        alertaCarregamento();

        //roda simulador
        gameLoop(graphicsContext);

    }
    public static void main(String[] args) throws IOException {
        launch(args);
    }

}
