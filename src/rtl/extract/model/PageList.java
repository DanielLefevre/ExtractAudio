package rtl.extract.model;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class PageList extends URLAdress {

    private int i;

    public PageList(int iIn) throws MalformedURLException {
        super("http://www.rtl.fr/emission/z-comme-zemmour/ecouter?page=" + iIn);
        this.i = iIn;
    }

    public List<String> extract() throws IOException {
        List<String> pages = new ArrayList<>();
        StringTokenizer st = new StringTokenizer(this.getCode());

        while (st.hasMoreTokens()) {
            String s = st.nextToken();
            if (s.startsWith("href=\"http://www.rtl.fr/emission/z-comme-zemmour/ecouter/")) {
                s = s.split("\"")[1];
                if (!pages.contains(s)) {
                    pages.add(s);
                }
            }
        }

        String display = new String(this.i + " pages-list extracted...");
        for (int j = 0; j < display.length(); j++)
            System.out.print("\b");
        System.out.print(display);

        return pages;
    }
}
