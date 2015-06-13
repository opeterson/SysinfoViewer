package ca.owenpeterson.sysinfoviewer.saxhandlers;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;

import ca.owenpeterson.sysinfoviewer.models.Adapter;
import ca.owenpeterson.sysinfoviewer.models.Temperature;

/**
 * This is a SAX Handler class that parses the XML file out into a list of Adapters.
 *
 * Created by owen on 6/13/15.
 */
public class SysinfoStreamHandler extends DefaultHandler {
    private boolean parsingAdapter;
    private boolean parsingName;
    private boolean parsingType;
    private boolean parsingAdapters;
    private boolean parsingValue;
    private boolean parsingTemperature;
    private boolean parsingTemperatures;
    private Adapter currentAdapter;
    private Temperature currentTemperature;
    private List<Adapter> adapterList;
    private List<Temperature> tempsForCurrentAdapter;
    private StringBuilder sysinfoItemBuilder;


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

        if (qName.equals("adapters")) {
            parsingAdapters = true;
            adapterList = new ArrayList<>();
            sysinfoItemBuilder = new StringBuilder();
        }

        if (qName.equals("adapter")) {
            parsingAdapter = true;

            currentAdapter = new Adapter();
            //Log.d("ca.owenpeterson", "Found opening 'adapters' element!");
        }

        if (qName.equals("name")) {
            parsingName = true;
        }

        if (qName.equals("type")) {
            parsingType = true;
        }

        if (qName.equals("value")) {
            parsingValue = true;
        }

        if (qName.equals("temperature")) {
            parsingTemperature = true;
            currentTemperature = new Temperature();
        }

        if (qName.equals("temperatures")) {
            parsingTemperatures = true;
            //Log.d("ca.owenpeterson", "Found opening 'temperatures' element!");
            tempsForCurrentAdapter = new ArrayList<>();
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        super.endElement(uri, localName, qName);

        if (qName.equals("adapter")) {
            adapterList.add(currentAdapter);
            parsingAdapter = false;
            //Log.d("ca.owenpeterson", "Found closing adapter element!");
        }

        if (qName.equals("name")) {
            parsingName = false;

            if (parsingAdapter && !parsingTemperatures) {
                String adapterName = sysinfoItemBuilder.toString();
                currentAdapter.setName(adapterName);
                clearBuilder();
                //Log.d("ca.owenpeterson", "Adapter Name: " + adapterName);
            }

            if (parsingAdapter && parsingTemperature) {
                String temperatureName = sysinfoItemBuilder.toString();
                currentTemperature.setName(temperatureName);
                clearBuilder();
                //Log.d("ca.owenpeterson", "Temperature Name: " + temperatureName);
            }
        }

        if (qName.equals("type")) {
            parsingType = false;

            if (parsingAdapter) {
                String adapterType = sysinfoItemBuilder.toString();
                currentAdapter.setType(adapterType);
                clearBuilder();
                //Log.d("ca.owenpeterson", "Adapter type: " + adapterType);
            }
        }

        if (qName.equals("value")) {
            parsingValue = false;

            String temperatureValue = sysinfoItemBuilder.toString();
            currentTemperature.setValue(temperatureValue);
            clearBuilder();
            //Log.d("ca.owenpeterson", "TemperatureValue: " + temperatureValue);
        }

        if (qName.equals("temperature")) {
            parsingTemperature = false;
            tempsForCurrentAdapter.add(currentTemperature);
        }

        if (qName.equals("temperatures")) {
            parsingTemperatures = false;
            currentAdapter.setTemperatures(tempsForCurrentAdapter);
            //Log.d("ca.owenpeterson", "Found closing 'temperatures' element!");
        }

        if (qName.equals("adapters")) {
            parsingAdapters = false;
//            Log.d("ca.owenpeterson", "Finished parsing list of adapters!");
//            Log.d("ca.owenpeterson", String.valueOf(adapterList.size()));
//
//            for (int i = 0; i < adapterList.size(); i++) {
//                Adapter a = adapterList.get(i);
//                Log.d("ca.owenpeterson", a.getName());
//                Log.d("ca.owenpeterson", a.getType());
//
//                for(Temperature t : a.getTemperatures()) {
//                    Log.d("ca.owenpeterson", t.getName());
//                    Log.d("ca.owenpeterson", t.getValue());
//                }
//            }
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        super.characters(ch, start, length);

        String chars = new String(ch, start, length);

        if (parsingAdapter && parsingName && !parsingTemperature) {
            sysinfoItemBuilder.append(chars);
        }

        if (parsingAdapter && parsingType) {
            sysinfoItemBuilder.append(chars);
        }

        if (parsingAdapter && parsingTemperatures) {
            if (parsingName) {
                sysinfoItemBuilder.append(chars);
            }

            if (parsingValue) {
                sysinfoItemBuilder.append(chars);
            }
        }
    }


    private void clearBuilder() {
        sysinfoItemBuilder.setLength(0);
    }

    public List<Adapter> getAdapterList() {
        return adapterList;
    }
}
