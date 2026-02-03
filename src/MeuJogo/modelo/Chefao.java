package MeuJogo.modelo;

import javax.swing.ImageIcon;
import java.awt.Image;
import java.awt.Rectangle;

public class Chefao extends InimigoFinal {
    // Definindo variáveis locais para evitar erro de 'private access' na superclasse
    private int xLocal;
    private int yLocal;
    private int vida = 7;
    private Image imagemChefao;

    private int vidaMax = 7;
    private int vidaAtual = 7;

    private static final int LARGURA_BOSS = 200;
    private static final int ALTURA_BOSS = 200;

    private int contadorAtaque = 0;
    private boolean investindo = false;
    private int xInicial;

    public Chefao(int x, int y) {
        // Erro "Expected 2 arguments": InimigoFinal só aceita x e y
        super(x, y);

        this.xLocal = x;
        this.yLocal = y;
        this.xInicial = x;

        // Carregando a imagem corretamente
        ImageIcon referencia = new ImageIcon("res/inimigopoderoso.png");
        this.imagemChefao = referencia.getImage().getScaledInstance(LARGURA_BOSS, ALTURA_BOSS, Image.SCALE_SMOOTH);
    }

    // Removido @Override se o método não existe na superclasse
    public void mexer() {
        int velocidadeBase = 2;

        if (investindo) {
            this.xLocal -= 8;
            if (this.xLocal < (xInicial - 350)) investindo = false;
        } else {
            this.xLocal -= 1;
        }

        contadorAtaque++;
        if (contadorAtaque > 150) {
            if (Math.random() > 0.7) investindo = true;
            contadorAtaque = 0;
        }
    }

    @Override
    public Rectangle getBounds() {
        // Resolvendo o erro de 'private access' usando as variáveis locais da classe
        return new Rectangle(xLocal, yLocal, 200, 200);
    }

    // Métodos para o jogo conseguir ler a posição do Chefao sem erro de acesso
    public int getX() {
        return xLocal;
    }
    public int getY() {
        return yLocal;
    }

    public void perderVida() {
        this.vida--;
        System.out.println("Vida do Boss: " + vida);
    }

    public int getVida() {
        return vida;
    }

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