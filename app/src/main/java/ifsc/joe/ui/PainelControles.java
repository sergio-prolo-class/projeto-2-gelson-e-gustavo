package ifsc.joe.ui;

import ifsc.joe.enums.Direcao;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Random;

import static com.sun.java.accessibility.util.AWTEventMonitor.addKeyListener;

/**
 * Classe responsável por gerenciar os controles e interações da interface.
 * Conecta os componentes visuais com a lógica do jogo (Tela).
 */
public class PainelControles {

    private final Random sorteio;
    private Tela tela;

    private ButtonGroup grupoFiltro;

    // Componentes da interface (gerados pelo Form Designer)
    private JPanel painelPrincipal;
    private JPanel painelTela;
    private JPanel painelLateral;
    private JButton bCriaAldeao;
    private JButton bCriaArqueiro;
    private JButton bCriaCavaleiro;
    private JRadioButton todosRadioButton;
    private JRadioButton aldeaoRadioButton;
    private JRadioButton arqueiroRadioButton;
    private JRadioButton cavaleiroRadioButton;
    private JButton atacarButton;
    private JButton buttonCima;
    private JButton buttonEsquerda;
    private JButton buttonBaixo;
    private JButton buttonDireita;
    private JLabel logo;
    private JButton montarButton;
    private JLabel labelRecursos;

    public PainelControles() {
        this.sorteio = new Random();
        configurarListeners();
        configurarTeclado();
//        criarLabelRecursos();
//        configurarBotaoColetar();
    }

//    private void criarLabelRecursos() {
//        labelRecursos = new JLabel("Recursos: ");
//        labelRecursos.setFont(new Font("Arial", Font.BOLD, 12));
//        labelRecursos.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
//        painelLateral.add(labelRecursos, BorderLayout.SOUTH);
//    }
//
//    private void configurarBotaoColetar() {
//        JButton coletarButton = new JButton("Verificar Coleta");
//        coletarButton.addActionListener(e -> {
//            getTela().verificarColetaveis();
//            //atualizarDisplayRecursos();
//        });
//        painelLateral.add(coletarButton);
//    }

    /**
     * Configura todos os listeners dos botões.
     */
    private void configurarListeners() {
        configurarRadioButtons();
        configurarBotoesMovimento();
        configurarBotoesCriacao();
        configurarBotaoAtaque();
        configurarBotaoMontar();

    }

    private void configurarRadioButtons() {
        todosRadioButton.addActionListener(e -> {

            if (todosRadioButton.isSelected()) {
                getTela().setFiltro("TODOS");
            }

        }
        );

        aldeaoRadioButton.addActionListener(e -> {

            if (aldeaoRadioButton.isSelected()) {
                getTela().setFiltro("ALDEAO");
            }
        });

        cavaleiroRadioButton.addActionListener(e -> {


            if (cavaleiroRadioButton.isSelected()) {
                getTela().setFiltro("CAVALEIRO");
            }
        });

        arqueiroRadioButton.addActionListener(e -> {


            if (arqueiroRadioButton.isSelected()) {
                getTela().setFiltro("ARQUEIRO");
            }
        });
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

    private void processarTecla(int keyCode) {
        switch (keyCode) {
            // Movimentaçao
            case KeyEvent.VK_W:
            case KeyEvent.VK_UP:
                getTela().movimentarPersonagens(Direcao.CIMA);
                break;
            case KeyEvent.VK_S:
            case KeyEvent.VK_DOWN:
                getTela().movimentarPersonagens(Direcao.BAIXO);
                break;
            case KeyEvent.VK_A:
            case KeyEvent.VK_LEFT:
                getTela().movimentarPersonagens(Direcao.ESQUERDA);
                break;
            case KeyEvent.VK_D:
            case KeyEvent.VK_RIGHT:
                getTela().movimentarPersonagens(Direcao.DIREITA);
                break;

            // Criar personagens
            case KeyEvent.VK_1:
                criarAldeaoAleatorio();
                break;
            case KeyEvent.VK_2:
                criarCavaleiroAleatorio();
                break;
            case KeyEvent.VK_3:
                criarArqueiroAleatorio();
                break;

            // Açoes
            case KeyEvent.VK_SPACE:
                getTela().atacarPersonagens();

                // Feedback visual no botão de ataque
                atacarButton.setBackground(Color.RED);
                atacarButton.setForeground(Color.WHITE);
                Timer timer = new Timer(200, evt -> {
                    atacarButton.setBackground(null);
                    atacarButton.setForeground(null);
                });
                timer.setRepeats(false);
                timer.start();
                break;

            case KeyEvent.VK_M:
                getTela().alternarMontaria();
                break;

            // Filtros - Tab para alternar
            case KeyEvent.VK_SHIFT:
                alternarFiltroTab();
                break;
        }
    }

    private void alternarFiltroTab() {
        if (todosRadioButton.isSelected()) {
            aldeaoRadioButton.setSelected(true);
            getTela().setFiltro("ALDEAO");
        }
        else if (aldeaoRadioButton.isSelected()) {
            cavaleiroRadioButton.setSelected(true);
            getTela().setFiltro("CAVALEIRO");
        }
        else if (cavaleiroRadioButton.isSelected()) {
            arqueiroRadioButton.setSelected(true);
            getTela().setFiltro("ARQUEIRO");
        }
        else if (arqueiroRadioButton.isSelected()) {
            todosRadioButton.setSelected(true);
            getTela().setFiltro("TODOS");
        }
        else {

            todosRadioButton.setSelected(true);
            getTela().setFiltro("TODOS");
        }
    }




    private void configurarBotaoMontar() {
        montarButton.addActionListener(e -> {
            getTela().alternarMontaria();
        });
    }

    private void configurarBotaoAtaque() {
        atacarButton.addActionListener(e -> {
            // Executa ataque com filtro
            getTela().atacarPersonagens();

            // visual
            atacarButton.setBackground(Color.RED);
            atacarButton.setForeground(Color.WHITE);

            // Volta ao normal após 0.3 segundos
            Timer timer = new Timer(200, evt -> {
                atacarButton.setBackground(null);
                atacarButton.setForeground(null);
            });
            timer.setRepeats(false);
            timer.start();
        });
    }


    /**
     * Configura todos os listeners dos botões de movimento
     */
    private void configurarBotoesMovimento() {
         // buttonCima.addActionListener(e -> getTela().movimentarAldeoes(Direcao.CIMA));

        buttonCima.addActionListener(e -> getTela().movimentarPersonagens(Direcao.CIMA));
        buttonBaixo.addActionListener(e -> getTela().movimentarPersonagens(Direcao.BAIXO));
        buttonEsquerda.addActionListener(e -> getTela().movimentarPersonagens(Direcao.ESQUERDA));
        buttonDireita.addActionListener(e -> getTela().movimentarPersonagens(Direcao.DIREITA));
    }

    /**
     * Configura todos os listeners dos botões de criação
     */
    private void configurarBotoesCriacao() {
        bCriaAldeao.addActionListener(e -> criarAldeaoAleatorio());

        bCriaArqueiro.addActionListener(e -> criarArqueiroAleatorio());

        bCriaCavaleiro.addActionListener(e -> criarCavaleiroAleatorio());


    }

    /**
     * Configura o listener do botão de ataque
     */
//    private void configurarBotaoAtaque() {
//        atacarButton.addActionListener(e -> getTela().atacarAldeoes());
//    }

    /**
     * Cria um aldeão em posição aleatória na tela.
     */
    private void criarAldeaoAleatorio() {
        final int PADDING = 50;
        int posX = sorteio.nextInt(painelTela.getWidth() - PADDING);
        int posY = sorteio.nextInt(painelTela.getHeight() - PADDING);

        getTela().criarAldeao(posX, posY);
    }

    private void criarCavaleiroAleatorio() {
        final int PADDING = 50;
        int posX = sorteio.nextInt(painelTela.getWidth() - PADDING);
        int posY = sorteio.nextInt(painelTela.getHeight() - PADDING);

        getTela().criarCavaleiro(posX, posY);
    }

    private void criarArqueiroAleatorio() {
        final int PADDING = 50;
        int posX = sorteio.nextInt(painelTela.getWidth() - PADDING);
        int posY = sorteio.nextInt(painelTela.getHeight() - PADDING);

        getTela().criarArqueiro(posX, posY);
    }


    /**
     * Exibe mensagem informando que a funcionalidade ainda não foi implementada.
     */
    private void mostrarMensagemNaoImplementado(String funcionalidade) {
        JOptionPane.showMessageDialog(
                painelPrincipal,
                "Preciso ser implementado",
                funcionalidade,
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    /**
     * Obtém a referência da Tela com cast seguro.
     */
    private Tela getTela() {
        if (tela == null) {
            tela = (Tela) painelTela;
        }
        return tela;
    }

    /**
     * Retorna o painel principal para ser adicionado ao JFrame.
     */
    public JPanel getPainelPrincipal() {
        return painelPrincipal;
    }

    /**
     * Método chamado pelo Form Designer para criar componentes customizados.
     * Este método é invocado antes do construtor.
     */
    private void createUIComponents() {
        this.painelTela = new Tela();
    }
}