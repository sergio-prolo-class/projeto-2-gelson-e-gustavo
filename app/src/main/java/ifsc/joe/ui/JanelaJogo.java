package ifsc.joe.ui;

import ifsc.joe.consts.Constantes;

import javax.swing.*;

/**
 * Classe responsável pela configuração e exibição da janela principal do jogo.
 */
public class JanelaJogo {

    private final JFrame frame;
    private final PainelControles painelControles;

    public JanelaJogo() {
        this.frame = new JFrame(Constantes.TITULO_JOGO);
        this.painelControles = new PainelControles();

        this.configurarJanela();
    }

    /**
     * Configura o conteúdo da janela
     */
    private void configurarJanela() {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setContentPane(painelControles.getPainelPrincipal());
        frame.pack();
        frame.setLocationRelativeTo(null);
    }

    /**
     * Torna a janela visível.
     */
    public void exibir() {
        frame.setVisible(true);
    }
}