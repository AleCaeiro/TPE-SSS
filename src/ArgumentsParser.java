public class ArgumentsParser {
    private String mode;
    private String imgPath;
    private int k;
    private String shadesDirectory;

    private int BLOCK_SIZE;

    public ArgumentsParser(String mode, String imgPath, String k, String shadesDirectory) {

        if(!mode.equals("d") && !mode.equals("r")){
            System.out.println("Los modos validos son 'd' o 'r'");
            return;
        }

        this.mode = mode;
        this.imgPath = imgPath;
        this.k = Integer.parseInt(k);
        this.shadesDirectory = shadesDirectory;

        this.BLOCK_SIZE = (2*this.k)-2;

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

    public int getBLOCK_SIZE() {
        return BLOCK_SIZE;
    }
}
