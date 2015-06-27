package ca.owenpeterson.sysinfoviewer.service;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import ca.owenpeterson.sysinfoviewer.listeners.OnSensorsRead;
import ca.owenpeterson.sysinfoviewer.models.Sensors;


/**
 * This class will use the methods in the SystemInfoServiceClient jar to contact the service
 * running on my server and get the sensor information back as an object.
 * The sensors object is essentially a DTO (Data Transfer Object).
 *
 * Created by owen on 6/27/15.
 */
public class ServiceContact  extends AsyncTask<Void, Void, Sensors> {

    private ProgressDialog dialog;
    private Context context;
    private OnSensorsRead listener;

    public ServiceContact(OnSensorsRead listener, Context context) {
        this.listener = listener;
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        dialog = new ProgressDialog(context);
        dialog.setMessage("Contacting Server");
        dialog.show();
    }

    @Override
    protected Sensors doInBackground(Void... params) {
        SystemInfoService systemInfoService = new SystemInfoServiceImpl();
        Sensors sensors = systemInfoService.getSystemSensorInfo();
        return sensors;
    }

    @Override
    protected void onPostExecute(Sensors sensors) {
        super.onPostExecute(sensors);

        if (dialog.isShowing()) {
            dialog.dismiss();
        }

        listener.onSensorsRead();
    }
}
