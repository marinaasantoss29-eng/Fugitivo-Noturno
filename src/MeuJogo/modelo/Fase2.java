package MeuJogo.modelo;

import javax.swing.ImageIcon;
import java.util.ArrayList;
import java.util.Random;

public class Fase2 extends Fase{

    public Fase2(Janela Janela) {
        super(Janela); // Carrega as configurações básicas da classe mãe (Fase)
    }

    @Override
    public void criarCenario() {
        // Limpamos o que veio da memória da fase anterior
        plataformas.clear();
        moedas.clear();
        inimigos.clear();
        vigias = new ArrayList<>();
        
        fundo = new ImageIcon("res/cenario2.png").getImage();
        TAM_MAPA = 4000;

        // Cria um chão contínuo para o jogador começar com segurança
        for (int x = 0; x <= 800; x += 300) {
            criarPlataformaChao(x, 350);
        }
        
        //super.gerarChaoComBuracos();

        Random gerador = new Random();
        int xAtual = 1000;

        int quantidadePlataformas = 12; // Quantas você quiser
        int xInicial = 500;
        int distanciaMinimaX = 400;
        int variacaoX = 200;

        for (int i = 0; i < quantidadePlataformas; i++) {
            int x = xInicial + (i * distanciaMinimaX) + gerador.nextInt(variacaoX);

            int y = 120 + gerador.nextInt(160);

            criarPlataforma(x, y);
        }

        while (xAtual < 3200) {
            // Gera plataformas em alturas e distâncias variadas
            int larguraPlat = 160;
            int yAleatorio = 150 + gerador.nextInt(150); // Altura entre 150 e 300

            criarPlataforma(xAtual, yAleatorio);

            // Salto entre plataformas (distância entre 250 e 400 pixels)
            xAtual += 300 + gerador.nextInt(150);
        }


        // Onde o Chefão e o Baú estarão esperando
        for (int x = 3300; x <= TAM_MAPA; x += 300) {
            criarPlataformaChao(x, 350);
        }

        // 4. Adicionamos inimigos específicos da Fase 2
        inimigos.add(new Inimigo(1200, 150, 1000, 1600));
        inimigos.add(new Inimigo(2500, 250, 2500, 3000));
        inimigos.add(new Inimigo(2500, 200, 2200, 1800));
        inimigos.add(new InimigoEscudo(1600, 280, 1500, 2000));

        // 6. Resetamos o Boss e o Baú (para que não apareçam logo no início)
        inimigoFinal = new Chefao(3700, 220);

        if (inimigoFinal instanceof Chefao) {
            ((Chefao) inimigoFinal).setLimites(3400, 3950);
        }

        bauChefao = null;

        System.out.println("Fase 2 Carregada com Sucesso!");

    }

    private void criarPlataformaChao(int x, int y) {
        Plataforma chao = new Plataforma(x, y, 300, 50);
        plataformas.add(chao);
    }

}
