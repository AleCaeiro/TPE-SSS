import java.util.ArrayList;
import java.util.List;

public class Shades {

    private final List<Pair> shades;
    public Shades(Block block, int n) {
        List<Pair> shadesList = new ArrayList<>();

        for(int i = 1 ; i <= n ; i++) {
            shadesList.add(new Pair(block.getF().evaluate(i), block.getG().evaluate(i)));
        }

        this.shades = shadesList;
    }

    public List<Pair> getShades() {
        return shades;
    }

    private static class Pair {

        private final Integer f_x;
        private final Integer g_x;
        public Pair(Integer f_x, Integer g_x) {
            this.f_x = f_x;
            this.g_x = g_x;
        }

        public Integer getF_x() {
            return f_x;
        }

        public Integer getG_x() {
            return g_x;
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
            return this.f_x.equals(other.f_x) && this.g_x.equals(other.g_x);
        }
    }



}
