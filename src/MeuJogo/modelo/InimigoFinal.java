package MeuJogo.modelo;

import java.awt.*;
import java.util.ArrayList;
import javax.swing.ImageIcon;

public class InimigoFinal {

    // ===== POSIÇÃO E TAMANHO =====
    private int x, y;
    private int largura = 340;
    private int altura = 420;


    // ===== STATUS =====
    private int velocidade = 2;
    private int vida = 5;
    private boolean vivo = true;

    // ===== CONTROLE =====
    private boolean olhandoDireita = true;
    private int direcao = 1;


    private int limiteEsquerdo;
    private int limiteDireito;

    // ===== ATAQUE =====
    private ArrayList<LaserGuiado> lasers = new ArrayList<>();
    private int contadorTiro = 0;
    private int alcanceVisao = 400;

    // ===== ANIMAÇÃO =====
    private Image imagem;
    private Image parado;


    // ===== CHÃO =====
    private int chao;           // chão REAL da fase
    private int ajustePe = 0;  // corrige sprite (pé transparente)

    // ===== CONSTRUTOR =====
    public InimigoFinal(int x, int chao) {
        this.x = x;
        this.chao = chao;

        // posiciona no chão UMA VEZ
        this.y = chao - altura+ 20;

        // Sprites
        parado = new ImageIcon("res/inimigofinal0.1.png").getImage();

        imagem = parado;
    }

    // ===== UPDATE =====
    public void update(Personagem jogador) {

        if (!vivo) return;

        // ===== CENTROS =====
        int centroBossX = x + largura / 2;
        int centroJogadorX = jogador.getX() + jogador.getLargura() / 2;

        int centroBossY = y + altura / 2;
        int centroJogadorY = jogador.getY() + jogador.getALTURA() / 2;

        int distancia = Math.abs(centroJogadorX - centroBossX);

        // ===== DETECÇÃO =====
        boolean jogadorVisivel = distancia <= alcanceVisao;

        // ===== PARADO SE NÃO VÊ =====
        if (!jogadorVisivel) {
            imagem = parado;
            contadorTiro++;
            return;
        }

        // ===== DIREÇÃO / ESPELHAMENTO =====

        if (centroJogadorX < centroBossX) {
            direcao = -1;
            olhandoDireita = false;
        } else {
            direcao = 1;
            olhandoDireita = true;
        }

        // ===== VELOCIDADE POR VIDA =====
        if (vida >= 4) {
            velocidade = 2;
        } else if (vida >= 2) {
            velocidade = 3;
        } else {
            velocidade = 5;
        }

        // ===== MOVIMENTO =====
        x += velocidade * direcao;

        // ===== LIMITES =====
        if (x < limiteEsquerdo) x = limiteEsquerdo;
        if (x + largura > limiteDireito) x = limiteDireito - largura;

        // ===== ATAQUE =====

        contadorTiro++;
        if (contadorTiro >= 25) {

            for (int i = -1; i <= 1; i++) { // cria 3 lasers
                LaserGuiado laser = new LaserGuiado(centroBossX, centroBossY);

                // Pequeno espalhamento vertical
                laser.setDirecaoOlhar(direcao, i * 0.2);

                laser.tentarAtivar(
                        centroBossX,
                        centroBossY,
                        centroJogadorX,
                        centroJogadorY + (i * 40) // espalha a mira
                );

                lasers.add(laser);
            }
        }

        // Atualiza todos os lasers
        for (LaserGuiado l : lasers) {
            l.update(centroJogadorX, centroJogadorY);
        }

    }

    // ===== DRAW =====
    public void draw(Graphics g) {
        if (!vivo) return;

        if (olhandoDireita) {
            // agora espelha quando olha pra direita
            g.drawImage(imagem, x + largura, y, -largura, altura, null);
        } else {
            // sprite original (que já está olhando pra esquerda)
            g.drawImage(imagem, x, y, largura, altura, null);
        }

    }

    // ===== MÉTODOS AUXILIARES =====
    public void levarDano() {
        vida--;
        if (vida <= 0) vivo = false;
    }

    public boolean isVivo() {
        return vivo;
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, largura, altura);
    }

    public void drawLasers(Graphics g) {
        for (LaserGuiado l : lasers) {
            l.draw((Graphics2D) g);
        }
    }

    public ArrayList<LaserGuiado> getLasers() {
        return lasers;
    }

    public void setLimites(int esq, int dir) {
        this.limiteEsquerdo = esq;
        this.limiteDireito = dir;
    }
}
