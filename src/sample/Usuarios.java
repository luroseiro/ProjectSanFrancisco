package sample;

public class Usuarios {

    //ordem de chegada
    int userOrdem = 0;
    public int getUserOrdem() {
        return userOrdem;
    }
    public void setUserOrdem(int userOrdem) {
        this.userOrdem = userOrdem;
    }

    //chegada
    int chegada;
    public int getChegada() {
        return chegada;
    }
    public void setChegada(int chegada) {
        this.chegada = chegada;
    }

    //onde usuário precisa ir
    String precisaIr;
    public String getPrecisaIr() {
        return precisaIr;
    }
    public void setPrecisaIr(String precisaIr) {
        this.precisaIr = precisaIr;
    }

    //último guichê necessário para usuário
    char ultimoNecessario;
    public char getUltimoNecessario() {
        return ultimoNecessario;
    }
    public void setUltimoNecessario(char ultimoNecessario) {
        this.ultimoNecessario = ultimoNecessario;
    }

    //turnos necessários para o usuário
    int turnosNecessarios = -1;
    public int getTurnosNecessarios() {
        return turnosNecessarios;
    }
    public void setTurnosNecessarios(int turnosNecessarios) {
        this.turnosNecessarios += turnosNecessarios;
    }
    public void aumentaNecessarios() {
        this.turnosNecessarios++;
    }
    public void diminuiTurnosNecessarios() {
        this.turnosNecessarios--;
    }

    //sendo atendido
    boolean sendoAtendido = false;
    public boolean getSendoAtendido() {
        return sendoAtendido;
    }
    public void setSendoAtendido(boolean sendoAtendido) {
        this.sendoAtendido = sendoAtendido;
    }

    //em qual guichê o usuário está sendo atendido
    int qualGuicheSendoAtendido;
    public int getQualGuicheSendoAtendido() {
        return qualGuicheSendoAtendido;
    }
    public void setQualGuicheSendoAtendido(int qualGuicheSendoAtendido) {
        this.qualGuicheSendoAtendido = qualGuicheSendoAtendido;
    }

}
