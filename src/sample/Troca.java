package sample;

class Troca {

    //custo da troca
    private int custo;
    int getCusto() {
        return custo;
    }
    void setCusto(int custo) {
        this.custo = custo;
    }

    //guichê no qual o atendente estava
    private char guicheAntes;
    char getGuicheAntes() {
        return guicheAntes;
    }
    void setGuicheAntes(char guicheAntes) {
        this.guicheAntes = guicheAntes;
    }

    //guichê pro qual o atendente vai
    private char guicheDepois;
    char getGuicheDepois() {
        return guicheDepois;
    }
    void setGuicheDepois(char guicheDepois) {
        this.guicheDepois = guicheDepois;
    }

}
