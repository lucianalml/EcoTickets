package eco_tickets.ecotickets;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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

    public void enviarIngressos(View view) {

        String vlNome = edtNome.getText().toString();
        String vlDocumento = edtDocumento.getText().toString();
        String vlEmail = edtEmail.getText().toString();


        // Salva novo ingresso no banco de dados
        realm.beginTransaction();

        Ingresso ingresso = realm.createObject(Ingresso.class);

        ingresso.setNome(vlNome);
        ingresso.setDocumento(vlDocumento);
        ingresso.setEmail(vlEmail);
        ingresso.setQrCode("1231882321");

        realm.commitTransaction();

        Log.w("DEBUG", "ok!!");

    }

}
