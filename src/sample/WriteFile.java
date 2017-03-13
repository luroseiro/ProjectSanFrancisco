package sample;

import java.io.*;
import java.text.DecimalFormat;

class WriteFile {

    static void escreverArquivo(double turnosMedia, Usuarios usuario, Fila[] filas, Combinacoes[] combinacoes) throws IOException {

        //cria quebra de linha
        String quebraLinha = System.getProperty("line.separator");

        //define formato para números
        DecimalFormat formato = new DecimalFormat("#.##");

        //inicia escritor
        BufferedWriter escritor = new BufferedWriter(new FileWriter("C:\\Users\\lf_ro\\Downloads\\saida.txt"));

        //escreve tempo médio de um usuário no sistema
        escritor.append("Tempo médio de um usuário no sistema: ").append(formato.format(turnosMedia))
                .append(" turnos").append(quebraLinha).append(quebraLinha);

        //escreve tempo médio de espera por tipo de fila
        for(Fila fila: filas) {
            escritor.append("Tempo médio de espera na fila ").append(String.valueOf(fila.getTipoFila()))
                    .append(": ").append(formato.format(fila.getTempoMedioEspera())).append(" turnos").append(quebraLinha).append(quebraLinha);
        }

        //escreve tempo médio por combinações de necessidades de atendimento
        for(Combinacoes combinacao: combinacoes) {
            escritor.append("Tempo médio na combinação ").append(combinacao.getCombinacaoGuiche())
                    .append(": ").append(formato.format(combinacao.getTempoMedio())).append(" turnos").append(quebraLinha).append(quebraLinha);
        }

        //escreve usuário que passou mais tempo no sistema
        escritor.append("Quem passou mais tempo no sistema foi o usuário ").append(String.valueOf(usuario.getNumeroUsuario()))
                .append(quebraLinha).append(quebraLinha);

        //fecha escritor
        escritor.close();

    }

}
