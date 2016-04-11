package eco_tickets.ecotickets;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import java.io.File;
import java.io.FileOutputStream;
import java.security.SecureRandom;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class GerarActivity extends AppCompatActivity {

    Button btnEnviar;
    EditText edtNome, edtDocumento, edtEmail;
    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gerar);

        setUI();
        setActions();

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

    private void setUI() {
        btnEnviar = (Button) findViewById(R.id.btn_Enviar);

        edtNome = (EditText) findViewById(R.id.edt_nome);
        edtDocumento = (EditText) findViewById(R.id.edt_documento);
        edtEmail = (EditText) findViewById(R.id.edt_email);

    }

    private void setActions() {

        btnEnviar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                enviarIngressos(v);
            }
        });
    }

    private void enviarIngressos(View view) {

        if (verificaCampos() == false ){
            return;
        }

        String vlNome = edtNome.getText().toString();
        String vlDocumento = edtDocumento.getText().toString();
        String vlEmail = edtEmail.getText().toString();
        String vlQrCode;

// Testes
//        CheckBox cvChecked = (CheckBox) findViewById(R.id.cb_checkedIn);
//        cvChecked.setChecked();

// Gera Novo QrCode
        vlQrCode = randon();
//        Log.w("QrCode", vlQrCode);

//TODO Verificar se é unico

        // Salva novo ingresso no banco de dados
        realm.beginTransaction();

        Ingresso ingresso = realm.createObject(Ingresso.class);

        ingresso.setNome(vlNome);
        ingresso.setDocumento(vlDocumento);
        ingresso.setEmail(vlEmail);
        ingresso.setQrCode(vlQrCode);

        realm.commitTransaction();

// Envia por email
        enviaEmail(vlEmail, vlQrCode);

        edtNome.setText("");
        edtDocumento.setText("");
        edtEmail.setText("");
    }

    private boolean verificaCampos() {

        if (edtNome.getText().toString().trim().equals("")) {
            edtNome.setError("Campo Nome é obrigatório");
            return false;
        }
        else if (edtDocumento.getText().toString().trim().equals("")){
            edtDocumento.setError("Campo Documento é obrigatório");
            return false;
        }
        else if (edtEmail.getText().toString().trim().equals("")){
            edtEmail.setError("Campo Email é obrigatório");
            return false;
        }
        else {
            return true;
        }
    }

    private void enviaEmail(String vlEmail, String vlQrCode) {
        try {

            Bitmap bitmap = encodeAsBitmap(vlQrCode);

            File file = new File(GerarActivity.this.getExternalCacheDir(), "qrCode" + ".png");

            FileOutputStream fOut = new FileOutputStream(file);

            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
            fOut.flush();
            fOut.close();
            file.setReadable(true, false);

            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("message/rfc822");
            i.putExtra(Intent.EXTRA_EMAIL, new String[]{vlEmail});
            i.putExtra(Intent.EXTRA_SUBJECT, "Seu Ingresso");
//            i.putExtra(Intent.EXTRA_TEXT   , vlQrCode);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
            i.setType("image/png");

            try {
                startActivity(Intent.createChooser(i, "Enviar ingresso por e-mail"));
            } catch (android.content.ActivityNotFoundException ex) {
                Toast.makeText(GerarActivity.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    Bitmap encodeAsBitmap(String str) throws WriterException {

        BitMatrix result;
        try {
            result = new MultiFormatWriter().encode(str,
                    BarcodeFormat.QR_CODE, 400, 400, null);
        } catch (IllegalArgumentException iae) {
            // Unsupported format
            return null;
        }
        int w = result.getWidth();
        int h = result.getHeight();
        int[] pixels = new int[w * h];
        for (int y = 0; y < h; y++) {
            int offset = y * w;
            for (int x = 0; x < w; x++) {
                pixels[offset + x] = result.get(x, y) ? Color.BLACK : Color.WHITE;
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, 400, 0, 0, w, h);
        return bitmap;
    }

    private String randon(){

        String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        SecureRandom rnd = new SecureRandom();
        int tamString = 20;

        StringBuilder sb = new StringBuilder( tamString );
        for( int i = 0; i < tamString; i++ )
            sb.append( AB.charAt( rnd.nextInt(AB.length()) ) );
        return sb.toString();

    }

}