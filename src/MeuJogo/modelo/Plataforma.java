package MeuJogo.modelo;

import java.awt.*;

public class Plataforma {

    private int x, y, largura, altura;

    private boolean movel = false;
    private int velocidade = 1;
    private int limiteEsq, limiteDir;

    private int deltaX = 0;

    public Plataforma(int x, int y, int largura, int altura) {
        this.x = x;
        this.y = y;
        this.largura = largura;
        this.altura = altura;
    }

    public Plataforma(int x, int y, int largura, int altura, int limiteEsq, int limiteDir, int velocidade){
        this.x = x;
        this.y = y;
        this.largura = largura;
        this.altura = altura;
        this.limiteEsq = limiteEsq;
        this.limiteDir = limiteDir;
        this.velocidade = velocidade;
        this.movel = true;
    }
    private int deltax;

    public void atualizar(){
        deltax = 0;

        if(movel){
            x += velocidade;

            if (x <= limiteEsq || x + largura >= limiteDir){
                velocidade *= -1;
            }
        }
    }

    public int getDeltax(){
        return deltaX;
    }
    public void desenhar(Graphics2D g2){
        g2.setColor(Color.DARK_GRAY);
        g2.fillRect(x, y, largura, altura);
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, largura, altura);
    }

    public void draw(Graphics2D g2) {
        g2.setColor(new Color(10, 20, 60));
        g2.fillRect(x, y, largura, altura);
    }

    public int getX() {
        return x;
    }
    public int getY() {

        return y;
    }
    public int getLargura() {

        return largura;
    }
    public int getAltura() {

        return altura;
    }

    public void setX(int x) {

        this.x = x;
    }
    public void setY(int y) {
        this.y = y;
    }
}
