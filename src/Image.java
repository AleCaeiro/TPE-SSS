import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Iterator;
import java.util.NoSuchElementException;
import javax.imageio.ImageIO;

public class Image {

    private BufferedImage image;
    private final String filePath;
    private final static int OFFSET_BYTES_RESERVED = 6;
    private final static int OFFSET_BYTES_PER_PIXEL = 28;
    private final static int BYTES_PER_PIXEL = 8;
    private int carryId = -1;

    // Constructor para una imagen existente en el FileSystem
    public Image(String imgPath) {
        this.filePath = imgPath;
        try {
            this.image = ImageIO.read(new File(imgPath));
            int b = getByteOfHeader(OFFSET_BYTES_PER_PIXEL);
            if (b != BYTES_PER_PIXEL) {
                System.out.println("La imagen no es de 8 bits por píxel");
                System.exit(1);
            }
            // En caso de que no se haya asignado aun, queda en 0
            // Se usa mayormente en recuperacion
            // Al distribuir se trabaja con el orden en que fueron leidas
            this.carryId = getByteOfHeader(OFFSET_BYTES_RESERVED);
        } catch (IOException e) {
            System.out.println("Error al abrir la imagen: " + e.getMessage());
            System.exit(1);
        }
    }

    // Constructor para una imagen vacía, para usar en recuperacion
    public Image(String imgPath, int height, int width) {
        this.filePath = imgPath;
        this.image = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
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

    private int getByteOfHeader(int offset) {
        try (FileInputStream fileInputStream = new FileInputStream(filePath)) {
            fileInputStream.skip(offset);
            return fileInputStream.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public int getCarryId() {
        return carryId;
    }

    public void setCarryId(int carryId) {
        this.carryId = carryId;
    }

    public ImageIterator iterator() {
        return new ImageIterator();
    }

    // Iterator para recorrer la matriz de bytes de la imagen en orden
    public class ImageIterator implements Iterator<Integer> {
        int currentX = 0;
        int currentY = image.getHeight() - 1;
        int totalPixels = 0;
        int prevX = currentX;
        int prevY = currentY;

        @Override
        public boolean hasNext() {
            return totalPixels < getTotalSize();
        }

        @Override
        public Integer next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }

            prevX = currentX;
            prevY = currentY;

            Integer toRet = getPixel(currentX, currentY);

            currentX++;
            totalPixels++;
            if (currentX % image.getWidth() == 0) {
                currentY--;
                currentX = 0;
            }

            return toRet;
        }

        // Función que además de setear el pixel mueve el current al siguiente
        public void setNextPixel(Integer pixel) {
            setPixel(currentX, currentY, pixel);
            next();
        }

        // Función utilizada para setear un pixel luego de haber hecho un next()
        public void setCurrentPixel(Integer pixel) {
            setPixel(prevX, prevY, pixel);
        }
    }
}
