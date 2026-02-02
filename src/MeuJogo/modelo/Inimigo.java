package MeuJogo.modelo;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Inimigo {

    protected int x, y;
    protected int dx;
    private int velocidade = 2;

    private int limiteEsq, limiteDir;

    private ArrayList<Laser> lasers = new ArrayList<>();

    // ===== CONTROLE DE TIRO =====
    private int cooldown = 0;
    private final int TEMPO_TIRO = 60; // frames (1 segundo)
    private Image imagem;

    public Inimigo(int x, int y, int limiteEsq, int limiteDir) {
        this.x = x;
        this.y = y;
        this.limiteEsq = limiteEsq;
        this.limiteDir = limiteDir;

        imagem = new ImageIcon("res/inimigo01.png").getImage();
    }

    public void atualizar(Personagem jogador, int cameraX, int larguraTela) {

        // ===== MOVIMENTO =====
        x += velocidade;
        if (x <= limiteEsq || x >= limiteDir) {
            velocidade *= -1;
        }

        // ===== DETECTA JOGADOR =====
        int distancia = Math.abs(jogador.getX() - x);
        boolean jogadorVisivel = distancia < 400; // alcance de visão

        // ===== ATIRA SE AVISTAR =====
        if (jogadorVisivel && cooldown <= 0) {

            int dir = jogador.getX() > x ? 1 : -1;

            lasers.add(new Laser(
                    x + (dir == 1 ? 40 : -10),
                    y + 20,
                    dir,
                    jogador.getY()));

            cooldown = TEMPO_TIRO;
        }

        cooldown--;

        // ===== ATUALIZA LASERS =====
        for (int i = 0; i < lasers.size(); i++) {
            lasers.get(i).update();
            if (!lasers.get(i).ativo()) {
                lasers.remove(i);
                i--;
            }
        }
    }

    public void desenhar(Graphics2D g2) {

        g2.drawImage(imagem, x, y, 40, 60, null);

        // ===== DESENHA LASERS =====
        for (Laser l : lasers) {
            l.draw(g2);
        }
    }


    public ArrayList<Laser> getLasers() {
        return lasers;
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, 40, 60);
    }

}
