package MeuJogo.modelo;

import java.awt.*;
import java.util.ArrayList;
import javax.swing.ImageIcon;

public class DroneInimigo {

    private int x, y;
    private int velocidade = 2;
    private int dy = 0;

    private final int largura= 120;
    private final int altura = 90;

    private boolean invisivel = false;
    private int tempoCamuflagem = 0;

    private Image imagem;
    private Image imagemRastro;

    private static final int PATRULHA=0;
    private static final int ATAQUE=1;
    private static final int ESPERA=2;
    private static final int DISTANCIA_MINIMA = 180;

    private int estado= PATRULHA;

    private int alturaFixa;
    private int direcao = 1;

    private int tempoAtaque=0;
    private int tempoEspera=0;

    private boolean vivo = true;

    private boolean caindo = false;
    private int velocidadeQueda = 5;

    private ArrayList<LaserGuiado> lasers = new ArrayList<>();

    public DroneInimigo(int x, int y) {
        this.x = x;
        this.y = y;
        this.alturaFixa=y;

        imagem = new ImageIcon("res/drones01.png").getImage();
        imagemRastro = new ImageIcon("res/drones_rastro.png").getImage();
    }
    public void update(int jogadorX, int jogadorY){
        if(!vivo){
            dy += 1;
            y += dy;
            return;
        }

        switch (estado){
            case PATRULHA:

                x += velocidade * direcao;

                if(x < 300 || x > 3000){
                direcao *= -1;
                }
                y = alturaFixa;

                if(Math.abs(jogadorX - x) < 200 && jogadorY > y){
                    estado = ATAQUE;
                    tempoAtaque = 0;
                }
                break;

            case ATAQUE:
                tempoAtaque++;

                if(tempoAtaque % 25 == 0){
                    lasers.add(new LaserGuiado(x,y+20, jogadorX,jogadorY));
                }

                if(tempoAtaque > 120){
                    estado = ESPERA;
                    tempoEspera = 0;
                }
                break;
            case ESPERA:
                tempoEspera++;

                x += velocidade * direcao;
                y = alturaFixa;

                if(tempoEspera > 180){
                    estado = PATRULHA;
                }
                break;

        }
        for(int i = 0; i < lasers.size(); i++){
            lasers.get(i).update();
        }
    }
    public ArrayList<LaserGuiado> getLasers(){
        return lasers;
    }
    public boolean estaMuitoPerto(DroneInimigo outro){
        return Math.abs(this.x - outro.x) < DISTANCIA_MINIMA;
    }

    public boolean isVivo(){
        return vivo;
    }
    public void morrer(){
        vivo = false;
        dy = 2;
    }
    public Rectangle getBounds(){
        return new Rectangle(x, y, largura, altura);
    }

    public void draw(Graphics g){
        if(!vivo) return;

        if(invisivel){
            g.drawImage(imagemRastro, x , y, largura, altura,null);
        }else{
            g.drawImage(imagem, x , y, largura, altura, null);
            for(LaserGuiado l: lasers){
                l.draw(g);
            }
        }
    }
}
