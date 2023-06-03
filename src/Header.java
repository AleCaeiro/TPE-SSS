import java.io.*;

public class Header {
    private static final int OFFSET_RESERVED1 = 6;

    private String filePath;

    public Header(String filePath) {
        this.filePath = filePath;
    }

    public void setReserved1(short value) {
        try {
            RandomAccessFile file = new RandomAccessFile(filePath, "rw");

            file.seek(OFFSET_RESERVED1);
            file.writeShort(Short.reverseBytes(value));

            file.close();

            System.out.println("Valor bfreserved1 establecido correctamente.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public short getReserved1() {
        try {
            RandomAccessFile file = new RandomAccessFile(filePath, "r");

            file.seek(OFFSET_RESERVED1);
            short value = Short.reverseBytes(file.readShort());

            file.close();

            return value;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return -1; // Valor de error
    }
}

