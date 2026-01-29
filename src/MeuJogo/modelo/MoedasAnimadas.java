package MeuJogo.modelo;

import java.awt.*;
import javax.swing.ImageIcon;

public class MoedasAnimadas {
    private int x, y;
    private int velocidadeY = 2;
    private int vida = 40; // duração da animação
    private Image imagem;

    public MoedasAnimadas(int x, int y) {
        this.x = x;
        this.y = y;
        imagem = new ImageIcon("res/moeda.png").getImage();
    }

    public void update() {
        y -= velocidadeY;
        vida--;
    }

    public boolean acabou() {
        return vida <= 0;
    }

    public void draw(Graphics g) {
        g.drawImage(imagem, x, y, 24, 24, null);
    }
}
