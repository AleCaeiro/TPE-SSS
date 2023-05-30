
import javax.imageio.ImageIO;
import java.io.File;
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
        OriginalImage img = new OriginalImage(parser.getImgPath());


        //Chequeamos que la imagen sea divisible por 2k-2
        if((img.getTotalSize()) % (parser.getBLOCK_SIZE()) != 0){
            System.out.println("La imagen no es divisible por la dimension del bloque");
            return;
        }



        List<Integer> pixels = new ArrayList<>();
        List<Block> blocks = new ArrayList<>();
        int count = 0;
        for (int y = 0; y < img.getHeight(); y++) {
            for (int x = 0; x < img.getWidth(); x++) {

                //todo check pixels not be 0, if its 0 change it to 1
                pixels.add(img.getPixel(x, y));
                if(count % parser.getBLOCK_SIZE() == 0){
                    blocks.add(new Block(pixels, parser.getK()-1));
                    count = 0;
                    pixels.clear();
                }
                count++;
            }
        }
    }
}
