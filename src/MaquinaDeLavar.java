public class MaquinaDeLavar {

    // Estados da maquina
    public enum Estado {
        DESLIGADA("Desligada"),
        LIGADA("Ligada (ociosa)"),
        LAVANDO("Lavando"),
        PAUSADA("Lavagem pausada"),
        LAVAGEM_CONCLUIDA("Lavagem concluída"),
        CENTRIFUGANDO("Centrifugando"),
        CENTRIFUGACAO_CONCLUIDA("Centrifugação concluída");

        private final String descricao;

        Estado(String descricao) {
            this.descricao = descricao;
        }

        public String getDescricao() {
            return descricao;
        }
    }

    private final String modelo;
    private Estado estado;
    private boolean tampaAberta;

    public MaquinaDeLavar(String modelo) {
        this.modelo = modelo;
        this.estado = Estado.DESLIGADA;
        this.tampaAberta = false;
    }

    // Getters
    public String getModelo() { return modelo; }
    public Estado getEstado() { return estado; }
    public boolean isTampaAberta() { return tampaAberta; }
    public boolean isLigada() { return estado != Estado.DESLIGADA; }

    public String getStatus() {
        String situacaoTampa = tampaAberta ? "aberta" : "fechada";
        return modelo + " | estado = " + estado.getDescricao() + " | tampa = " + situacaoTampa;
    }

    public void exibirStatus() {
        System.out.println("STATUS: " + getStatus());
    }

    //auxiliares de resposta
    private boolean aceitar(String operacao, String mensagem) {
        System.out.println("OK " + operacao + ": " + mensagem + ".");
        return true;
    }

    private boolean recusar(String operacao, String motivo) {
        System.out.println("NEGADO " + operacao + ": " + motivo + ".");
        return false;
    }

    //Comportamentos
    public boolean ligar() {
        if (estado != Estado.DESLIGADA) {
            return recusar("ligar", "a máquina já está ligada");
        }
        estado = Estado.LIGADA;
        return aceitar("ligar", "máquina ligada e pronta para uso");
    }

    public boolean desligar() {
        if (estado == Estado.DESLIGADA) {
            return recusar("desligar", "a máquina já está desligada");
        }
        if (estado == Estado.LAVANDO || estado == Estado.CENTRIFUGANDO) {
            return recusar("desligar", "operação em andamento (" + estado.getDescricao() + ")");
        }
        estado = Estado.DESLIGADA;
        return aceitar("desligar", "máquina desligada com segurança");
    }

    public boolean abrirTampa() {
        if (tampaAberta) {
            return recusar("abrir tampa", "a tampa já está aberta");
        }
        if (estado == Estado.LAVANDO || estado == Estado.CENTRIFUGANDO) {
            return recusar("abrir tampa", "travada durante a operação (" + estado.getDescricao() + ")");
        }
        tampaAberta = true;
        return aceitar("abrir tampa", "tampa aberta");
    }

    public boolean fecharTampa() {
        if (!tampaAberta) {
            return recusar("fechar tampa", "a tampa já está fechada");
        }
        tampaAberta = false;
        return aceitar("fechar tampa", "tampa fechada");
    }

    public boolean iniciarLavagem() {
        if (estado == Estado.DESLIGADA) {
            return recusar("iniciar lavagem", "a máquina está desligada");
        }
        if (tampaAberta) {
            return recusar("iniciar lavagem", "a tampa está aberta");
        }
        if (estado != Estado.LIGADA && estado != Estado.CENTRIFUGACAO_CONCLUIDA) {
            return recusar("iniciar lavagem", "máquina ocupada ou em estado inválido (" + estado.getDescricao() + ")");
        }
        estado = Estado.LAVANDO;
        return aceitar("iniciar lavagem", "lavagem em andamento");
    }

    public boolean pausarLavagem() {
        if (estado == Estado.DESLIGADA) {
            return recusar("pausar lavagem", "a máquina está desligada");
        }
        if (estado != Estado.LAVANDO) {
            return recusar("pausar lavagem", "não há lavagem em andamento");
        }
        estado = Estado.PAUSADA;
        return aceitar("pausar lavagem", "lavagem pausada");
    }

    public boolean retomarLavagem() {
        if (estado != Estado.PAUSADA) {
            return recusar("retomar lavagem", "não há lavagem pausada");
        }
        estado = Estado.LAVANDO;
        return aceitar("retomar lavagem", "lavagem retomada");
    }

    public boolean concluirLavagem() {
        if (estado != Estado.LAVANDO) {
            return recusar("concluir lavagem", "não há lavagem em andamento");
        }
        estado = Estado.LAVAGEM_CONCLUIDA;
        return aceitar("concluir lavagem", "lavagem finalizada; centrifugação liberada");
    }

    public boolean iniciarCentrifugacao() {
        if (estado == Estado.DESLIGADA) {
            return recusar("iniciar centrifugação", "a máquina está desligada");
        }
        if (tampaAberta) {
            return recusar("iniciar centrifugação", "a tampa está aberta");
        }
        if (estado != Estado.LAVAGEM_CONCLUIDA) {
            return recusar("iniciar centrifugação", "a lavagem ainda não foi concluída (estado atual: " + estado.getDescricao() + ")");
        }
        estado = Estado.CENTRIFUGANDO;
        return aceitar("iniciar centrifugação", "centrifugação em andamento");
    }

    public boolean concluirCentrifugacao() {
        if (estado != Estado.CENTRIFUGANDO) {
            return recusar("concluir centrifugação", "não há centrifugação em andamento");
        }
        estado = Estado.CENTRIFUGACAO_CONCLUIDA;
        return aceitar("concluir centrifugação", "ciclo completo; a máquina pode ser aberta ou desligada");
    }
}