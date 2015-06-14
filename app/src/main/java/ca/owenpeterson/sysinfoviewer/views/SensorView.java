package ca.owenpeterson.sysinfoviewer.views;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import ca.owenpeterson.sysinfoviewer.R;
import ca.owenpeterson.sysinfoviewer.listeners.OnSensorsRead;
import ca.owenpeterson.sysinfoviewer.models.Adapter;
import ca.owenpeterson.sysinfoviewer.models.Temperature;
import ca.owenpeterson.sysinfoviewer.saxhandlers.SysinfoStreamHandler;
import ca.owenpeterson.sysinfoviewer.saxparsers.SysinfoParser;
import ca.owenpeterson.sysinfoviewer.utils.ServerUrlStorage;


public class SensorView extends Activity {

    private List<Adapter> adapterList;
    private SysinfoStreamHandler handler;
    private SysinfoParser parser;
    private OnSensorsReadListener listener;
    private URL resourceLocation;
    private LinearLayout adapterPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor_view);
        adapterPane = (LinearLayout) findViewById(R.id.pane_adapters);

        try {
            resourceLocation = new URL(ServerUrlStorage.getServerURL());
        } catch (MalformedURLException mex) {
            Log.e(this.getClass().getName(), "The supplied URL is not valid! " + mex.getMessage());
        }

        handler = new SysinfoStreamHandler();
        listener = new OnSensorsReadListener();
        createAndExecuteParser();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sensor_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch(id) {
            case R.id.action_settings:
                //do nothing
                break;
            case R.id.action_refresh:
                //reload the view
                adapterPane.removeAllViews();
                createAndExecuteParser();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private class OnSensorsReadListener implements OnSensorsRead {
        @Override
        public void onSensorsRead() {
            adapterList = handler.getAdapterList();
            Log.d(this.getClass().getName(), "List of Adapters Loaded!");

            if (adapterList != null && adapterList.size() > 0) {
                buildAndPopulateView();
                Log.d(this.getClass().getName(), "Layouts Built!");
            } else {
                //display a server connection error message.
                AlertDialog.Builder builder = new AlertDialog.Builder(SensorView.this);
                builder.setMessage("Failed to contact server. The server may be offline. Check your connection.")
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }
        }
    }

    private void buildAndPopulateView() {
        LayoutInflater inflater = LayoutInflater.from(this);
        for (Adapter adapter : adapterList) {
            LinearLayout adapterLayout = (LinearLayout)  inflater.inflate(R.layout.adapter_layout, null, false);

            String adapterName = adapter.getName();
            String adapterType = adapter.getType();

            if (adapterName != null) {
                TextView adapterNameView = (TextView) adapterLayout.findViewById(R.id.adapter_name);
                adapterNameView.setText(adapterName);
            }

            if (adapterType != null) {
                TextView adapterTypeView = (TextView) adapterLayout.findViewById(R.id.adapter_type);
                adapterTypeView.setText(adapterType);
            }

            for (Temperature temperature : adapter.getTemperatures()) {
                LinearLayout temperatureLayout = (LinearLayout) inflater.inflate(R.layout.temperature_layout, null, false);

                String temperatureName = temperature.getName();
                String temperatureValue = temperature.getValue();

                if (temperatureName != null) {
                    TextView temperatureNameView = (TextView) temperatureLayout.findViewById(R.id.temperature_name);
                    temperatureNameView.setText(temperatureName);
                }

                if (temperatureValue != null) {
                    TextView temperatureValueView = (TextView) temperatureLayout.findViewById(R.id.temperature_value);
                    temperatureValueView.setText(temperatureValue);
                }

                adapterLayout.addView(temperatureLayout);
            }

            adapterPane.addView(adapterLayout);

        }
    }

    private void createAndExecuteParser() {
        parser = new SysinfoParser(resourceLocation, handler, this, listener);
        parser.execute();
    }
}
