import java.util.ArrayList;
import java.util.List;

public class Block {

    private final static int MOD = 251;
    private List<Integer> pixels;
    private Polynomial f;
    private Polynomial g;
    private int blockNum;

    private GF GF251;

    public Block(List<Integer> pixels, int degree, int blockNum) {
        this.GF251 = new GF(MOD);
        this.pixels = pixels;
        //sublist do not include the right end of parameter, because of that we need to add 1 in each one
        this.f = new Polynomial(pixels.subList(0, degree+1));
        this.g = new Polynomial(calculateG(this.f, pixels.subList(degree + 1, pixels.size())));
        this.blockNum = blockNum;
    }

    private List<Integer> calculateG(Polynomial f, List<Integer> restG) {
        int ri;

        do {
            ri = this.GF251.generateRandom();
        }while (ri == 0);

        Integer b0 = calculateEquation(ri, f.getCoefficient(0));
        Integer b1 = calculateEquation(ri, f.getCoefficient(1));

        List<Integer> aux = new ArrayList<>();
        aux.add(b0);
        aux.add(b1);
        aux.addAll(restG);

        return aux;
    }

    private Integer calculateEquation(int r, int a) {
        return this.GF251.transformToGF(-(r * a));
    }

    public List<Integer> getPixels() {
        return pixels;
    }

    public Polynomial getF() {
        return f;
    }

    public Polynomial getG() {
        return g;
    }

    public int getBlockNum() {
        return blockNum;
    }
}
