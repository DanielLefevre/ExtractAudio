package rtl.extract.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.StringTokenizer;

public class Page extends URLAdress {

    private AudioFile file;

    public Page(String urlIn) throws MalformedURLException {
        super(urlIn);
    }

    public Page(URL urlIn) {
        super(urlIn);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        Page other = (Page) obj;
        if (this.file == null) {
            if (other.file != null)
                return false;
        } else if (!this.file.equals(other.file))
            return false;
        return true;
    }

    public void extract() throws IOException {

        BufferedReader in = new BufferedReader(new InputStreamReader(
                this.url.openStream()));

        while (this.file == null) {
            try {
                String inputLine = in.readLine();
                StringTokenizer st = new StringTokenizer(inputLine);

                while (st.hasMoreTokens() && this.file == null) {
                    String s = st.nextToken();

                    if (s.startsWith("href=\"http://media.rtl.fr/online/sound/")
                            && s.contains(".mp3")) {
                        s = s.split("\"")[1];
                        String title = this.url.toString().split("/")[6];
                        title = title.replace("-", " ");
                        title = title.substring(0, title.length() - 11);
                        this.file = new AudioFile(s, title);
                    }
                }
            } catch (NullPointerException e) {
                // Do nothing.
            }
        }

        in.close();
    }

    public AudioFile getFile() {
        return this.file;
    }

    public URL getUrl() {
        return this.url;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result
                + ((this.file == null) ? 0 : this.file.hashCode());
        return result;
    }

    public void setFile(AudioFile fileIn) {
        this.file = fileIn;
    }
}
