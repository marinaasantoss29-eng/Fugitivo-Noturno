package MeuJogo.modelo;

import javax.sound.sampled.*;
import java.io.File;


public class Som {
    private Clip clip;

    public void tocarLoop(String caminho) {
        try {
            File arquivo = new File(caminho);
            AudioInputStream audio = AudioSystem.getAudioInputStream(arquivo);

            clip = AudioSystem.getClip();
            clip.open(audio);
            clip.loop(Clip.LOOP_CONTINUOUSLY);

        } catch (Exception e) {
            System.out.println("Erro ao tocar som: " + e.getMessage());
        }
    }

    public void parar() {
        if (clip != null && clip.isRunning()) {
            clip.stop();
            clip.close();
        }
    }
}
