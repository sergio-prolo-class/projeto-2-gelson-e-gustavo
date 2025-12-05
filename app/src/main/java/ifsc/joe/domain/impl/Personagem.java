package ifsc.joe.domain.impl;

import ifsc.joe.enums.Direcao;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;


public abstract class Personagem {

    protected int vida;
    protected double velocidade;
    protected int ataque;
    protected int posX, posY;
    protected Image icone;
    protected boolean atacando;
    protected  String nomeImagem;


    protected Personagem(int x,int y){
        this.posX = x;
        this.posY = y;
        this.nomeImagem = this.getClass().getSimpleName().toLowerCase();
        this.vida = 0;
        this.ataque = 0;
        this.velocidade = 0;

    }

    /**
     * Desenhando o Aldeão, nas coordenadas X e Y, com a imagem 'icone'
     * no JPanel 'pai'
     *
     * @param g objeto do JPanel que será usado para desenhar o Aldeão
     */
    public void desenhar(Graphics g, JPanel painel) {
        // verificando qual imagem carregar
        this.icone = this.carregarImagem(nomeImagem + (atacando ? "2" : ""));
        // desenhando de fato a imagem no pai
        g.drawImage(this.icone, this.posX, this.posY, painel);
    }

    /**
     * Atualiza as coordenadas X e Y do personagem
     *
     * @param direcao indica a direcao a ir.
     */

    /**
     * Metodo auxiliar para carregar uma imagem do disco
     *
     * @param imagem Caminho da imagem
     * @return Retorna um objeto Image
     */

    private Image carregarImagem(String imagem) {
        return new ImageIcon(Objects.requireNonNull(
                getClass().getClassLoader().getResource("./"+imagem+".png")
        )).getImage();
    }

    public void mover(Direcao direcao, int maxLargura, int maxAltura) {
        switch (direcao) {
            case CIMA     -> this.posY -= 10;
            case BAIXO    -> this.posY += 10;
            case ESQUERDA -> this.posX -= 10;
            case DIREITA  -> this.posX += 10;
        }

        //Não deixa a imagem ser desenhada fora dos limites do JPanel pai
        this.posX = Math.min(Math.max(0, this.posX), maxLargura - this.icone.getWidth(null));
        this.posY = Math.min(Math.max(0, this.posY), maxAltura - this.icone.getHeight(null));
    }

    public String sofrerDano(int dano) {
        if (this.vida <= 0) {
            return String.format("O %s já está morto!", this.getClass().getSimpleName());
        }
        this.vida = Math.max(this.vida - dano, 0);
        return String.format("O %s sofre %d de dano e agora tem %d de vida",
                this.getClass().getSimpleName(), dano, this.vida
        );
    }

    // Acessos
    public final int getAtaque() {
        return ataque;
    }

    public final int getVida() {
        return vida;
    }

    public final double getVelocidade() {
        return velocidade;
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

}
