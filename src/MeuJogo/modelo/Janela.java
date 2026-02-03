package MeuJogo.modelo;

import javax.swing.*;


public class Janela extends JFrame {


    public Janela() {
        setTitle("Meu Jogo");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        mostrarTelaInicial();

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void mostrarTelaInicial() {
        Telainicial tela = new Telainicial(this::iniciarFase);
        setContentPane(tela);
        revalidate();
        tela.requestFocusInWindow();
    }

    private void iniciarFase() {
        Fase fase = new Fase(this);
        setContentPane(fase);
        revalidate();
        fase.requestFocusInWindow();
    }

    // NOVO MÉTODO: Para trocar para a Fase 2
    public void irParaFase2() {
        Fase2 fase2 = new Fase2(this);
        setContentPane(fase2);
        revalidate();
        repaint();
        fase2.requestFocusInWindow();
    }

    public static void main(String[] args){
        new Janela();
    }
}

