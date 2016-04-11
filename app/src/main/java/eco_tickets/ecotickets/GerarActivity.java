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

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.math.BigInteger;
import java.security.SecureRandom;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class GerarActivity extends AppCompatActivity {

    Button btnEnviar;
    EditText edtNome, edtDocumento, edtEmail;
    private Realm realm;

//lalalala

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

        String vlNome = edtNome.getText().toString();
        String vlDocumento = edtDocumento.getText().toString();
        String vlEmail = edtEmail.getText().toString();
        String vlQrCode;

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

    private void enviaEmail(String vlEmail, String vlQrCode) {
        try {
            File file = new File(getApplicationContext().getCacheDir(), "qrCode" + ".png");
            FileOutputStream fOut = new FileOutputStream(file);

            Bitmap bitmap = encodeToQrCode(vlQrCode, 400, 400);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);

            fOut.flush();
            fOut.close();
            file.setReadable(true, false);
            final Intent intent = new Intent(android.content.Intent.ACTION_SEND);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
            intent.setType("image/png");
            startActivity(intent);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static Bitmap encodeToQrCode(String text, int width, int height){
        QRCodeWriter writer = new QRCodeWriter();
        BitMatrix matrix = null;
        try {
            matrix = writer.encode(text, BarcodeFormat.QR_CODE, 100, 100);
        } catch (WriterException ex) {
            ex.printStackTrace();
        }
        Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        for (int x = 0; x < width; x++){
            for (int y = 0; y < height; y++){
                bmp.setPixel(x, y, matrix.get(x,y) ? Color.BLACK : Color.WHITE);
            }
        }
        return bmp;
    }

    private String randon(){
        SecureRandom random = new SecureRandom();
        return new BigInteger(20, random).toString(32);
    }

}