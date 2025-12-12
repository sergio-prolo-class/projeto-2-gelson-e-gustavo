package ifsc.joe.ui;

import ifsc.joe.domain.impl.Aldeao;
import ifsc.joe.domain.impl.Arqueiro;
import ifsc.joe.domain.impl.Cavaleiro;
import ifsc.joe.domain.impl.Personagem;
import ifsc.joe.enums.Direcao;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Tela extends JPanel {

    private final Set<Personagem> personagem;
    private String filtroAtual = "TODOS";

    public Tela() {

        //TODO preciso ser melhorado

        this.setBackground(Color.white);
        this.personagem = new HashSet<>();
    }

    /**
     * Method que invocado sempre que o JPanel precisa ser resenhado.
     * @param g Graphics componente de java.awt
     */
    @Override
    public void paint(Graphics g) {
        super.paint(g);

        //TODO preciso ser melhorado

        // percorrendo a lista de aldeões e pedindo para cada um se desenhar na tela
        this.personagem.forEach(personagem -> personagem.desenhar(g, this));

        // liberando o contexto gráfico
        g.dispose();
    }

    public void setFiltro(String filtro) {
        this.filtroAtual = filtro.toUpperCase();
        repaint();
    }
    private boolean selecionarFiltro(Personagem personagem) {

        switch (filtroAtual) {
            case "TODOS":
                return true;
            case "ALDEAO":
                return personagem instanceof Aldeao;
            case "CAVALEIRO":
                return personagem instanceof Cavaleiro;
            case "ARQUEIRO":
                return personagem instanceof Arqueiro;
            default:
                return true;
        }
    }



    public void atacarPersonagens() {

        int ataquesRealizados = 0;

        // Limpa mortos antes do ataque
        limparMortos();

        // Lista de todos os personagens
        Personagem[] todosPersonagens = personagem.toArray(new Personagem[0]);

        for (Personagem atacante : personagem) {
            // Só ataca se estiver vivo
            if (atacante.estaVivo() && selecionarFiltro(atacante) && atacante instanceof ifsc.joe.api.Guerreiro) {

                // Procura  alvos no alcance
                List<Personagem> alvos = encontrarAlvo(atacante, todosPersonagens);

                // Ataca cada alvo
                for (Personagem alvo : alvos) {
                    ((ifsc.joe.api.Guerreiro) atacante).atacar(alvo);
                    ataquesRealizados++;

                }
            }
        }

        // Limpa mortos apos  ataque
        limparMortos();
        repaintForce();
    }



    public void alternarMontaria() {
        int cavaleirosAlterados = 0;

        for (Personagem p : personagem) {
            if (p instanceof ifsc.joe.api.ComMontaria && p.estaVivo() && selecionarFiltro(p)) {
                ((ifsc.joe.api.ComMontaria) p).alternarMontado();
                cavaleirosAlterados++;


            }
        }

        if (cavaleirosAlterados > 0) {
            repaint();
        }
    }

    /**
     * Encontra um alvo para o atacante
     */
    private List<Personagem> encontrarAlvo(Personagem atacante, Personagem[] todosPersonagens) {
        List<Personagem> alvos = new ArrayList<>();
        for (Personagem alvo : todosPersonagens) {
            if (alvo == atacante) continue;
            if (!alvo.estaVivo()) continue;
            if (atacante.estaNoAlcance(alvo)) {
                alvos.add(alvo);
            }
        }
        return alvos;
    }

    public void limparMortos() {
        personagem.removeIf(p -> !p.estaVivo());
        repaint();
    }

    private void repaintForce() {
        repaint();

        Timer timer = new Timer(350, e -> repaint());
        timer.setRepeats(false);
        timer.start();
    }

    /**
     * Cria um aldeao nas coordenadas X e Y, desenha-o neste JPanel
     * e adiciona o mesmo na lista de aldeoes
     *
     * @param x coordenada X
     * @param y coordenada Y
     */
    public void criarAldeao(int x, int y) {
        Aldeao a = new Aldeao(x, y);
        a.desenhar(super.getGraphics(), this);
        this.personagem.add(a);
    }

    public void criarCavaleiro(int x, int y) {
        Cavaleiro c = new Cavaleiro(x, y);
        c.desenhar(super.getGraphics(), this);
        this.personagem.add(c);
    }

    public void criarArqueiro(int x, int y) {
        Arqueiro c = new Arqueiro(x, y);
        c.desenhar(super.getGraphics(), this);
        this.personagem.add(c);
    }



    public void movimentarPersonagens(Direcao direcao) {
        int movidos = 0;
        for (Personagem p : personagem) {
            if (p.estaVivo() && selecionarFiltro(p)) {
                p.mover(direcao, getWidth(), getHeight());
                movidos++;
            }
        }
        if (movidos > 0) {
            repaint();
        }
    }


    /**
     * Atualiza as coordenadas X ou Y de todos os aldeoes
     *
     * @param direcao direcao para movimentar
     */
//    public void movimentarAldeoes(Direcao direcao) {
//        //TODO preciso ser melhorado
//
//        this.personagem.forEach(aldeao -> aldeao.mover(direcao, this.getWidth(), this.getHeight()));
//
//        // Depois que as coordenadas foram atualizadas é necessário repintar o JPanel
//        this.repaint();
//    }

    /**
     * Altera o estado do aldeão de atacando para não atacando e vice-versa
     */
//    public void atacarAldeoes() {
//
//        //TODO preciso ser melhorado
//
//
//        this.personagem.forEach(Personagem :: atacar);
//
//        // Fazendo o JPanel ser redesenhado
//        this.repaint();
//    }
}