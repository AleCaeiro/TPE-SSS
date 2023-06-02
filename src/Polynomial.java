import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Polynomial {
    private List<Integer> coefficients;

    public Polynomial(List<Integer> coefficients) {
        this.coefficients = new ArrayList<>(coefficients);
    }

    public Integer getCoefficient(int i){
        return coefficients.get(i);
    }

    public int getDegree() {
        return coefficients.size()-1;
    }

    public Integer evaluate(int shadeNum) {
        int degree = this.getDegree();
        int result = 0;

        for (int i = 0; i <= degree; i++) {
            int coefficient = this.getCoefficient(i);
            int term = (int) Math.pow(shadeNum, i);
            result += coefficient * term;
        }

        return result % 251;
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

    public Polynomial sumByPolynomial(Polynomial polynomial) {
        List<Integer> resultCoefficients = new ArrayList<>();

        int degree = getDegree();

        for (int i = 0; i <= degree; i++) {
            int coefficient1 = getCoefficient(i);
            int coefficient2 = polynomial.getCoefficient(i);
            int sum = (coefficient1 + coefficient2) % 251;
            resultCoefficients.add(sum);
        }

        return new Polynomial(resultCoefficients);
    }

    public Polynomial multiplyByScalar(int scalar) {
        List<Integer> resultCoefficients = new ArrayList<>();

        for (int i = 0; i <= getDegree(); i++) {
            int coefficient = getCoefficient(i);
            int product = (coefficient * scalar) % 251;
            resultCoefficients.add(product);
        }

        return new Polynomial(resultCoefficients);
    }

}
