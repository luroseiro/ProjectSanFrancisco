package sample;

class Usuarios {

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

}
