package MeuJogo.modelo;

import javax.sound.sampled.*;
import java.io.File;

public class Som {

    private Clip clip;

    // Caminho FIXO da música
    private final String CAMINHO_MUSICA = "res/musica_fase.wav";

    public Som() {
        tocarMusica();
    }

    private void tocarMusica() {
        try {
            File arquivo = new File(CAMINHO_MUSICA);

            if (!arquivo.exists()) {
                System.out.println("Arquivo de som não encontrado: " + CAMINHO_MUSICA);
                return;
            }

            AudioInputStream audio = AudioSystem.getAudioInputStream(arquivo);

            clip = AudioSystem.getClip();
            clip.open(audio);

            clip.setFramePosition(0);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
            clip.start();

        } catch (Exception e) {
            System.out.println("Erro ao tocar música: " + e.getMessage());
        }
    }

    public void parar() {
        if (clip != null) {
            clip.stop();
            clip.close();
        }
    }
}
