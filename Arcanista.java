import java.util.Random;

public class Arcanista extends Combatente {
    private int mana = 40;

    public Arcanista(String nome) {
        super(nome, 80, 80);
    }

    @Override
    public int atacar() {
        Random rand = new Random();
        
        if (this.mana >= 10) {
            this.mana -= 10;
            return 20 + rand.nextInt(10);
        } else {
            this.mana += 5;
            return 5;
        }
    }

    public int getMana() {
        return mana;
    }
}