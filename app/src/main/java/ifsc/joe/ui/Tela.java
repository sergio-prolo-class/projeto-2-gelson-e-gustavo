package ifsc.joe.ui;

import ifsc.joe.domain.impl.Aldeao;
import ifsc.joe.domain.impl.Cavaleiro;
import ifsc.joe.domain.impl.Personagem;
import ifsc.joe.enums.Direcao;

import javax.swing.*;
import java.awt.*;
import java.util.HashSet;
import java.util.Set;

public class Tela extends JPanel {

    private final Set<Personagem> personagem;

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

    /**
     * Atualiza as coordenadas X ou Y de todos os aldeoes
     *
     * @param direcao direcao para movimentar
     */
    public void movimentarAldeoes(Direcao direcao) {
        //TODO preciso ser melhorado

        this.personagem.forEach(aldeao -> aldeao.mover(direcao, this.getWidth(), this.getHeight()));

        // Depois que as coordenadas foram atualizadas é necessário repintar o JPanel
        this.repaint();
    }

    /**
     * Altera o estado do aldeão de atacando para não atacando e vice-versa
     */
    public void atacarAldeoes() {

        //TODO preciso ser melhorado


        this.personagem.forEach(Personagem :: atacar);

        // Fazendo o JPanel ser redesenhado
        this.repaint();
    }
}