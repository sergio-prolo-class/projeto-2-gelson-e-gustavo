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
    private int comidaColetada;

    public Arqueiro(int x, int y) {
        super(x, y,Constantes.ARQUEIRO_VIDA_INICIAL,
               Constantes.ARQUEIRO_ATAQUE,
                Constantes.ARQUEIRO_VELOCIDADE,Constantes.ARQUEIRO_ALCANCE);
        this.flechas = Constantes.ARQUEIRO_FLECHAS_INICIAL;
        this.madeiraColetada = 0;
        this.comidaColetada = 0;

    }


    public double getAlcanceColeta() {
        return this.getAlcance() / 3.0;
    }
    //alcance menor pra coletar

    @Override
    public boolean coletar(Recursos recurso) {
        if (!COLETAVEIS.contains(recurso)) {
            System.out.println("[ARQUEIRO] Não pode coletar " + recurso + "!");
            return false;
        }

        switch (recurso) {
            case MADEIRA:
                this.madeiraColetada++;
                System.out.println("[ARQUEIRO] Coletou madeira! Total: " + this.madeiraColetada);
                break;

            case COMIDA:
                this.comidaColetada++;
                System.out.println("[ARQUEIRO] Coletou um pouco de comida" + this.comidaColetada);
                break;

            case OURO:
                System.out.println("[ARQUEIRO] Encontrou um pequeno tesouro de ouro!");
                break;
        }
        return true;
    }

    @Override
    public void atacar(Personagem alvo) {
        System.out.println("Arqueiro atacando! Vida do alvo antes " + alvo.getVida());

        if (this.flechas <= 0) {
            System.out.println("[ARQUEIRO] sem flechas! não pode atacar.");
            return;
        }
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

        this.flechas--;
        System.out.println("[ARQUEIRO] flecha disparada! flechas restantes" + this.flechas);

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






