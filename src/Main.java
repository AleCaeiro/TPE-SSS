
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
        SecretImage img = new SecretImage(parser.getImgPath());

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
        if((img.getTotalSize()) % (parser.getBlockSize()) != 0){
            System.out.println("La imagen no es divisible por la dimension del bloque");
            return;
        }

        List<Integer> pixels = new ArrayList<>();
        List<Shades> shades = new ArrayList<>();
        int count = 0;
        int blockNum = 1;

        int n = 4; //todo: revisar este n. dependiente de numero de portadoras

        for (int y = 0; y < img.getHeight(); y++) {
            for (int x = 0; x < img.getWidth(); x++) {
                //pixel obtained can not be 0. Neither 251 because 251 = 0 mod(251)
                int pixel = img.getPixel(x, y);
                if(pixel == 0 || pixel == 251){
                    pixel = 1;
                }
                pixels.add(pixel);
                count++;
                if(count == parser.getBlockSize()){
                    Block currentBlock = new Block(pixels, parser.getK() - 1, blockNum);
                    shades.add(new Shades(currentBlock, n));
                    blockNum++;
                    count = 0;
                    pixels.clear();
                }
            }
        }

        //todo: despues de generar las shades insertarlas en formato lsb2 o lsb4 según corresponda

    }
}
