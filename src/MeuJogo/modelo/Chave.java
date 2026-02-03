package MeuJogo.modelo;

import java.awt.*;
import javax.swing.ImageIcon;

public class Chave {
    private int x, y;
    private boolean coletada = false;
    private Image imagem;

    public Chave(int x, int y) {
        this.x = x;
        this.y = y;

        ImageIcon icon = new ImageIcon("res/chave01.png");
        imagem = icon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
    }

    public void draw(Graphics2D g2) {
        if (!coletada) {
            g2.drawImage(imagem, x, y, null);
        }
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, 40, 40);
    }

    public void coletar() {
        coletada = true;
    }

    public boolean foiColetada() {
        return coletada;
    }
}
