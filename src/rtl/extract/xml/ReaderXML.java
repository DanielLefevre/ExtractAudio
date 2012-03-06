package rtl.extract.xml;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import rtl.extract.display.Display;
import rtl.extract.model.AudioFile;
import rtl.extract.model.Page;

public class ReaderXML {

    private String fileName;

    public ReaderXML(String fileNameIn) {
        this.fileName = fileNameIn;
    }

    public List<Page> load() throws JDOMException, IOException {
        List<Page> pageList = new ArrayList<>();

        SAXBuilder sxb = new SAXBuilder();
        Document document = sxb.build(new File(this.fileName));
        Element racine = document.getRootElement();

        List<?> liste = racine.getChildren("page");

        for (Object e : liste) {
            Element page = (Element) e;
            Page p = new Page(page.getAttributeValue("url"));
            pageList.add(p);

            Element file = (Element) page.getChildren().get(0);
            AudioFile f = new AudioFile(file.getAttributeValue("url"),
                    file.getAttributeValue("title"));
            f.setPath(file.getAttributeValue("path"));
            f.setSize(file.getAttribute("size").getIntValue());
            f.setYear(file.getAttribute("year").getIntValue());
            f.setMonth(file.getAttribute("month").getIntValue());
            f.setDay(file.getAttribute("day").getIntValue());
            p.setFile(f);

            Display.displayProgression(pageList.size() + " URLs loaded.");
        }

        return pageList;
    }
}
