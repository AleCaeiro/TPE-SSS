import java.util.ArrayList;
import java.util.List;

public class Polynomial {
    private List<Integer> coefficients;

    public Polynomial(List<Integer> coefficients) {
        this.coefficients = coefficients;
    }

    public Integer getCoefficient(int i){
        return coefficients.get(i);
    }

    public int getDegree() {
        return coefficients.size()-1;
    }
}
