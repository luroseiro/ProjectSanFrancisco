package sample;

class Combinacoes {

    //qual é a combinação
    private String combinacaoGuiche = null;
    String getCombinacaoGuiche() {
        return combinacaoGuiche;
    }
    void setCombinacaoGuiche(String combinacaoGuiche) {
        this.combinacaoGuiche = combinacaoGuiche;
    }

    //quantidade de usuários da combinação
    private double qtdeUsuarios = 0;
     double getQtdeUsuarios() {
        return qtdeUsuarios;
    }
    void aumentaQtdeUsuarios() {
        this.qtdeUsuarios++;
    }

    //tempo total da combinação
    private double totalTempo = 0;
    private double getTotalTempo() {
        return totalTempo;
    }
    void setTotalTempo(int totalTempo) {
        this.totalTempo += totalTempo;
    }

    //define o tempo médio da combinação
    private double tempoMedio;
    double getTempoMedio() {
        return tempoMedio;
    }
    void setTempoMedio() {
        tempoMedio = getTotalTempo() / getQtdeUsuarios();
    }

}
