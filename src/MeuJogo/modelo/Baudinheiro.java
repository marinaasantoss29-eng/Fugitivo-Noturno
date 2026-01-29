package MeuJogo.modelo;

import java.awt.*;
import javax.swing.ImageIcon;

public class Baudinheiro {
    private int x, y;
    private int largura = 80;
    private int altura = 60;

    private boolean ativo = true;
    private Image imagem;

    public Baudinheiro(int x, int y) {
        this.x = x;
        this.y = y;
        imagem = new ImageIcon("res/Baudedinheiro01.png").getImage();
    }

    public void ativar() {
        ativo = true;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void draw(Graphics g) {
        if (!ativo) return;
        g.drawImage(imagem, x, y, largura, altura, null);
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, largura, altura);
    }

}
