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

        BufferedImage image = null;
        try{
            // Cargar la imagen BMP
            image = ImageIO.read(new File(parser.getImgPath()));
        }catch (IOException e){
            System.out.println("Error al abrir la imagen: " + e.getMessage());
        }


        // Obtener las dimensiones de la imagen
        assert image != null;
        int imgWidth = image.getWidth();
        int imgHeight = image.getHeight();

        if((imgWidth*imgHeight) % (parser.getBLOCK_SIZE()) != 0){
            System.out.println("La imagen no es divisible por la dimension del bloque");
            return;
        }

        // Procesar la imagen píxel por píxel
        for (int y = 0; y < imgHeight; y++) {
            for (int x = 0; x < imgWidth; x++) {
                /*
                getRaster: Devuelve una matriz de pixeles
                getSample: Devuelve un pixel de la matriz
                */
                int pixel = image.getRaster().getSample(x, y, 0);
            }
        }

        ImageIO.write(image, "bmp", new File("resources/inverted.bmp"));
    }
}
