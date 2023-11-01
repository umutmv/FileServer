import com.sun.net.httpserver.HttpServer;
import java.io.*;
import java.net.InetSocketAddress;

public class FileServer {
    static final String STORAGE_DIRECTORY = "C:\\Users\\umutm\\OneDrive\\Masaüstü\\ServerStorage\\";

    /* public static void main(String[] args) {
    }*/
    public static void startServer() throws IOException {

        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.createContext("/upload", new FileHandler());
        server.setExecutor(null); // gelen istekleri sırayla işle
        server.start();

    }
}


