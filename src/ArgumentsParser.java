public class ArgumentsParser {
    private String mode;
    private String imgPath;
    private Integer k;
    private String shadesDirectory;

    public ArgumentsParser(String mode, String imgPath, String k, String shadesDirectory) {

        try {
            if (!mode.equals("d") && !mode.equals("r")) {
                throw new UnsupportedOperationException("Los modos validos son 'd' o 'r'");
            }
        } catch (UnsupportedOperationException e) {
            System.out.println("Error: " + e.getMessage());
        }

        int kNum = Integer.parseInt(k);

        try {
            if (kNum < 3 || kNum > 8) {
                throw new UnsupportedOperationException("Los k validos solo son 3, 4, 5, 6, 7 y 8");
            }
        } catch (UnsupportedOperationException e) {
            System.out.println("Error: " + e.getMessage());
        }

        this.mode = mode;
        this.imgPath = imgPath;
        this.k = kNum;
        this.shadesDirectory = shadesDirectory;

        //TODO chequear que shadesdirectory Debe contener
        //imágenes de extensión .bmp, de 8 bits por píxel, de igual tamaño que la imagen secreta.
        //Además, deberá verificarse que existan por lo menos k imágenes en el directorio
    }

    public String getMode() {
        return mode;
    }

    public String getImgPath() {
        return imgPath;
    }

    public int getK() {
        return k;
    }

    public String getShadesDirectory() {
        return shadesDirectory;
    }

}
