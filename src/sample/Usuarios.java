package sample;

class Usuarios {

    //define numero de usuário
    private int numeroUsuario;
    int getNumeroUsuario() {
        return numeroUsuario;
    }
    void setNumeroUsuario(int numeroUsuario) {
        this.numeroUsuario = numeroUsuario;
    }

    //chegada
    private int chegada;
    int getChegada() {
        return chegada;
    }
    void setChegada(int chegada) {
        this.chegada = chegada;
    }

    //onde usuário precisa ir
    private String precisaIr;
    String getPrecisaIr() {
        return precisaIr;
    }
    void setPrecisaIr(String precisaIr) {
        this.precisaIr = precisaIr;
    }

    //último guichê necessário para usuário
    private char ultimoNecessario;
    char getUltimoNecessario() {
        return ultimoNecessario;
    }
    void setUltimoNecessario(char ultimoNecessario) {
        this.ultimoNecessario = ultimoNecessario;
    }

    //sendo atendido
    private boolean sendoAtendido = false;
    boolean getSendoAtendido() {
        return sendoAtendido;
    }
    void setSendoAtendido(boolean sendoAtendido) {
        this.sendoAtendido = sendoAtendido;
    }

    //em qual guichê o usuário está sendo atendido
    private int qualGuicheSendoAtendido;
    int getQualGuicheSendoAtendido() {
        return qualGuicheSendoAtendido;
    }
    void setQualGuicheSendoAtendido(int qualGuicheSendoAtendido) {
        this.qualGuicheSendoAtendido = qualGuicheSendoAtendido;
    }

    //turnos necessários para o usuário
    private int turnosNecessarios = -1;
    int getTurnosNecessarios() {
        return turnosNecessarios;
    }
    void setTurnosNecessarios(int turnosNecessarios) {
        this.turnosNecessarios += turnosNecessarios;
    }
    void diminuiTurnosNecessarios() {
        this.turnosNecessarios--;
    }

    //turnos totais que o usuário demorou
    private int turnosTotais = 0;
    int getTurnosTotais() {
        return turnosTotais;
    }
    void setTurnosTotais(int turnosTotais) {
        this.turnosTotais = turnosTotais;
    }

    //coloca o usuário em uma combinação
    private Combinacoes combinacao;
    Combinacoes getCombinacao() {
        return combinacao;
    }
    void setCombinacao(Combinacoes combinacao) {
        this.combinacao = combinacao;
    }

    //verifica se o usuário está numa combinação
    private boolean estaNumaCombinacao = false;
    boolean getEstaNumaCombinacao() {
        return estaNumaCombinacao;
    }
    void setEstaNumaCombinacao() {
        this.estaNumaCombinacao = true;
    }

    //verifica se o usuário já foi subtraido da conta de combinação
    private boolean jaSubtraiu = false;
    boolean getJaSubtraiu() {
        return jaSubtraiu;
    }
    void setJaSubtraiu() {
        this.jaSubtraiu = true;
    }

}
