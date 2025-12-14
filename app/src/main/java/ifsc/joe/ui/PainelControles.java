package ifsc.joe.ui;

import ifsc.joe.enums.Direcao;
import ifsc.joe.enums.TipoPersonagem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Random;

import static ifsc.joe.enums.TipoPersonagem.*;

public class PainelControles {

    private final Random sorteio = new Random();
    private Tela tela;

    private JPanel painelPrincipal;
    private JPanel painelTela;
    private JPanel painelLateral;

    private JButton bCriaAldeao;
    private JButton bCriaArqueiro;
    private JButton bCriaCavaleiro;

    private JButton atacarButton;
    private JButton montarButton;
    private JButton coletarButton;

    private JButton buttonCima;
    private JButton buttonBaixo;
    private JButton buttonEsquerda;
    private JButton buttonDireita;

    private JRadioButton todosRadioButton;
    private JRadioButton aldeaoRadioButton;
    private JRadioButton arqueiroRadioButton;
    private JRadioButton cavaleiroRadioButton;

    private JLabel logo;
    private JLabel labelRecursos;

    public PainelControles() {
        configurarListeners();
        configurarTeclado();
        configurarBotaoColetar();
    }

    private void configurarListeners() {
        configurarRadioButtons();
        configurarBotoesMovimento();
        configurarBotoesCriacao();
        configurarBotaoAtaque();
        configurarBotaoMontar();
    }

    private void configurarRadioButtons() {
        todosRadioButton.addActionListener(e -> setFiltro("TODOS"));
        aldeaoRadioButton.addActionListener(e -> setFiltro("ALDEAO"));
        cavaleiroRadioButton.addActionListener(e -> setFiltro("CAVALEIRO"));
        arqueiroRadioButton.addActionListener(e -> setFiltro("ARQUEIRO"));
    }

    private void setFiltro(String filtro) {
        if (((AbstractButton) getBotaoFiltro(filtro)).isSelected()) {
            getTela().setFiltro(filtro);
        }
    }

    private AbstractButton getBotaoFiltro(String filtro) {
        return switch (filtro) {
            case "ALDEAO" -> aldeaoRadioButton;
            case "CAVALEIRO" -> cavaleiroRadioButton;
            case "ARQUEIRO" -> arqueiroRadioButton;
            default -> todosRadioButton;
        };
    }

    private void configurarTeclado() {
        painelPrincipal.setFocusable(true);
        painelPrincipal.requestFocusInWindow();

        painelPrincipal.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyPressed(java.awt.event.KeyEvent e) {
                processarTecla(e.getKeyCode());
            }
        });
    }

    private void processarTecla(int key) {
        switch (key) {
            case KeyEvent.VK_W, KeyEvent.VK_UP -> getTela().movimentarPersonagens(Direcao.CIMA);
            case KeyEvent.VK_S, KeyEvent.VK_DOWN -> getTela().movimentarPersonagens(Direcao.BAIXO);
            case KeyEvent.VK_A, KeyEvent.VK_LEFT -> getTela().movimentarPersonagens(Direcao.ESQUERDA);
            case KeyEvent.VK_D, KeyEvent.VK_RIGHT -> getTela().movimentarPersonagens(Direcao.DIREITA);

            case KeyEvent.VK_1 -> criarPersonagemAleatorio(ALDEAO);
            case KeyEvent.VK_2 -> criarPersonagemAleatorio(ARQUEIRO);
            case KeyEvent.VK_3 -> criarPersonagemAleatorio(CAVALEIRO);

            case KeyEvent.VK_SPACE -> executarAtaque();
            case KeyEvent.VK_M -> getTela().alternarMontaria();
            case KeyEvent.VK_SHIFT -> alternarFiltro();
            case KeyEvent.VK_C -> getTela().coletarComBotao();
        }
    }

    private void executarAtaque() {
        getTela().atacarPersonagens();
        destacarBotao(atacarButton, Color.RED);
    }

    private void destacarBotao(JButton botao, Color cor) {
        botao.setBackground(cor);
        botao.setForeground(Color.WHITE);

        Timer timer = new Timer(200, e -> {
            botao.setBackground(null);
            botao.setForeground(null);
        });
        timer.setRepeats(false);
        timer.start();
    }

    private void alternarFiltro() {
        if (todosRadioButton.isSelected()) {
            aldeaoRadioButton.setSelected(true);
            getTela().setFiltro("ALDEAO");
        } else if (aldeaoRadioButton.isSelected()) {
            cavaleiroRadioButton.setSelected(true);
            getTela().setFiltro("CAVALEIRO");
        } else if (cavaleiroRadioButton.isSelected()) {
            arqueiroRadioButton.setSelected(true);
            getTela().setFiltro("ARQUEIRO");
        } else {
            todosRadioButton.setSelected(true);
            getTela().setFiltro("TODOS");
        }
    }

    private void configurarBotaoMontar() {
        montarButton.addActionListener(e -> getTela().alternarMontaria());
    }

    private void configurarBotaoAtaque() {
        atacarButton.addActionListener(e -> executarAtaque());
    }

    private void configurarBotaoColetar() {
        coletarButton.addActionListener(e -> getTela().coletarComBotao());
    }

    private void configurarBotoesMovimento() {
        buttonCima.addActionListener(e -> getTela().movimentarPersonagens(Direcao.CIMA));
        buttonBaixo.addActionListener(e -> getTela().movimentarPersonagens(Direcao.BAIXO));
        buttonEsquerda.addActionListener(e -> getTela().movimentarPersonagens(Direcao.ESQUERDA));
        buttonDireita.addActionListener(e -> getTela().movimentarPersonagens(Direcao.DIREITA));
    }

    private void configurarBotoesCriacao() {
        bCriaAldeao.addActionListener(e -> criarPersonagemAleatorio(TipoPersonagem.ALDEAO));
        bCriaCavaleiro.addActionListener(e -> criarPersonagemAleatorio(TipoPersonagem.CAVALEIRO));
        bCriaArqueiro.addActionListener(e -> criarPersonagemAleatorio(TipoPersonagem.ARQUEIRO));
    }

    private void criarPersonagemAleatorio(TipoPersonagem tipo) {
        Point p = gerarPosicaoAleatoria();

        switch (tipo) {
            case ALDEAO -> getTela().criarAldeao(p.x, p.y);
            case CAVALEIRO -> getTela().criarCavaleiro(p.x, p.y);
            case ARQUEIRO -> getTela().criarArqueiro(p.x, p.y);
        }
    }

    private Point gerarPosicaoAleatoria() {
        final int PADDING = 50;
        int x = sorteio.nextInt(Math.max(1, painelTela.getWidth() - PADDING));
        int y = sorteio.nextInt(Math.max(1, painelTela.getHeight() - PADDING));
        return new Point(x, y);
    }

    private Tela getTela() {
        if (tela == null) {
            tela = (Tela) painelTela;
        }
        return tela;
    }

    public JPanel getPainelPrincipal() {
        return painelPrincipal;
    }

    private void createUIComponents() {
        painelTela = new Tela();
        painelTela.setPreferredSize(new Dimension(
                ifsc.joe.consts.Constantes.LARGURA_TELA,
                ifsc.joe.consts.Constantes.ALTURA_TELA
        ));
    }
}
