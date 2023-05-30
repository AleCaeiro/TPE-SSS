import java.util.Random;

public class RandomGF251 {
    public RandomGF251() {
    }

    public int generateRandom() {
        Random auxRandom = new Random();
        return auxRandom.nextInt(251);
    }
}
