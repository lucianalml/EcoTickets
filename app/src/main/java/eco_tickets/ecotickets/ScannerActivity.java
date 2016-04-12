package eco_tickets.ecotickets;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.google.zxing.Result;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ScannerActivity extends Activity implements ZXingScannerView.ResultHandler {

    private ZXingScannerView mScannerView;
    private Realm realm;

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        mScannerView = new ZXingScannerView(this);   // Programmatically initialize the scanner view
        setContentView(mScannerView);                // Set the scanner view as the content view

        // Create the Realm configuration
        RealmConfiguration realmConfig = new RealmConfiguration.Builder(getApplicationContext()).build();
//        Realm.deleteRealm(realmConfig); // Testes -> Exclui o banco de dados da execução anterior
        realm = Realm.getInstance(realmConfig);


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close(); // Remember to close Realm when done.
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
        mScannerView.startCamera();          // Start camera on resume
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();           // Stop camera on pause
    }

    @Override
    public void handleResult(Result result) {


        if (verificaUsuario(result.getText())){

// Atualiza banco de dados
            Ingresso ingresso = realm.createObject(Ingresso.class);
            ingresso = realm.where(Ingresso.class).equalTo("qrCode", result.getText()).findFirst();

            realm.beginTransaction();
            ingresso.setChecked(true);
            realm.commitTransaction();

        }

        // Do something with the result here
        Log.v("QRCODE", result.getText()); // Prints scan results
        Log.v("QRCODE", result.getBarcodeFormat().toString()); // Prints the scan format (qrcode, pdf417 etc.)

        // If you would like to resume scanning, call this method below:
        mScannerView.resumeCameraPreview(this);

    }

    private boolean verificaUsuario(String vQrCode) {

        Ingresso ingresso = realm.createObject(Ingresso.class);

// Verifica se o qrCode está na lista de convidados e ainda não entrou no evento
        ingresso = realm.where(Ingresso.class).equalTo("qrCode", vQrCode).findFirst();

        if (ingresso.getQrCode() == vQrCode && ingresso.isChecked() == false ) {
            return true;
        }
        else {
            return false;

        }

    }
}
