import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class Main {
    public static void main(String[] args) {
        try {
            FileServer.startServer();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Sunucu başlatılırken bir hata oluştu: " + e.getMessage());
        }
    }
}
