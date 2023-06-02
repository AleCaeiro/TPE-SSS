
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


        //Chequeamos que la imagen sea divisible por 2k-2
        if((img.getTotalSize()) % (parser.getBlockSize()) != 0){
            System.out.println("La imagen no es divisible por la dimension del bloque");
            return;
        }


        List<Integer> pixels = new ArrayList<>();
        List<Block> blocks = new ArrayList<>();
        List<Shades> shades = new ArrayList<>();
        int count = 0;
        int blockNum = 1;
        int n = 4; //todo: revisar este n
        for (int y = 0; y < img.getHeight(); y++) {
            for (int x = 0; x < img.getWidth(); x++) {

                //pixel obtained can not be 0
                int pixel = img.getPixel(x, y);
                if(pixel == 0){
                    pixel = 1;
                }
                pixels.add(pixel);
                count++;
                if(count == parser.getBlockSize()){

                    Block currentBlock = new Block(pixels, parser.getK()-1, blockNum);
                    blocks.add(currentBlock);
                    shades.add(new Shades(currentBlock, n));
                    blockNum++;
                    count = 0;
                    pixels.clear();
                }

            }
        }

    }
}
