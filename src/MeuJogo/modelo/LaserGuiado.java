package MeuJogo.modelo;

import java.awt.*;

public class LaserGuiado {

    private double x, y;
    private double dx = 0, dy = 0;

    private final double velocidadeMax = 15.0;
    private final double aceleracao = 0.42;
    private final double raioVisao = 600;
    private final double anguloVisao = 120;

    private boolean trajetoriaDefinida = false;

    private boolean ativo = false;
    private double olharX = 1;
    private double olharY = 0;
    private double pulso = 0;


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

    private boolean jogadorNaFrente(int jogadorX, int jogadorY) {
        double vx = jogadorX - x;
        double vy = jogadorY - y;
        double distancia = Math.sqrt(vx * vx + vy * vy);
        if (distancia > raioVisao) return false;


        vx /= distancia;
        vy /= distancia;

        // Produto escalar (cos do ângulo)
        double dot = vx * olharX + vy * olharY;

        double limite = Math.cos(Math.toRadians(anguloVisao / 2));

        return dot >= limite;
    }
    public void definirTrajetoria(int alvoX, int alvoY){
        double dirX = alvoX - x;
        double dirY = alvoY - y;
        double dist = Math.sqrt(dirX * dirX + dirY * dirY);

        if(dist > 0){
            dx = (dirX / dist) * velocidadeMax;
            dy = (dirY / dist) * velocidadeMax;
        }
        trajetoriaDefinida = true;
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

    public void update(int jogadorX, int jogadorY) {
        if (!ativo) return;

        if(!trajetoriaDefinida){
            ajustarDirecao(jogadorX, jogadorY);
        }


        x += dx;
        y += dy;

        if(x < -500 || x > 6500 || y < -500 || y > 1500){
            ativo = false;
        }
    }

    public void draw(Graphics g2) {
        if (!ativo) return;
        g2.setColor(Color. RED);
        g2.fillRect((int ) x, (int) y, 18, 6);

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

        this.trajetoriaDefinida = false;

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
