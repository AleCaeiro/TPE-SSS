import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {

        if (args.length != 4) {
            System.out.println("Uso: java Program <modo> <imagen> <k> <directorio_sombras>");
            return;
        }

        ArgumentsParser parser = new ArgumentsParser(args[0], args[1], args[2], args[3]);
        SecretImage img = new SecretImage(parser.getImgPath(), parser.getMode());

        /*MOVE to test
        List<Integer> portNum = new ArrayList<>();
        List<Integer> f = new ArrayList<>();

        portNum.add(1);
        f.add(3);

        portNum.add(5);
        f.add(10);

        portNum.add(2);
        f.add(9);

        Shades recoveredShades = new Shades(portNum, f, f);
        recoveredShades.applyLagrange(3);*/

        //Chequeamos que la imagen sea divisible por 2k-2
        if ((img.getTotalSize()) % (parser.getBlockSize()) != 0) {
            System.out.println("La imagen no es divisible por la dimension del bloque");
            return;
        }

        List<Integer> pixels = new ArrayList<>();
        List<Shades> shades = new ArrayList<>();
        int count = 0;
        int blockNum = 1;

        int n = 4; //todo: revisar este n. dependiente de numero de portadoras

        // Generamos sombras de cada bloque de la imagen (n veces según número de portadora
        for (int y = 0; y < img.getHeight(); y++) {
            for (int x = 0; x < img.getWidth(); x++) {
                //pixel obtained can not be 0. Neither 251 because 251 = 0 mod(251)
                int pixel = img.getPixel(x, y);
                if (pixel % 251 == 0) {
                    pixel = 1;
                }
                pixels.add(pixel);
                count++;
                if (count == parser.getBlockSize()) {
                    Block currentBlock = new Block(pixels, parser.getK() - 1, blockNum);
                    shades.add(new Shades(currentBlock, n));
                    blockNum++;
                    count = 0;
                    pixels.clear();
                }
            }
        }

        //todo: despues de generar las shades insertarlas en formato lsb2 o lsb4 según corresponda
        // Tendremos que insertar en cada imagen portadora las sombras
        for (int i = 1; i <= n; i++) {
            // abrimos la imagen portadora numero n en cada ciclo
            // función para modificar y colocar numero de sombra a la imagen

            Integer f_x;
            Integer g_x;


            for (int y = 0; y < img.getHeight(); y++) {
                for (int x = 0; x < img.getWidth(); x++) {
                    int pixel = img.getPixel(x, y);

                    //usar un count con modulo lsb para ver cuando terminaste de ocultar el numero

                    // levantamos el primer valor de la lista de shades (f_x y g_x)
                    // transformamos según cantidad de bits de lsb (a 4 o 2 números)
                    // sumamos 252 o 240 según corresponda para llenar de 1's
                    // hacemos & con el pixel y lo seteamos
                    img.setPixel(x, y, pixel);
                }
            }


        }
    }

    // Función que divide el número según valor lsb
    private static List<Integer> generateMaskedInteger(Integer num, Integer lsb) {
        int mask;
        if (lsb == 2) {
            mask = 0x03;
        } else {
            mask = 0x0F;
        }

        List<Integer> parsedNum = new ArrayList<>();

        for (int i = 0; i < 8 / lsb; i++) {
            int aux = num >> lsb * i;
            parsedNum.add(aux & mask);
        }

        return parsedNum;
    }
}
