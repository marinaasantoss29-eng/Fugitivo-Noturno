package MeuJogo.modelo;

import javax.swing.*;
import java.awt.*;

public class Caixa {

    private int x, y;
    private int largura = 60;
    private int altura = 60;

    private Image imagem;

    public Caixa(int x, int y) {
        this.x = x;
        this.y = y;

        imagem = new ImageIcon("res/caixas1.png").getImage();
    }

    public void draw(Graphics2D g2) {
        g2.drawImage(imagem, x, y, largura, altura, null);
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, largura, altura);
    }
}
