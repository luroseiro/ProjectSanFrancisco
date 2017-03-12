package sample;

class Troca {

    //custo da troca
    private int custo;
    public int getCusto() {
        return custo;
    }
    public void setCusto(int custo) {
        this.custo = custo;
    }

    //guichê no qual o atendente estava
    private char guicheAntes;
    public char getGuicheAntes() {
        return guicheAntes;
    }
    public void setGuicheAntes(char guicheAntes) {
        this.guicheAntes = guicheAntes;
    }

    //guichê pro qual o atendente vai
    private char guicheDepois;
    public char getGuicheDepois() {
        return guicheDepois;
    }
    public void setGuicheDepois(char guicheDepois) {
        this.guicheDepois = guicheDepois;
    }


}
