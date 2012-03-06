package rtl.extract.xml;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import rtl.extract.model.Page;

public class WriterXML {

    private String fileName;

    public WriterXML(String fileNameIn) {
        this.fileName = fileNameIn;
    }

    public void save(List<Page> pageList) throws FileNotFoundException,
            IOException {

        Element racine = new Element("rtlextract");
        Document document = new Document(racine);

        for (Page p : pageList) {
            Element page = new Element("page");
            page.setAttribute(new Attribute("url", p.getUrl().toString()));

            Element file = new Element("file");
            file.setAttribute(new Attribute("url", p.getFile().getUrl()
                    .toString()));
            file.setAttribute(new Attribute("title", p.getFile().getTitle()));
            file.setAttribute(new Attribute("path", p.getFile().getPath()
                    .toString()));
            file.setAttribute(new Attribute("size", new Integer(p.getFile()
                    .getSize()).toString()));
            file.setAttribute(new Attribute("year", new Integer(p.getFile()
                    .getYear()).toString()));
            file.setAttribute(new Attribute("month", new Integer(p.getFile()
                    .getMonth()).toString()));
            file.setAttribute(new Attribute("day", new Integer(p.getFile()
                    .getDay()).toString()));
            page.addContent(file);

            racine.addContent(page);
        }

        XMLOutputter sortie = new XMLOutputter(Format.getPrettyFormat());
        sortie.output(document, new FileOutputStream(this.fileName));
    }
}
