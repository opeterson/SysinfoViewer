package ca.owenpeterson.sysinfoviewer.views;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.net.URL;
import java.util.List;

import ca.owenpeterson.sysinfoviewer.R;
import ca.owenpeterson.sysinfoviewer.listeners.OnSensorsRead;
import ca.owenpeterson.sysinfoviewer.models.Adapter;
import ca.owenpeterson.sysinfoviewer.saxhandlers.SysinfoStreamHandler;
import ca.owenpeterson.sysinfoviewer.saxparsers.SysinfoParser;


public class SensorView extends Activity {

    private List<Adapter> adapterList;
    private SysinfoStreamHandler handler;
    private SysinfoParser parser;
    private OnSensorsReadListener listener;
    private URL resourceLoation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor_view);
    }

    @Override
    protected void onStart() {
        super.onStart();
        handler = new SysinfoStreamHandler();
        listener = new OnSensorsReadListener();
        parser = new SysinfoParser("", handler, this, listener);
        parser.execute();
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class OnSensorsReadListener implements OnSensorsRead {
        @Override
        public void onSensorsRead() {
            adapterList = handler.getAdapterList();
            buildAndPopulateView();

            Log.d(this.getClass().getName(), "List of Adapters Loaded!");
        }
    }

    private void buildAndPopulateView() {

    }
}
