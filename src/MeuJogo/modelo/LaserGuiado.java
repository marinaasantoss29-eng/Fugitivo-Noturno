package MeuJogo.modelo;

import java.awt.*;

public class LaserGuiado {

    private int x, y;
    private int dx, dy;
    private int velocidade = 3;
    private boolean ativo = true;

    public LaserGuiado(int x, int y, int alvoX, int alvoY) {
        this.x = x;
        this.y = y;

        dx = alvoX - x;
        dy = alvoY - y;

        int distancia = Math.max(1, Math.abs(dx) + Math.abs(dy));
        dx = dx * velocidade / distancia;
        dy = dy * velocidade / distancia;
    }

    public void update() {
        x += dx;
        y += dy;
    }

    public void draw(Graphics g) {
        g.setColor(Color.CYAN);
        g.fillRect(x, y, 6, 2);
    }

    public boolean isAtivo() {
        return ativo;
    }
    public void desativar(){
        ativo= false;
    }
    public Rectangle getBounds(){
        return new Rectangle(x, y, 6, 2);
    }
}
