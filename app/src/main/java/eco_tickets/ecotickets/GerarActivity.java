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
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import java.io.File;
import java.io.FileOutputStream;
import java.security.SecureRandom;

import io.realm.Realm;

public class GerarActivity extends AppCompatActivity {

    Button btnEnviar;
    EditText edtNome, edtDocumento, edtEmail;
    TextView tvNomeEvento;

    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gerar);

        realm = Realm.getDefaultInstance();

        setUI();
        setActions();

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

        tvNomeEvento = (TextView) findViewById(R.id.txt_evento);

// Recupera o nome do evento
        String nome = ((EcoTickets) this.getApplication()).getNomeEvento();
        Evento evento = realm.where(Evento.class).findFirst();

        if ( evento != null ){
            nome = evento.getNomeEvento();
        }

        tvNomeEvento.setText(nome);

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

// Gera Novo QrCode
        String vlQrCode = randon();

//TODO Verificar se é unico

// Salva o novo ingresso no banco de dados
        realm.beginTransaction();

        Ingresso ingresso = realm.createObject(Ingresso.class);

        ingresso.setNome(edtNome.getText().toString());
        ingresso.setDocumento(edtDocumento.getText().toString());
        ingresso.setEmail(edtEmail.getText().toString());
        ingresso.setQrCode(vlQrCode);

        realm.commitTransaction();

// Envia por email
        enviaEmail(edtEmail.getText().toString(), vlQrCode);

// Limpa os campos da tela
        edtNome.setText("");
        edtDocumento.setText("");
        edtEmail.setText("");
    }

// Verifica se os campos obrigatórios estão preenchidos
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

// Chama o aplicativo de email para enviar o ingresso para o email do convidado
    private void enviaEmail(String vlEmail, String vlQrCode) {
        try {

// Gera o QrCode
            Bitmap bitmap = encodeAsBitmap(vlQrCode);

// Salva a imagem gerada no diretório de cache do armazenamento externo
            File file = new File(GerarActivity.this.getExternalCacheDir(), "qrCode" + ".png");
            FileOutputStream fOut = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
            fOut.flush();
            fOut.close();
            file.setReadable(true, false);

// Chama o aplicativo de e-mails para enviar o QrCode gerado para o destinatário
            Intent email = new Intent(Intent.ACTION_SEND);

            email.setType("message/rfc822");
            email.putExtra(Intent.EXTRA_EMAIL, new String[]{vlEmail});
            email.putExtra(Intent.EXTRA_SUBJECT, "Seu ticket para " + tvNomeEvento.getText());
            email.putExtra(Intent.EXTRA_TEXT, "Apresente o código em anexo para acessar o evento.");
            email.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            email.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
            email.setType("image/png");

            try {
                startActivity(Intent.createChooser(email, "Enviar ingresso por e-mail"));

            } catch (android.content.ActivityNotFoundException ex) {
                Toast.makeText(GerarActivity.this, "Não existem aplicativos de e-mail cadastrados.", Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

// Transforma uma string em uma imagem QrCode
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

// Gera uma string aleatória de tamanho 20
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