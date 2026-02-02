package MeuJogo.modelo;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import javax.swing.ImageIcon;

public class Vigia {

    // Atributos de posição e movimento
    private double x, y, startX;
    private double velocidade = 2.0;
    private int raio;
    private double angulo = 0;
    private Image imagem; // Variável para a imagem
    private int largura = 50;
    private int altura = 50;

    public Vigia(int x, int y, int raio) {
        this.x = x;
        this.y = y;
        this.startX = x;
        this.raio = raio;

        // CARREGANDO A IMAGEM
        ImageIcon referencia = new ImageIcon("res/vigias01.png");
        Image imgOriginal = referencia.getImage();

        this.imagem = imgOriginal.getScaledInstance(largura, altura, Image.SCALE_SMOOTH);
    }

    public void atualizar() {
        x += velocidade;
        if (Math.abs(x - startX) > raio) {
            velocidade *= -1;
        }
        angulo += 0.05; // Velocidade da flutuação
    }

    public void desenhar(Graphics2D g2) {
        int vY = (int) (y + Math.sin(angulo) * 15); // Efeito flutuar
        g2.drawImage(imagem, (int)x, vY, null);
    }

    public Rectangle getBounds() {
        return new Rectangle((int)x, (int)y, largura, altura);
    }
}