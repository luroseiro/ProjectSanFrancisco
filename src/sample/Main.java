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
    private int turno = 0, tipoGuiche = 0, countCombinacao = 0, vezExplorador = 1;
    private boolean done = false, desenhadoTurno = false, primeiraVez = true;

    //explorador de arquivos
    private String[] exploradorDeArquivos() {

        //inicia explorador
        FileChooser exploradorDeArquivos = new FileChooser();
        exploradorDeArquivos.setInitialDirectory(new File("."));
        exploradorDeArquivos.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Arquivo de texto", "*.txt"));

        //define título do explorador
        if(vezExplorador == 1) {
            exploradorDeArquivos.setTitle("Carregar setup");
        }
        else if(vezExplorador == 2) {
            exploradorDeArquivos.setTitle("Carregar fila");
        }

        //cria arquivo
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
                    guiche.setAtendente(true);
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

    //desenhos
    private void desenhoInicial(GraphicsContext gc, Guiches[] guiches, Fila[] filas, double posicaoXProximoCima, double posicaoXProximoBaixo, int desenhados) {

        //propriedades gerais
        gc.setFill(Color.BLACK);
        gc.setFont(Font.font(20));
        gc.setLineWidth(2);
        gc.setTextAlign(TextAlignment.CENTER);

        //legenda texto
        gc.fillText("Guichê vazio", 1210,160);
        gc.fillText("Guichê não", 1210,290);
        gc.fillText("atendendo", 1210,310);
        gc.fillText("Guichê", 1210,440);
        gc.fillText("atendendo", 1210,460);
        gc.fillText("Guichê", 1210,590);
        gc.fillText("no processo", 1210,610);
        gc.fillText("de troca", 1210,630);
        //legenda guichê
        gc.fillRoundRect(1170, 50, 85, 85, 10, 10);
        gc.setFill(Color.YELLOW);
        gc.fillRoundRect(1170, 180, 85, 85, 10, 10);
        gc.setFill(Color.GREEN);
        gc.fillRoundRect(1170, 330, 85, 85, 10, 10);
        gc.setFill(Color.ORANGE);
        gc.fillRoundRect(1170,480,85,85,10,10);

        //desenha rótulo do guichê
        for(Guiches guiche: guiches) {
            //propriedades dos rótulos
            gc.setFill(Color.BLACK);
            //desenha em cima
            if(desenhados < 10) {
                gc.fillText(guiche.getRotulo(), posicaoXProximoCima + 42.5, 20, 90);
                desenhados++;
                posicaoXProximoCima += 100;
            }
            //desenha embaixo
            else {
                gc.fillText(guiche.getRotulo(), posicaoXProximoBaixo + 42.5, 625, 90);
                posicaoXProximoBaixo += 100;
            }
        }

        //controle de posicionamento para borda da fila
        posicaoXProximoCima = 92.5;
        posicaoXProximoBaixo = 92.5;

        //desenha borda
        for(Fila fila: filas) {
            //desenha em cima
            if(posicaoXProximoCima <= 992.5) {
                if (fila.getQtdeGuiches() > 1) {
                    gc.strokeRoundRect(posicaoXProximoCima + 50 * (fila.getQtdeGuiches() - 1),135,50,175,10,10);
                    posicaoXProximoCima += 100 * fila.getQtdeGuiches();
                } else {
                    gc.strokeRoundRect(posicaoXProximoCima,135,50,175,10,10);
                    posicaoXProximoCima += 100;
                }
            }
            //desenha embaixo
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
    private void desenhaTurno(GraphicsContext gc) {

        //propriedades
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setFont(Font.font(40));
        gc.setFill(Color.BLACK);

        //atualiza texto
        if (!desenhadoTurno) {
            gc.clearRect(1100, 0, 250, 50);
            gc.fillText("Turno: " + Integer.toString(turno), 1200, 30);
            desenhadoTurno = true;
        } else {
            gc.clearRect(1100, 0, 250, 50);
            gc.fillText("Turno: " + Integer.toString(turno), 1200, 30);
            desenhadoTurno = false;
        }

    }
    private void desenhaGuiches(GraphicsContext gc, Guiches[] guiches, double posicaoXProximoCima, double posicaoXProximoBaixo, int desenhados) {

        for(Guiches guiche: guiches) {
            //verifica atendente/atendendo e defina pintura
            if(!guiche.getAtendendo()) {
                if(!guiche.getRecebendoTroca()) {
                    if (guiche.getAtendente()) {
                        gc.setFill(Color.YELLOW);
                    } else {
                        gc.setFill(Color.BLACK);
                    }
                }
                else {
                    gc.setFill(Color.ORANGE);
                }
            }
            else {
                gc.setFill(Color.GREEN);
            }

            //desenha em cima
            if(desenhados < 10) {
                //guichê
                gc.fillRoundRect(posicaoXProximoCima, 40, 85, 85, 10, 10);

                //tipo
                if(guiche.getAtendente()) {
                    gc.setFill(Color.BLACK);
                }
                else {
                    gc.setFill(Color.WHITE);
                }
                gc.setFont(Font.font(50));
                gc.fillText(Character.toString(guiche.getTipo()), posicaoXProximoCima + 42.5, 100, 90);

                //controle de posicionamento superior
                desenhados++;
                posicaoXProximoCima += 100;
            }
            //desenha embaixo
            else {
                //guichê
                gc.fillRoundRect(posicaoXProximoBaixo, 510, 85, 85, 10, 10);

                //desenha tipo
                if(guiche.getAtendente()) {
                    gc.setFill(Color.BLACK);
                }
                else {
                    gc.setFill(Color.WHITE);
                }
                gc.setFont(Font.font(50));
                gc.fillText(Character.toString(guiche.getTipo()), posicaoXProximoBaixo + 42.5, 570, 90);

                //controle de posicionamento inferior
                posicaoXProximoBaixo += 100;
            }

        }

    }
    private void desenhaFilas(GraphicsContext gc, Fila[] filas, double posicaoXProximoCima, double posicaoXProximoBaixo) {

        //controle
        int posicaoXProximoCimaTurno = 117, posicaoXProximoBaixoTurno = 117;

        for(Fila fila: filas) {
            //define pintura da barra
            gc.setFill(Color.DARKORANGE);

            //desenha em cima
            if(posicaoXProximoCima <= 994) {
                if (fila.getQtdeGuiches() > 1) {
                    //limpa barra
                    gc.clearRect(posicaoXProximoCima + 2 + 50 * (fila.getQtdeGuiches() - 1),138,43,170);

                    //desenha barra
                    if(fila.getTamanhoFila() <= 165) {
                        gc.fillRoundRect(posicaoXProximoCima + 4.5 + 50 * (fila.getQtdeGuiches() - 1),140,38,(fila.getTamanhoFila() * 165) / 100,10,10);
                    } else {
                        gc.fillRoundRect(posicaoXProximoCima + 4.5 + 50 * (fila.getQtdeGuiches() - 1),140,38,165,10,10);
                    }

                    //desenha tamanho da fila
                    gc.setFont(Font.font(35));
                    gc.setFill(Color.BLACK);
                    if(fila.getTamanhoFila() <= 99) {
                        gc.fillText(Integer.toString(fila.getTamanhoFila()), posicaoXProximoCimaTurno + 50 * (fila.getQtdeGuiches() - 1), 298, 50);
                    } else {
                        gc.fillText("99", posicaoXProximoCimaTurno + 50 * (fila.getQtdeGuiches() - 1), 278, 50);
                        gc.fillText("+", posicaoXProximoCimaTurno + 50 * (fila.getQtdeGuiches() - 1), 298, 50);
                    }

                    //controle de posicionamento superior
                    posicaoXProximoCima += 100 * fila.getQtdeGuiches();
                    posicaoXProximoCimaTurno += 100 * fila.getQtdeGuiches();
                } else {
                    //limpa barra
                    gc.clearRect(posicaoXProximoCima + 2,138,43,170);

                    //desenha barra
                    if(fila.getTamanhoFila() <= 165) {
                        gc.fillRoundRect(posicaoXProximoCima + 4.5,140,38,(fila.getTamanhoFila() * 165) / 100,10,10);
                    } else {
                        gc.fillRoundRect(posicaoXProximoCima + 4.5,140,38,165,10,10);
                    }

                    //desenha tamanho da fila
                    gc.setFont(Font.font(35));
                    gc.setFill(Color.BLACK);
                    if(fila.getTamanhoFila() <= 99) {
                        gc.fillText(Integer.toString(fila.getTamanhoFila()), posicaoXProximoCimaTurno, 298, 50);
                    } else {
                        gc.fillText("99", posicaoXProximoCimaTurno, 278, 50);
                        gc.fillText("+", posicaoXProximoCimaTurno, 298, 50);
                    }

                    //controle de posicionamento superior
                    posicaoXProximoCima += 100;
                    posicaoXProximoCimaTurno += 100;
                }
            }
            //desenha embaixo
            else {
                if (fila.getQtdeGuiches() > 1) {
                    //limpa barra
                    gc.clearRect(posicaoXProximoBaixo + 2 + 50 * (fila.getQtdeGuiches() - 1),328,43,170);

                    //desenha barra
                    if(fila.getTamanhoFila() <= 165) {
                        gc.fillRoundRect(posicaoXProximoBaixo + 4.5 + 50 * (fila.getQtdeGuiches() - 1),326,38,(fila.getTamanhoFila() * 165) / 100,10,10);
                    } else {
                        gc.fillRoundRect(posicaoXProximoBaixo + 4.5 + 50 * (fila.getQtdeGuiches() - 1),326,38,165,10,10);
                    }

                    //desenha tamanho da fila
                    gc.setFont(Font.font(35));
                    gc.setFill(Color.BLACK);
                    if(fila.getTamanhoFila() <= 99) {
                        gc.fillText(Integer.toString(fila.getTamanhoFila()), posicaoXProximoBaixoTurno + 50 * (fila.getQtdeGuiches() - 1), 360, 50);
                    } else {
                        gc.fillText("99", posicaoXProximoBaixoTurno + 50 * (fila.getQtdeGuiches() - 1), 360, 50);
                        gc.fillText("+", posicaoXProximoBaixoTurno + 50 * (fila.getQtdeGuiches() - 1), 380, 50);
                    }

                    //controle de posicionamento inferior
                    posicaoXProximoBaixo += 100 * fila.getQtdeGuiches();
                    posicaoXProximoBaixoTurno += 100 * fila.getQtdeGuiches();
                } else {
                    //limpa barra
                    gc.clearRect(posicaoXProximoBaixo + 2,328,43,170);

                    //desenha barra
                    if(fila.getTamanhoFila() <= 165) {
                        gc.fillRoundRect(posicaoXProximoBaixo + 4.5,326,38,(fila.getTamanhoFila() * 165) / 100,10,10);
                    } else {
                        gc.fillRoundRect(posicaoXProximoBaixo + 4.5,326,38,165,10,10);
                    }

                    //desenha tamanho da fila
                    gc.setFont(Font.font(35));
                    gc.setFill(Color.BLACK);
                    if(fila.getTamanhoFila() <= 99) {
                        gc.fillText(Integer.toString(fila.getTamanhoFila()), posicaoXProximoBaixoTurno + 50 * (fila.getQtdeGuiches() - 1), 360, 50);
                    } else {
                        gc.fillText("99", posicaoXProximoBaixoTurno, 360, 50);
                        gc.fillText("+", posicaoXProximoBaixoTurno, 380, 50);
                    }

                    //controle de posicionamento inferior
                    posicaoXProximoBaixo += 100;
                    posicaoXProximoBaixoTurno += 100;
                }
            }

        }

    }

    //atualiza guichês e filas
    private void atualizaGuiches(Guiches[] guiches, Fila[] filas, GraphicsContext gc) {

        //controle
        double posicaoXProximoCima = 75, posicaoXProximoBaixo = 75;
        int desenhados = 0;

        //desenho inicial
        if(primeiraVez) {
            desenhoInicial(gc, guiches, filas, posicaoXProximoCima, posicaoXProximoBaixo, desenhados);
            primeiraVez = false;
        }

        //desenha turnos
        desenhaTurno(gc);

        //desenha guichês
        desenhaGuiches(gc, guiches, posicaoXProximoCima, posicaoXProximoBaixo, desenhados);

        //desenha filas
        desenhaFilas(gc, filas, posicaoXProximoCima + 19, posicaoXProximoBaixo + 19);

    }
    private void atualizaFila(Usuarios[] usuarios, Guiches[] guiches, Troca troca) {

        //verifica se troca é necessária
        verificaMudanca(guiches, troca, true);

        //atualiza usuário para fila da triagem
        for(Usuarios usuario: usuarios) {
            if(usuario.getChegada() == turno) {
                guiches[0].getFila().aumentaTamanhoFila();
                guiches[0].getFila().aumentaQtdeUsuarios();
                usuario.setTurnosNecessarios(guiches[0].getCusto() + 1);
                usuario.setTurnosTotais(guiches[0].getCusto());
            }
        }

        //atualiza usuário para outros guichês
        //controle de fluxo
        int proximoGuiche = 1, count = 0, proximo, contaFinal = 0;

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
                                    if (usuario.getPrecisaIr().charAt(0) == guiches[i].getTipo()) {
                                        if (!guiches[i].getAtendendo()) {
                                            usuario.setSendoAtendido(true);
                                            usuario.diminuiTurnosNecessarios();
                                            usuario.setQualGuicheSendoAtendido(i);
                                            guiches[i].setAtendendo(true);
                                            guiches[i].getFila().aumentaQtdeGuichesAtendendo();
                                        } else {
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
                                                        break;
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
                                            guiches[i].getFila().diminuiQtdeGuichesAtendendo();
                                            usuario.setSendoAtendido(false);

                                            //se estiver no último guichê define precisaIr = null
                                            if (usuario.getPrecisaIr().charAt(0) == usuario.getUltimoNecessario()) {
                                                //usuário acabou
                                                usuario.setPrecisaIr(null);
                                                usuario.setTurnosNecessarios(-2);
                                                if(usuario.getChegada() == 1) {
                                                    usuario.setTurnosTotais(turno + 1 - usuario.getChegada());
                                                }
                                                else {
                                                    usuario.setTurnosTotais(turno - usuario.getChegada());
                                                }
                                            }
                                            //define proximo guichê para usuário
                                            else {
                                                usuario.setPrecisaIr(usuario.getPrecisaIr().substring(1));
                                            }

                                            //verifica se algum usuário anterior está na mesma fila para ser atendido
                                            if(guiches[i].getFila().getTamanhoFila() > 0) {
                                                for(Usuarios usuario2: usuarios) {
                                                    //se o usuário já chegou e foi passado atende
                                                    if(usuario2.getChegada() <= turno && !usuario2.getSendoAtendido() && usuario2.getPrecisaIr() != null
                                                            && guiches[i].getFila().getQtdeGuichesAtendendo() < guiches[i].getFila().getAtendentes()) {
                                                        if (usuario2.getPrecisaIr().charAt(0) == guiches[i].getTipo()) {
                                                            if (usuario2.getNumeroUsuario() < usuario.getNumeroUsuario()) {
                                                                usuario2.setSendoAtendido(true);
                                                                usuario2.diminuiTurnosNecessarios();
                                                                usuario2.setQualGuicheSendoAtendido(i);
                                                                guiches[i].setAtendendo(true);
                                                                guiches[i].getFila().aumentaQtdeGuichesAtendendo();
                                                                guiches[i].getFila().diminuiTempoTotalEspera();
                                                            }
                                                        }
                                                    }
                                                }
                                            }

                                            //usuário não acabou
                                            if (usuario.getPrecisaIr() != null) {
                                                //usuário vai pro próximo
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
                                            //verifica se troca é necessária
                                            verificaMudanca(guiches, troca, false);
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

        //verifica se troca é necessária
        verificaMudanca(guiches, troca, false);

    }

    //troca guichê do atendente
    private void verificaMudanca(Guiches[] guiches, Troca troca, boolean inicial) {

        //controle de divisão
        int aux1, aux2;

        //guichês que podem precisar de troca
        for(Guiches guicheQuePrecisa: guiches) {
            //condições para o guichê precisar de uma troca
            if (guicheQuePrecisa.getFila().getQtdeGuiches() > guicheQuePrecisa.getFila().getAtendentes()
                    && !guicheQuePrecisa.getAtendente() && guicheQuePrecisa.getFila().getTamanhoFila() >= 1
                    && guicheQuePrecisa.getFila().getAtendentes() < guicheQuePrecisa.getFila().getTamanhoFila()) {

                //verifica guichês que podem fazer a troca
                for(Guiches guiche: guiches) {
                    //se a fila já estiver recebendo uma troca
                    if(guicheQuePrecisa.getFila().getRecebendoTroca()) {
                        //se o guichê já estiver recebendo uma troca
                        if(guicheQuePrecisa.getRecebendoTroca()) {
                            trocaAtendente(guicheQuePrecisa, troca, inicial);
                            break;
                        //se o guichê não estiver recebendo uma troca e vai começar agora
                        } else {
                            //condições para começar uma nova troca
                            if(guicheQuePrecisa.getFila().getQtdeTrocas()
                                    + guicheQuePrecisa.getFila().getAtendentes() < guicheQuePrecisa.getFila().getTamanhoFila()
                                    && guiche.getAtendente() && !guiche.getAtendendo()
                                    && guiche.getFila().getTamanhoFila() < guiche.getFila().getAtendentes()) {

                                //auxiliar para divisão do que vai ceder
                                if(guiche.getFila().getAtendentes() == 0) {
                                    aux1 = 1;
                                }
                                else {
                                    aux1 = guiche.getFila().getAtendentes();
                                }

                                //auxiliar para divisão do que vai receber
                                if(guicheQuePrecisa.getFila().getAtendentes() == 0) {
                                    aux2 = 1;
                                } else {
                                    aux2 = guicheQuePrecisa.getFila().getAtendentes();
                                }

                                //verifica se vale a pena começar uma troca
                                if (Math.round(((guicheQuePrecisa.getFila().getTamanhoFila() - guicheQuePrecisa.getFila().getQtdeGuichesAtendendo())
                                        * guicheQuePrecisa.getCusto()) / aux2)
                                        > Math.round((((guiche.getFila().getTamanhoFila() * guiche.getCusto()) / aux1) + troca.getCusto()) * 1.5)) {

                                    trocaAtendente(guicheQuePrecisa, troca, inicial);
                                    guiche.setAtendente(false);
                                    guiche.getFila().diminuiAtendentes();
                                    guicheQuePrecisa.setRecebendoTroca(true);
                                    guicheQuePrecisa.getFila().setRecebendoTroca(true);
                                    guicheQuePrecisa.getFila().aumentaQtdeTrocas();
                                    break;
                                }
                            }
                        }
                    //se a fila não estiver recebendo troca
                    } else {
                        //condições para começar uma nova troca
                        if (!guiche.getAtendendo() && guiche.getTipo() != guicheQuePrecisa.getTipo()
                                && guiche.getFila().getTamanhoFila() < guiche.getFila().getAtendentes()) {

                            //auxiliar para divisão do que vai ceder
                            if(guiche.getFila().getAtendentes() == 0) {
                                aux1 = 1;
                            }
                            else {
                                aux1 = guiche.getFila().getAtendentes();
                            }

                            //se o que precisa não tiver atendente
                            if(guicheQuePrecisa.getFila().getAtendentes() == 0) {
                                if(guiche.getAtendente()) {
                                    trocaAtendente(guicheQuePrecisa, troca, inicial);
                                    guiche.setAtendente(false);
                                    guiche.getFila().diminuiAtendentes();
                                    guicheQuePrecisa.setRecebendoTroca(true);
                                    guicheQuePrecisa.getFila().setRecebendoTroca(true);
                                    guicheQuePrecisa.getFila().aumentaQtdeTrocas();
                                    break;
                                }
                            //se tiver atendente
                            } else {
                                //verifica se vale a pena
                                if (Math.round(((guicheQuePrecisa.getFila().getTamanhoFila() - guicheQuePrecisa.getFila().getQtdeGuichesAtendendo())
                                        * guicheQuePrecisa.getCusto()) / guicheQuePrecisa.getFila().getAtendentes())
                                        > Math.round((((guiche.getFila().getTamanhoFila() * guiche.getCusto()) / aux1) + troca.getCusto()) * 1.5)) {

                                    trocaAtendente(guicheQuePrecisa, troca, inicial);
                                    guiche.setAtendente(false);
                                    guiche.getFila().diminuiAtendentes();
                                    guicheQuePrecisa.setRecebendoTroca(true);
                                    guicheQuePrecisa.getFila().setRecebendoTroca(true);
                                    guicheQuePrecisa.getFila().aumentaQtdeTrocas();
                                    break;
                                }
                            }
                        }
                    }
                }

            }
        }

    }
    private void trocaAtendente(Guiches novo, Troca troca, boolean inicial) {

        //verifica se ele não está recebendo troca
        if(!novo.getRecebendoTroca()) {
            novo.aumentaCountTroca();
        }
        //se estiver, só roda se for no começo de cada turno
        else {
            if(inicial) {
                novo.aumentaCountTroca();
                //verifica se o custo da troca já foi pago
                if (novo.getCountTroca() == troca.getCusto()) {
                    novo.setAtendente(true);
                    novo.getFila().aumentaAtendentes();
                    novo.setRecebendoTroca(false);
                    novo.getFila().diminuiQtdeTrocas();
                    novo.resetCountTroca();
                    //verifica se a fila não está mais recebendo trocas
                    if(novo.getFila().getQtdeTrocas() == 0) {
                        novo.getFila().setRecebendoTroca(false);
                    }
                }
            }
        }

    }

    //registra informações para arquivo de saída
    private void infoSaida(Usuarios[] usuarios, Combinacoes[] combinacoes, Fila[] filas) throws IOException {

        //define tempo médio do usuário no sistema
        double turnosTotaisGeral = 0, turnosMedia;
        for(Usuarios usuario: usuarios) {
            turnosTotaisGeral += usuario.getTurnosTotais();
        }
        turnosMedia = turnosTotaisGeral / (double) usuarios.length;

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
        String[] arquivoSetup = exploradorDeArquivos();
        vezExplorador++;
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

        //cria troca
        Troca troca = new Troca();

        //define guichês e suas filas
        defineGuiches(arquivoSetup, guiches, filas, troca);

        //cria arquivo fila e usuários
        String[] arquivoFila = exploradorDeArquivos();
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
            atualizaFila(usuarios, guiches, troca);
            atualizaGuiches(guiches, filas, graphicsContext);
            turno++;

            //encerra processamento após fim da fila
            if (done) {
                graphicsContext.fillText("Fim!",1210,600);
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
        graphicsContext.setFont(Font.font("Arial"));

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
