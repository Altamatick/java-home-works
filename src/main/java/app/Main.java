package app;

public class Main {
    public static void main(String[] args) {
        if (args.length > 0 && "server".equals(args[0])) {
            Server.main(args);
        } else if (args.length > 0 && "client".equals(args[0])) {
            Client.main(args);
        } else {
            System.out.println("Usage: java Main server | java Main client");
        }
    }
}
