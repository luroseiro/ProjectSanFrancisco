package sample;

class Guiches {

    //tipo
    private char tipo;
    char getTipo() {
        return tipo;
    }
    void setTipo(char tipo) {
        this.tipo = tipo;
    }

    //número
    private int numero;
    int getNumero() {
        return numero;
    }
    void setNumero(int numero) {
        this.numero = numero;
    }

    //atendente
    private boolean atendente;
    boolean getAtendente() {
        return atendente;
    }
    void setAtendente(boolean atendente) {
        this.atendente = atendente;
    }

    //custo
    private int custo;
    int getCusto() {
        return custo;
    }
    void setCusto(int custo) {
        this.custo = custo;
    }

    //rótulo
    private String rotulo;
    String getRotulo() {
        return rotulo;
    }
    void setRotulo(String rotulo) {
        this.rotulo = rotulo;
    }

    //fila
    private Fila fila;
    Fila getFila() {
        return fila;
    }
    void setFila(Fila fila) {
        this.fila = fila;
    }

    //atendendo
    private boolean atendendo = false;
    boolean getAtendendo() {
        return atendendo;
    }
    void setAtendendo(boolean atendendo) {
        this.atendendo = atendendo;
    }

    private boolean recebendoTroca = false;
    boolean getRecebendoTroca() {
        return recebendoTroca;
    }
    void setRecebendoTroca(boolean recebendoTroca) {
        this.recebendoTroca = recebendoTroca;
    }

    private int countTroca = 0;
    int getCountTroca() {
        return countTroca;
    }
    void aumentaCountTroca() {
        this.countTroca++;
    }
    void resetCountTroca() {
        this.countTroca = 0;
    }

}
