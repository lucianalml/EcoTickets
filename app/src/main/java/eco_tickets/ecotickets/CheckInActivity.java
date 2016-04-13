package eco_tickets.ecotickets;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import io.realm.Realm;

public class CheckInActivity extends AppCompatActivity {

    private Realm realm;
    Button btnConfirma;
    EditText edtNome, edtDocumento;
    TextView tvAutorizado;
    String nome, documento, qrcode;
    boolean checked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_checkin);

// Recupera dados atribuidos ao qrCode
        Intent intent = getIntent();
        nome = intent.getStringExtra("NOME");
        documento = intent.getStringExtra("DOCUMENTO");
        qrcode = intent.getStringExtra("QRCODE");
        checked = intent.getBooleanExtra("CHECKED",false);

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

        btnConfirma = (Button) findViewById(R.id.btn_Confirma);
        edtNome = (EditText) findViewById(R.id.edt_nome);
        edtDocumento = (EditText) findViewById(R.id.edt_documento);
        tvAutorizado = (TextView) findViewById(R.id.tv_autorizado);


// Recupera nome e documento
        edtNome.setText(nome);
        edtDocumento.setText(documento);

// Se a pessoa já fez check-in da alerta
        if (checked) {
            tvAutorizado.setText("Código já utilizado.");
            tvAutorizado.setBackgroundColor(Color.RED);
        }
            else {
            tvAutorizado.setText("Autorizado.");
            tvAutorizado.setBackgroundColor(Color.GREEN);

        }

//

    }

    private void setActions() {

        btnConfirma.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                confirmarCheckIn(v);
            }
        });
    }

    private void confirmarCheckIn(View view) {

        Ingresso ingresso = realm.where(Ingresso.class).equalTo("qrCode", qrcode).findFirst();

        realm.beginTransaction();
        ingresso.setChecked(true);
// TODO salvar data e hora da leitura
        realm.commitTransaction();

        Toast.makeText(CheckInActivity.this, "Check-in confirmado.", Toast.LENGTH_SHORT).show();


    }


    }
