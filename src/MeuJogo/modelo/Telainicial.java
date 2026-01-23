package MeuJogo.modelo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Telainicial extends JPanel {

    private Image fundo;
    private Runnable iniciarJogo;

    public Telainicial(Runnable iniciarJogo) {
        this.iniciarJogo = iniciarJogo;

        setPreferredSize(new Dimension(900, 400));
        setLayout(null);

        fundo = new ImageIcon("res/telainicio.png").getImage();
        
        int btnLargura = 220;
        int btnAltura = 80;

        ImageIcon iconOriginal = new ImageIcon("res/botaojogo01.png");
        Image img = iconOriginal.getImage()
                        .getScaledInstance(btnLargura, btnAltura, Image.SCALE_SMOOTH);
        ImageIcon iconRedimensionado = new ImageIcon(img);
        
        JButton btnJogar = new JButton(iconRedimensionado);
        btnJogar.setBounds(300, 220, 200, 50);

        btnJogar.setBorderPainted(false);
        btnJogar.setContentAreaFilled(false);
        btnJogar.setFocusPainted(false);

        btnJogar.addActionListener(e -> iniciarJogo.run());

        add(btnJogar);
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.drawImage(fundo, 0, 0, getWidth(), getHeight(), null);

    }
}
