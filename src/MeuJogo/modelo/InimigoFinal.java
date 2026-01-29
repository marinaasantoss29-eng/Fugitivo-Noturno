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

    private double anguloFlutuacao = 0;
    private int yBase;

    // ===== CONTROLE =====
    private boolean olhandoDireita = true;
    private int direcao = 1;


    private int limiteEsquerdo;
    private int limiteDireito;

    // ===== ATAQUE =====
    private ArrayList<LaserGuiado> lasers = new ArrayList<>();
    private int contadorTiro = 0;
    private final int TEMPO_TIRO = 90;
    private final int MAX_LASERS = 4;
    private int alcanceVisao = 450;

    // ===== ANIMAÇÃO =====
    private Image imagem;
    private Image parado;


    // ===== CHÃO =====
    private int chao;           // chão REAL da fase
    private int ajustePe = 0;



    // ===== CONSTRUTOR =====
    public InimigoFinal(int x, int chao) {
        this.x = x;
        this.chao = chao;

        this.yBase = chao - altura + 20;
        this.y = yBase;

        // posiciona no chão UMA VEZ
        this.y = chao - altura+ 20;

        // Sprites
        parado = new ImageIcon("res/inimigofinal0.1.png").getImage();

        imagem = parado;
    }

    // ===== UPDATE =====
    public void update(Personagem jogador) {

        if (!vivo) return;

        anguloFlutuacao += 0.05;
        y = yBase + (int)(Math.sin(anguloFlutuacao) * 20);


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
            contadorTiro = 0;

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
        if (contadorTiro >= TEMPO_TIRO && lasers.size() < MAX_LASERS) {

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
            contadorTiro = 0;
        }
        for(int i = 0; i < lasers.size(); i++){
            LaserGuiado l = lasers.get(i);
            l.update(centroJogadorX, centroJogadorY);

            if(!l.isAtivo()){
                lasers.remove(i);
                i--;
            }
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

    public void morrer() {
        vivo = false;
    }
}
