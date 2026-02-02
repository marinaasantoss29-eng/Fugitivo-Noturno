package MeuJogo.modelo;

import java.awt.*;


public class MoedasAnimadas {
    private int x, y;
    private int contador = 0;
    private final int LIMITE = 40; // Quantidade de pixels que ela sobe

    public MoedasAnimadas(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void update() {
        y -= 2; // Velocidade de subida
        contador++;
    }

    public boolean acabou() {
        return contador >= LIMITE;
    }

    public void draw(Graphics2D g2) {
        g2.setColor(Color.YELLOW);
        g2.fillOval(x, y, 10, 10); // Desenha um pequeno círculo dourado
    }
}