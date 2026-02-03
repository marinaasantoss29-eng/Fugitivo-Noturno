package MeuJogo.modelo;

import java.awt.*;

public class Plataforma {

    protected int x, y, largura, altura;

    private int deltaX = 0;
    private int deltaY = 0;

    // Movimento horizontal
    private boolean movelHorizontal = false;
    private int velocidadeX = 0;
    private int limiteEsq, limiteDir;

    // Movimento vertical
    private boolean movelVertical = false;
    private int velocidadeY = 0;
    private int limiteCima, limiteBaixo;

    // ================== PLATAFORMA PARADA (ESSA QUE ESTAVA FALTANDO) ==================
    public Plataforma(int x, int y, int largura, int altura) {
        this.x = x;
        this.y = y;
        this.largura = largura;
        this.altura = altura;
    }

    // ================== PLATAFORMA QUE ANDA PRO LADO ==================
    public Plataforma(int x, int y, int largura, int altura,
                      int limiteEsq, int limiteDir, int velocidadeX) {
        this(x, y, largura, altura);
        this.movelHorizontal = true;
        this.limiteEsq = limiteEsq;
        this.limiteDir = limiteDir;
        this.velocidadeX = velocidadeX;
    }

    // ================== PLATAFORMA QUE SOBE E DESCE ==================
    public Plataforma(int x, int y, int largura, int altura,
                      int limiteCima, int limiteBaixo, int velocidadeY, boolean vertical) {
        this(x, y, largura, altura);
        this.movelVertical = true;
        this.limiteCima = limiteCima;
        this.limiteBaixo = limiteBaixo;
        this.velocidadeY = velocidadeY;
    }

    // ================== ATUALIZA MOVIMENTO ==================
    public void atualizar() {
        deltaX = 0;
        deltaY = 0;

        // Movimento horizontal
        if (movelHorizontal) {
            x += velocidadeX;
            deltaX = velocidadeX;

            if (x <= limiteEsq || x + largura >= limiteDir) {
                velocidadeX *= -1;
            }
        }

        // Movimento vertical
        if (movelVertical) {
            y += velocidadeY;
            deltaY = velocidadeY;

            if (y <= limiteCima || y + altura >= limiteBaixo) {
                velocidadeY *= -1;
            }
        }
    }

    // ================== DESENHO ==================
    public void draw(Graphics2D g2) {
        g2.setColor(new Color(10, 20, 60));
        g2.fillRect(x, y, largura, altura);
    }

    // ================== COLISÃO ==================
    public Rectangle getBounds() {
        return new Rectangle(x, y, largura, altura);
    }

    // ================== GETTERS ==================
    public int getX() { return x; }
    public int getY() { return y; }
    public int getLargura() { return largura; }
    public int getAltura() { return altura; }

    public int getDeltaX() {
        return deltaX;
    }

    public int getDeltaY() {
        return deltaY;
    }
}
