import java.util.ArrayList;
import java.util.List;

public class Block {
    private List<Integer> pixels;
    private Polynomial f;
    private Polynomial g;

    public Block(List<Integer> pixels, int degree) {
        this.pixels = pixels;
        this.f = new Polynomial(pixels.subList(0, degree));
        this.g = new Polynomial(calculateG(this.f, pixels.subList(degree + 1, pixels.size()-1)));
    }

    private List<Integer> calculateG(Polynomial f, List<Integer> restG) {
        RandomGF251 randInstance = new RandomGF251();
        int ri = randInstance.generateRandom();

        while (ri == 0){
            ri = randInstance.generateRandom();
        }

        Integer b0 = calculateEquation(ri, f.getCoefficient(0));
        Integer b1 = calculateEquation(ri, f.getCoefficient(1));

        List<Integer> aux = new ArrayList<>(b0);
        aux.add(b1);
        aux.addAll(restG);

        return aux;
    }

    private Integer calculateEquation(int r, int a) {

    }
}
