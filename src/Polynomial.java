import java.util.ArrayList;
import java.util.List;

public class Polynomial {

    private final List<Integer> coefficients;
    private final static GF251 GF251 = new GF251();

    public Polynomial(List<Integer> coefficients) {
        this.coefficients = new ArrayList<>(coefficients);
    }

    public Integer getCoefficient(int degree) {
        return coefficients.get(degree);
    }

    public List<Integer> getCoefficients() {
        return coefficients;
    }

    public int getDegree() {
        return coefficients.size() - 1;
    }

    public Integer evaluate(int shadeNum) {
        int degree = this.getDegree();
        int result = 0;

        for (int i = 0; i <= degree; i++) {
            int coefficient = coefficients.get(i);
            int term = (int) Math.pow(shadeNum, i);
            result += coefficient * term;
        }

        return GF251.transformToGF(result);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        int degree = getDegree();

        for (int i = 0; i <= degree; i++) {
            int coefficient = getCoefficient(i);

            if (coefficient != 0) {
                if (coefficient > 0 && i != 0) {
                    sb.append(" + ");
                } else if (coefficient < 0) {
                    sb.append(" - ");
                    coefficient = -coefficient;
                }

                if (i == 0 || coefficient != 1) {
                    sb.append(coefficient);
                }

                if (i > 0) {
                    sb.append("x");
                    if (i > 1) {
                        sb.append("^").append(i);
                    }
                }
            }
        }

        return sb.toString();
    }
}
