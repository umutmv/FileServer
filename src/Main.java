
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
