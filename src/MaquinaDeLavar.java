public class MaquinaDeLavar {

  //constantes
  public static final int DESLIGADA = 0;
  public static final int LIGADA = 1;
  public static final int LAVANDO = 2;
  public static final int PAUSADA = 3;
  public static final int LAVAGEM_CONCLUIDA = 4;
  public static final int CENTRIFUGANDO = 5;
  public static final int CENTRIFUGACAO_CONCLUIDA = 6;

  //atributos
  private final String modelo;
  private int estado;
  private boolean tampaAberta;

  public int getEstado() {
    return estado;
  }

  //metodos de consulta
  public String descricaoEstado(int estado) {
    return switch (estado) {
      case DESLIGADA -> "Desligada";
      case LIGADA -> "Ligada (ociosa)";
      case LAVANDO -> "Lavando";
      case PAUSADA -> "Lavagem pausada";
      case LAVAGEM_CONCLUIDA -> "Lavagem concluída";
      case CENTRIFUGANDO -> "Centrifugando";
      case CENTRIFUGACAO_CONCLUIDA -> "Centrifugacao concluida";
      default -> "Estado desconhecido";
    };
  }

  //construtor
  public MaquinaDeLavar(String modelo) {
    this.modelo = modelo;
    this.estado = DESLIGADA;
    this.tampaAberta = false;
  }

  //getters and setters
  public String getDescricaoEstado() {
    return descricaoEstado(estado);
  }

  public boolean isTampaAberta() {
    return tampaAberta;
  }

  public boolean isLigada() {
    return estado != DESLIGADA;
  }

  public String getModelo() {
    return modelo;
  }

  public String getStatus() {
    String situacaoTampa;
    if (tampaAberta) {
      situacaoTampa = "aberta";
    } else {
      situacaoTampa = "fechada";
    }
    return (
      modelo +
      " | estado = " +
      getDescricaoEstado() +
      " | tampa = " +
      situacaoTampa
    );
  }

  //exibir status atual da maquina
  public void exibirStatus() {
    System.out.println("STATUS: " + getStatus());
  }

  private boolean aceitar(String operacao, String mensagem) {
    System.out.println("OK " + operacao + ": " + mensagem + ".");
    return true;
  }

  private boolean recusar(String operacao, String motivo) {
    System.out.println("NEGADO " + operacao + ": " + motivo + ".");
    return false;
  }

  //comportamentos da maquina(ligar, desligar, lavar, centrifugar)
  public boolean ligar() {
    if (estado != DESLIGADA) {
      return recusar("ligar", "a maquina ja esta ligada");
    }
    estado = LIGADA;
    return aceitar("ligar", "maquina ligada e pronta para uso");
  }

  public boolean desligar() {
    if (estado == DESLIGADA) {
      return recusar("desligar", "a maquina ja esta desligada");
    }
    if (estado == LAVANDO || estado == CENTRIFUGANDO) {
      return recusar(
        "desligar",
        "operacao em andamento (" + descricaoEstado(estado) + ")"
      );
    }
    estado = DESLIGADA;
    return aceitar("desligar", "maquina desligada com seguranca");
  }

  public boolean abrirTampa() {
    if (tampaAberta) {
      return recusar("abrir tampa", "a tampa ja esta aberta");
    }
    if (estado == LAVANDO || estado == CENTRIFUGANDO) {
      return recusar(
        "abrir tampa",
        "travada durante a operacao (" + descricaoEstado(estado) + ")"
      );
    }
    tampaAberta = true;
    return aceitar("abrir tampa", "tampa aberta");
  }

  public boolean fecharTampa() {
    if (!tampaAberta) {
      return recusar("fechar tampa", "a tampa ja esta fechada");
    }
    tampaAberta = false;
    return aceitar("fechar tampa", "tampa fechada");
  }

  public boolean iniciarLavagem() {
    if (estado == DESLIGADA) {
      return recusar("iniciar lavagem", "a maquina esta desligada");
    }
    if (tampaAberta) {
      return recusar("iniciar lavagem", "a tampa esta aberta");
    }
    if (estado == LAVANDO) {
      return recusar("iniciar lavagem", "ja existe uma lavagem em andamento");
    }
    if (estado == PAUSADA) {
      return recusar("iniciar lavagem", "existe uma lavagem pausada");
    }
    if (estado == CENTRIFUGANDO) {
      return recusar("iniciar lavagem", "a maquina esta centrifugando");
    }
    estado = LAVANDO;
    return aceitar("iniciar lavagem", "lavagem em andamento");
  }

  public boolean pausarLavagem() {
    if (estado == DESLIGADA) {
      return recusar("pausar lavagem", "a maquina esta desligada");
    }
    if (estado != LAVANDO) {
      return recusar("pausar lavagem", "nao ha lavagem em andamento");
    }
    estado = PAUSADA;
    return aceitar("pausar lavagem", "lavagem pausada");
  }

  public boolean retomarLavagem() {
    if (estado != PAUSADA) {
      return recusar("retomar lavagem", "nao ha lavagem pausada");
    }
    estado = LAVANDO;
    return aceitar("retomar lavagem", "lavagem retomada");
  }

  public boolean concluirLavagem() {
    if (estado != LAVANDO) {
      return recusar("concluir lavagem", "nao ha lavagem em andamento");
    }
    estado = LAVAGEM_CONCLUIDA;
    return aceitar(
      "concluir lavagem",
      "lavagem finalizada; centrifugacao liberada"
    );
  }

  public boolean iniciarCentrifugacao() {
    if (estado == DESLIGADA) {
      return recusar("iniciar centrifugacao", "a maquina esta desligada");
    }
    if (tampaAberta) {
      return recusar("iniciar centrifugacao", "a tampa esta aberta");
    }
    if (estado != LAVAGEM_CONCLUIDA) {
      return recusar(
        "iniciar centrifugacao",
        "a lavagem ainda nao foi concluída (estado atual: " +
          descricaoEstado(estado) +
          ")"
      );
    }
    estado = CENTRIFUGANDO;
    return aceitar("iniciar centrifugacao", "centrifugacao em andamento");
  }

  public boolean concluirCentrifugacao() {
    if (estado != CENTRIFUGANDO) {
      return recusar(
        "concluir centrifugacao",
        "nao ha centrifugacao em andamento"
      );
    }
    estado = CENTRIFUGACAO_CONCLUIDA;
    return aceitar(
      "concluir centrifugacao",
      "ciclo completo; a maquina pode ser aberta ou desligada"
    );
  }
}
