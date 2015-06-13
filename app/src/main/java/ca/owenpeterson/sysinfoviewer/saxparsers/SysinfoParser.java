package ca.owenpeterson.sysinfoviewer.saxparsers;

import android.os.AsyncTask;
import android.util.Log;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import ca.owenpeterson.sysinfoviewer.saxhandlers.SysinfoStreamHandler;

/**
 * Created by owen on 6/13/15.
 */
public class SysinfoParser extends AsyncTask<Void, Void, Void> {

    private String feedURL;
    private SysinfoStreamHandler handler;

    public SysinfoParser(String feedURL, SysinfoStreamHandler handler) {
        this.feedURL = feedURL;
        this.handler = handler;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Void doInBackground(Void... params) {
        URL feedLocation = null;
        try {
            feedLocation = new URL("http://192.168.100.150:8080/sysinfo/system/sensors");
            BufferedReader in = new BufferedReader(new InputStreamReader(feedLocation.openStream()));

            SAXParserFactory spf = SAXParserFactory.newInstance();
            SAXParser sp = spf.newSAXParser();

            sp.parse(new InputSource(in), handler);


        } catch (MalformedURLException mex) {
            Log.e(this.getClass().getName(), "The supplied URL is not valid! " + mex.getMessage());
        } catch (IOException iox) {
            Log.e(this.getClass().getName(), "Could not read data from the supplied URL! " + iox.getMessage());
        } catch (ParserConfigurationException pcex) {
            Log.d(this.getClass().getName(), "Could not configure new parser. " + pcex.getMessage());
        } catch (SAXException saxex) {
            Log.d(this.getClass().getName(), "Could not create new sax parser. " + saxex.getMessage());
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
    }
}
