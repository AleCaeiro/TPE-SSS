import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

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

        RandomGF251 randInstance = new RandomGF251();
        int ri = randInstance.generateRandom();

        while (ri == 0){
            ri = randInstance.generateRandom();
        }
    }
}
