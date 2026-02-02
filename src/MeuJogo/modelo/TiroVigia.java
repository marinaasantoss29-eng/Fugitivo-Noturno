package MeuJogo.modelo;

import java.awt.*;

public class TiroVigia {

    private int x, y;
    private int velocidade = 6;
    private int direcao;
    private boolean ativo = true;

    private static final int LARGURA = 8;
    private static final int ALTURA = 4;

    public TiroVigia(int x, int y, int direcao) {
        this.x = x;
        this.y = y;
        this.direcao = direcao;
    }

    public void update() {
        x += velocidade * direcao;

        if (x < -50 || x > 5000) {
            ativo = false;
        }
    }

    public void draw(Graphics2D g2) {
        g2.setColor(Color.RED);
        g2.fillRect(x, y, LARGURA, ALTURA);
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, LARGURA, ALTURA);
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void desativar() {
        ativo = false;
    }
}
