import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

class FileHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if (exchange.getRequestMethod().equalsIgnoreCase("POST")) {
            handleFileContentUpload(exchange);
        } else if (exchange.getRequestMethod().equalsIgnoreCase("GET")) {
            handleSearchRequest(exchange);
        }
    }

    private void handleFileContentUpload(HttpExchange exchange) throws IOException {  // isteği işle, içeriği oku string olarak hedefe yaz
        System.out.println("Dosya yükleme isteği alındı." );
        try (InputStream is = exchange.getRequestBody();
             InputStreamReader isr = new InputStreamReader(is);
             BufferedReader br = new BufferedReader(isr)) {
            StringBuilder content = new StringBuilder();
            String fileName = br.readLine();
            String line;
            while ((line = br.readLine()) != null) {
                content.append(line).append("\n");
            }

            File file = new File(FileServer.STORAGE_DIRECTORY + fileName);

            try (FileWriter fileWriter = new FileWriter(file)) {
                fileWriter.write(content.toString());
            }

            exchange.sendResponseHeaders(200, 0);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Dosya yüklenirken hata oluştu." + e.getMessage());
            exchange.sendResponseHeaders(500, 0);
        }
        exchange.getResponseBody().close();
    }

    private void handleSearchRequest(HttpExchange exchange) throws IOException {
        String query = exchange.getRequestURI().getQuery(); //sorgu parametrelerini string olarak al
        String searchString = query != null ? query : "";

        if (query != null) {
            String[] queryParams = query.split("=");
            if (queryParams.length == 2 && queryParams[0].equals("search")) {
                searchString = queryParams[1];
            }
            System.out.println(searchString);
        }

        if (searchString != null) {
            List<String> searchResults = searchInFiles(searchString);
            exchange.sendResponseHeaders(200, 0);
            OutputStream os = exchange.getResponseBody();
            for (String result : searchResults) {
                os.write(result.getBytes());
                os.write("\n".getBytes());
            }
            os.close();
        } else {
            String errorMessage = "Geçersiz arama.";
            exchange.sendResponseHeaders(400, errorMessage.length());
            OutputStream os = exchange.getResponseBody();
            os.write(errorMessage.getBytes());
            os.close();
        }
    }

    private List<String> searchInFiles(String searchString) {
        List<String> searchResults = new ArrayList<>();
        File storageDir = new File(FileServer.STORAGE_DIRECTORY);
        File[] files = storageDir.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                        String line;
                        while ((line = br.readLine()) != null) {
                            if (line.contains(searchString)) {
                                searchResults.add(file.getName() + ": " + line);
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return searchResults;
    }
}
