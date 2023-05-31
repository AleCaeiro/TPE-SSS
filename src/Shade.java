import java.util.ArrayList;
import java.util.List;

public class Shade {
    List<Integer> shade;
    public Shade(Polynomial f, Polynomial g, int shadeNum) {
        List<Integer> aux = new ArrayList<>();
        aux.add(f.evaluate(shadeNum));
        aux.add(g.evaluate(shadeNum));

        this.shade = aux;
    }
}
