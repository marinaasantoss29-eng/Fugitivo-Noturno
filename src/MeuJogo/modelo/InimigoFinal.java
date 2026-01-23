package MeuJogo.modelo;

import java.awt.*;
import javax.swing.ImageIcon;

public class InimigoFinal {

    private int x, y;
    private int largura =120;
    private int altura = 160;

    private int velocidade = 2;
    private int limiteEsq, limiteDir;
    private int vida = 5;

    private boolean vivo = true;

    private Image imagem;

    public InimigoFinal(int x, int y) {
        this.x = x;
        this.y = y;


        this.imagem = new ImageIcon("res/inimigoparado.png").getImage();

    }

    public void update() {
    }

    public void draw(Graphics g) {
        if (!vivo) return;

        g.drawImage(imagem, x, y, largura, altura, null);

    }
    public void levarDano(){
        vida--;
        if(vida <= 0){
            vivo = false;
        }
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, largura, altura);
    }

    public void morrer() {
        vivo = false;
    }

    public boolean isVivo() {
        return vivo;
    }
}
