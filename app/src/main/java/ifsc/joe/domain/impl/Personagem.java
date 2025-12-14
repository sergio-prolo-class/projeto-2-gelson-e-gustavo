package ifsc.joe.domain.impl;

import ifsc.joe.api.Posicionavel;
import ifsc.joe.enums.Direcao;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;


public abstract class Personagem implements Posicionavel {

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
    private  Image iconeMorto = null;
    private boolean acabouDeMorrer = false;




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
        this.iconeMorto = carregarImagem("morto");
    }


    public void desenhar(Graphics g, JPanel painel) {
        //  Mostra caveira se acabou de morrer
        if (!vivo && acabouDeMorrer) {
            if (iconeMorto != null) {
                g.drawImage(iconeMorto, this.posX, this.posY, painel);
            }

            acabouDeMorrer = false;
            return;
        }

        // já mostrou caveira, não desenha nada
        if (!vivo) {
            return;
        }

        // Se vivo, desenha sprite normal ou atacando
        String sufixo = atacando ? "2" : "";
        this.icone = carregarImagem(nomeImagem + sufixo);

        if (this.icone != null) {
            g.drawImage(this.icone, this.posX, this.posY, painel);
        }

        // Desenha barra de vida
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

            this.acabouDeMorrer = true;
        }
    }
    public boolean mostrouCaveira() {
        return !acabouDeMorrer;
    }

    public boolean isAcabouDeMorrer() {
        return acabouDeMorrer;
    }

    @Override
    public int getX() { return posX; }

    @Override
    public int getY() { return posY; }

    @Override
    public int getLargura() {
        return icone != null ? icone.getWidth(null) : 0;
    }

    @Override
    public int getAltura() {
        return icone != null ? icone.getHeight(null) : 0;
    }

    public double calcularDistancia(Posicionavel outro) {
        if (outro == null) return Double.MAX_VALUE;

        int centroX1 = this.getX() + this.getLargura() / 2;
        int centroY1 = this.getY() + this.getAltura() / 2;

        int centroX2 = outro.getX() + outro.getLargura() / 2;
        int centroY2 = outro.getY() + outro.getAltura() / 2;

        int dx = centroX1 - centroX2;
        int dy = centroY1 - centroY2;

        return Math.sqrt(dx * dx + dy * dy);
    }

    public void desenharAlcance(Graphics g) {
        if (icone == null) return;

        int centroX = posX + icone.getWidth(null) / 2;
        int centroY = posY + icone.getHeight(null) / 2;

        g.setColor(new Color(0, 0, 255, 60));
        g.fillOval(centroX - alcance, centroY - alcance, alcance * 2, alcance * 2);

        g.setColor(Color.BLUE);
        g.drawOval(centroX - alcance, centroY - alcance, alcance * 2, alcance * 2);
    }

    public boolean estaNoAlcance(Posicionavel outro) {
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
