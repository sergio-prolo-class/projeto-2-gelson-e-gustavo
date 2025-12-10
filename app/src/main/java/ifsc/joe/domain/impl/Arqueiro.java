package ifsc.joe.domain.impl;

import ifsc.joe.api.Coletador;
import ifsc.joe.api.Guerreiro;
import ifsc.joe.consts.Constantes;
import ifsc.joe.enums.Recursos;

import java.util.Locale;
import java.util.Set;



public class Arqueiro extends Personagem implements Coletador, Guerreiro {

    public static final  Set<Recursos> COLETAVEIS;

    static {
        COLETAVEIS = Set.of(Recursos.COMIDA, Recursos.OURO, Recursos.MADEIRA);
    }

    private int flechas;
    private int madeiraColetada;

    public Arqueiro(int x, int y) {
        super(x, y,Constantes.ARQUEIRO_VIDA_INICIAL,
               Constantes.ARQUEIRO_ATAQUE,
                Constantes.ARQUEIRO_VELOCIDADE,Constantes.ARQUEIRO_ALCANCE);
        this.flechas = Constantes.ARQUEIRO_FLECHAS_INICIAL;
        this.madeiraColetada = 0;

    }


    @Override
    public boolean coletar(Recursos recurso) {
        if (recurso == Recursos.MADEIRA) {
            this.madeiraColetada++;
            System.out.println("[ARQUEIRO] Madeira coletada! Total: " + this.madeiraColetada);
            return true;
        }

        // Se não for madeira, não coleta
        System.out.println("[ARQUEIRO] Não coleta " + recurso + "! (Só coleta MADEIRA)");
        return false;
    }

    @Override
    public void atacar(Personagem alvo) {
        if (this.flechas <= 0) {
            System.out.println("Arqueiro sem flechas para atacar!");
            return;
        }

        this.flechas--;
        int dano = calcularDano();
        alvo.receberDano(dano);

        System.out.printf("Arqueiro atacou %s causando %d de dano! Flechas restantes: %d%n",
                alvo.getNome(), dano, this.flechas);
    }

    private int calcularDano() {
        // Exemplo: dano base + bônus por nível ou atributos
        int danoBase = 10;
        // Adicione sua lógica específica aqui
        return danoBase;
    }


    public String produzirFlechas() {
        if (this.madeiraColetada == 0) return "Arqueiro sem madeira para produção!";
        this.madeiraColetada--;
        return this.recarregarFlechas(Constantes.ARQUEIRO_FLECHAS_PRODUCAO);
    }

    public String recarregarFlechas(int quant) {
        if (quant <= 0) return "Não é possível recarregar flechas negativas!";
        this.flechas +=  quant;
        return String.format("Arqueiro agora com %d flechas%n", quant);
    }

    @Override
    protected void receberDano(int dano) {

    }


}




