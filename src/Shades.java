import java.lang.reflect.AnnotatedType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Shades {
    private final static int MOD = 251;
    private final static GF GF251 = new GF(MOD);

    // Mapa clave valor donde la clave es el X donde se evaluo.
    // El valor es la imagen valuada en (f_x, g_x)
    // Me quedan ya los tres valores asociados en una sola estructura
    private final Map<Integer, Pair> evaluatedValues = new HashMap<>();

    /*This constructor is for distribution*/
    public Shades(Block block, int n) {
        for(int i = 1 ; i <= n ; i++) {
            evaluatedValues.put(i, new Pair(block.getF().evaluate(i), block.getG().evaluate(i)));
        }
    }

    /*This constructor is for recovery*/
    public Shades(List<Integer> x, List<Integer> f_x, List<Integer> g_x) {
        for (int i = 0; i < x.size(); i++) {
            evaluatedValues.put(x.get(i), new Pair(f_x.get(i), g_x.get(i)));
        }
    }

    private static class Pair {
        private Integer left;
        private Integer right;

        public Pair(Integer left, Integer right) {
            this.left = left;
            this.right = right;
        }

        public Integer getLeft() {
            return left;
        }

        public Integer getRight() {
            return right;
        }

        public void setLeft(Integer left) {
            this.left = left;
        }

        public void setRight(Integer right) {
            this.right = right;
        }

        @Override
        public boolean equals(Object obj) {
            if(this == obj) {
                return true;
            }

            if (!(obj instanceof Pair)) {
                return false;
            }
            Pair other = (Pair) obj;
            return this.left.equals(other.left) && this.right.equals(other.right);
        }
    }

    public List<Polynomial> applyLagrange(int k) {
        if(evaluatedValues.size() < k) {
            System.out.println("No es posible hallar la imagen con menos de k sombras");
            return null;
        }

        List<Polynomial> interpolatedPolynomials = new ArrayList<>();

        List<Pair> recoveredF = new ArrayList<>();
        List<Pair> recoveredG = new ArrayList<>();

        for (Map.Entry<Integer, Pair> entry : evaluatedValues.entrySet() ) {
            recoveredF.add(new Pair(entry.getKey(), entry.getValue().getLeft()));
            recoveredG.add(new Pair(entry.getKey(), entry.getValue().getRight()));
        }

        interpolatedPolynomials.add(lagrangeInterpolation(recoveredF));
        interpolatedPolynomials.add(lagrangeInterpolation(recoveredG));

        return interpolatedPolynomials;
    }

    private Polynomial lagrangeInterpolation(List<Pair> shades) {
        List<Integer> resultSi = new ArrayList<>();
        Integer result = 0;

        // TODO: revisar que pasa cuando tenemos más n de los k necesarios
        // Agregar otra condicion de corte para no tener un polinomio de grado n
        while (shades.size() > 0) {
            result = 0;
            for (int i = 0; i < shades.size(); i++) {
                result += (Li(shades.get(i), shades) * shades.get(i).getRight());
            }

            result = GF251.transformToGF(result);

            resultSi.add(result);
            shades.remove(shades.size() - 1);

            recalculateY(shades, result);
        }

        return new Polynomial(resultSi);
    }

    private void recalculateY(List<Pair> shades, int si) {
        for (Pair shade : shades) {
            Integer aux = ((shade.getRight() - si) * GF251.getInverse(shade.getLeft()));
            aux = GF251.transformToGF(aux);
            shade.setRight(aux);
        }
    }

    private int Li(Pair currentShade, List<Pair> recoveredShades) {
        int result = 1;
        for(int i = 0 ; i < recoveredShades.size() ; i++) {
            if(!recoveredShades.get(i).equals(currentShade)) {
                Integer denominator = ((currentShade.getLeft() - recoveredShades.get(i).getLeft()));

                denominator = GF251.transformToGF(denominator);

                result*= -recoveredShades.get(i).getLeft() * GF251.getInverse(denominator);
            }
        }

        result = GF251.transformToGF(result);

        return result;
    }
}
