package ifsc.joe.domain.impl;

import ifsc.joe.api.ComMontaria;
import ifsc.joe.api.Guerreiro;
import ifsc.joe.consts.Constantes;

public class Cavaleiro extends Personagem implements ComMontaria, Guerreiro {

    private boolean isMontado;


    public Cavaleiro(int x, int y) {
        super(x, y);
        this.isMontado = true;
        this.ataque = Constantes.CAVALEIRO_ATAQUE;
        this.vida = Constantes.CAVALEIRO_VIDA_INICIAL;
        this.velocidade = Constantes.CAVALEIRO_VELOCIDADE;
    }

    // Cavaleiro desmontado ataque reduz
    @Override
    public void alternarMontado() {
        if (!isMontado) {
            isMontado = true;
            this.velocidade = Constantes.CAVALEIRO_VELOCIDADE;
            this.ataque = Constantes.CAVALEIRO_ATAQUE;
        } else {
            isMontado = false;
            this.velocidade = this.velocidade /2;
            this.ataque = this.ataque -2;
        }
    }

        @Override
    public void atacar(Personagem alvo) {
        this.atacando = true;
        alvo.sofrerDano(this.ataque);

        }
    }

