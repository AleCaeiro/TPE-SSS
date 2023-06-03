import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

// Clase que maneja una imagen en formato bmp ignorando el header
public class SecretImage {
    private BufferedImage image;

    public SecretImage(String imgPath, String mode) {
        BufferedImage image = null;
        try{
            this.image = ImageIO.read(new File(imgPath));
        }catch (IOException e){
            System.out.println("Error al abrir la imagen: " + e.getMessage());
        }
    }

    public int getWidth() {
        return this.image.getWidth();
    }

    public int getHeight() {
        return this.image.getHeight();
    }

    public int getTotalSize() {
        return this.getWidth()*this.getHeight();
    }

    public int getPixel(int x, int y) {
        return this.image.getRaster().getSample(x, y, 0);
    }

    public void setPixel(int x, int y, int newPixel) {
        this.image.getRaster().setSample(x, y, 0, newPixel);
    }

    public BufferedImage getImage() {
        return this.image;
    }

    public void generateFile(String filename) throws IOException {
        ImageIO.write(image, "bmp", new File(filename));
    }
}
