package rtl.extract;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

public class Main {

    private static List<String> list1 = new ArrayList<>();
    private static List<String> list2 = new ArrayList<>();

    public static void main(String[] args) throws IOException, JDOMException {
        if (args.length != 0 && !args[0].equals("--update") && !new File("urls.xml").exists()) {
            int i = 1;
            boolean again;
            String prefix = "http://www.rtl.fr/emission/z-comme-zemmour/ecouter?page=";
            do {
                again = analyze1(getCode(prefix + i));
                System.out.println("page : " + i + " extracted.");
                i++;
            } while (again);

            i = 1;
            for (String s : list1) {
                getCodeAndAnalyze2(s);
                System.out.println("page-file " + i + " analyzed on "
                        + list1.size() + ".");
                i++;
            }

            save("urls.xml");

            System.out.println("URLs found and all saved.");
            
        } else {
            load("urls.xml");
        }

        System.out.println("Download begins.");
        int i = 1;
        for (String s : list2) {
            URL url = new URL(s);
            if (!new File("files/" + url.getFile().substring(
                    url.getFile().lastIndexOf('/') + 1)).exists()) {
                Download.getFile(s, "files/");
                System.out.println("file " + i + " downloaded on "
                        + list2.size() + ".");
            }
            i++;
        }
        System.out.println("Everything has been downloaded.");
    }

    public static void load(String fileName) throws JDOMException, IOException {
        SAXBuilder sxb = new SAXBuilder();
        Document document = sxb.build(new File(fileName));
        Element racine = document.getRootElement();

        List<?> liste = racine.getChildren("page");

        for (Object e : liste) {
            Element page = (Element) e;
            list1.add(page.getAttributeValue("url"));

            Element file = (Element) page.getChildren().get(0);
            list2.add(file.getAttributeValue("url"));

            System.out.println("Loaded " + list2.size() + " elements.");
        }
        System.out.println("URLs loaded.");
    }

    public static void save(String fileName) throws FileNotFoundException,
            IOException {
        Element racine = new Element("rtlextract");

        Document document = new Document(racine);

        for (String s2 : list1) {
            Element page = new Element("page");
            page.setAttribute(new Attribute("url", s2));

            Element file = new Element("file");
            file.setAttribute(new Attribute("url", list2.get(list1.indexOf(s2))));
            page.addContent(file);

            racine.addContent(page);
        }

        XMLOutputter sortie = new XMLOutputter(Format.getPrettyFormat());
        sortie.output(document, new FileOutputStream(fileName));
    }

    public static String getCode(String url) throws IOException {
        String code = new String();

        URL site = new URL(url);

        BufferedReader in = null;

        in = new BufferedReader(new InputStreamReader(site.openStream()));

        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            code = code + "\n" + (inputLine);
        }

        in.close();

        return code;
    }

    public static void getCodeAndAnalyze2(String url) throws IOException {

        URL site = new URL(url);

        BufferedReader in = null;
        in = new BufferedReader(new InputStreamReader(site.openStream()));

        String inputLine;
        boolean again = true;

        while (again) {
            inputLine = in.readLine();
            StringTokenizer st = new StringTokenizer(inputLine);

            while (st.hasMoreTokens()) {
                String s = st.nextToken();

                if (s.startsWith("href=\"http://media.rtl.fr/online/sound/")
                        && s.contains(".mp3")) {
                    s = s.split("\"")[1];
                    list2.add(s);
                    again = false;
                    break;
                }
            }
        }

        in.close();
    }

    public static boolean analyze1(String code) {
        StringTokenizer st = new StringTokenizer(code);

        int i = 0;
        while (st.hasMoreTokens()) {
            String s = st.nextToken();
            if (s.startsWith("href=\"http://www.rtl.fr/emission/z-comme-zemmour/ecouter/")) {
                s = s.split("\"")[1];
                if (!list1.contains(s)) {
                    list1.add(s);
                }
                i++;
            }
        }
        return i != 0;
    }

}
