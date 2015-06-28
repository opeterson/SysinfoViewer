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

import java.util.List;
import java.util.concurrent.ExecutionException;

import ca.owenpeterson.sysinfoviewer.R;
import ca.owenpeterson.sysinfoviewer.listeners.OnSensorsRead;
import ca.owenpeterson.sysinfoviewer.models.Adapter;
import ca.owenpeterson.sysinfoviewer.models.AdapterList;
import ca.owenpeterson.sysinfoviewer.models.Sensors;
import ca.owenpeterson.sysinfoviewer.models.Temperature;
import ca.owenpeterson.sysinfoviewer.models.TemperatureList;
import ca.owenpeterson.sysinfoviewer.service.ServiceContact;

public class SensorView extends Activity {

    //private List<Adapter> adapterList;
    private OnSensorsReadListener listener;
    private LinearLayout adapterPane;
    private ServiceContact contact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor_view);
        adapterPane = (LinearLayout) findViewById(R.id.pane_adapters);

        listener = new OnSensorsReadListener();
        contactService();
    }

    private void contactService() {
        contact = new ServiceContact(listener, this);
        contact.execute();
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
                contactService();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private class OnSensorsReadListener implements OnSensorsRead {
        @Override
        public void onSensorsRead() {
            Sensors sensors = null;
            List<Adapter> adapterList = null;
            String requestDate = "";
            try {
                sensors = contact.get();
            } catch (InterruptedException ex) {
                Log.e(this.getClass().getName(), ex.getMessage());
            } catch (ExecutionException eex) {
                Log.e(this.getClass().getName(), eex.getMessage());
            }

            Log.d(this.getClass().getName(), "Sensor information Loaded!");

            if (sensors != null) {

                requestDate = sensors.getRequestDate();
                AdapterList sensorsAdapterList = sensors.getAdapters();

                if (sensorsAdapterList != null) {
                    adapterList = sensorsAdapterList.getAdapters();
                }

                if (adapterList != null && adapterList.size() > 0) {
                    buildAndPopulateView(requestDate, adapterList);
                    Log.d(this.getClass().getName(), "Layouts Built!");
                }
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

    private void buildAndPopulateView(String requestDate, List<Adapter> adapterList) {
        TextView tv_requestDate = (TextView) findViewById(R.id.request_date);
        tv_requestDate.setText(requestDate);

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

            TemperatureList adapterTemperatureList = adapter.getTemperatures();

            if (adapterTemperatureList != null) {
                List<Temperature> temperatureList = adapterTemperatureList.getTemperatures();

                for (Temperature temperature : temperatureList) {
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
            }

            adapterPane.addView(adapterLayout);

        }
    }
}
