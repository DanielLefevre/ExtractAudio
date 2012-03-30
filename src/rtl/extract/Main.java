package rtl.extract;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jdom.JDOMException;

import rtl.extract.display.Display;
import rtl.extract.model.Page;
import rtl.extract.model.PageList;
import rtl.extract.xml.ReaderXML;
import rtl.extract.xml.WriterXML;

public class Main {

    private static List<Page> pageList = new ArrayList<>();

    public static void main(String[] args) throws IOException, JDOMException {
        if ((args.length != 0 && args[0].equals("--update"))) {
            // Updates.
            update();
            new WriterXML("urls.xml").save(pageList);
            System.out.println("URLs found and all saved in urls.xml.");
        } else if ((args.length != 0 && args[0].equals("--updateall"))
                || !new File("urls.xml").exists()) {
            // Updates all.
            updateAll();
            new WriterXML("urls.xml").save(pageList);
            System.out.println("URLs found and all saved in urls.xml.");
        } else {
            // Loads URLs.
            System.out.println("Loading the URLS of the files : ");
            pageList = new ReaderXML("urls.xml").load();
            System.out.println("URLs loaded from urls.xml.");
        }

        // Downloads.
        System.out.println("Downloading the mp3 files : ");
        int i = 1;
        for (Page p : pageList) {
            p.getFile().download();
            Display.display(i + " files downloaded on " + pageList.size());
            i++;
        }
        System.out.println("Everything has been downloaded.");
    }

    public static void update() throws IOException, JDOMException {

        System.out.println("Extraction of the page-lists : ");
        int i = 1;
        List<String> pagesURLS = new ArrayList<>();

        do {
            pagesURLS = new PageList(i).extract();
            for (String s : pagesURLS) {
                pageList.add(new Page(s));
            }
            Display.displayProgression(i + " pages-lists extracted...");
            i++;
        } while (!pagesURLS.isEmpty());
        Display.display("");
        System.out.println("Extraction of the page-lists done.");

        System.out.println("Loading the URLS of the files : ");
        List<Page> loadList = new ReaderXML("urls.xml").load();
        System.out.println("URLs loaded from urls.xml.");

        System.out.println("Extraction of the pages : ");
        int j = 0;
        for (Page p : pageList) {
            if (!loadList.contains(p)) {
                p.extract();
                p.getFile().downloadContext();
                loadList.add(p);
            }

            j++;
            Display.displayProgression(j + " pages analyzed on "
                    + pageList.size() + "...");
        }

        pageList = loadList;

        System.out.println("Extraction of the page done.");
    }

    public static void updateAll() throws IOException {

        System.out.println("Extraction of the page-lists : ");

        int i = 1;
        List<String> pagesURLS = new ArrayList<>();

        do {
            pagesURLS = new PageList(i).extract();
            for (String s : pagesURLS) {
                pageList.add(new Page(s));
            }
            Display.displayProgression(i + " pages-lists extracted...");
            i++;
        } while (!pagesURLS.isEmpty());
        Display.display("");

        System.out.println("Extraction done.");

        System.out.println("Extraction of the pages : ");

        int j = 0;

        for (Page p : pageList) {
            p.extract();
            p.getFile().downloadContext();

            Display.displayProgression(j + " pages analyzed on "
                    + pageList.size() + "...");
            j++;
        }
    }
}
