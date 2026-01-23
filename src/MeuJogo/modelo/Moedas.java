package MeuJogo.modelo;

import java.awt.*;
import javax.swing.ImageIcon;

public class Moedas {
    private int x, y;
    private final int TAM = 32;
    private boolean coletada = false;
    private Image imagem;

    private Plataforma plataforma;

    public Moedas(int x, int y) {
        this.x = x;
        this.y = y;
        imagem = new ImageIcon("res/moeda01.png").getImage();
    }

    public Moedas(int offsetX, int offsetY, Plataforma plataforma){
        this.plataforma = plataforma;
        this.x = plataforma.getX() + offsetX;
        this.y = plataforma.getY() + offsetY;

        imagem = new ImageIcon("res/moeda01.png").getImage();
    }

    public void update(){
        if(plataforma != null && !coletada){
            x += plataforma.getDeltax();
        }
    }

    public void draw(Graphics2D g) {
        if (!coletada) {
            g.drawImage(imagem, x, y, TAM, TAM, null);
        }
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, TAM, TAM);
    }

    public boolean foiColetada() {
        return coletada;
    }

    public void coletar() {
        coletada = true;
    }

    public int getX() {
        return 0;
    }
}
