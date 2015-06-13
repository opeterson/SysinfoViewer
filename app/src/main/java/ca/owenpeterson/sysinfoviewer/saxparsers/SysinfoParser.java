package ca.owenpeterson.sysinfoviewer.saxparsers;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import ca.owenpeterson.sysinfoviewer.listeners.OnSensorsRead;
import ca.owenpeterson.sysinfoviewer.models.Adapter;
import ca.owenpeterson.sysinfoviewer.saxhandlers.SysinfoStreamHandler;

/**
 * Sets up the SAX Parser for use against the sensors web service.
 *
 * Created by owen on 6/13/15.
 */
public class SysinfoParser extends AsyncTask<Void, Void, List<Adapter>> {

    private URL resourceLocaton;
    private SysinfoStreamHandler handler;
    private ProgressDialog dialog;
    private Context context;
    private OnSensorsRead listener;

    public SysinfoParser(URL resourceLocation, SysinfoStreamHandler handler, Context context, OnSensorsRead listener) {
        this.resourceLocaton = resourceLocation;
        this.handler = handler;
        this.context = context;
        this.listener = listener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        dialog = new ProgressDialog(context);
        dialog.setMessage("Contacting Server");
        dialog.show();
    }

    @Override
    protected List<Adapter> doInBackground(Void... params) {
        List<Adapter> adapterList = new ArrayList<>();
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(resourceLocaton.openStream()));

            SAXParserFactory spf = SAXParserFactory.newInstance();
            SAXParser sp = spf.newSAXParser();

            sp.parse(new InputSource(in), handler);

            adapterList = handler.getAdapterList();


        } catch (MalformedURLException mex) {
            Log.e(this.getClass().getName(), "The supplied URL is not valid! " + mex.getMessage());
        } catch (IOException iox) {
            Log.e(this.getClass().getName(), "Could not read data from the supplied URL! " + iox.getMessage());
        } catch (ParserConfigurationException pcex) {
            Log.d(this.getClass().getName(), "Could not configure new parser. " + pcex.getMessage());
        } catch (SAXException saxex) {
            Log.d(this.getClass().getName(), "Could not create new sax parser. " + saxex.getMessage());
        }

        return adapterList;
    }

    @Override
    protected void onPostExecute(List<Adapter> adapters)
    {
        super.onPostExecute(adapters);

        if (dialog.isShowing()) {
            dialog.dismiss();
        }

        listener.onSensorsRead();
    }
}
