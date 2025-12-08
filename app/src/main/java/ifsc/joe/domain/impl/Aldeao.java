package ifsc.joe.domain.impl;

import ifsc.joe.api.Coletador;
import ifsc.joe.api.ComMontaria;
import ifsc.joe.consts.Constantes;
import ifsc.joe.enums.Direcao;
import ifsc.joe.enums.Recursos;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;
import java.util.Set;

public class Aldeao extends Personagem implements Coletador, ComMontaria {

    public static final Set<Recursos> COLETAVEIS;

    static {
        COLETAVEIS = Set.of(Recursos.COMIDA, Recursos.OURO);
    }

    private int ouroColetado;
    private int comidaColetada;
    private boolean isMontado;

    public Aldeao(int x, int y) {
        super(x,y);
        this.vida = Constantes.ALDEAO_VIDA_INICIAL;
        this.ataque = Constantes.ALDEAO_ATAQUE;
        this.velocidade = Constantes.ALDEAO_VELOCIDADE;
        this.alcance = Constantes.ALDEAO_ALCANCE;
        this.isMontado = false;
    }


    @Override
    public boolean coletar(Recursos recursos) {
        if (!COLETAVEIS.contains(recursos)) return false;
        if (recursos == Recursos.OURO) this.ouroColetado++;
        if (recursos == Recursos.COMIDA) this.comidaColetada++;
        return true;
    }


    @Override
    public void alternarMontado() {
        if (!isMontado) {
            isMontado = true;
            this.velocidade = this.velocidade * 2;
        }else  {
            isMontado = false;
            this.velocidade = Constantes.ALDEAO_VELOCIDADE;
        }

    }
}