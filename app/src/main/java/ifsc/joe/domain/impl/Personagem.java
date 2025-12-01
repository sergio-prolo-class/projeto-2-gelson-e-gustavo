package ifsc.joe.domain.impl;

public abstract class Personagem {

    protected int vida;
    protected double velocidade;
    protected int ataque;

    protected Personagem(int vida, int ataque, double velocidade) {
        this.vida = Math.max(vida, 0);
        this.ataque = Math.max(ataque, 0);
        this.velocidade = Math.max(velocidade, 0);
    }

    public String mover() {
        return String.format("O %s se move com velocidade %.2f", this.getClass().getSimpleName(), this.velocidade);
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
