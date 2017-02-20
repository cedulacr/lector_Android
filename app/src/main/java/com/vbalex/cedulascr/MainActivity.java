package com.vbalex.cedulascr;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.microblink.activity.Pdf417ScanActivity;
import com.microblink.recognizers.BaseRecognitionResult;
import com.microblink.recognizers.RecognitionResults;
import com.microblink.recognizers.blinkbarcode.pdf417.Pdf417RecognizerSettings;
import com.microblink.recognizers.blinkbarcode.pdf417.Pdf417ScanResult;
import com.microblink.recognizers.settings.RecognitionSettings;
import com.microblink.recognizers.settings.RecognizerSettings;
import com.microblink.results.barcode.BarcodeDetailedData;


import android.widget.ArrayAdapter;
import com.microblink.view.recognition.RecognizerView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends Activity {

    // demo license key for package com.microblink.barcode
    // obtain your licence key at http://microblink.com/login or
    // contact us at http://help.microblink.com
    private static final String LICENSE_KEY = LicenciaMicroBlink.Licencia;
    private static final int MY_REQUEST_CODE = 1337;

    private DBHelper db;
    private ArrayList<String> als = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView tvVersion = (TextView) findViewById(R.id.tvVersion);
        tvVersion.setText(buildVersionString());
        this.db = new DBHelper(this);
        List<Persona> personas = this.db.getAll();

        for (Persona p : personas) {
            this.als.add(p.toString());
        }

        ListView listView = (ListView) findViewById(R.id.lista);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, this.als);

        listView.setAdapter(adapter);
    }

    /**
     * Builds string which contains information about application version and library version.
     * @return String which contains information about application version and library version.
     */
    private String buildVersionString() {
        String nativeVersionString = RecognizerView.getNativeLibraryVersionString();
        PackageInfo pInfo;
        try {
            pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            String appVersion = pInfo.versionName;
            int appVersionCode = pInfo.versionCode;

            StringBuilder infoStr = new StringBuilder();
            infoStr.append("Application version: ");
            infoStr.append(appVersion);
            infoStr.append(", build ");
            infoStr.append(appVersionCode);
            infoStr.append("\nLibrary version: ");
            infoStr.append(nativeVersionString);
            return infoStr.toString();
        } catch (NameNotFoundException e) {
            return "";
        }
    }

    public void btnScan_click(View v) {
        // Intent for ScanActivity
        Intent intent = new Intent(this, Pdf417ScanActivity.class);

        // If you want sound to be played after the scanning process ends,
        // put here the resource ID of your sound file. (optional)
        intent.putExtra(Pdf417ScanActivity.EXTRAS_BEEP_RESOURCE, R.raw.beep);

        // In order for scanning to work, you must enter a valid licence key. Without licence key,
        // scanning will not work. Licence key is bound the the package name of your app, so when
        // obtaining your licence key from Microblink make sure you give us the correct package name
        // of your app. You can obtain your licence key at http://microblink.com/login or contact us
        // at http://help.microblink.com.
        // Licence key also defines which recognizers are enabled and which are not. Since the licence
        // key validation is performed on image processing thread in native code, all enabled recognizers
        // that are disallowed by licence key will be turned off without any error and information
        // about turning them off will be logged to ADB logcat.
        intent.putExtra(Pdf417ScanActivity.EXTRAS_LICENSE_KEY, LICENSE_KEY);

        // Pdf417RecognizerSettings define the settings for scanning plain PDF417 barcodes.
        Pdf417RecognizerSettings pdf417RecognizerSettings = new Pdf417RecognizerSettings();
        pdf417RecognizerSettings.setNullQuietZoneAllowed(true);
        RecognitionSettings recognitionSettings = new RecognitionSettings();
        recognitionSettings.setRecognizerSettingsArray(new RecognizerSettings[]{pdf417RecognizerSettings});

        // additionally, there are generic settings that are used by all recognizers or the
        // whole recognition process

        // finally send that settings object over intent to scan activity
        // use Pdf417ScanActivity.EXTRAS_RECOGNITION_SETTINGS to set recognizer settings
        intent.putExtra(Pdf417ScanActivity.EXTRAS_RECOGNITION_SETTINGS, recognitionSettings);

        // if you do not want the dialog to be shown when scanning completes, add following extra
        // to intent
        intent.putExtra(Pdf417ScanActivity.EXTRAS_SHOW_DIALOG_AFTER_SCAN, false);

        // if you want to enable pinch to zoom gesture, add following extra to intent
        intent.putExtra(Pdf417ScanActivity.EXTRAS_ALLOW_PINCH_TO_ZOOM, true);

        // if you want Pdf417ScanActivity to display rectangle where camera is focusing,
        // add following extra to intent
        intent.putExtra(Pdf417ScanActivity.EXTRAS_SHOW_FOCUS_RECTANGLE, true);

        // if you want to use camera fit aspect mode to letterbox the camera preview inside
        // available activity space instead of cropping camera frame (default), add following
        // extra to intent.
        // Camera Fit mode does not look as nice as Camera Fill mode on all devices, especially on
        // devices that have very different aspect ratios of screens and cameras. However, it allows
        // all camera frame pixels to be processed - this is useful when reading very large barcodes.
//        intent.putExtra(Pdf417ScanActivity.EXTRAS_CAMERA_ASPECT_MODE, (Parcelable) CameraAspectMode.ASPECT_FIT);

        // Start Activity
        startActivityForResult(intent, MY_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MY_REQUEST_CODE && resultCode == Pdf417ScanActivity.RESULT_OK) {
            // First, obtain recognition result
            RecognitionResults results = data.getParcelableExtra(Pdf417ScanActivity.EXTRAS_RECOGNITION_RESULTS);
            // Get scan results array. If scan was successful, array will contain at least one element.
            // Multiple element may be in array if multiple scan results from single image were allowed in settings.
            BaseRecognitionResult[] resultArray = results.getRecognitionResults();

            // Each recognition result corresponds to active recognizer. As stated earlier, there are 1types of
            // recognizers available (PDF417)


            for(BaseRecognitionResult res : resultArray) {
                if(res instanceof Pdf417ScanResult) { // check if scan result is result of Pdf417 recognizer
                    Pdf417ScanResult result = (Pdf417ScanResult) res;
                    boolean uncertainData = result.isUncertain();
                    BarcodeDetailedData rawData = result.getRawData();
                    byte[] rawDataBuffer = rawData.getAllData();
                    Persona p;

                    // add data to string builder
                    if (uncertainData) {
                        Toast.makeText(getApplicationContext(),
                                "El codigo no se reconocio", Toast.LENGTH_SHORT).show();
                    }else{
                        p  = CedulaCR.parse(rawDataBuffer);
                        this.db.add(p);
                        this.als.add(p.toString());
                        Toast.makeText(getApplicationContext(),
                                p.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }
}