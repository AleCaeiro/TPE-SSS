import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import javax.imageio.ImageIO;

// Clase que maneja una imagen en formato bmp ignorando el header
public class Image {
    private BufferedImage image;
    private final String filePath;
    private final static int OFFSET_BYTES_RESERVED = 6;
    private final static int OFFSET_BYTES_PER_PIXEL = 28;
    private final static int BYTES_PER_PIXEL = 8;

    public Image(String imgPath) {
        this.filePath = imgPath;
        try {
            this.image = ImageIO.read(new File(imgPath));
            int b = getByteOfHeader(OFFSET_BYTES_PER_PIXEL);
            try {
                if (b != BYTES_PER_PIXEL) {
                    throw new UnsupportedOperationException("La imagen no es de 8 bits por píxel");
                }
            } catch (UnsupportedOperationException e) {
                System.out.println("Error: " + e.getMessage());
            }
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

    public String getFilePath() {
        return this.filePath;
    }

    public BufferedImage getImage() {
        return this.image;
    }

    public void writeImage() throws IOException {
        ImageIO.write(image, "bmp", new File(filePath));
    }

    public void setReservedByte(short value) {
        try {
            RandomAccessFile file = new RandomAccessFile(filePath, "rw");
            file.seek(OFFSET_BYTES_RESERVED);
            file.writeShort(Short.reverseBytes(value));
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private short getByteOfHeader(int offset) {
        try {
            RandomAccessFile file = new RandomAccessFile(filePath, "r");
            file.seek(offset);
            short value = Short.reverseBytes(file.readShort());
            file.close();
            return value;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return -1;
    }

}
