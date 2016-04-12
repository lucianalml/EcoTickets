package eco_tickets.ecotickets;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import io.realm.Realm;

public class CheckInActivity extends AppCompatActivity {

    private Realm realm;
    Button btnConfirma;
    EditText edtNome, edtDocumento;
    String qrcode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_checkin);

// Recupera o QrCode lido
        Intent intent = getIntent();
        qrcode = intent.getStringExtra("QRCODE");

        setUI();
        setActions();

        realm = Realm.getDefaultInstance();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close(); // Remember to close Realm when done.
    }

    private void setUI() {
        btnConfirma = (Button) findViewById(R.id.btn_Confirma);
        edtNome = (EditText) findViewById(R.id.edt_nome);
        edtDocumento = (EditText) findViewById(R.id.edt_documento);

// Recupera nome e documento
        realm.beginTransaction();
        Ingresso ingresso = realm.createObject(Ingresso.class);

        ingresso = realm.where(Ingresso.class).equalTo("qrCode", qrcode).findFirst();
        edtNome.setText(ingresso.getNome());
        edtDocumento.setText(ingresso.getDocumento());

        realm.commitTransaction();

    }

    private void setActions() {

        btnConfirma.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                confirmarCheckIn(v);
            }
        });
    }

    private void confirmarCheckIn(View view) {
        realm.beginTransaction();
        Ingresso ingresso = realm.createObject(Ingresso.class);

        ingresso = realm.where(Ingresso.class).equalTo("qrCode", qrcode).findFirst();
        ingresso.setChecked(true);
        realm.commitTransaction();

    }


    }
