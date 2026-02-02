package MeuJogo.modelo;

import java.awt.*;
import javax.swing.ImageIcon;

public class Baudinheiro {
    private int x, y;
    private int largura = 80;
    private int altura = 60;

    private boolean ativo = false;
    private Image imagem;
    private Image imagemCorrente;

    public Baudinheiro(int x, int y) {
        this.x = x;
        this.y = y;
        imagem = new ImageIcon("res/Baudedinheiro01.png").getImage();
        imagemCorrente = new ImageIcon("res/correntes.png").getImage();
    }

    public void ativar() {

        ativo = true;
    }

    public boolean isAtivo() {

        return ativo;
    }

    public void draw(Graphics g) {

        g.drawImage(imagem, x, y, largura, altura, null);

        if(!ativo){
            g.setColor(new Color(255, 0, 0, 100));
            g.drawImage(imagemCorrente, x - 5, y - 5, largura + 10, altura + 10, null);
            g.setColor(Color.RED);
            g.drawRect(x, y, largura, altura);
        }
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, largura, altura);
    }

}
