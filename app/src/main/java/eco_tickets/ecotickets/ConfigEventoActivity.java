package eco_tickets.ecotickets;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import io.realm.Realm;

public class ConfigEventoActivity extends AppCompatActivity {

    TextView tvNomeEvento;
    Button btnSalvar, btnApagar;
    EditText edtNomeEvento;
    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config_evento);

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
        btnSalvar = (Button) findViewById(R.id.btn_salvar);
        btnApagar = (Button) findViewById(R.id.btn_apagar_lista);
        edtNomeEvento = (EditText) findViewById(R.id.edt_nome);
        tvNomeEvento = (TextView) findViewById(R.id.txt_evento);

// TODO Recuperar do banco de dados
        tvNomeEvento.setText(((EcoTickets) this.getApplication()).getNomeEvento());
        edtNomeEvento.setHint(((EcoTickets) this.getApplication()).getNomeEvento());

    }

    private void setActions() {
        btnSalvar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                salvarConfig();
            }
        });

        btnApagar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                apagarLista();
            }
        });

    }

    public void salvarConfig (){

    }

    public void apagarLista (){

    }

}
