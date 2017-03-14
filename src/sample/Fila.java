package sample;

class Fila {

    //tipo de guichês para a fila
    private char tipoFila;
    char getTipoFila() {
        return tipoFila;
    }
    void setTipoFila(char tipoFila) {
        this.tipoFila = tipoFila;
    }

    //quantidade de guichês para a fila
    private int qtdeGuiches;
    int getQtdeGuiches() {
        return qtdeGuiches;
    }
    void aumentaQtdeGuiches() {
        this.qtdeGuiches++;
    }

    //tamanho da fila
    private int tamanhoFila = 0;
    int getTamanhoFila() {
        return tamanhoFila;
    }
    void aumentaTamanhoFila() {
        this.tamanhoFila++;
    }
    void diminuiTamanhoFila() {
        this.tamanhoFila--;
    }

    //quantidade de atendentes na fila
    private int atendentes = 0;
    int getAtendentes() {
        return atendentes;
    }
    void aumentaAtendentes() {
        this.atendentes++;
    }

    //quantidade de usuários que passaram pela fila
    private double qtdeUsuarios = 0;
    private double getQtdeUsuarios() {
        return qtdeUsuarios;
    }
    void aumentaQtdeUsuarios() {
        this.qtdeUsuarios++;
    }

    //tempo total de espera dos usuários
    private double tempoTotalEspera = 0;
    private double getTempoTotalEspera() {
        return tempoTotalEspera;
    }
    void aumentaTempoTotalEspera() {
        this.tempoTotalEspera++;
    }
    void diminuiTempoTotalEspera() {
        this.tempoTotalEspera--;
    }

    //tempo médio de espera dos usuários
    private double tempoMedioEspera;
    double getTempoMedioEspera() {
        return tempoMedioEspera;
    }
    void setTempoMedioEspera() {
        tempoMedioEspera = getTempoTotalEspera() / getQtdeUsuarios();

    }

}
