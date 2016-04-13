package eco_tickets.ecotickets;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.zxing.Result;

import io.realm.Realm;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ScannerActivity extends Activity implements ZXingScannerView.ResultHandler {

    private ZXingScannerView mScannerView;
    private Realm realm;

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);

        realm = Realm.getDefaultInstance();

        mScannerView = new ZXingScannerView(this);   // Programmatically initialize the scanner view
        setContentView(mScannerView);                // Set the scanner view as the content view

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

// Verifica se o qrCode está na lista de convidados e ainda não entrou no evento
        Ingresso ingresso = realm.where(Ingresso.class).equalTo("qrCode", result.getText()).findFirst();

// Se o código não é válido
        if (!ingresso.isValid() || !ingresso.getQrCode().equals(result.getText())) {
            Toast.makeText(ScannerActivity.this, "Código inválido.",
                    Toast.LENGTH_SHORT).show();

// Continua a ler
            mScannerView.resumeCameraPreview(this);

        }

// Se o código está atribuido a uma pessoa chama a tela de check-in
        else {

            Intent intent = new Intent(this, CheckInActivity.class);
            intent.putExtra("NOME", ingresso.getNome());
            intent.putExtra("DOCUMENTO", ingresso.getDocumento());
            intent.putExtra("QRCODE", ingresso.getQrCode());
            intent.putExtra("CHECKED", ingresso.isChecked());
            startActivity(intent);

        }

    }

}
