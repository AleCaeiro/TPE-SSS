import javax.imageio.ImageIO;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.util.ArrayList;
import java.util.List;

public class SSSencoder {
    //Constructor para encode -> recibe imagen a ocultar y modifica imagenes portadoras
    //Constructor para decode -> recibe portadores y genera imagen oculta

    private final Image secretImage;
    private final List<Image> images;
    private final int n;
    private final int k;
    private final int blockSize;
    private final int lsb;
    private final int mask2;
    private final int mask6;
    private final static int BLOCK_MULTIPLIER = 2;

    public SSSencoder(ArgumentsParser parser) {
        this.k = parser.getK();
        this.lsb = this.k > 4 ? 2 : 4;
        this.mask2 = this.lsb == 2 ? 0x03 : 0x0F;
        this.mask6 = this.lsb == 2 ? 0xFC : 0xF0;
        this.blockSize = (BLOCK_MULTIPLIER * this.k) - BLOCK_MULTIPLIER;
        this.images = getBMPImages(parser.getShadesDirectory());

        // Verificar si el archivo existe y si tiene la extensión .bmp
        if (parser.getMode().equals("d")) {
            this.secretImage = new Image(parser.getImgPath());
            try {
                File file = new File(parser.getImgPath());
                if (!file.exists() || !file.isFile() || !file.getName().toLowerCase().endsWith(".bmp")) {
                    throw new NoSuchFileException("El archivo no existe o no es formato .bmp");
                }
            } catch (NoSuchFileException e) {
                System.out.println("Error: " + e.getMessage());
            }
            setShadeNumInHeader(this.images);
        }
        else {
            this.secretImage = new Image(parser.getImgPath(), this.images.get(0).getHeight(), this.images.get(0).getWidth());
        }

        //Chequeamos que la imagen sea divisible por 2k-2
        try {
            if ((this.secretImage.getTotalSize()) % (this.blockSize) != 0) {
                throw new IllegalArgumentException("La imagen no es divisible por la dimension del bloque");
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }

        this.n = this.images.size();
    }

    public int getK() {
        return this.k;
    }

    public int getN() {
        return this.n;
    }

    public List<Image> getImages() {
        return this.images;
    }

    public Image getSecretImage() {
        return this.secretImage;
    }

    public int getBlockSize() {
        return this.blockSize;
    }

    public int getLsb() {
        return this.lsb;
    }

    public int getMask2() {
        return this.mask2;
    }

    public int getMask6() {
        return this.mask6;
    }

    /*--------------------------------------------------------------
    -------------------------DISTRIBUTE-----------------------------
     --------------------------------------------------------------*/
    public void distribute() {
        List<Integer> pixels = new ArrayList<>();
        int count = 0;
        int blockNum = 1;
        int[] newPosition = {0, secretImage.getHeight() - 1};

        // Generamos sombras de cada bloque de la imagen (n veces según número de portadora)
        for (int y = secretImage.getHeight() - 1; y >= 0; y--) {
            for (int x = 0; x < secretImage.getWidth(); x++) {
                int pixel = secretImage.getPixel(x, y);
                pixels.add(pixel);
                count++;
                if (count == this.blockSize) {
                    Block currentBlock = new Block(pixels, k - 1);
                    Shades shades = new Shades(currentBlock, n);
                    newPosition = insertShades(shades, newPosition[0], newPosition[1]);
                    blockNum++;
                    count = 0;
                    pixels.clear();
                }
            }
        }

        for (Image image : this.images) {
            try {
                image.writeImage();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void setShadeNumInHeader(List<Image> shades) {
        short count = 1;
        for (Image im : shades) {
            im.setReservedByte(count);
            count++;
        }
    }

    private List<Image> getBMPImages(String pathDirectory) {
        List<Image> imageList = new ArrayList<>();
        File folder = new File(pathDirectory);
        File[] files = folder.listFiles();
        if (!folder.exists()) {
            throw new IllegalArgumentException("El path al directorio especificado es erroneo o  no existe");
        }
        if (files != null) {
            for (File file : files) {
                imageList.add(new Image(String.format(pathDirectory + '/' + file.getName())));
            }
        }
        return imageList;
    }

    private int[] insertShades(Shades shades, Integer currentX, Integer currentY) {
        int[] newPosition = null;
        Integer f_x;
        Integer g_x;
        Image tmpImage;
        for (int i = 1; i <= n; i++) {
            tmpImage = images.get(i - 1);
            f_x = shades.getPair(i).getLeft();
            g_x = shades.getPair(i).getRight();
            newPosition = insertLsb(currentX, currentY, tmpImage, f_x);
            newPosition = insertLsb(newPosition[0], newPosition[1], tmpImage, g_x);
        }
        return newPosition;
    }

    private int[] insertLsb(int currentX, int currentY, Image tmpImage, int value) {
        int aux;
        int pixel;
        for (int x = 8 / this.lsb - 1; x >= 0; x--) {
            pixel = tmpImage.getPixel(currentX, currentY);
            aux = (value >> this.lsb * x) & this.mask2;
            pixel = (pixel & this.mask6) + aux;
            tmpImage.setPixel(currentX, currentY, pixel);
            currentX++;
            if (currentX % tmpImage.getWidth() == 0) {
                currentY--;
                currentX = 0;
            }
        }
        return new int[]{currentX, currentY};
    }

    /*--------------------------------------------------------------
    ---------------------------RECOVER------------------------------
     --------------------------------------------------------------*/

    public void recover() {
        List<Integer> x = new ArrayList<>();

        for (Image image: images) {
            x.add(image.getCarryId());
        }

        List<Integer> f_x = new ArrayList<>();
        List<Integer> g_x = new ArrayList<>();

        int[] newPositionRead = {0, secretImage.getHeight() - 1};
        int[] newPositionWrite = {0, secretImage.getHeight() - 1};

        int currentX, currentY;

        int blockCount = 0;
        int totalBlocks = secretImage.getTotalSize() / blockSize;

        while(blockCount < totalBlocks) {
            currentX = newPositionRead[0];
            currentY = newPositionRead[1];
            for (int i = 0; i < n ; i++) {
                // Recuperar valor f_x y g_x de cada sombra por bloques
                newPositionRead = recoverInteger(currentX, currentY, images.get(i), f_x);
                newPositionRead = recoverInteger(newPositionRead[0], newPositionRead[1], images.get(i), g_x);
            }

            // Instanciar una Shades con el constructor para recovery
            // Shades(List<Integer> x, List<Integer> f_x, List<Integer> g_x) {
            Shades shades = new Shades(x, f_x, g_x);
            // Instanciamos el block que estamos parados al recuperarlo según shades
            Block currentBlock = new Block(shades.applyLagrange(k));
            // Escribimos en la secretImage los pixels de ese block
            newPositionWrite = writeSecretImage(newPositionWrite[0], newPositionWrite[1], currentBlock.getPixels());
            f_x.clear();
            g_x.clear();
            blockCount++;
        }

        try {
            secretImage.writeImage();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int[] recoverInteger(int currentX, int currentY, Image image, List<Integer> toAdd) {
        int aux;
        Integer toRet = 0;

        for (int x = 8 / this.lsb - 1; x >= 0; x--) {
            toRet = toRet << 2;
            aux = image.getPixel(currentX, currentY) & mask2;
            toRet += aux;
            currentX++;
            if (currentX % image.getWidth() == 0) {
                currentY--;
                currentX = 0;
            }
        }

        toAdd.add(toRet);
        return new int[]{currentX, currentY};
    }

    private int[] writeSecretImage(int currentX, int currentY, List<Integer> pixels) {
        for (Integer pixel:pixels) {
            secretImage.setPixel(currentX, currentY, pixel);
            currentX++;
            if (currentX % secretImage.getWidth() == 0) {
                currentY--;
                currentX = 0;
            }
        }
        return new int[]{currentX, currentY};
    }
}
