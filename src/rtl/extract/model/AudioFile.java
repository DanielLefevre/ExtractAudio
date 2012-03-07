package rtl.extract.model;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import rtl.extract.display.Display;

public class AudioFile extends URLAdress {

    private String path;
    private int size;
    private int year;
    private int month;
    private int day;
    private String title;

    public AudioFile(String urlIn, String titleIn) throws MalformedURLException {
        super(urlIn);
        this.title = titleIn;
    }

    public AudioFile(URL urlIn) {
        super(urlIn);
    }

    public String getCompleteName() {
        return "files/" + this.year + "/" + this.day + "-" + this.month + " - "
                + this.title + ".mp3";
    }

    public void download() {
        BufferedInputStream input = null;
        BufferedOutputStream writeFile = null;

        Display.display("Downloading : " + this.getCompleteName());

        if (new File(this.getCompleteName()).exists()
                && new File(this.getCompleteName()).length() == this.size) {
            Display.display("The file has already been downloaded. ");

        } else {
            try {
                // Tries to connect.
                URLConnection connection = this.url.openConnection();

                // Gets dataflow.
                input = new BufferedInputStream(connection.getInputStream());

                writeFile = new BufferedOutputStream(new FileOutputStream(
                        new File(this.getCompleteName())));

                byte[] buffer = new byte[1024];
                int read, totalRead = 0;

                while ((read = input.read(buffer)) > 0) {
                    totalRead += read;
                    Display.displayProgression(Math.floor((double) 1000
                            * (double) totalRead / this.size)
                            / 10 + "% downloaded.");

                    writeFile.write(buffer, 0, read);
                }
                Display.display("");

                writeFile.flush();

            } catch (IOException e) {
                System.out.println("Error while trying to download the file.");
                e.printStackTrace();
            }
        }
    }

    public String getTitle() {
        return this.title;
    }

    @SuppressWarnings("boxing")
    public void downloadContext() {
        try {
            // Tries to connect.
            URLConnection connection = this.url.openConnection();

            // Gets size.
            this.size = connection.getContentLength();

            // Gets file name.
            this.path = this.url.getFile().substring(
                    this.url.getFile().lastIndexOf('/') + 1);

            // Gets the date.
            this.year = new Integer(this.url.toString().split("/")[5]);
            String temp = this.url.toString().split("/")[6];
            this.month = new Integer(temp.substring(0, 2));
            this.day = new Integer(temp.substring(2, 4));

        } catch (IOException e) {
            System.out
                    .println("Error while trying to get the context of the file.");
            e.printStackTrace();
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        AudioFile other = (AudioFile) obj;
        if (this.day != other.day)
            return false;
        if (this.month != other.month)
            return false;
        if (this.path == null) {
            if (other.path != null)
                return false;
        } else if (!this.path.equals(other.path))
            return false;
        if (this.size != other.size)
            return false;
        if (this.title == null) {
            if (other.title != null)
                return false;
        } else if (!this.title.equals(other.title))
            return false;
        if (this.year != other.year)
            return false;
        return true;
    }

    public int getDay() {
        return this.day;
    }

    public int getMonth() {
        return this.month;
    }

    public String getPath() {
        return this.path;
    }

    public int getSize() {
        return this.size;
    }

    public URL getUrl() {
        return this.url;
    }

    public int getYear() {
        return this.year;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + this.day;
        result = prime * result + this.month;
        result = prime * result
                + ((this.path == null) ? 0 : this.path.hashCode());
        result = prime * result + this.size;
        result = prime * result
                + ((this.title == null) ? 0 : this.title.hashCode());
        result = prime * result + this.year;
        return result;
    }

    public void setDay(int dayIn) {
        this.day = dayIn;
    }

    public void setMonth(int monthIn) {
        this.month = monthIn;
    }

    public void setPath(String pathIn) {
        this.path = pathIn;
    }

    public void setSize(int sizeIn) {
        this.size = sizeIn;
    }

    public void setYear(int yearIn) {
        this.year = yearIn;
    }
}
