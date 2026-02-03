package MeuJogo.modelo;

public class PlataformaMovel extends Plataforma {
    private int xInicial;
    private int limite;
    private int direcao = 1;
    private int velocidade = 2;

    public PlataformaMovel(int x, int y, int limite) {
        super(x, y, 160, 20);
        this.xInicial = y;
        this.limite = limite;
    }


    public void mexer() {
        this.y += direcao * velocidade;


        if (this.x > xInicial + limite || this.x < xInicial) {
            direcao *= -1;
        }
    }
}