package MeuJogo.modelo;

import java.awt.*;

public class LaserGuiado {

    private double x, y;
    private double dx = 0, dy = 0;

    private final double velocidadeMax = 6.0;
    private final double aceleracao = 0.18;

    private final double raioVisao = 250;      // alcance
    private final double anguloVisao = 60;     // graus (campo frontal)

    private boolean ativo = false;

    // Direção que o boss está olhando (ex: direita)
    private double olharX = 1;
    private double olharY = 0;



    public LaserGuiado(int x, int y) {
        this.x = x;
        this.y = y;
        this.ativo = false;
    }
    public LaserGuiado(){
        this.x = 0;
        this.y = 0;
        this.ativo = false;
    }

    // Verifica se o jogador está NA FRENTE do boss
    private boolean jogadorNaFrente(int jogadorX, int jogadorY) {
        double vx = jogadorX - x;
        double vy = jogadorY - y;

        double distancia = Math.sqrt(vx * vx + vy * vy);
        if (distancia > raioVisao) return false;

        // Normaliza vetor jogador
        vx /= distancia;
        vy /= distancia;

        // Produto escalar (cos do ângulo)
        double dot = vx * olharX + vy * olharY;

        double limite = Math.cos(Math.toRadians(anguloVisao / 2));

        return dot >= limite;
    }

    private void ajustarDirecao(int alvoX, int alvoY) {
        double dirX = alvoX - x;
        double dirY = alvoY - y;

        double dist = Math.sqrt(dirX * dirX + dirY * dirY);
        if (dist == 0) return;

        dirX /= dist;
        dirY /= dist;

        dx += dirX * aceleracao;
        dy += dirY * aceleracao;

        double vel = Math.sqrt(dx * dx + dy * dy);
        if (vel > velocidadeMax) {
            dx = (dx / vel) * velocidadeMax;
            dy = (dy / vel) * velocidadeMax;
        }
    }

    // SÓ ATACA SE ESTIVER NA FRENTE
    public void update(int jogadorX, int jogadorY) {
        if (!ativo) return;

        if (!jogadorNaFrente(jogadorX, jogadorY)) {
            return; // boss não reage
        }

        ajustarDirecao(jogadorX, jogadorY);

        // Movimento normal
        x += dx;
        y += dy;

    }

    public void draw(Graphics g) {
        if (!ativo) return;

        Graphics2D g2 = (Graphics2D) g;

        g2.setColor(new Color(180, 0, 255, 80));
        g2.fillOval((int) x - 6, (int) y - 6, 20, 14);

        g2.setColor(new Color(150, 0, 220));
        g2.fillRect((int) x, (int) y, 8, 4);

        g2.setColor(Color.WHITE);
        g2.fillRect((int) x + 2, (int) y + 1, 4, 2);
    }

    public void setDirecaoOlhar(double dx, double dy) {
        double dist = Math.sqrt(dx * dx + dy * dy);
        if (dist == 0) return;

        olharX = dx / dist;
        olharY = dy / dist;
    }
    public void tentarAtivar(int xInicial, int yInicial, int jogadorX, int jogadorY) {
        if (ativo) return;

        this.x = xInicial;
        this.y = yInicial;

        dx = 0;
        dy = 0;

        if (!jogadorNaFrente(jogadorX, jogadorY)) return;

        ativo = true;
    }

    public Rectangle getBounds() {
        return new Rectangle((int) x, (int) y, 8, 4);
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void desativar() {
        ativo = false;
    }
}
