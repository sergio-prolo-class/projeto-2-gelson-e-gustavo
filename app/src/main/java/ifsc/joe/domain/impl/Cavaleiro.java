package ifsc.joe.domain.impl;

import ifsc.joe.api.ComMontaria;
import ifsc.joe.api.Guerreiro;
import ifsc.joe.consts.Constantes;

public class Cavaleiro extends Personagem implements ComMontaria, Guerreiro {

    private boolean montado;


    public Cavaleiro(int x, int y) {

        super(x, y,
                Constantes.CAVALEIRO_VIDA_INICIAL,
                Constantes.CAVALEIRO_ATAQUE,
                Constantes.CAVALEIRO_VELOCIDADE,
                Constantes.CAVALEIRO_ALCANCE);

        this.montado = true;
        this.nomeImagem = "cavaleiro_montado";

    }

    // Cavaleiro desmontado ataque reduz
    @Override
    public void alternarMontado() {
       // this.montado = !montado;

        if (!montado) {
            montado = true;
            this.velocidade = Constantes.CAVALEIRO_VELOCIDADE;
            this.ataque = Constantes.CAVALEIRO_ATAQUE;
            this.nomeImagem = "cavaleiro_montado";
        } else {
            montado = false;
            this.velocidade = this.velocidade /2;
            this.ataque = this.ataque -2;
            this.nomeImagem = "cavaleiro_desmontado";
        }
    }

    public boolean isMontado() {
        return montado;
    }
    public int getDano() {
        return this.ataque;
    }

//        @Override
//    public void atacar(Personagem alvo) {
//        this.atacando = true;
//        alvo.sofrerDano(this.ataque);
//
//        }

    @Override
    public void atacar(Personagem alvo) {

        // Verifica se pode atacar
        if (!this.estaVivo()) {
            return; //  morto não ataca
        }
        if (!alvo.estaVivo()) {
            return; //não ataca mortos
        }
        // Verifica alcance
        double distancia = this.calcularDistancia(alvo);
        if (distancia > this.getAlcance()) {
            return; // Fora do alcance
        }

//        // cavaleiro naoo ataca outros cavaleiros
//        if (alvo instanceof Cavaleiro) {
//            return;
//        }

        // ataque
        this.atacando = true;
        int vidaAntes = alvo.getVida();
        alvo.sofrerDano(this.getDano());



        // ataque (dura 0.3 segundos)
        new Thread(() -> {
            try {
                Thread.sleep(300);
                this.atacando = false;
            } catch (InterruptedException e) {
                // Ignora interrupção
            }
        }).start();
    }
    }

