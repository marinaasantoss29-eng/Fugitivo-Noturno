package MeuJogo.modelo;

import java.awt.*;
import javax.swing.ImageIcon;

public class DroneVigia {

    private int x, y;
    private int largura = 60;
    private int altura = 40;

    private int limiteEsq, limiteDir;
    private int velocidade = 2;
    private int direcao = 1;

    private double anguloFlutuacao = 0;
    private int yBase;

    // ===== VISÃO =====
    private int alcanceVisao = 300;
    private int anguloVisao = 60;

    private boolean detectou = false;

    private Image sprite;

    public DroneVigia(int x, int y, int limiteEsq, int limiteDir) {
        this.x = x;
        this.y = y;
        this.yBase = y;

        this.limiteEsq = limiteEsq;
        this.limiteDir = limiteDir;

        sprite = new ImageIcon("res/vigias.png").getImage();
    }

    // ===== UPDATE =====
    public void update(Personagem jogador) {

        // Flutuação
        anguloFlutuacao += 0.05;
        y = yBase + (int)(Math.sin(anguloFlutuacao) * 10);

        // Movimento lateral
        x += velocidade * direcao;
        if (x < limiteEsq || x + largura > limiteDir) {
            direcao *= -1;
        }

        // Detecção
        detectou = detectarJogador(jogador);
    }

    // ===== DETECÇÃO =====
    private boolean detectarJogador(Personagem jogador) {

        if (jogador.estaCamuflado()) return false;

        int cxDrone = x + largura / 2;
        int cyDrone = y + altura / 2;

        int cxJog = jogador.getX() + jogador.getLargura() / 2;
        int cyJog = jogador.getY() + jogador.getALTURA() / 2;

        int dx = cxJog - cxDrone;
        int dy = cyJog - cyDrone;

        double dist = Math.sqrt(dx * dx + dy * dy);
        if (dist > alcanceVisao) return false;

        double nx = dx / dist;
        double ny = dy / dist;

        double olharX = direcao;
        double olharY = 0;

        double dot = nx * olharX + ny * olharY;
        double limite = Math.cos(Math.toRadians(anguloVisao / 2));

        return dot > limite;
    }

    // ===== DRAW =====
    public void draw(Graphics2D g2) {

        // Luz de varredura
        desenharLuz(g2);

        g2.drawImage(sprite, x, y, largura, altura, null);
    }

    private void desenharLuz(Graphics2D g2) {

        g2.setColor(detectou ?
                new Color(255, 0, 0, 80) :
                new Color(255, 255, 0, 60));

        int cx = x + largura / 2;
        int cy = y + altura / 2;

        Polygon cone = new Polygon();
        cone.addPoint(cx, cy);
        cone.addPoint(
                cx + direcao * alcanceVisao,
                cy - alcanceVisao / 3
        );
        cone.addPoint(
                cx + direcao * alcanceVisao,
                cy + alcanceVisao / 3
        );

        g2.fillPolygon(cone);
    }

    public boolean detectouJogador() {
        return detectou;
    }
}
