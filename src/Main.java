public class Main {
    private final static int COUNT_ARGS = 4;

    public static void main(String[] args) {
        if (args.length != COUNT_ARGS) {
            System.out.println("Uso: java Main <modo> <imagen> <k> <directorio_sombras>");
            return;
        }
        ArgumentsParser parser = new ArgumentsParser(args[0], args[1], args[2], args[3]);
        SSSencoder sss = new SSSencoder(parser);
        if (parser.getMode().equals("d")) {
            sss.distribute();
        } else {
            sss.recover();
        }
    }
}
