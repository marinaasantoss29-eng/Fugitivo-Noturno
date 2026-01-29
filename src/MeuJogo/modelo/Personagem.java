package MeuJogo.modelo;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import java.util.ArrayList;

public class Personagem {

    private int x, y;
    private int largura = 50;
    private int altura = 80;
    private int dx = 0, dy = 0;
    private int velocidade = 5;

    private Image parado, pulando, imagem;
    private Image[] andando;

    private boolean olhandoDireita = true;

    private boolean invencivel = false;
    private long tempoInvencivel = 0;

    private int frame = 0;
    private int contadorAnimacao = 0;

    private final int LARGURA = 80;
    private final int ALTURA = 80;

    private boolean noChao = false;
    private boolean morto = false;

    private int tempoCoyote = 0;
    private static final int COYOTE_TIME = 8;

    private int bufferPulo = 0;
    private static final int PULO_BUFFER = 6;

    // DASH
    private boolean podeDash = true;
    private boolean emDash = false;
    private int dashVelocidade = 18;
    private int dashTempo = 0;
    private final int DASH_DURACAO = 10;

    // ESCUDO
    private boolean escudoAtivo = false;
    private int tempoEscudo = 0;
    private final int DURACAO_ESCUDO = 180;

    // TIRO
    private boolean carregandoTiro = false;
    private int cargaTiro = 0;
    private final int CARGA_MAX = 40;

    // MOEDAS
    private int moedas = 0;
    private boolean levarDano = false;
    private int tempoDano = 0;
    private final int DURACAO_DANO = 30;

    private int vidas= 5;
    private boolean vivo = true;

    private int pulosRestantes = 2;
    private static final int MAX_PULOS = 2;
    private boolean podePular = true;

    private ArrayList<Tiro> tiros = new ArrayList<>();

    private boolean camuflado = false;
    private int tempoCamuflagem = 0;
    private final int DURACAO_CAMUFLAGEM = 180;

    public Personagem(int x, int y) {
        this.x = x;
        this.y = y;

        this.moedas = 0;
        this.vidas = 5;

        load();
    }



    private void load() {
        parado = new ImageIcon("res/parado.png").getImage();
        pulando = new ImageIcon("res/pulo.png").getImage();
        andando = new Image[2];
        andando[0] = new ImageIcon("res/andar1.png").getImage();
        andando[1] = new ImageIcon("res/andar2.png").getImage();
        imagem = parado;
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, LARGURA, ALTURA);
    }

    // ================= UPDATE =================
    public void update(ArrayList<Plataforma> plataformas) {

        if (morto) return;

        if (!noChao) dy += 1;
        if (dy > 12) dy = 12;

        if (emDash) {
            dy = 0;
            x += olhandoDireita ? dashVelocidade : -dashVelocidade;
            dashTempo--;
            if (dashTempo <= 0) emDash = false;
        }

        x += dx * velocidade;
        y += dy;

        noChao = false;

        for (Plataforma p : plataformas) {

            Rectangle pb = p.getBounds();
            Rectangle pj = getBounds();


            if (pj.intersects(pb) && dy >= 0) {

                if (pj.y + pj.height - dy <= pb.y) {

                    y = pb.y - ALTURA;
                    dy = 0;
                    noChao = true;

                    pulosRestantes = MAX_PULOS;
                    podePular = true;
                }
            }
        }

        if (noChao) {
            tempoCoyote = COYOTE_TIME;
            podeDash = true;
        } else if (tempoCoyote > 0) {
            tempoCoyote--;
        }

        if (bufferPulo > 0) bufferPulo--;


        if (dy != 0) {
            imagem = pulando;

        }
        else if (dx != 0) {
            contadorAnimacao++;

            if(contadorAnimacao >= 10){
                frame = (frame + 1) % andando.length;
                contadorAnimacao = 0;
            }
            imagem = andando[frame];
        }
        else{
            imagem = parado;
            frame = 0;
        }


        if (escudoAtivo && --tempoEscudo <= 0) escudoAtivo = false;

        carregarTiro();

        if(invencivel && System.currentTimeMillis() - tempoInvencivel > 1000){
            invencivel = false;
        }
        if(levarDano && --tempoDano <= 0){
            levarDano = false;
        }
        if(camuflado){
            tempoCamuflagem--;
            velocidade = 2;

            if(tempoCamuflagem <= 0){
                camuflado = false;
                velocidade = 5;
            }
        }
    }

    public void draw(Graphics2D g2) {
        if(camuflado){
            g2.setComposite(AlphaComposite.getInstance(
                    AlphaComposite.SRC_OVER, 0.4f
            ));
        }
        
        if (imagem == null) return;

        if(levarDano && tempoDano % 6 < 3) return;

        if (olhandoDireita)
            g2.drawImage(imagem, x, y, LARGURA, ALTURA, null);
        else
            g2.drawImage(imagem, x + LARGURA, y, -LARGURA, ALTURA, null);
    }

    public void atirar(){
        int direcao = olhandoDireita ? 1: -1;

        tiros.add(new Tiro(
                x + (olhandoDireita ? LARGURA : 0),
                y + ALTURA / 2,
                olhandoDireita,
                cargaTiro
        ));
        cargaTiro = 0;
        carregandoTiro = false;
    }

    public void pular() {
        if (pulosRestantes > 0 && podePular) {
            dy = -18;
            pulosRestantes--;
            noChao = false;
            podePular = false;
        }
    }


    public void atualizarTiros(){

        for(int i = 0; i < tiros.size(); i++){
            tiros.get(i).update();

            if(!tiros.get(i).ativo()){
                tiros.remove(i);
                i--;
            }
        }
    }
    public void ativarCamuflagem(){
        if(!camuflado){
            camuflado = true;
            tempoCamuflagem = DURACAO_CAMUFLAGEM;
        }
    }
    public boolean estaCamuflado(){
        return camuflado;
    }

    public void desenharTiros(Graphics2D g2){
        for(Tiro t : tiros){
            t.draw(g2);
        }
    }

    public ArrayList<Tiro> getTiros(){
        return tiros;
    }

    // ================= TECLADO =================
    public void keyPressed(KeyEvent e) {

        int k = e.getKeyCode();


        if (k == KeyEvent.VK_LEFT) { dx = -1; olhandoDireita = false; }
        if (k == KeyEvent.VK_RIGHT) { dx = 1; olhandoDireita = true; }
        if (k == KeyEvent.VK_UP) { pular();}
        if (k == KeyEvent.VK_SHIFT && podeDash && !emDash) {
            emDash = true;
            podeDash = false;
            dashTempo = DASH_DURACAO;
        }
        if (k == KeyEvent.VK_E) ativarEscudo();
        if (k == KeyEvent.VK_SPACE) iniciarCarga();
        if (k == KeyEvent.VK_C) ativarCamuflagem();


    }

    public void keyReleased(KeyEvent e) {

        int k = e.getKeyCode();


        if (k == KeyEvent.VK_LEFT || k == KeyEvent.VK_RIGHT) {
            dx = 0;
        }


        if (k == KeyEvent.VK_UP) {
            podePular = true;
        }


        if (k == KeyEvent.VK_SPACE) {
            atirar();
        }
    }


    public void ativarEscudo() {
        if (!escudoAtivo) {
            escudoAtivo = true;
            tempoEscudo = DURACAO_ESCUDO;
        }
    }

    public void iniciarCarga() { carregandoTiro = true; }
    public void carregarTiro() { if (carregandoTiro && cargaTiro < CARGA_MAX) cargaTiro++; }

    public void adicionarMoeda() {

        moedas++;
    }
    public int getMoedas() {

        return moedas;
    }
    public int getX() {

        return x;
    }
    public int getY() {

        return y;
    }
    public int getLargura() {

        return LARGURA;
    }

    public void tomarDano() {
        if(invencivel || !vivo) return;
        vidas--;

        levarDano = true;
        tempoDano = DURACAO_DANO;

        invencivel = true;
        tempoInvencivel = System.currentTimeMillis();

        if(vidas <= 0){
            vivo = false;
            morto = true;
        }
    }

    public Rectangle getPe(){
        return new Rectangle(
                x + 10,
                y + LARGURA - 5,
                LARGURA - 20,
                5
        );
    }

    public void setPosicao(int i, int i1) {
        this.x = x;
        this.y= y;
    }

    public void setDx(int i) {
        this.dx = dx;
    }
    public void perderVida(){
        if(!vivo) return;

        vidas --;

        if(vidas <= 0){
            vivo = false;
        }
    }

    public boolean estaVivo(){
        return vivo;
    }
    public int getVidas(){

        return vidas;
    }
    public void setX(int x) {

        this.x = x;
    }
    public void setY(int y) {

        this.y = y;
    }
    public void setVidas(int v) {

        this.vidas = v;
    }

    public int getLARGURA() {
        return LARGURA;
    }

    public int getALTURA() {
        return ALTURA;
    }

}
