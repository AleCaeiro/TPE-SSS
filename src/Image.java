import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import javax.imageio.ImageIO;

// Clase que maneja una imagen en formato bmp ignorando el header
public class Image {

    private BufferedImage image;
    private String filePath;
    private static final int OFFSET_RESERVED1 = 6;


    public Image(String imgPath) {
        this.filePath = imgPath;
        try {
            this.image = ImageIO.read(new File(imgPath));
        } catch (IOException e) {
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
        return this.getWidth() * this.getHeight();
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

    public void setReservedByte(short value) {
        try {
            RandomAccessFile file = new RandomAccessFile(filePath, "rw");
            file.seek(OFFSET_RESERVED1);
            file.writeShort(Short.reverseBytes(value));
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public short getReservedByte() {
        try {
            RandomAccessFile file = new RandomAccessFile(filePath, "r");
            file.seek(OFFSET_RESERVED1);
            short value = Short.reverseBytes(file.readShort());
            file.close();
            return value;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return -1;
    }
}
