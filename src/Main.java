public class Main {
     public static void main(String[] args){

        MaquinaDeLavar maquina1 = new MaquinaDeLavar("Brastemp BWK11");
        maquina1.exibirStatus();
 
        maquina1.ligar();
        maquina1.fecharTampa();
        maquina1.iniciarLavagem();
        maquina1.exibirStatus();
 
        maquina1.concluirLavagem();
        maquina1.iniciarCentrifugacao();
        maquina1.exibirStatus();
 
        maquina1.concluirCentrifugacao();
        maquina1.abrirTampa();
        maquina1.desligar();
        maquina1.exibirStatus();
 
        System.out.println();
        System.out.println("Máquina 2: tentativas inválidas");
        MaquinaDeLavar maquina2 = new MaquinaDeLavar("Electrolux LAC12");
        maquina2.exibirStatus();
     }
}
