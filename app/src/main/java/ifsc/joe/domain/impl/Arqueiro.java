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
        super(x, y);

    }

//    public Arqueiro() {
//        super(Constantes.ARQUEIRO_VIDA_INICIAL,
//                Constantes.ARQUEIRO_ATAQUE,
//                Constantes.ARQUEIRO_VELOCIDADE);
//        this.flechas = Constantes.ARQUEIRO_FLECHAS_INICIAL;
//        this.madeiraColetada = 0;
//    }

    @Override
    public boolean coletar(Recursos recursos) {
        if (!COLETAVEIS.contains(recursos)) return false;
        if (recursos == Recursos.MADEIRA) this.madeiraColetada++;
        return true;
    }

    @Override
    public void atacar(Personagem alvo) {

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
}




