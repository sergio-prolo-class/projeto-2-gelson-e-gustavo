package ifsc.joe.domain.impl;

import ifsc.joe.enums.Recursos;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;
import java.util.Random;

public class Coletavel {

        private final Recursos tipo;
        private final int quantidade;
        private final int posX, posY;
        private final Image icone;
        private boolean coletado = false;
        private final String nomeImagem;
        private static final Random random = new Random();

        public Coletavel(Recursos tipo, int quantidade, int x, int y) {
            this.tipo = tipo;
            this.quantidade = quantidade;
            this.posX = x;
            this.posY = y;

            // Escolhe imagem aleatória baseada no tipo
            this.nomeImagem = gerarNomeImagemAleatoria(tipo);
            this.icone = carregarImagem(nomeImagem);
        }

        public Coletavel(Recursos tipo, int maxX, int maxY) {

            this.tipo = tipo;
            this.quantidade = gerarQuantidadeAleatoria(tipo);
            this.posX = random.nextInt(maxX - 100) + 50;
            this.posY = random.nextInt(maxY - 100) + 50;
            this.nomeImagem = gerarNomeImagemAleatoria(tipo);

            this.icone = carregarImagem(nomeImagem);

        }

        private String gerarNomeImagemAleatoria(Recursos tipo) {


            String base = "coletavel_" + tipo.name().toLowerCase();

            switch (tipo) {

                case MADEIRA:
                    return base + (random.nextInt(3) + 1);

                case OURO:
                    return base + (random.nextInt(3) + 1);

                case COMIDA:
                    return base + (random.nextInt(2) + 1);

                default:
                    return base;
            }
        }

        private int gerarQuantidadeAleatoria(Recursos tipo) {

            switch (tipo) {

                case COMIDA:
                    return 10 + random.nextInt(40);

                case MADEIRA:
                    return 5 + random.nextInt(25);

                case OURO:
                    return 3 + random.nextInt(12);

                default:
                    return 10;


            }
        }

//    private Image carregarImagem(String imagem) {
//        return new ImageIcon(Objects.requireNonNull(
//                getClass().getClassLoader().getResource("./"+imagem+".png")
//        )).getImage();
//    }

    private Image carregarImagem(String imagem) {
        java.net.URL url = getClass().getClassLoader().getResource("./" + imagem + ".png");


        if (url == null) {
            System.err.println("Imagem não encontrada: " + imagem + ".png");

            if (imagem.contains("coletavel")) {

                url = getClass().getClassLoader().getResource("./coletavel_comida1.png");
            if (url == null) {
                    System.err.println("Imagem fallback também não encontrada");
                    return null;
                }
            }
        }

        return new ImageIcon(url).getImage();
    }

        public void desenhar(Graphics g) {
            if (!coletado && icone != null) {
                g.drawImage(icone, posX, posY, null);


                g.setColor(Color.WHITE);
                g.setFont(new Font("Arial", Font.BOLD, 10));
                g.drawString("" + quantidade, posX + 5, posY + 15);
            }
        }


    public boolean colidiuCom(Personagem personagem) {
        if (coletado) return false;


        int diferencaX = Math.abs(posX - personagem.getPosX());
        int diferencaY = Math.abs(posY - personagem.getPosY());

        // Colide se estiver dentro de 20 pixels
        return diferencaX < 20 && diferencaY < 20;
    }


        public Recursos getTipo() { return tipo; }
        public int getQuantidade() { return quantidade; }
        public boolean isColetado() { return coletado; }
        public int getPosX() { return posX; }
        public int getPosY() { return posY; }

        public void coletar() {
            this.coletado = true;
        }

        @Override
        public String toString() {
            return quantidade + " " + tipo + " em (" + posX + "," + posY + ")";
        }
    }

