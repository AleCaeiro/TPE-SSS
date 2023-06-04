import java.util.ArrayList;
import java.util.List;

public class Block {

    private final List<Integer> pixels;
    private final Polynomial f;
    private final Polynomial g;
    private final int blockNum;
    private final static GF251 GF251 = new GF251();

    public Block(List<Integer> pixels, int degree, int blockNum) {
        this.pixels = pixels;
        this.f = new Polynomial(pixels.subList(0, degree + 1));
        this.g = calculateG(this.f, pixels.subList(degree + 1, pixels.size()));
        this.blockNum = blockNum;
    }

    private Polynomial calculateG(Polynomial f, List<Integer> restG) {
        int ri;

        do {
            ri = GF251.generateRandom();
        } while (ri == 0);

        Integer b0 = calculateEquation(ri, f.getCoefficient(0));
        Integer b1 = calculateEquation(ri, f.getCoefficient(1));

        List<Integer> aux = new ArrayList<>();
        aux.add(b0);
        aux.add(b1);
        aux.addAll(restG);

        return new Polynomial(aux);
    }

    private Integer calculateEquation(int r, int a) {
        // Check pixel isn't 0 or 251
        a = GF251.transformToGF(a) == 0 ? 1 : a;
        return GF251.transformToGF(-(r * a));
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
