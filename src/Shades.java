import java.util.ArrayList;
import java.util.List;

public class Shades {


    private final static int MOD = 11;

    private List<Pair> shades;
    private List<Pair> recoveredF;
    private List<Pair> recoveredG;
    private GF GF251;


    /*This constructor it's for distribute*/
    public Shades(Block block, int n) {
        List<Pair> shadesList = new ArrayList<>();

        for (int i = 1; i <= n; i++) {
            shadesList.add(new Pair(block.getF().evaluate(i), block.getG().evaluate(i)));
        }

        this.shades = shadesList;
    }

    /*This constructor it's for recover*/
    public Shades() {
        this.GF251 = new GF(MOD);
        this.recoveredF = new ArrayList<>();
        this.recoveredG = new ArrayList<>();
    }

    public List<Pair> getShades() {
        return shades;
    }

    public void addRecoveredValue(Integer portNum, Integer value, boolean bool) {
        if (bool) {
            this.recoveredF.add(new Pair(portNum, value));
        } else {
            this.recoveredG.add(new Pair(portNum, value));
        }
    }

    public List<Polynomial> applyLagrange(int k) {

        if (this.recoveredF.size() < k && this.recoveredG.size() < k) {
            System.out.println("No es posible hallar la imagen con menos de k sombras");
            return null;
        }

        List<Polynomial> recalculatedPolynomial = new ArrayList<>();

        recalculatedPolynomial.add(calculateLagrange(this.recoveredF));
        recalculatedPolynomial.add(calculateLagrange(this.recoveredG));

        //todo: Revisar que estemos guardando el orden de los coeficientes de 0 a n
        return recalculatedPolynomial;
    }

    private Polynomial calculateLagrange(List<Pair> shades) {
        List<Integer> resultSi = new ArrayList<>();
        Integer result;

        while (shades.size() > 0) {
            result = 0;
            for (int i = 0; i < shades.size(); i++) {
                result += (Li(shades.get(i), shades) * shades.get(i).getRight());
            }
            result = this.GF251.transformToGF(result);
            resultSi.add(result);
            shades.remove(shades.size() - 1);
            recalculateY(shades, result);
        }

        return new Polynomial(resultSi);
    }

    private void recalculateY(List<Pair> shades, int si) {
        for (Pair shade : shades) {
            Integer aux = (shade.getRight() - si) * this.GF251.getInvert(shade.getLeft() - 1);
            aux = this.GF251.transformToGF(aux);
            shade.setRight(aux);
        }
    }

    private int Li(Pair currentShade, List<Pair> recoveredShades) {
        int result = 1;
        for (Pair recoveredShade : recoveredShades) {
            if (!recoveredShade.equals(currentShade)) {
                Integer denominator = ((currentShade.getLeft() - recoveredShade.getLeft()));
                denominator = this.GF251.transformToGF(denominator);
                result *= -recoveredShade.getLeft() * this.GF251.getInvert(denominator - 1);
            }
        }

        return this.GF251.transformToGF(result);
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
            if (this == obj) {
                return true;
            }

            if (!(obj instanceof Pair)) {
                return false;
            }
            Pair other = (Pair) obj;
            return this.left.equals(other.left) && this.right.equals(other.right);
        }
    }

}
