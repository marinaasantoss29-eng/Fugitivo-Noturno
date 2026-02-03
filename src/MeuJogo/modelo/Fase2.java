package MeuJogo.modelo;

import javax.swing.ImageIcon;
import java.util.ArrayList;
import java.util.Random;

public class Fase2 extends Fase{

    public Fase2(Janela Janela) {
        super(Janela);
        this.faseAtual = 2;
    }

    @Override
    public void criarCenario() {
        // Limpamos o que veio da memória da fase anterior
        plataformas.clear();
        moedas.clear();
        inimigos.clear();
        vigias = new ArrayList<>();
        inimigoFinal = new Chefao(3700, 220);
        fundo = new ImageIcon("res/cenario2.png").getImage();
        TAM_MAPA = 4000;



        // Cria um chão contínuo para o jogador começar com segurança
        for (int x = 0; x <= 700; x += 300) {
            criarPlataformaChao(x, 350);
        }
        
        //super.gerarChaoComBuracos();

        Random gerador = new Random();

        int xAtual = 900;
        int quantidadePlataformas = 4;
        int xInicial = 900;
        int distanciaMinimaX = 380;
        int variacaoX = 150;

        int ultimaAltura = 220; // altura inicial

        for (int i = 0; i < quantidadePlataformas; i++) {

            int x = xInicial + (i * distanciaMinimaX) + gerador.nextInt(variacaoX);

            int novaAltura;

            // Garante diferença mínima de altura entre plataformas
            do {
                novaAltura = 140 + gerador.nextInt(140); // entre 140 e 280
            } while (Math.abs(novaAltura - ultimaAltura) < 70);

            ultimaAltura = novaAltura;

            criarPlataforma(x, novaAltura);
        }

        int ultimaAltura2 = 220;

        while (xAtual < 3200) {

            int novaAltura;

            do {
                novaAltura = 140 + gerador.nextInt(140);
            } while (Math.abs(novaAltura - ultimaAltura2) < 70);

            ultimaAltura2 = novaAltura;

            criarPlataforma(xAtual, novaAltura);

            xAtual += 320 + gerador.nextInt(120);
        }

        // Onde o Chefão e o Baú estarão esperando
        for (int x = 3300; x <= TAM_MAPA; x += 300) {
            criarPlataformaChao(x, 350);
        }
        plataformas.add(new Plataforma(1500, 220, 120, 20, 1400, 1800, 2));
        plataformas.add(new Plataforma(2500, 320, 220, 40, 2400, 2800, 1));

        // 4. Adicionamos inimigos específicos da Fase 2
        inimigos.add(new Inimigo(1200, 150, 1000, 1600));
        inimigos.add(new Inimigo(2500, 250, 2500, 3000));
        inimigos.add(new Inimigo(2500, 200, 2200, 1800));
        inimigos.add(new Inimigo(1300, 220, 1200, 1500));
        inimigos.add(new Inimigo(2600, 110, 2500, 3000));

        // 6. Resetamos o Boss e o Baú (para que não apareçam logo no início)
        inimigoFinal = new Chefao(3700, 220);

        if (inimigoFinal instanceof Chefao) {
            ((Chefao) inimigoFinal).setLimites(3400, 3950);
        }

        // Onde o Chefão e o Baú estarão esperando
        for (int x = 3300; x <= TAM_MAPA; x += 300) {
            criarPlataformaChao(x, 350);
        }

        // Chefão
        inimigoFinal = new Chefao(3700, 220);
        ((Chefao) inimigoFinal).setLimites(3400, 3950);

    //  BAÚ
        bauChefao = new Baudinheiro(3850, 300);

        System.out.println("Fase 2 Carregada com Sucesso!");

    }

    private void criarPlataformaChao(int x, int y) {
        Plataforma chao = new Plataforma(x, y, 300, 50);
        plataformas.add(chao);
    }

}
