package ca.owenpeterson.sysinfoviewer.saxhandlers;

import android.util.Log;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;

import ca.owenpeterson.sysinfoviewer.models.Adapter;

/**
 * Created by owen on 6/13/15.
 */
public class SysinfoStreamHandler extends DefaultHandler {
    private Boolean parsingAdapter;
    private Adapter currentAdapter;
    private List<Adapter> adapterList = new ArrayList<Adapter>();

    @Override
    public void startDocument() throws SAXException {
        super.startDocument();
    }

    @Override
    public void endDocument() throws SAXException {
        super.endDocument();
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        super.startElement(uri, localName, qName, attributes);

        if (qName.equals("adapter")) {
            parsingAdapter = true;

            currentAdapter = new Adapter();

            Log.d("ca.owenpeterson", "Found opening adapter element!");
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        super.endElement(uri, localName, qName);

        if (qName.equals("adapter")) {
            adapterList.add(currentAdapter);
            parsingAdapter = false;
            Log.d("ca.owenpeterson", "Found closing adapter element!");
        }
    }
}
