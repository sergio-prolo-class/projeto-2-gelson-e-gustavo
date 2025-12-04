package ifsc.joe.domain.impl;

import ifsc.joe.api.Coletador;
import ifsc.joe.api.Guerreiro;
import ifsc.joe.consts.Constantes;
import ifsc.joe.enums.Recursos;

import java.util.Set;

import static ifsc.joe.consts.Constantes.*;

public class Arqueiro extends Personagem implements Coletador, Guerreiro {

    public static final  Set<Recursos> COLETAVEIS;

    static {
        COLETAVEIS = Set.of(Recursos.COMIDA, Recursos.OURO, Recursos.MADEIRA);
    }

    private int flechas;
    private int madeiraColetada;

    public Arqueiro() {}
      super(Constantes.ARQUEIRO_VIDA_INICIAL,
            Constantes.ARQUEIRO_ATAQUE,
            Constantes.ARQUEIRO_VELOCIDADE);
            this.flechas = Constantes.ARQUEIRO_FLECHAS_INICIAL;
            this.madeiraColetada = 0;
}



