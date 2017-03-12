package sample;

import java.io.*;

public class ReadFile {

    private String caminho;

    public ReadFile(String caminhoArquivo) {
        caminho = caminhoArquivo;
    }

    public String[] abreArquivo() throws IOException {
        BufferedReader leitorTexto = new BufferedReader(new InputStreamReader(new FileInputStream(caminho), "ISO-8859-1"));

        int linhas = linhasLidas();
        String[] dadosTexto = new String[linhas];

        for (int i = 0; i < linhas; i++) {
            dadosTexto[i] = leitorTexto.readLine();
        }

        leitorTexto.close();
        return dadosTexto;

    }

    private int linhasLidas() throws IOException {

        FileReader arquivoParaLer = new FileReader(caminho);
        BufferedReader leitorArquivo = new BufferedReader(arquivoParaLer);

        String arrayLinha;
        int linhas = 0;

        while ((arrayLinha = leitorArquivo.readLine()) != null) {
            linhas++;
        }
        leitorArquivo.close();

        return linhas;

    }

}