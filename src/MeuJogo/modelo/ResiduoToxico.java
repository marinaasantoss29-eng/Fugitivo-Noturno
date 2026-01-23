package MeuJogo.modelo;

import java.awt.*;
import javax.swing.ImageIcon;

public class ResiduoToxico {

    private int x, y;
    private int largura = 96;
    private int altura = 48;
    private Image imagem;
    private boolean ativo = true;

    public boolean Ativo(){

        return ativo;
    }

    public void desativar(){

        ativo = false;
    }

    public ResiduoToxico(int x, int y) {
        this.x = x;
        this.y = y;
        imagem = new ImageIcon("res/residuoToxico1.png").getImage();
    }


    public void draw(Graphics2D g2) {
        if(ativo)
            g2.drawImage(imagem, x, y, largura, altura, null);
    }


    public Rectangle getBounds() {
        return new Rectangle(x, y, largura, altura);
    }
}
