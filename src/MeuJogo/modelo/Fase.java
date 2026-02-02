package MeuJogo.modelo;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.ArrayList;
import MeuJogo.modelo.Som;
import MeuJogo.modelo.Baudinheiro;

public class Fase extends JPanel implements ActionListener {

    public static final int LARGURA = 900;
    public static final int ALTURA = 400;
    private static final int RECOMPENSA_BAU = 10;

    protected Image fundo;
    protected Personagem jogador;

    protected ArrayList<Plataforma> plataformas;
    protected ArrayList<Moedas> moedas;
    protected ArrayList<ResiduoToxico> residuoToxicos;
    protected ArrayList<Inimigo> inimigos;
    protected ArrayList<Vigia> vigias;
    protected InimigoFinal inimigoFinal;
    protected Baudinheiro bauChefao;

    private ArrayList<MoedasAnimadas> moedasAnimadas;
    private ArrayList<Rectangle> blocosChao;

    private Timer timer;
    private int cameraX = 0;

    private final int ALTURA_CHAO = 40;
    protected int TAM_MAPA = 5500;

    private boolean gameOver = false;
    private boolean vitoria = false;
    private int faseAtual = 1;

    private JButton btnReiniciar;

    protected int proximoChaoX = 0;
    private int tamanhoBlocoChao = 300;
    private int tamanhoBuraco = 180;

    protected Janela janela;

    private int proximaPlataformaX = 900;
    private int distanciaMinima = 600; // distância horizontal mínima
    private int alturaMinima = 130;
    private int alturaMaxima = 260;

    private static final int MAX_DRONES_ATIVOS = 2;
    private static final int DISTANCIA_MINIMA_DR0NES = 300;
    private static final int ALTURA_DRONE = 150;

    private Som musicaFundo;

    private boolean exibindoTesouro = false;
    private int timerTesouro = 0;
    private final int DURACAO_EXIBICAO = 120;
    private Image imgTesouroGrande;


    public Fase(Janela janela) {
        this.janela = janela;

        setPreferredSize(new Dimension(LARGURA, ALTURA));
        setFocusable(true);
        setLayout(null);

        ImageIcon iconOriginal = new ImageIcon("res/botaoreinicia01.png");
        imgTesouroGrande = new ImageIcon("res/bau_aberto_grande.png").getImage();
        musicaFundo = new Som();
        moedasAnimadas = new ArrayList<>();


        int btnLargura = 350;
        int btnAltura = 105;

        Image img = iconOriginal.getImage().getScaledInstance(btnLargura, btnAltura, Image.SCALE_SMOOTH);
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
        blocosChao = new ArrayList<>();

        jogador = new Personagem(100, ALTURA - ALTURA_CHAO - 80);

        criarCenario();

        addKeyListener(new Teclado());
        timer = new Timer(16, this);
        timer.start();
    }


    public void encerrarFase() {
        musicaFundo.parar();
    }

    private void btnReiniciarJogo() {


        if (musicaFundo != null) {
            musicaFundo.parar();
        }

        // CRIA UMA NOVA INSTÂNCIA (isso reinicia o som)
        musicaFundo = new Som();

        gameOver = false;
        vitoria = false;
        boolean chefaoAlertado = false;

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

    protected void criarCenario() {

        plataformas.clear();
        moedas.clear();
        residuoToxicos.clear();
        inimigos.clear();
        vigias = new ArrayList<>();
        blocosChao.clear();


            // Adicione os vigias em pontos estratégicos (x, y, raio de patrulha)
            vigias.add(new Vigia(600, 100, 200));  // Perto da primeira plataforma
            vigias.add(new Vigia(1500, 150, 300)); // Voando sobre um buraco
            vigias.add(new Vigia(2800, 80, 150));  // Bem alto no cenário

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

            // Cria primeiro com Y temporário
            int xFinal = 4000;
            int chaoFinal = ALTURA - ALTURA_CHAO - 80;

            this.inimigoFinal = new InimigoFinal(xFinal, chaoFinal);
            this.inimigoFinal.setLimites(xFinal - 300, xFinal + 300);

            bauChefao = new Baudinheiro(xFinal + 200, chaoFinal - 20);

            // Forçamos o fim do mapa um pouco depois do baú para criar o buraco
            TAM_MAPA = xFinal + 400;

    }

    private void proximaFase() {
        faseAtual++;

        this.faseAtual = 2;
        this.criarCenario();

        jogador.setX(100);
        jogador.setY(ALTURA - ALTURA_CHAO - 80);
        cameraX = 0;
        criarCenario();
        repaint();
    }

    private void criarDronesControlados() {

        int xInicial = 400;

        for (int i = 0; i < MAX_DRONES_ATIVOS; i++) {

            int x = xInicial + i * DISTANCIA_MINIMA_DR0NES;


            int direcao = (i % 2 == 0) ? 1 : -1;

        }
    }



    protected void criarPlataforma(int x, int y) {
        Plataforma p = new Plataforma(x, y, 160, 20);
        plataformas.add(p);
        moedasEmCima(p);
    }


    protected void moedasEmCima(Plataforma p) {
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

    protected void gerarChaoComBuracos() {

        int limite = jogador.getX() + LARGURA;

        while (proximoChaoX < TAM_MAPA) {

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
        int inicio = (cameraX / larguraFundo) * larguraFundo;
        for (int x = inicio - larguraFundo; x < cameraX + LARGURA + larguraFundo; x += larguraFundo) {
            g2.drawImage(fundo, x - cameraX, 0, null);
        }

        g2.translate(-cameraX, 0);
        for (Plataforma p : plataformas) p.draw(g2);
        for(ResiduoToxico r : residuoToxicos) r.draw(g2);
        for (Moedas m : moedas) m.draw(g2);
        for(Inimigo i : inimigos){ i.desenhar(g2);}
        for (Vigia v : vigias) {
            v.desenhar(g2);
        }
        if (inimigoFinal != null && inimigoFinal.isVivo()){
            inimigoFinal.draw(g2);
            inimigoFinal.drawLasers(g2);
        }

        if (bauChefao != null) {
            bauChefao.draw(g2);
        }

        for (MoedasAnimadas m : moedasAnimadas) {
            m.draw(g2);
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
            g2.setColor(new Color(0, 0, 0, 180));
            g2.fillRect(0, 0, LARGURA, ALTURA);

            String texto = "VOCÊ VENCEU!";

            g2.setFont(new Font("Arial", Font.BOLD, 42));
            FontMetrics fm = g2.getFontMetrics();

            int x = (LARGURA - fm.stringWidth(texto)) / 2;
            int y = (ALTURA - fm.getHeight()) / 2 + fm.getAscent();

            g2.setColor(Color.WHITE);
            g2.drawString(texto, x, y);
        }
        if(exibindoTesouro){
            g2.setColor(new Color(0, 0, 0, 100));
            g2.fillRect(0, 0, LARGURA, ALTURA);

            int larguraImg = 200;
            int alturaImg = 200;
            int xCentrado = (LARGURA - larguraImg) / 2;
            int yCentrado = (ALTURA - alturaImg) / 2;

            g2.drawImage(imgTesouroGrande, xCentrado, yCentrado, larguraImg, alturaImg, null);

        }


    }

    @Override
    public void actionPerformed(ActionEvent e) {

        jogador.update(plataformas);
        jogador.atualizarTiros();

        // ATUALIZAÇÃO DOS VIGIAS
        for (int i = 0; i < vigias.size(); i++) {
            Vigia v = vigias.get(i);
            v.atualizar();

            ArrayList<Tiro> tiros = jogador.getTiros();
            for (int j = 0; j < tiros.size(); j++) {
                Tiro t = tiros.get(j);
                if (t.getBounds().intersects(v.getBounds())) {
                    vigias.remove(i);
                    t.destruir();
                    i--;
                    break;
                }
            }
        }

        if(inimigoFinal != null && inimigoFinal.isVivo()){
            jogador.bloquearComBoss(inimigoFinal);
            inimigoFinal.update(jogador);
        }

        // --- PROTEÇÃO 01: Verificamos se o baú existe antes de medir distância ---
        if(bauChefao != null) {
            if(inimigoFinal != null && inimigoFinal.isVivo()){
                int distanciaJogadorBau = Math.abs(jogador.getX() - bauChefao.getBounds().x);
                if(distanciaJogadorBau < 180){
                    inimigoFinal.iniciarDescida();
                }
            }
        }

        // Movimentação básica e limites
        if (jogador.getX() < 0) jogador.setX(0);
        if (jogador.getX() + jogador.getLargura() > TAM_MAPA) jogador.setX(TAM_MAPA - jogador.getLargura());

        // Colisões de tiro e inimigos
        Rectangle pj = jogador.getBounds();
        for(int i = 0; i < jogador.getTiros().size(); i++){
            Tiro t = jogador.getTiros().get(i);
            for(int j = 0; j < inimigos.size(); j++){
                if(t.getBounds().intersects(inimigos.get(j).getBounds())){
                    inimigos.remove(j);
                    t.destruir();
                    break;
                }
            }
            if(inimigoFinal != null && inimigoFinal.isVivo() && t.getBounds().intersects(inimigoFinal.getBounds())){
                inimigoFinal.levarDano();
                t.destruir();
            }
        }

        for (Plataforma p : plataformas) p.atualizar();
        for (Moedas m : moedas) m.update();

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

        // Coleta moedas normais
        for (int i = 0; i < moedas.size(); i++) {
            if (!moedas.get(i).foiColetada() && pj.intersects(moedas.get(i).getBounds())) {
                jogador.adicionarMoeda();
                moedas.get(i).coletar();
            }
        }

        // Morte do jogador
        if (jogador.getY() > ALTURA || jogador.getVidas() <= 0) {
            // Se ele caiu no buraco APÓS pegar o baú, ele vence!
            if (jogador.getY() > ALTURA && bauChefao == null && (inimigoFinal != null && !inimigoFinal.isVivo())) {
                vitoria = true; // Ativa a tela de vitória ou próxima fase
                musicaFundo.parar();
                timer.stop();
            } else {
                // Se morreu ou caiu sem completar o objetivo, é Game Over
                gameOver = true;
                musicaFundo.parar();
                timer.stop();
                btnReiniciar.setBounds((LARGURA - 350) / 2, ALTURA / 2 + 20, 350, 105);
                btnReiniciar.setVisible(true);
            }
            repaint();
            return;
        }

        // Lasers do Boss
        if(inimigoFinal != null && inimigoFinal.isVivo()){
            for (LaserGuiado l : inimigoFinal.getLasers()) {
                l.update(jogador.getX() + jogador.getLargura() / 2, jogador.getY() + jogador.getALTURA() / 2);
                if (l.isAtivo() && l.getBounds().intersects(pj)) {
                    jogador.tomarDano();
                    l.desativar();
                }
            }
        }

        // Ativação do Baú
        if (inimigoFinal != null && !inimigoFinal.isVivo() && bauChefao != null) {
            bauChefao.ativar();
        }

        cameraX = jogador.getX() - LARGURA / 2;
        int limiteCamera = TAM_MAPA - LARGURA;
        if (cameraX > limiteCamera) cameraX = limiteCamera;


        if (bauChefao != null && bauChefao.isAtivo()) {
            if (pj.intersects(bauChefao.getBounds())) {
                exibindoTesouro = true;
                timerTesouro = DURACAO_EXIBICAO;

                for (int i = 0; i < RECOMPENSA_BAU; i++) {
                    jogador.adicionarMoeda();
                    // Criamos a animação ANTES de matar o objeto bauChefao
                    moedasAnimadas.add(new MoedasAnimadas(
                            bauChefao.getBounds().x + 20 + (i * 10),
                            bauChefao.getBounds().y
                    ));
                }

                bauChefao = null; // Agora ele vira null com segurança
                janela.irParaFase2();


                // Em vez de apenas exibir o tesouro, chamamos a transição
                Timer transitionTimer = new Timer(2000, e1 -> proximaFase());
                transitionTimer.setRepeats(false);
                transitionTimer.start();
            }

        }

        if(exibindoTesouro){
            timerTesouro--;
            if(timerTesouro <= 0) exibindoTesouro = false;
        }

        // --- ATUALIZAÇÃO DAS MOEDAS (Isso faz elas subirem!) ---
        for (int i = 0; i < moedasAnimadas.size(); i++) {
            moedasAnimadas.get(i).update();
            if (moedasAnimadas.get(i).acabou()) {
                moedasAnimadas.remove(i);
                i--;
            }
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
