package MeuJogo.modelo;

import javax.swing.ImageIcon;
import java.awt.Image;
import java.awt.Rectangle;

public class Chefao extends InimigoFinal {


    private int vidaMax = 5;
    private int vidaAtual = 5;
    private boolean vivo = true;

    private Image imagemChefao;

    private static final int LARGURA_BOSS = 200;
    private static final int ALTURA_BOSS = 200;

    private int contadorAtaque = 0;
    private boolean investindo = false;
    private int xInicial;

    public Chefao(int x, int y) {

        super(x, y);
        this.xInicial = x;

        ImageIcon referencia = new ImageIcon("res/inimigopoderoso.png");
        imagemChefao = referencia.getImage().getScaledInstance(LARGURA_BOSS, ALTURA_BOSS, Image.SCALE_SMOOTH);
    }

        // ===== MOVIMENTO =====

        public void mexer() {
            if (!vivo) return;

            if (investindo) {
                setX(getX() - 8);
                if (getX() < (xInicial - 350)) investindo = false;
            } else {
                setX(getX() - 1);
            }

            contadorAtaque++;
            if (contadorAtaque > 150) {
                if (Math.random() > 0.7) investindo = true;
                contadorAtaque = 0;
            }
        }

        // ===== COLISÃO =====
        @Override
        public Rectangle getBounds() {
            return new Rectangle(getX(), getY(), LARGURA_BOSS, ALTURA_BOSS);
        }

        // ===== VIDA / DANO =====
        @Override
        public void receberDano(int dano) {
            if (!vivo) return;

            vidaAtual -= dano;
            System.out.println("Boss tomou dano! Vida: " + vidaAtual);

            if (vidaAtual <= 0) {
                morrer();
            }
        }

        @Override
        public boolean estaVivo() {
            return vivo;
        }

        // ===== GETS =====
        public Image getImagem() {
            return imagemChefao;
        }

        public int getVidaAtual() {
            return vidaAtual;
        }

        public int getVidaMax() {
            return vidaMax;
        }
    }

