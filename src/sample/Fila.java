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

}
