package ifsc.joe.ui;

import ifsc.joe.api.Coletador;
import ifsc.joe.consts.Constantes;
import ifsc.joe.domain.impl.*;
import ifsc.joe.enums.Direcao;
import ifsc.joe.enums.Recursos;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class Tela extends JPanel {

    private final List<Coletavel> coletaveis = new ArrayList<>();
    private final Set<Personagem> personagens = new HashSet<>();

    private int comida;
    private int madeira;
    private int ouro;

    private boolean coletaveisInicializados;
    private boolean modoRaio;

    private String filtroAtual = "TODOS";

    private Image background;

    public Tela() {
        setPreferredSize(new Dimension(Constantes.LARGURA_TELA, Constantes.ALTURA_TELA));
        setBackground(Color.WHITE);
        carregarBackground();
    }

    private void carregarBackground() {
        try {
            background = new ImageIcon(
                    getClass().getClassLoader().getResource("background.png")
            ).getImage();
        } catch (Exception e) {
            background = null;
        }
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        desenharBackground(g);
        inicializarColetaveis();
        desenharColetaveis(g);
        desenharPersonagens(g);
        desenharAlcances(g);
        desenharRecursos(g);

        g.dispose();
    }

    private void desenharBackground(Graphics g) {
        if (background != null) {
            g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
        } else {
            g.setColor(new Color(180, 220, 180));
            g.fillRect(0, 0, getWidth(), getHeight());
        }
    }

    private void inicializarColetaveis() {
        if (!coletaveisInicializados && getWidth() > 0 && getHeight() > 0) {
            distribuirColetaveisIniciais();
            coletaveisInicializados = true;
        }
    }

    private void desenharColetaveis(Graphics g) {
        for (Coletavel c : coletaveis) {
            if (!c.isColetado()) {
                c.desenhar(g);
            }
        }
    }

    private void desenharPersonagens(Graphics g) {
        for (Personagem p : personagens) {
            if (p.estaVivo() || p.isAcabouDeMorrer()) {
                p.desenhar(g, this);
            }
        }
    }

    private void desenharAlcances(Graphics g) {
        if (!modoRaio) return;
        for (Personagem p : personagens) {
            if (p.estaVivo() && selecionarFiltro(p)) {
                p.desenharAlcance(g);
            }
        }
    }

    private void desenharRecursos(Graphics g) {
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 14));

        int y = 20;
        g.drawString("RECURSOS COLETADOS", 10, y);
        g.drawString("Comida: " + comida, 20, y += 20);
        g.drawString("Madeira: " + madeira, 20, y += 20);
        g.drawString("Ouro: " + ouro, 20, y += 20);
    }

    private void distribuirColetaveisIniciais() {
        Random rand = new Random();
        coletaveis.clear();

        int maxX = Math.max(1, getWidth() - 100);
        int maxY = Math.max(1, getHeight() - 100);

        for (int i = 0; i < 15; i++) {
            Recursos tipo = Recursos.values()[rand.nextInt(Recursos.values().length)];
            int x = 50 + rand.nextInt(maxX);
            int y = 50 + rand.nextInt(maxY);
            int quantidade = 10 + rand.nextInt(30);
            coletaveis.add(new Coletavel(tipo, quantidade, x, y));
        }
    }

    public void adicionarColetavel(Coletavel coletavel) {
        coletaveis.add(coletavel);
        repaint();
    }

    public void verificarColetaveis() {
        List<Coletavel> coletados = new ArrayList<>();

        for (Personagem p : personagens) {
            if (!p.estaVivo() || !(p instanceof Coletador)) continue;

            for (Coletavel c : coletaveis) {
                if (c.isColetado() || !podeColetar(p, c.getTipo())) continue;

                double alcance = (p instanceof Arqueiro)
                        ? ((Arqueiro) p).getAlcanceColeta()
                        : p.getAlcance();

                boolean colidiu =
                        p.getPosX() < c.getPosX() + 20 &&
                                p.getPosX() + 20 > c.getPosX() &&
                                p.getPosY() < c.getPosY() + 20 &&
                                p.getPosY() + 20 > c.getPosY();

                if (colidiu || p.calcularDistancia(c) <= alcance) {
                    if (((Coletador) p).coletar(c.getTipo())) {
                        coletarRecurso(c);
                        coletados.add(c);
                    }
                    break;
                }
            }
        }

        coletaveis.removeAll(coletados);
        repaint();
    }

    private boolean podeColetar(Personagem p, Recursos tipo) {
        if (p instanceof Aldeao) return tipo == Recursos.COMIDA || tipo == Recursos.OURO;
        if (p instanceof Arqueiro) return tipo == Recursos.COMIDA || tipo == Recursos.MADEIRA;
        return false;
    }

    private void coletarRecurso(Coletavel c) {
        c.coletar();
        switch (c.getTipo()) {
            case COMIDA -> comida += c.getQuantidade();
            case MADEIRA -> madeira += c.getQuantidade();
            case OURO -> ouro += c.getQuantidade();
        }
    }

    public void coletarComBotao() {
        verificarColetaveis();
        ativarModoRaio();
    }

    public void atacarPersonagens() {
        ativarModoRaio();
        limparMortos();

        Personagem[] todos = personagens.toArray(new Personagem[0]);

        for (Personagem atacante : personagens) {
            if (!(atacante instanceof ifsc.joe.api.Guerreiro)) continue;
            if (!atacante.estaVivo() || !selecionarFiltro(atacante)) continue;

            for (Personagem alvo : encontrarAlvos(atacante, todos)) {
                ((ifsc.joe.api.Guerreiro) atacante).atacar(alvo);
            }
        }

        limparMortos();
    }

    private List<Personagem> encontrarAlvos(Personagem atacante, Personagem[] todos) {
        List<Personagem> alvos = new ArrayList<>();
        for (Personagem p : todos) {
            if (p != atacante && p.estaVivo() && atacante.estaNoAlcance(p)) {
                alvos.add(p);
            }
        }
        return alvos;
    }

    public void alternarMontaria() {
        for (Personagem p : personagens) {
            if (p instanceof ifsc.joe.api.ComMontaria && p.estaVivo() && selecionarFiltro(p)) {
                ((ifsc.joe.api.ComMontaria) p).alternarMontado();
            }
        }
        repaint();
    }

    public void movimentarPersonagens(Direcao direcao) {
        boolean moveu = false;
        for (Personagem p : personagens) {
            if (p.estaVivo() && selecionarFiltro(p)) {
                p.mover(direcao, getWidth(), getHeight());
                moveu = true;
            }
        }
        if (moveu) repaint();
    }

    public void limparMortos() {
        personagens.removeIf(p -> !p.estaVivo() && p.mostrouCaveira());
        repaint();
    }

    private void ativarModoRaio() {
        modoRaio = true;
        repaint();
        new javax.swing.Timer(Constantes.DELAY, e -> {
            modoRaio = false;
            repaint();
        }).start();
    }

    public void criarAldeao(int x, int y) {
        personagens.add(new Aldeao(x, y));
        repaint();
    }

    public void criarCavaleiro(int x, int y) {
        personagens.add(new Cavaleiro(x, y));
        repaint();
    }

    public void criarArqueiro(int x, int y) {
        personagens.add(new Arqueiro(x, y));
        repaint();
    }

    public void setFiltro(String filtro) {
        filtroAtual = filtro.toUpperCase();
        repaint();
    }

    private boolean selecionarFiltro(Personagem p) {
        return switch (filtroAtual) {
            case "ALDEAO" -> p instanceof Aldeao;
            case "CAVALEIRO" -> p instanceof Cavaleiro;
            case "ARQUEIRO" -> p instanceof Arqueiro;
            default -> true;
        };
    }

    public int getComida() { return comida; }
    public int getMadeira() { return madeira; }
    public int getOuro() { return ouro; }
}
