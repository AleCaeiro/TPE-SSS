import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
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
        this.k = parser.getK();
        this.blockSize = (BLOCK_MULTIPLIER * this.k) - BLOCK_MULTIPLIER;

        //Chequeamos que la imagen sea divisible por 2k-2
        if ((this.secretImage.getTotalSize()) % (this.blockSize) != 0) {
            System.out.println("La imagen no es divisible por la dimension del bloque");
            //TODO throw an exception
        }

        this.images = getBMPImages(parser.getShadesDirectory());

        if (parser.getMode().equals("d")) {
            setShadeNumInHeader(this.images);
        }

        this.n = this.images.size();
        this.lsb = this.k > 4 ? 2 : 4;
        this.mask2 =  this.lsb == 2 ? 0x03 : 0x0F;
        this.mask6 =  this.lsb == 2 ? 0xFC : 0xF0;
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
        for (int y = secretImage.getHeight() - 1; y >= 0 ; y-- ) {
            for (int x = 0; x < secretImage.getWidth(); x++) {
                int pixel = secretImage.getPixel(x, y);
                pixels.add(pixel);
                count++;
                if (count == this.blockSize) {
                    Block currentBlock = new Block(pixels, this.k - 1, blockNum);
                    Shades shades = new Shades(currentBlock, n);
                    newPosition = insertShades(shades, newPosition[0], newPosition[1]);
                    blockNum++;
                    count = 0;
                    pixels.clear();
                }
            }
        }

        for (Image image: this.images) {
            try {
                image.writeImage();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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
        return new int[] {currentX, currentY};
    }

    /*--------------------------------------------------------------
    ---------------------------RECOVER------------------------------
     --------------------------------------------------------------*/

    public void recover() {

    }

}
