import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ArgumentsParser {
    private String mode;
    private Image secretImage;
    private int k;
    private int blockSize;
    private List<Image> images;

    public ArgumentsParser(String mode, String imgPath, String k, String shadesDirectory) {
        if (!mode.equals("d") && !mode.equals("r")) {
            System.out.println("Los modos validos son d y r");
            System.exit(1);
        }

        int kNum = Integer.parseInt(k);

        if (kNum < 3 || kNum > 8) {
            System.out.println("Los k validos solo son 3, 4, 5, 6, 7 y 8");
            System.exit(1);
        }

        File folder = new File(shadesDirectory);
        if (!folder.exists()) {
            System.out.println("El path al directorio especificado es erroneo o  no existe");
            System.exit(1);
        }

        this.images = getBMPImages(shadesDirectory);

        if (mode.equals("d")) {
            this.secretImage = new Image(imgPath);
            File file = new File(imgPath);
            if (!file.exists() || !file.isFile()) {
                System.out.println("La imagen a ocultar no existe");
                System.exit(1);
            }
            if (!file.getName().toLowerCase().endsWith(".bmp")) {
                System.out.println("La imagen a ocultar no es de formato .bmp");
                System.exit(1);
            }

            for (Image shadeImage : images) {
                if (shadeImage.getWidth() != secretImage.getWidth() || shadeImage.getHeight() != secretImage.getHeight()) {
                    System.out.println("La imagen portadora " + shadeImage.getFilePath() + " no es del mismo tamaño que la imagen a ocultar");
                    System.exit(1);
                }
            }
        } else {
            int height = images.get(0).getHeight();
            int width = images.get(0).getWidth();
            for (Image shadeImage : images) {
                if (shadeImage.getWidth() != width || shadeImage.getHeight() != height) {
                    System.out.println("La imagen portadora " + shadeImage.getFilePath() + " no es del mismo tamaño que las demas");
                    System.exit(1);
                }
            }

            this.secretImage = new Image(imgPath, height, width);
        }
        this.k = kNum;
        this.blockSize = (2 * this.k) - 2;

        if ((this.secretImage.getTotalSize()) % (this.blockSize) != 0) {
            System.out.println("La imagen no es divisible por la dimension del bloque");
            System.exit(1);
        }

        if (images.size() < this.k) {
            System.out.println("No existen al menos k = " + this.k + " imagenes en el directorio");
            System.exit(1);
        }

        this.mode = mode;
    }

    public String getMode() {
        return mode;
    }

    public Image getSecretImage() {
        return this.secretImage;
    }

    public int getK() {
        return k;
    }

    public List<Image> getImages() {
        return images;
    }

    public int getBlockSize() {
        return blockSize;
    }

    private List<Image> getBMPImages(String pathDirectory) {
        List<Image> imageList = new ArrayList<>();
        File folder = new File(pathDirectory);
        File[] files = folder.listFiles();

        if (files != null) {
            for (File file : files) {
                if (file.getName().toLowerCase().endsWith(".bmp")) {
                    imageList.add(new Image(pathDirectory + '/' + file.getName()));
                }
            }
        }

        return imageList;
    }
}
