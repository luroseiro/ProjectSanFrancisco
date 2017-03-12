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

    //atendente
    private boolean atendente;
    boolean getAtendente() {
        return atendente;
    }
    void setAtendente() {
        this.atendente = true;
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

}
