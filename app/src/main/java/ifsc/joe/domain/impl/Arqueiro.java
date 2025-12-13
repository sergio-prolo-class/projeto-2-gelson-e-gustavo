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


        System.out.println("[ARQUEIRO] Não coleta " + recurso + "! (Só coleta MADEIRA)");
        return false;
    }

    @Override
    public void atacar(Personagem alvo) {
        System.out.println("Arqueiro atacando! Vida do alvo antes " + alvo.getVida());
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

        System.out.println("Vida do alvo " + alvo.getVida());
        System.out.println("atacando" + atacando);
    }

    private int getDano() {
        return this.ataque;
    }

    @Override
    protected void receberDano(int dano) {
        sofrerDano(dano);
    }
    }






