import java.util.Random;

public class GF {

    private final int mod;
    public GF(int mod) {
        this.mod = mod;
    }

    public int generateRandom() {
        Random auxRandom = new Random();
        return auxRandom.nextInt(this.mod);
    }

    public Integer transformToGF(Integer num) {
        num = num % this.mod;

        if(num < 0) {
            num += this.mod;
        }

        return num;
    }
}
