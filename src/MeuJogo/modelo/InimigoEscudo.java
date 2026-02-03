package MeuJogo.modelo;

import java.awt.Graphics2D;
import java.awt.Rectangle;

public class InimigoEscudo extends Inimigo {
    public InimigoEscudo(int x, int y, int limiteInicial, int limiteFinal) {
        super(x, y, limiteInicial, limiteFinal);
    }


    public boolean levarDano(int tiroX) {
        // Se o inimigo está olhando para a esquerda e o tiro vem da esquerda: BLOQUEIA
        // Se o inimigo está olhando para a direita e o tiro vem da direita: BLOQUEIA

        boolean tiroPelaFrente = (dx < 0 && tiroX < x) || (dx > 0 && tiroX > x);

        if (tiroPelaFrente) {
            System.out.println("Bloqueado pelo escudo!");
            return false; // Não morre
        } else {
            setVisivel(false); // Morre se atingido pelas costas
            return true;
        }
    }

    private void setVisivel(boolean b) {
    }

}
