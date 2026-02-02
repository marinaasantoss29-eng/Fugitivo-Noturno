package MeuJogo.modelo;

import javax.swing.ImageIcon;
import java.util.ArrayList;

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


        // 1. Mudamos o fundo para a segunda fase
        fundo = new ImageIcon("res/cenario2.png").getImage();

        // 2. Definimos um novo tamanho de mapa
        TAM_MAPA = 6000;

        // 3. Criamos um chão novo (chamando o método da classe mãe)
        super.gerarChaoComBuracos();

        // 4. Adicionamos inimigos específicos da Fase 2
        inimigos.add(new Inimigo(1200, 280, 1000, 1600));
        inimigos.add(new Inimigo(2500, 280, 2200, 3000));
        inimigos.add(new InimigoEscudo(1600, 280, 1500, 2000));

        vigias.add(new Vigia(800, 100, 400));
        vigias.add(new Vigia(2000, 150, 250));


        // 5. Adicionamos plataformas diferentes
        criarPlataforma(900, 220);
        criarPlataforma(1800, 180);

        // 6. Resetamos o Boss e o Baú (para que não apareçam logo no início)
        inimigoFinal = null;
        bauChefao = null;

        System.out.println("Fase 2 Carregada com Sucesso!");
    }
}
