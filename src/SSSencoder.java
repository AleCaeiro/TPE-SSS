import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class SSSencoder {
    private final Image secretImage;
    private final List<Image> images;
    private final int n;
    private final int k;
    private final int blockSize;
    private final int lsb;
    private final int lsbmask;
    private final int pixelmask;

    private final static GF251 GF251 = new GF251();

    public SSSencoder(ArgumentsParser parser) {
        this.k = parser.getK();
        this.lsb = this.k > 4 ? 2 : 4;
        this.lsbmask = this.lsb == 2 ? 0x03 : 0x0F;
        this.pixelmask = this.lsb == 2 ? 0xFC : 0xF0;
        this.blockSize = parser.getBlockSize();
        this.images = parser.getImages();
        this.secretImage = parser.getSecretImage();
        this.n = this.images.size();
    }

    /*--------------------------------------------------------------
    -------------------------DISTRIBUTE-----------------------------
     --------------------------------------------------------------*/
    public void distribute() {
        List<Integer> pixels = new ArrayList<>();
        int count = 0;
        Image.ImageIterator pixelIterator = secretImage.iterator();

        List<Image.ImageIterator> shadesIterators = new ArrayList<>();
        for (Image shadeImage: images) {
            shadesIterators.add(shadeImage.iterator());
        }

        while(pixelIterator.hasNext()) {
            int pixel = pixelIterator.next();
            pixel = GF251.transformToGF(pixel) == 0 ? 1 : pixel;
            pixels.add(pixel);
            count++;
            if (count == this.blockSize) {
                Block currentBlock = new Block(pixels, k - 1);
                Shades shades = new Shades(currentBlock, n);
                insertShades(shades, shadesIterators);
                count = 0;
                pixels.clear();
            }
        }

        for (Image image : this.images) {
            try {
                image.writeImage();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        short shadeCount = 1;
        for (Image image: this.images) {
            image.setReservedByte(shadeCount);
            shadeCount++;
        }
    }

    private void insertShades(Shades shades, List<Image.ImageIterator> shadesIterators) {
        Integer f_x;
        Integer g_x;

        int i = 1;

        for (Image.ImageIterator currentIt: shadesIterators) {
            f_x = shades.getPair(i).getLeft();
            g_x = shades.getPair(i).getRight();

            insertLsb(currentIt, f_x);
            insertLsb(currentIt, g_x);
            i++;
        }
    }

    private void insertLsb(Image.ImageIterator iterator, int value) {
        int aux;
        int pixel;
        for (int x = 8 / lsb - 1; x >= 0; x--) {
            pixel = iterator.next();
            aux = (value >> lsb * x) & lsbmask;
            pixel = (pixel & pixelmask) + aux;
            iterator.setCurrentPixel(pixel);
        }
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

        Image.ImageIterator pixelIterator = secretImage.iterator();
        List<Image.ImageIterator> shadesIterators = new ArrayList<>();

        for (Image shadeImage: images) {
            shadesIterators.add(shadeImage.iterator());
        }

        int blockCount = 0;
        int totalBlocks = secretImage.getTotalSize() / blockSize;

        while(blockCount < totalBlocks) {
            for (Image.ImageIterator currentIt: shadesIterators) {
                // Recuperar valor f_x y g_x de cada sombra por bloques
                recoverInteger(currentIt, f_x);
                recoverInteger(currentIt, g_x);
            }

            Shades shades = new Shades(x, f_x, g_x);
            Block currentBlock = new Block(shades.applyLagrange(k));
            writeSecretImage(pixelIterator, currentBlock.getPixels());

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

    private void recoverInteger(Image.ImageIterator iterator, List<Integer> toAdd) {
        int aux;
        Integer toRet = 0;

        for (int x = 8 / lsb - 1; x >= 0; x--) {
            toRet = toRet << lsb;
            aux = iterator.next() & lsbmask;
            toRet += aux;
        }

        toAdd.add(toRet);
    }

    private void writeSecretImage(Image.ImageIterator iterator, List<Integer> pixels) {
        for (Integer pixel:pixels) {
            iterator.setNextPixel(pixel);
        }
    }
}
