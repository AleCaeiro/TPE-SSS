import java.util.ArrayList;
import java.util.List;

public class Shades {


    private final static int MOD = 11;

    private final static int [] INVERTS = {
            /*0, 1, 126, 84, 63, 201, 42, 36, 157, 28, 226, 137, 21, 58, 18, 67, 204,
            192, 14, 185, 113, 12, 194, 131, 136, 241, 29, 93, 9, 26, 159, 81, 102,
            213, 96, 208, 7, 95, 218, 103, 182, 49, 6, 216, 97, 106, 191, 235, 68,
            41, 246, 64, 140, 90, 172, 178, 130, 229, 13, 234, 205, 107, 166, 4, 51,
            112, 232, 15, 48, 211, 104, 99, 129, 196, 173, 164, 109, 163, 177, 197,
            91, 31, 150, 124, 3, 189, 108, 176, 174, 110, 53, 80, 221, 27, 243, 37,
            34, 44, 146, 71, 123, 169, 32, 39, 70, 153, 45, 61, 86, 76, 89, 199, 65,
            20, 240, 227, 132, 118, 117, 135, 228, 195, 179, 100, 83, 249, 2, 168,
            151, 72, 56, 23, 116, 134, 133, 119, 24, 11, 231, 186, 52, 162, 175, 165,
            190, 206, 98, 181, 212, 219, 82, 128, 180, 105, 207, 217, 214, 8, 224,
            30, 171, 198, 141, 77, 75, 143, 62, 248, 127, 101, 220, 160, 54, 74, 88,
            142, 87, 78, 55, 122, 152, 147, 40, 203, 236, 19, 139, 200, 247, 85, 144,
            46, 17, 238, 22, 121, 73, 79, 161, 111, 187, 5, 210, 183, 16, 60, 145,
            154, 35, 245, 202, 69, 148, 33, 156, 244, 43, 155, 38, 149, 170, 92, 225,
            242, 158, 222, 10, 115, 120, 57, 239, 138, 66, 237, 59, 47, 184, 233,
            193, 230, 114, 25, 223, 94, 215, 209, 50, 188, 167, 125, 250*/
            1, 6, 4, 3, 9, 2, 8, 7, 5, 10
    };

    private List<Pair> shades;
    private List<Pair> recoveredF;
    private List<Pair> recoveredG;
    private GF GF251;


    /*This constructor it's for distribute*/
    public Shades(Block block, int n) {
        List<Pair> shadesList = new ArrayList<>();

        for(int i = 1 ; i <= n ; i++) {
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
        if(bool) {
            this.recoveredF.add(new Pair(portNum, value));
        }else {
            this.recoveredG.add(new Pair(portNum, value));
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

        if(this.recoveredF.size() < k && this.recoveredG.size() < k) {
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
        Integer result = 0;

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
            Integer aux = ((shade.getRight() - si) * INVERTS[shade.getLeft()-1]);
            aux = this.GF251.transformToGF(aux);
            shade.setRight(aux);
        }
    }

    private int Li(Pair currentShade, List<Pair> recoveredShades) {
        int result = 1;
        for(int i = 0 ; i < recoveredShades.size() ; i++) {
            if(!recoveredShades.get(i).equals(currentShade)) {
                Integer denominator = ((currentShade.getLeft() - recoveredShades.get(i).getLeft()));

                denominator = this.GF251.transformToGF(denominator);

                result*= -recoveredShades.get(i).getLeft() * INVERTS[denominator-1];
            }
        }

        result = this.GF251.transformToGF(result);

        return result;
    }


}
