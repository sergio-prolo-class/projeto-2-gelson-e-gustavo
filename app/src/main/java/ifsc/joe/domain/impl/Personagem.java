package ifsc.joe.domain.impl;

import ifsc.joe.enums.Direcao;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;


public abstract class Personagem {

    protected int vida;
    protected int vidaMaxima;
    protected double velocidade;
    protected int ataque;
    protected int posX, posY;
    protected Image icone;
    protected boolean atacando;
    protected String nomeImagem;
    protected int alcance;
    protected boolean vivo;


    protected Personagem(int x, int y, int vida, int ataque, double velocidade, int alcance) {
        this.posX = x;
        this.posY = y;
        this.vida = vida;
        this.vidaMaxima = vida;
        this.ataque = ataque;
        this.velocidade = velocidade;
        this.alcance = alcance;
        this.vivo = true;
        this.atacando = false;
        this.nomeImagem = this.getClass().getSimpleName().toLowerCase();
        this.icone = carregarImagem(nomeImagem);
    }


    public void desenhar(Graphics g, JPanel painel) {
        if (!vivo) {
            // Se morto, desenha sprite de morto
            Image iconeMorto = carregarImagem(nomeImagem + "_morto");
            if (iconeMorto != null) {
                g.drawImage(iconeMorto, this.posX, this.posY, painel);
            }
            return;
        }

        // Se vivo, desenha sprite normal ou atacando
        String sufixo = atacando ? "2" : "";
        this.icone = carregarImagem(nomeImagem + sufixo);

        if (this.icone != null) {
            g.drawImage(this.icone, this.posX, this.posY, painel);
        }

        // Desenha barra de vida (se não estiver com vida cheia)
        if (vida < vidaMaxima) {
            desenharBarraVida(g);
        }
    }

    private void desenharBarraVida(Graphics g) {
        int larguraBarra = 30;
        int alturaBarra = 4;
        int yBarra = posY - 8;

        // Fundo vermelho (vida perdida)
        g.setColor(Color.RED);
        g.fillRect(posX, yBarra, larguraBarra, alturaBarra);

        // Vida atual (verde)
        g.setColor(Color.GREEN);
        int larguraVida = (int)(larguraBarra * ((double)vida / vidaMaxima));
        g.fillRect(posX, yBarra, larguraVida, alturaBarra);

        // Borda preta
        g.setColor(Color.BLACK);
        g.drawRect(posX, yBarra, larguraBarra, alturaBarra);
    }


    private Image carregarImagem(String imagem) {
        return new ImageIcon(Objects.requireNonNull(
                getClass().getClassLoader().getResource("./"+imagem+".png")
        )).getImage();
    }

    public void mover(Direcao direcao, int maxLargura, int maxAltura) {
        if (!vivo) return; // Mortos não se movem

        int movimento = (int)(10 * velocidade);

        switch (direcao) {
            case CIMA     -> this.posY -= movimento;
            case BAIXO    -> this.posY += movimento;
            case ESQUERDA -> this.posX -= movimento;
            case DIREITA  -> this.posX += movimento;
        }

        // Limita aos limites da tela
        if (icone != null) {
            this.posX = Math.max(0, Math.min(this.posX, maxLargura - this.icone.getWidth(null)));
            this.posY = Math.max(0, Math.min(this.posY, maxAltura - this.icone.getHeight(null)));
        }
    }

    public void sofrerDano(int dano) {
        if (!vivo) return; // Mortos não sofrem dano

        this.vida -= dano;

        if (this.vida <= 0) {
            this.vida = 0;
            this.vivo = false;
            this.atacando = false;
        }
    }
    public double calcularDistancia(Personagem outro) {
        if (outro == null) return Double.MAX_VALUE;

        int dx = this.posX - outro.posX;
        int dy = this.posY - outro.posY;
        return Math.sqrt(dx * dx + dy * dy);
    }
    public boolean estaNoAlcance(Personagem outro) {
        return calcularDistancia(outro) <= this.alcance;
    }


    // Acessos
    public final int getVida() {
        return vida;
    }

    public final int getVidaMaxima() {
        return vidaMaxima;
    }

    public final int getAtaque() {
        return ataque;
    }

    public final double getVelocidade() {
        return velocidade;
    }

    public final int getAlcance() {
        return alcance;
    }

    public final int getPosX() {
        return posX;
    }

    public final int getPosY() {
        return posY;
    }

    public boolean estaVivo() {
        return vivo;
    }

    public boolean estaAtacando() {
        return atacando;
    }

    // Padrões
    @Override
    public final String toString() {
        return this.getClass().getSimpleName()+ "{" +
                "vida=" + vida +
                ", ataque=" + ataque +
                ", velocidade=" + velocidade +
                '}';
    }

    protected abstract void receberDano(int dano);

    protected Object getNome() {

        return null;
    }
}
