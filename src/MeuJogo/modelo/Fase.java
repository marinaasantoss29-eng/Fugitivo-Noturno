package MeuJogo.modelo;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

public class Fase extends JPanel implements ActionListener {

    public static final int LARGURA = 900;
    public static final int ALTURA = 400;

    private Image fundo;
    private Personagem jogador;

    private ArrayList<Plataforma> plataformas;
    private ArrayList<Moedas> moedas;
    private ArrayList<ResiduoToxico> residuoToxicos;
    private ArrayList<Inimigo> inimigos;
    private ArrayList<DroneInimigo> drones;
    private InimigoFinal inimigoFinal;

    private ArrayList<Rectangle> blocosChao;

    private Timer timer;
    private int cameraX = 0;

    private final int ALTURA_CHAO = 40;
    private final int TAM_MAPA = 4200;

    private boolean gameOver = false;

    private JButton btnReiniciar;

    private int proximoChaoX = 0;
    private int tamanhoBlocoChao = 300;
    private int tamanhoBuraco = 180;

    private int proximaPlataformaX = 900;
    private int distanciaMinima = 600; // distância horizontal mínima
    private int alturaMinima = 130;
    private int alturaMaxima = 260;

    private static final int MAX_DRONES_ATIVOS = 2;
    private static final int DISTANCIA_MINIMA_DR0NES = 200;


    private boolean vitoria = false;

    public Fase() {
        setPreferredSize(new Dimension(LARGURA, ALTURA));
        setFocusable(true);
        setLayout(null);

        ImageIcon iconOriginal = new ImageIcon("res/botaoreinicia01.png");

        int btnLargura = 350;
        int btnAltura = 105;

        Image img = iconOriginal.getImage()
                .getScaledInstance(btnLargura, btnAltura, Image.SCALE_SMOOTH);


        btnReiniciar = new JButton(new ImageIcon(img));

        btnReiniciar.setBorderPainted(false);
        btnReiniciar.setContentAreaFilled(false);
        btnReiniciar.setFocusPainted(false);
        btnReiniciar.setVisible(false);

        btnReiniciar.addActionListener(e -> btnReiniciarJogo());

        add(btnReiniciar);


        fundo = new ImageIcon("res/cenario01.png").getImage();


        plataformas = new ArrayList<>();
        moedas = new ArrayList<>();
        residuoToxicos = new ArrayList<>();
        inimigos = new ArrayList<>();
        drones = new ArrayList<>();

        blocosChao = new ArrayList<>();

        jogador = new Personagem(100, ALTURA - ALTURA_CHAO - 80);

        criarCenario();

        addKeyListener(new Teclado());

        timer = new Timer(16, this);
        timer.start();
    }

    private void btnReiniciarJogo() {
        gameOver = false;

        jogador = new Personagem(100, ALTURA - ALTURA_CHAO - 80);

        cameraX = 0;

        plataformas.clear();
        moedas.clear();
        residuoToxicos.clear();
        inimigos.clear();


        // Reinicia o gerador de chão
        proximoChaoX = 0;
        proximaPlataformaX = 500;

        criarCenario();

        jogador = new Personagem(100, ALTURA - ALTURA_CHAO - 80);

        criarCenario(); // recria tudo novamente

        btnReiniciar.setVisible(false);

        timer.start();
        repaint();
    }

    private void criarCenario() {

        plataformas.clear();
        moedas.clear();
        residuoToxicos.clear();
        inimigos.clear();
        drones.clear();
        blocosChao.clear();

        proximoChaoX = 0;
        gerarChaoComBuracos();


        criarPlataforma(450, 260);
        criarPlataforma(850, 200);
        criarPlataforma(1200, 260);

        criarPlataforma(1750, 250);
        criarPlataforma(2150, 190);
        criarPlataforma(2550, 250);

        criarPlataforma(3050, 260);
        criarPlataforma(3450, 200);
        criarPlataforma(3850, 260);


        Plataforma p1 = new Plataforma(1400, 180, 160, 20, 1300, 1700, 2);
        Plataforma p2 = new Plataforma(2900, 180, 160, 20, 2800, 3300, -2);


        plataformas.add(p1);
        plataformas.add(p2);

        moedasEmCima(p1);
        moedasEmCima(p2);


        residuoToxicos.add(new ResiduoToxico(900, ALTURA - ALTURA_CHAO));
        residuoToxicos.add(new ResiduoToxico(2100, ALTURA - ALTURA_CHAO));
        residuoToxicos.add(new ResiduoToxico(3300, ALTURA - ALTURA_CHAO));


        inimigos.add(new Inimigo(1000, 280, 900, 1150));
        inimigos.add(new Inimigo(2600, 280, 2500, 2900));


        drones.add(new DroneInimigo(600, 120));
        drones.add(new DroneInimigo(2300, 130));


        // Cria primeiro com Y temporário
        int xFinal = 0;
        inimigoFinal = new InimigoFinal(xFinal, 0);

        xFinal = TAM_MAPA - 200;

        // posição exata em cima do chão
        int yFinal = ALTURA - ALTURA_CHAO - 160;

        inimigoFinal = new InimigoFinal(xFinal, yFinal);

        inimigoFinal.setLimites(xFinal - 200, xFinal + 200);

    }

    private void criarPlataforma(int x, int y) {
        Plataforma p = new Plataforma(x, y, 160, 20);
        plataformas.add(p);
        moedasEmCima(p);
    }


    private void moedasEmCima(Plataforma p) {
        for (int x = 20; x < p.getLargura() - 20; x += 40) {
            moedas.add(new Moedas(x, -35, p));
        }
    }

    private void criarResiduoSomenteNoChao(){
        for(Rectangle chao: blocosChao){

            if(Math.random() < 0.15){
                residuoToxicos.add(
                        new ResiduoToxico(
                                chao.x + chao.width / 2 - 15,
                                chao.y - 20
                        )
                );
            }
        }
    }

    private void gerarChaoComBuracos() {

        int limite = jogador.getX() + LARGURA;

        while (proximoChaoX < limite) {


            Plataforma bloco= new Plataforma(
                    proximoChaoX,
                    ALTURA - ALTURA_CHAO,
                    tamanhoBlocoChao,
                    ALTURA_CHAO
            );
            plataformas.add(bloco);

            blocosChao.add(new Rectangle(
                    proximoChaoX,
                    ALTURA - ALTURA_CHAO,
                    tamanhoBlocoChao,
                    ALTURA_CHAO
            ));

            proximoChaoX += tamanhoBlocoChao;


            if (Math.random() < 0.45) {
                proximoChaoX += tamanhoBuraco;
            }
        }
    }

    private int ultimaAlturaPlataforma = ALTURA - ALTURA_CHAO - 120;

    private void gerarPlataformasInfinitas() {

        int limite = jogador.getX() + LARGURA;

        while (proximaPlataformaX < limite && plataformas.size() < 25) {

            int novaAltura;
            int tentativas = 0;

            do {
                novaAltura = alturaMinima + (int)(Math.random() * (alturaMaxima - alturaMinima));
                tentativas++;
            }
            while (Math.abs(novaAltura - ultimaAlturaPlataforma) < 100 && tentativas < 10);
            // garante diferença de altura


            Plataforma p = new Plataforma(
                    proximaPlataformaX,
                    novaAltura,
                    160,
                    20
            );

            plataformas.add(p);
            moedasEmCima(p); // mantém suas moedas funcionando

            ultimaAlturaPlataforma = novaAltura;

            proximaPlataformaX += distanciaMinima + 200 + (int)(Math.random() * 200);
        }
    }



    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        if(fundo == null) return;

        int larguraFundo = fundo.getWidth(null);

        // Fundo infinito acompanhando a câmera
        int inicio = (cameraX / larguraFundo) * larguraFundo;

        // Fundo
        for (int x = inicio - larguraFundo; x < cameraX + LARGURA + larguraFundo; x += larguraFundo) {
            g2.drawImage(fundo, x - cameraX, 0, null);
        }

        g2.translate(-cameraX, 0);


        for (Plataforma p : plataformas) p.draw(g2);
        for(ResiduoToxico r : residuoToxicos) r.draw(g2);
        for (Moedas m : moedas) m.draw(g2);
        for(Inimigo i : inimigos){ i.desenhar(g2);}
        for(DroneInimigo d: drones){ d.draw(g2); }

        if (inimigoFinal != null && inimigoFinal.isVivo()){
            inimigoFinal.draw(g2);
            inimigoFinal.drawLasers(g2);
        }


        jogador.draw(g2);
        jogador.desenharTiros(g2);

        g2.translate(cameraX, 0);

        // HUD
        g2.setFont(new Font("Arial", Font.BOLD, 22));
        g2.setColor(Color.WHITE);
        g2.drawString("Vidas: " + jogador.getVidas(), 20, 40);
        g2.drawString("Moedas: " + jogador.getMoedas(), 20, 70);

        // ===== GAME OVER MINIMALISTA =====
        if (gameOver) {

            // Fundo escurecido
            g2.setColor(new Color(0, 0, 0, 170));
            g2.fillRect(0, 0, LARGURA, ALTURA);

            // Texto principal
            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Arial", Font.BOLD, 42));
            String texto = "GAME OVER";

            FontMetrics fm = g2.getFontMetrics();
            int x = (LARGURA - fm.stringWidth(texto)) / 2;
            int y = ALTURA / 2 - 20;

            g2.drawString(texto, x, y);

        }

        if (vitoria) {
            g2.setColor(new Color(0,0,0,180));
            g2.fillRect(0,0,LARGURA,ALTURA);

            g2.setColor(Color.GREEN);
            g2.setFont(new Font("Arial", Font.BOLD, 40));
            g2.drawString("VOCÊ DERROTOU O CHEFÃO!", 180, 180);
        }


    }

    @Override
    public void actionPerformed(ActionEvent e) {

        jogador.update(plataformas);
        jogador.atualizarTiros();

        if(inimigoFinal != null && inimigoFinal.isVivo()){
            inimigoFinal.update(jogador);
        }


        if (jogador.getX() < 0) {
            jogador.setX(0);
        }

        if (jogador.getX() + jogador.getLargura() > TAM_MAPA) {
            jogador.setX(TAM_MAPA - jogador.getLargura());
        }

        for(DroneInimigo drone : drones){
            drone.update(jogador.getX(), jogador.getY());
        }

        for(int i = 0; i < jogador.getTiros().size(); i++){

            Tiro t = jogador.getTiros().get(i);

            for(int j = 0; j < inimigos.size(); j++){

                Inimigo inimigo = inimigos.get(j);

                if(t.getBounds().intersects(inimigo.getBounds())){

                    t.destruir();
                    inimigos.remove(j);
                    break;
                }
            }
        }

        gerarChaoComBuracos();


        // ===== MORTE DIRETA AO CAIR =====
        if (jogador.getY() > ALTURA) {
            jogador.setVidas(0);
        }

        if (jogador.getVidas() <= 0){
            gameOver = true;
            timer.stop();

            btnReiniciar.setBounds(
                    (LARGURA - 350) / 2,
                    ALTURA / 2 + 20,
                    350,
                    105
            );

            btnReiniciar.setVisible(true);
            repaint();
            return;
        }
        Rectangle pj = jogador.getBounds();

        for(int i = 0; i < jogador.getTiros().size(); i++){
            Tiro t = jogador.getTiros().get(i);

            for(int j = 0; j < inimigos.size(); j++){
                if(t.getBounds(). intersects(inimigos.get(j).getBounds())){
                    inimigos.remove(j);
                    t.destruir();
                    break;
                }
            }
            if(inimigoFinal != null && inimigoFinal.isVivo() &&
            t.getBounds().intersects(inimigoFinal.getBounds())){
                inimigoFinal.levarDano();
                t.destruir();
            }
        }
        for (Plataforma p : plataformas) {
            p.atualizar();
        }

        // Atualiza moedas (seguem a plataforma)
        for (Moedas m : moedas) {
            m.update();
        }

        for(ResiduoToxico r : residuoToxicos){
            if(r.Ativo() && pj.intersects(r.getBounds())){
                jogador.tomarDano();
                r.desativar();
            }

        }
        for(Inimigo i : inimigos){

            i.atualizar(jogador, cameraX, LARGURA);

            for(Laser l : i.getLasers()){
                l.update();

                if(l.ativo() && pj.intersects(l.getBounds())){
                    jogador.tomarDano();
                    l.desativar();
                }
            }

        }

        // Coleta moedas
        for (int i = 0; i < moedas.size(); i++) {
            if (!moedas.get(i).foiColetada() &&
                    pj.intersects(moedas.get(i).getBounds())) {
                jogador.adicionarMoeda();
                moedas.get(i).coletar();
            }
        }
        if(jogador.getVidas() <= 0){
            gameOver = true;
            timer.stop();

            btnReiniciar.setBounds(
                    (LARGURA - 350) / 2,
                    ALTURA / 2 + 20,
                    350,
                    105
            );

            btnReiniciar.setVisible(true);
            repaint();
            return;
        }
        for(DroneInimigo d: drones){
            d.update(jogador.getX(), jogador.getY());

            for(LaserGuiado l: d.getLasers()){
                if(l.isAtivo() && l.getBounds().intersects(jogador.getBounds())){
                    jogador.tomarDano();
                    l.desativar();
                }
            }
        }
        for (LaserGuiado l : inimigoFinal.getLasers()) {
            l.update();

            if (l.isAtivo() && l.getBounds().intersects(jogador.getBounds())) {
                jogador.tomarDano();
                l.desativar();
            }
        }

        // 🏆 VERIFICA SE O CHEFÃO MORREU
        if (inimigoFinal != null && !inimigoFinal.isVivo()) {
            vitoria = true;
            timer.stop(); // para o jogo quando vence
        }

        //Câmera
        cameraX = jogador.getX() - LARGURA / 2;

        // ===== TRAVA NO FIM DO MAPA =====
        int limiteCamera = TAM_MAPA - LARGURA;
        if (cameraX > limiteCamera) {
            cameraX = limiteCamera;
        }

        repaint();

    }


    private class Teclado extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            jogador.keyPressed(e);
        }

        @Override
        public void keyReleased(KeyEvent e) {
            jogador.keyReleased(e);
        }
    }
}
