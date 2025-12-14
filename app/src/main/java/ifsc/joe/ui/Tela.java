package ifsc.joe.ui;

import ifsc.joe.consts.Constantes;
import ifsc.joe.domain.impl.*;
import ifsc.joe.enums.Direcao;
import ifsc.joe.enums.Recursos;

import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.*;
import java.util.List;
import java.util.Set;

import static com.sun.java.accessibility.util.AWTEventMonitor.addKeyListener;

public class Tela extends JPanel {

    private final List<Coletavel> coletaveis;
    private int comida = 0;
    private int madeira = 0;
    private int ouro = 0;
    private boolean coletaveisInicializados = false;
    private boolean modoRaio = false;
    private Image background;

    private final Set<Personagem> personagem;
    private String filtroAtual = "TODOS";

    public Tela() {

        this.setPreferredSize(new Dimension(Constantes.LARGURA_TELA, Constantes.ALTURA_TELA));

        this.setBackground(Color.white);
            try {
                background = new ImageIcon(
                        getClass().getClassLoader().getResource("./background.png")
                ).getImage();
            } catch (Exception e) {
                System.err.println("Erro ao carregar imagem de fundo: " + e.getMessage());
                background = null;
            }

        this.personagem = new HashSet<>();
        this.coletaveis = new ArrayList<>();

       // distribuirColetaveisIniciais();
    }



    public void adicionarColetavel(Coletavel coletavel) {
        coletaveis.add(coletavel);
        repaint();
    }
    private void distribuirColetaveisIniciais() {
        Random rand = new Random();
        int largura = getWidth();
        int altura = getHeight();

        System.out.println("Inicializando coletáveis em tela " +
                largura + "x" + altura);

        // Limpa qualquer coletável
        coletaveis.clear();


        for (int i = 0; i < 15; i++) {
            Recursos tipo = Recursos.values()[rand.nextInt(Recursos.values().length)];

            // ⭐ GARANTE bounds positivos
            int maxX = Math.max(1, largura - 100);
            int maxY = Math.max(1, altura - 100);

            int x = 50 + rand.nextInt(maxX);
            int y = 50 + rand.nextInt(maxY);
            int quantidade = 10 + rand.nextInt(30);

            coletaveis.add(new Coletavel(tipo, quantidade, x, y));
        }

        System.out.println(coletaveis.size() + " coletáveis distribuídos");
    }


    /**
     * Method que invocado sempre que o JPanel precisa ser resenhado.
     * @param g Graphics componente de java.awt
     */
    @Override
    public void paint(Graphics g) {
        super.paint(g);


        if (background != null) {
            g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
        } else {
            g.setColor(new Color(180, 220, 180)); // fallback verde-claro
            g.fillRect(0, 0, getWidth(), getHeight());
        }


        if (!coletaveisInicializados && getWidth() > 0 && getHeight() > 0) {
            distribuirColetaveisIniciais();
            coletaveisInicializados = true;
        }

        for (Coletavel c : coletaveis) {
            if (!c.isColetado()) {
                c.desenhar(g);
            }
        }
       for (Personagem p : personagem) {
           if (p.estaVivo() || p.isAcabouDeMorrer()){
               p.desenhar(g,this);
           }
       }
        for (Personagem p : personagem) {
            if ((modoRaio) && p.estaVivo() && selecionarFiltro(p)) {
                p.desenharAlcance(g);
            }
        }

        desenharRecursos(g);
        // liberando o contexto gráfico
        g.dispose();
    }


    private void desenharRecursos(Graphics g) {
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 14));

        int y = 20;
        g.drawString("RECURSOS COLETADOS", 10, y);
        y += 20;
        g.drawString(" Comida: " + comida, 20, y);
        y += 20;
        g.drawString("Madeira: " + madeira, 20, y);
        y += 20;
        g.drawString("Ouro: " + ouro, 20, y);
    }



    private boolean podeColetar(Personagem personagem, Recursos tipo) {
        if (personagem instanceof Aldeao) {
            // Aldeão coleta COMIDA e OURO
            return tipo == Recursos.COMIDA || tipo == Recursos.OURO;
        }
        if (personagem instanceof Arqueiro) {
            // Arqueiro coleta MADEIRA e COMIDA
            return tipo == Recursos.MADEIRA || tipo == Recursos.COMIDA;
        }
        // Cavaleiros não coletam
        return false;
    }

    private void coletarRecurso(Coletavel coletavel) {
        coletavel.coletar(); // Marca como coletado


        switch (coletavel.getTipo()) {

            case COMIDA:
                comida += coletavel.getQuantidade();
                System.out.println(coletavel.getQuantidade() + " comida (Total: " + comida + ")");
                break;

            case MADEIRA:
                madeira += coletavel.getQuantidade();
                System.out.println(coletavel.getQuantidade() + " madeira (Total: " + madeira + ")");
                break;

            case OURO:
                ouro += coletavel.getQuantidade();
                System.out.println(coletavel.getQuantidade() + " ouro (Total: " + ouro + ")");
                break;
        }
    }

    public void coletarComBotao() {
        verificarColetaveis();
        modoRaio = true;
        new javax.swing.Timer(Constantes.DELAY, e -> {
            modoRaio = false;
            repaint();
        }).start();
    }
    public void verificarColetaveis() {
        List<Coletavel> coletados = new ArrayList<>();

        for (Personagem p : personagem) {
            if (!p.estaVivo()) continue;

            for (Coletavel c : coletaveis) {
                if (c.isColetado()) continue;
                if (!podeColetar(p, c.getTipo())) continue;


                double distancia = p.calcularDistancia(c);


                boolean colidiu =
                        p.getPosX() < c.getX() + c.getLargura() &&
                                p.getPosX() + p.getLargura() > c.getX() &&
                                p.getPosY() < c.getY() + c.getAltura() &&
                                p.getPosY() + p.getAltura() > c.getY();


                if (colidiu || distancia <= p.getAlcance()) {
                    coletarRecurso(c);
                    coletados.add(c);
                    break;
                }
            }
        }

        if (!coletados.isEmpty()) {
            coletaveis.removeAll(coletados);
            repaint();
        }
    }






    public int getComida() { return comida; }
    public int getMadeira() { return madeira; }
    public int getOuro() { return ouro; }


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

        modoRaio = true;

        repaint();

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
            repaint();
        // Limpa mortos apos  ataque
        limparMortos();
        new javax.swing.Timer(Constantes.DELAY, e -> {
            modoRaio = false;
            repaint();
        }).start();
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
        personagem.removeIf(p -> !p.estaVivo() && p.mostrouCaveira());
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
        modoRaio = false;
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
        if (movidos > 0) {

            verificarColetaveis();
            repaint();


            System.out.println("Movidos: " + movidos + " personagens");
        }
    }


}