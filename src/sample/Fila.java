package sample;

public class Fila {

    //tipo de guichês para a fila
    char tipoFila;
    public char getTipoFila() {
        return tipoFila;
    }
    public void setTipoFila(char tipoFila) {
        this.tipoFila = tipoFila;
    }

    //quantidade de guichês para a fila
    int qtdeGuiches;
    public int getQtdeGuiches() {
        return qtdeGuiches;
    }
    public void aumentaQtdeGuiches() {
        this.qtdeGuiches++;
    }

    //tamanho da fila
    int tamanhoFila = 0;
    public int getTamanhoFila() {
        return tamanhoFila;
    }
    public void aumentaTamanhoFila() {
        this.tamanhoFila++;
    }
    public void diminuiTamanhoFila() {
        this.tamanhoFila--;
    }

}
