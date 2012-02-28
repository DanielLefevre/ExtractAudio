package rtl.extract;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class Download {

    public static void getFile(String host, String directory) {
        InputStream input = null;
        FileOutputStream writeFile = null;

        try {
            URL url = new URL(host);
            URLConnection connection = url.openConnection();
            int fileLength = connection.getContentLength();

            if (fileLength == -1) {
                System.out.println("Invalid URL or file.");
                return;
            }

            input = connection.getInputStream();
            String fileName = url.getFile().substring(
                    url.getFile().lastIndexOf('/') + 1);
            writeFile = new FileOutputStream(directory + fileName);
            byte[] buffer = new byte[1024];
            int read;

            while ((read = input.read(buffer)) > 0)
                writeFile.write(buffer, 0, read);
            writeFile.flush();
        } catch (IOException e) {
            System.out.println("Error while trying to download the file.");
            e.printStackTrace();
        } finally {
            try {
                writeFile.close();
                input.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
