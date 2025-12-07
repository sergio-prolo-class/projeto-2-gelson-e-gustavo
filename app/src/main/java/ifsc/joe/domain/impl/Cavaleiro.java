package ifsc.joe.domain.impl;

import ifsc.joe.api.ComMontaria;
import ifsc.joe.api.Guerreiro;
import ifsc.joe.consts.Constantes;

public class Cavaleiro extends Personagem implements ComMontaria, Guerreiro {

    private boolean isMontado = false;


    public Cavaleiro(int x, int y) {
        super(x, y);
        alternarMontado();
        this.ataque = Constantes.CAVALEIRO_ATAQUE;
        this.vida = Constantes.CAVALEIRO_VIDA_INICIAL;
        this.velocidade = Constantes.CAVALEIRO_VELOCIDADE;
    }


    @Override
    public void alternarMontado() {
        if (!isMontado) {
            isMontado = true;
            this.velocidade = this.velocidade / 2;
        } else {
            isMontado = false;
            this.velocidade = Constantes.CAVALEIRO_VELOCIDADE;
        }
    }

        @Override
    public String atacar(Personagem alvo) {
        return "";
    }
}
