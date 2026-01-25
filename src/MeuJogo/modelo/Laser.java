package MeuJogo.modelo;

import java.awt.*;

public class Laser {

    private int x, y;
    private int velocidade = 6;
    private boolean ativo = true;

    public Laser(int x, int y, int direcao, int jogadorY) {
        this.x = x;
        this.y = y;
        this.velocidade *= direcao;
    }

    public void update() {
        x += velocidade;
        if (x < 0 || x > 3000) {
            ativo = false;
        }
    }

    public void desativar() {
        ativo = false;
    }


    public void draw(Graphics2D g2) {
        if (!ativo) return;
        g2.setColor(Color.YELLOW);
        g2.fillRect(x, y, 10, 4);
    }


    public Rectangle getBounds() {
        return new Rectangle(x, y, 10, 4);
    }

    // ===== ESTADO =====
    public boolean ativo() {
        return ativo;
    }
}
