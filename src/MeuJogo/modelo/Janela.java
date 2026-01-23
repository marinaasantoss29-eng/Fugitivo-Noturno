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
        Fase fase = new Fase();
        setContentPane(fase);
        revalidate();
        fase.requestFocusInWindow();
    }
    public static void main(String[] args){
        new Janela();
    }
}

