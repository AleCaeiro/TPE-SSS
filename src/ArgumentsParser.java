public class ArgumentsParser {
    private String mode;
    private String imgPath;
    private Integer k;
    private Integer lsb;
    private String shadesDirectory;
    private Integer blockSize;

    private final static int BLOCK_MULTIPLIER = 2;

    public ArgumentsParser(String mode, String imgPath, String k, String shadesDirectory) {
        if (!mode.equals("d") && !mode.equals("r")) {
            System.out.println("Los modos validos son 'd' o 'r'");
            return;
        }

        int kNum = Integer.parseInt(k);
        if(kNum < 3 || kNum > 8) {
            System.out.println("Los k validos solo son 3, 4, 5, 6, 7, 8");
            return;
        }

        this.mode = mode;
        this.imgPath = imgPath;
        this.k = kNum;
        this.lsb = this.k > 4 ? 2 : 4;
        this.shadesDirectory = shadesDirectory;
        this.blockSize = (BLOCK_MULTIPLIER * this.k) - BLOCK_MULTIPLIER;
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

    public int getBlockSize() {
        return blockSize;
    }
}
