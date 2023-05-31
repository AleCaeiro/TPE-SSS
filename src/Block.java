import java.util.ArrayList;
import java.util.List;

public class Block {
    private List<Integer> pixels;
    private Polynomial f;
    private Polynomial g;

    public Block(List<Integer> pixels, int degree) {
        this.pixels = pixels;
        //todo: hablar esto: en una sublista no incluye el extremo derecho
        this.f = new Polynomial(pixels.subList(0, degree+1));
        this.g = new Polynomial(calculateG(this.f, pixels.subList(degree + 1, pixels.size())));
    }

    private List<Integer> calculateG(Polynomial f, List<Integer> restG) {
        RandomGF251 randInstance = new RandomGF251();
        int ri = randInstance.generateRandom();

        while (ri == 0){
            ri = randInstance.generateRandom();
        }

        Integer b0 = calculateEquation(ri, f.getCoefficient(0));
        Integer b1 = calculateEquation(ri, f.getCoefficient(1));

        List<Integer> aux = new ArrayList<>();
        aux.add(b0);
        aux.add(b1);
        aux.addAll(restG);

        return aux;
    }

    private Integer calculateEquation(int r, int a) {
        int b = (-(r * a)) % 251;
        if (b < 0) {
            b += 251;
        }
        return b;
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
}
