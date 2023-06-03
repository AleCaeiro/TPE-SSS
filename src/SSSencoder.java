import java.io.File;
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
        this.secretImage = new Image(parser.getImgPath());
        this.images = getBMPImages(parser.getShadesDirectory());
        setShadeNumInHeader(this.images);
        this.n = this.images.size();
        this.k = parser.getK();
        this.blockSize = (BLOCK_MULTIPLIER * this.k) - BLOCK_MULTIPLIER;
        this.lsb = this.k > 4 ? 2 : 4;
        this.mask2 =  this.lsb == 2 ? 0x03 : 0x0F;
        this.mask6 =  this.lsb == 2 ? 252 : 240;

        //Chequeamos que la imagen sea divisible por 2k-2
        if ((this.secretImage.getTotalSize()) % (this.blockSize) != 0) {
            System.out.println("La imagen no es divisible por la dimension del bloque");
            //TODO throw an exception
        }
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
    -------------------------DESTRIBUTE-----------------------------
     --------------------------------------------------------------*/
    public void distribute() {
        List<Integer> pixels = new ArrayList<>();
        int count = 0;
        int blockNum = 1;
        Integer currentX = 0;
        Integer currentY = 0;
        int[] newPosition;
        // Generamos sombras de cada bloque de la imagen (n veces según número de portadora)
        for (int y = 0; y < secretImage.getHeight(); y++) {
            for (int x = 0; x < secretImage.getWidth(); x++) {
                //pixel obtained can not be 0. Neither 251 because 251 = 0 mod(251)
                int pixel = secretImage.getPixel(x, y);
                if (pixel % 251 == 0) {
                    pixel = 1;
                }
                pixels.add(pixel);
                count++;
                if (count == this.blockSize) {
                    Block currentBlock = new Block(pixels, this.k - 1, blockNum);
                    Shades shades = new Shades(currentBlock, n);
                    newPosition = insertShades(shades, n, this.images, currentX, currentY);
                    currentX = newPosition[0];
                    currentY = newPosition[1];
                    blockNum++;
                    count = 0;
                    pixels.clear();
                }
            }
        }
        System.out.println(currentX);
    }

    private void setShadeNumInHeader(List<Image> shades) {
        int count = 1;
        for (Image im : shades) {
            im.setReservedByte((short) count);
            count++;
        }
    }

    private List<Image> getBMPImages(String pathDirectory) {
        List<Image> imageList = new ArrayList<>();
        File folder = new File(pathDirectory);
        File[] files = folder.listFiles();
        if (files != null) {
            for (File file : files) {
                imageList.add(new Image(String.format(pathDirectory + '/' + file.getName())));
            }
        }
        return imageList;
    }

    private int[] insertShades(Shades shades, int n, List<Image> sharedImages, Integer currentX, Integer currentY) {
        int[] newPosition = {currentX, currentY};
        Integer f_x;
        Integer g_x;
        Image tmpImage;
        for (int i = 1; i <= n; i++) {
            tmpImage = sharedImages.get(i - 1);
            f_x = shades.getPair(i).getLeft();
            g_x = shades.getPair(i).getRight();
            newPosition = insertLsb(currentX, currentY, tmpImage, f_x);
            newPosition = insertLsb(newPosition[0], newPosition[1], tmpImage, g_x);
        }
        return newPosition;
    }

    private int[] insertLsb(int currentX, int currentY, Image tmpImage, int value) {
        int[] newPosition = {currentX, currentY};
        int aux;
        int pixel;
        for (int x = 8 / this.lsb - 1; x >= 0; x--) {
            if (currentX < tmpImage.getWidth() && currentY < tmpImage.getHeight()) {
                System.out.println("x" + currentX);
                System.out.println("y" + currentY);

                pixel = tmpImage.getPixel(currentX, currentY);
                aux = (value >> this.lsb * x) & this.mask2;
                pixel = (pixel & this.mask6) + aux;
                tmpImage.setPixel(currentX, currentY, pixel);
            }
            currentX++;
            if (currentX % tmpImage.getWidth() == 0) {
                currentY++;
                currentX = 0;
            }
        }

        newPosition[0] = currentX;
        newPosition[1] = currentY;
        return newPosition;
    }

    /*--------------------------------------------------------------
    ---------------------------RECOVER------------------------------
     --------------------------------------------------------------*/

    public void recover() {

    }

}
