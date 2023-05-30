import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Main {
    public static void main(String[] args) throws IOException {
        // Cargar la imagen BMP
        BufferedImage image = ImageIO.read(new File("resources/einstein.bmp"));

        // Obtener las dimensiones de la imagen
        int width = image.getWidth();
        int height = image.getHeight();

        // Procesar la imagen píxel por píxel
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                /*
                getRaster: Devuelve una matriz de pixeles
                getSample: Devuelve un pixel de la matriz
                */
                int pixel = image.getRaster().getSample(x, y, 0);

                int invertedPixel = 255 - pixel; // Invierte el valor del píxel
                image.getRaster().setSample(x, y, 0, invertedPixel);
            }
        }

        ImageIO.write(image, "bmp", new File("resources/inverted.bmp"));
    }
}
