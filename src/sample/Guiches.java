package sample;

public class Guiches {

    //tipo
    char tipo;
    public char getTipo() {
        return tipo;
    }
    public void setTipo(char tipo) {
        this.tipo = tipo;
    }

    //atendente
    boolean atendente;
    public boolean getAtendente() {
        return atendente;
    }
    public void setAtendente(boolean atendente) {
        this.atendente = atendente;
    }

    //custo
    int custo;
    public int getCusto() {
        return custo;
    }
    public void setCusto(int custo) {
        this.custo = custo;
    }

    //rótulo
    String rotulo;
    public String getRotulo() {
        return rotulo;
    }
    public void setRotulo(String rotulo) {
        this.rotulo = rotulo;
    }

    //fila
    Fila fila;
    public Fila getFila() {
        return fila;
    }
    public void setFila(Fila fila) {
        this.fila = fila;
    }

    //atendendo
    boolean atendendo = false;
    public boolean getAtendendo() {
        return atendendo;
    }
    public void setAtendendo(boolean atendendo) {
        this.atendendo = atendendo;
    }

}
