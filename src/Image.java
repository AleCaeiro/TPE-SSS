import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Iterator;
import java.util.NoSuchElementException;
import javax.imageio.ImageIO;

// TODO: add iterator to go over imagePixels
// Clase que maneja una imagen en formato bmp ignorando el header
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
            try {
                if (b != BYTES_PER_PIXEL) {
                    throw new UnsupportedOperationException("La imagen no es de 8 bits por píxel");
                }
            } catch (UnsupportedOperationException e) {
                System.out.println("Error: " + e.getMessage());
            }
            this.carryId = getByteOfHeader(OFFSET_BYTES_RESERVED);
        } catch (IOException e) {
            System.out.println("Error al abrir la imagen: " + e.getMessage());
        }
    }

    // Constructor para una imagen vacía (a usar para recuperar imagen secreta)
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
            // Saltar los primeros 1C bytes (28 bytes) del encabezado
            fileInputStream.skip(offset);

            // Leer el byte que contiene la cantidad de bits por píxel
            int value = fileInputStream.read();

            return value;
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

//    public Iterator<Integer> iterator(){
//        return new ImageIterator();
//    }

//    private class ImageIterator implements Iterator<Integer> {
//        int currentX = 0;
//        int currentY = image.getHeight() - 1;
//
//        @Override
//        public boolean hasNext() {
//            return currentX < image.getWidth() && currentY >= 0;
//        }
//
//        @Override
//        public Integer next() {
//            if(!hasNext()){
//                throw new NoSuchElementException();
//            }
//
//            Integer toRet = getPixel(currentX, currentY);
//
//            currentX++;
//            currentX++;
//            if (currentX % image.getWidth() == 0) {
//                currentY--;
//                currentX = 0;
//            }
//
//            return toRet;
//        }
//    }
}
