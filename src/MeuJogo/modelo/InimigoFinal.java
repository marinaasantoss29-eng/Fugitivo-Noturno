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
    private boolean descendo = false;
    private boolean emCombateNoChao = false;
    private int yChao;

    private int xFixo;


    // ===== CHÃO =====
    private int chao;           // chão REAL da fase
    private int ajustePe = 0;



    // ===== CONSTRUTOR =====
    public InimigoFinal(int x, int chao) {
        this.x = x;
        this.chao = chao;
        this.yBase = chao - altura + 150;
        this.y = yBase;
        this.yChao = chao - altura + 20;

        // Sprites
        imagem = new ImageIcon("res/inimigofinal0.1.png").getImage();

    }

    // ===== UPDATE =====
    public void update(Personagem jogador) {
        if (!vivo) return;

        int distanciaX = Math.abs(jogador.getX() - this.x);

        boolean jogadorVisivel = distanciaX < 500;

        int centroBossX = x + largura / 2;
        int centroBossY = y + altura / 2;
        int centroJogadorX = jogador.getX() + jogador.getLargura() / 2;
        int centroJogadorY = jogador.getY() + jogador.getALTURA() / 2;
        int distancia = Math.abs(centroJogadorX - centroBossX);


        if (!descendo && !emCombateNoChao) {
            anguloFlutuacao += 0.08;
            y = yBase + (int) (Math.sin(anguloFlutuacao) * 25);

            x += velocidade * direcao;
            if (x < limiteEsquerdo) {
                direcao = 1;
                olhandoDireita = true;
            } else if (x + largura > limiteDireito) {
                direcao = -1;
                olhandoDireita = false;
            }

            boolean jogadorNaFrente = (olhandoDireita && centroJogadorX > centroBossX)
                    || (!olhandoDireita && centroJogadorX < centroBossX);

            if(distancia <= alcanceVisao && jogadorNaFrente){
                contadorTiro++;
                if(contadorTiro == 1 || (contadorTiro >= TEMPO_TIRO && lasers.size() < MAX_LASERS)){
                    dispararLaser(centroBossX, centroBossY, centroJogadorX,centroJogadorY);
                    contadorTiro = 2;
                }
            }else {
                contadorTiro = 0;
            }
        } else if (descendo) {
            if (y < yChao) {
                y += 15;
            } else {
                y = yChao;
                descendo = false;
                emCombateNoChao = true;
            }
        } else if (emCombateNoChao) {

            if (vida >= 4) {
                velocidade = 4;
            } else if (vida >= 2) {
                velocidade = 6;
            } else {
                velocidade = 9;
            }
            if(centroJogadorX < centroBossX) {
                olhandoDireita = false;
                direcao = -1;
            } else {
                olhandoDireita = true;
                direcao = 1;
            }

            x += velocidade * direcao;
            if (x < limiteEsquerdo) x = limiteEsquerdo;
            if (x + largura > limiteDireito) x = limiteDireito - largura;

            boolean jogadorNaFrente = (direcao == 1 && centroJogadorX > centroBossX || direcao == -1 && centroJogadorX < centroBossX);

            if(distancia <= alcanceVisao && jogadorNaFrente){
                contadorTiro++;
                if (contadorTiro == 1 || (contadorTiro >= TEMPO_TIRO && lasers.size() < MAX_LASERS)) {
                    dispararLaser(centroBossX, centroBossY, centroJogadorX, centroJogadorY);
                    contadorTiro = 2;
                }

            }else{
                contadorTiro = 0;
            }

        }
        for (int i = 0; i < lasers.size(); i++) {
            LaserGuiado l = lasers.get(i);
            l.update(centroJogadorX, centroJogadorY);

            if (!l.isAtivo()) {
                lasers.remove(i);
                i--;
            }
        }

        if (emCombateNoChao || descendo || distancia <= alcanceVisao) {
                olhandoDireita = (centroJogadorX > centroBossX);
        }

    }

    private void dispararLaser(int bx, int by, int jx, int jy) {
        for(int i = -1; i <= 1; i++){
            LaserGuiado laser = new LaserGuiado(bx, by);

            laser.setDirecaoOlhar(direcao, i * 0.3);

            laser.tentarAtivar(bx, by, jx, jy + (i * 45));
            lasers.add(laser);
        }
    }

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

    public void iniciarDescida(){
        descendo = true;
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
