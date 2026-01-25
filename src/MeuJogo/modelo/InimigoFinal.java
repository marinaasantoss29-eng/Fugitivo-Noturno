package MeuJogo.modelo;

import java.awt.*;
import java.util.ArrayList;
import javax.swing.ImageIcon;

public class InimigoFinal {

    private int x, y;
    private int largura =120;
    private int altura = 160;

    private int velocidade = 2;
    private int limiteEsq, limiteDir;
    private int vida = 5;


    private int jogadorX;
    private int jogadorY;


    private boolean vivo = true;

    private Image imagem;

    private int direcao = 1;
    private int limiteEsquerdo;
    private int limiteDireito;

    private ArrayList<LaserGuiado> lasers = new ArrayList<>();
    private int contadorTiro = 0;


    public InimigoFinal(int x, int y) {
        this.x = x;
        this.y = y;


        this.imagem = new ImageIcon("res/inimigoparado.png").getImage();

    }

    public void update(Personagem jogador) {

        // ===== FASES POR VIDA =====
        if (vida >= 4) {
            velocidade = 2;
        } else if (vida >= 2) {
            velocidade = 3;
        } else {
            velocidade = 5;
        }

        // ===== MOVIMENTO LADO A LADO =====
        x += velocidade * direcao;

        if (x <= limiteEsquerdo || x + largura >= limiteDireito) {
            direcao *= -1;
        }

        // ===== ATAQUE =====
        if (contadorTiro > 90) { // tempo entre tiros

            int origemX = x + largura / 2;
            int origemY = y + altura / 2;

            int alvoX = jogador.getX() + jogador.getLargura() / 2;
            int alvoY = jogador.getY() + jogador.getALTURA() / 2;

            // calcula direção
            double dx = alvoX - origemX;
            double dy = alvoY - origemY;

            double distancia = Math.sqrt(dx * dx + dy * dy);

            // normaliza e aplica velocidade
            double velocidadeTiro = 6;

            dx = (dx / distancia) * velocidadeTiro;
            dy = (dy / distancia) * velocidadeTiro;


            lasers.add(new LaserGuiado(x + largura/2, y + altura/2, jogadorX, jogadorY));


            contadorTiro = 0;
        }
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

    public Rectangle getBounds(){
        return new Rectangle(x, y, largura, altura);
    }


    public void morrer() {

        vivo = false;
    }

    public boolean isVivo() {

        return vivo;
    }

    public int getAltura() {
        return altura;
    }

    public void setY(int y) {
        this.y = y;
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
