import java.util.ArrayList;
import java.util.List;

public class Block {
    private final static int MOD = 251;
    private final static GF GF251 = new GF(MOD);

    private List<Integer> pixels;
    private Polynomial f;
    private Polynomial g;
    private int blockNum;

    public Block(List<Integer> pixels, int degree, int blockNum) {
        this.pixels = pixels;
        //sublist do not include the right end of parameter, because of that we need to add 1 in each one
        this.f = new Polynomial(pixels.subList(0, degree+1));
        this.g = calculateG(this.f, pixels.subList(degree + 1, pixels.size()));
        this.blockNum = blockNum;
    }

    private Polynomial calculateG(Polynomial f, List<Integer> restG) {
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

        return new Polynomial(aux);
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
