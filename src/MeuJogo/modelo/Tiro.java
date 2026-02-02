package MeuJogo.modelo;

import java.awt.*;

public class Tiro {

    private int x, y;
    private int velocidade;
    private int dano;
    private int tamanho;
    private boolean ativo = true;

    public Tiro(int x, int y, boolean direita, int carga) {
        this.x = x;
        this.y = y;

        // ===== CONFIGURAÇÃO PELO NÍVEL DE CARGA =====
        if (carga < 15) {
            dano = 1;
            tamanho = 8;
            velocidade = 10;
        } else if (carga < 30) {
            dano = 2;
            tamanho = 12;
            velocidade = 12;
        } else {
            dano = 4;
            tamanho = 18;
            velocidade = 14;
        }

        if (!direita) velocidade *= -1;
    }

    public void update() {
        x += velocidade;
        if (x < 0 || x > 4000) ativo = false;
    }

    public void draw(Graphics2D g2) {
        if (!ativo) return;

        g2.setColor(new Color(0, 150, 255, 80));
        g2.fillOval(x - 4, y - 4, tamanho + 8, tamanho + 8);

        g2.setColor(new Color(0, 220, 255));
        g2.fillOval(x, y, tamanho, tamanho);

        g2.setColor(Color.WHITE);
        g2.fillOval(x + tamanho / 4, y + tamanho / 4, tamanho / 2, tamanho / 2);
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, tamanho, tamanho);
    }

    public int getDano() {
        return dano;
    }

    public boolean ativo() {
        return ativo;
    }

    public void destruir() {
        ativo = false;
    }

    public void setVisivel(boolean b) {
    }

    public int getX() {
        return x;
    }
}
