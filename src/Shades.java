import java.util.ArrayList;
import java.util.List;

public class Shades {

    private final List<Shade> shades;
    public Shades(Polynomial f, Polynomial g, int n) {
        List<Shade> shadesList = new ArrayList<>();

        for(int i = 1 ; i <= n ; i++) {
            shadesList.add(new Shade(f, g, i));
        }

        this.shades = shadesList;
    }

    public List<Shade> getShades() {
        return shades;
    }
}
