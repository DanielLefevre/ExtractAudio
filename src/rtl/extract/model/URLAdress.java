package rtl.extract.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

public class URLAdress {

    protected URL url;

    public URLAdress(String urlIn) throws MalformedURLException {
        this.url = new URL(urlIn);
    }

    public URLAdress(URL urlIn) {
        this.url = urlIn;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        URLAdress other = (URLAdress) obj;
        if (this.url == null) {
            if (other.url != null)
                return false;
        } else if (!this.url.equals(other.url))
            return false;
        return true;
    }

    public String getCode() throws IOException {
        String code = new String();

        BufferedReader in = new BufferedReader(new InputStreamReader(
                this.url.openStream()));

        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            code = code + "\n" + (inputLine);
        }
        in.close();

        return code;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((this.url == null) ? 0 : this.url.hashCode());
        return result;
    }
}
