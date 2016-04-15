package eco_tickets.ecotickets;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import io.realm.Realm;

public class ConfigEventoActivity extends AppCompatActivity {

    TextView tvNomeEvento;
    Button btnSalvar, btnApagar;
    EditText edtNomeEvento;
    private Realm realm;
    private String nome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config_evento);

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
        btnSalvar = (Button) findViewById(R.id.btn_salvar);
        btnApagar = (Button) findViewById(R.id.btn_apagar);
        edtNomeEvento = (EditText) findViewById(R.id.edt_nome);
        tvNomeEvento = (TextView) findViewById(R.id.txt_evento);

//Recupera o nome do evento
        nome = ((EcoTickets) this.getApplication()).getNomeEvento();
        Evento evento = realm.where(Evento.class).findFirst();

        if ( evento != null ){
            nome = evento.getNomeEvento();
        }

        tvNomeEvento.setText(nome);
        edtNomeEvento.setHint(nome);

    }

    private void setActions() {
        btnSalvar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                salvarConfig();
                atualizaTela();
            }
        });

        btnApagar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                apagarLista();
            }
        });

    }

// Atualiza o nome do evento no banco de dados
    public void salvarConfig (){

        realm.beginTransaction();

        Evento evento = realm.where(Evento.class).findFirst();
        if (evento == null) {
            evento = realm.createObject(Evento.class);
        }
        nome = edtNomeEvento.getText().toString();
        evento.setNomeEvento(nome);
        realm.commitTransaction();

        Toast.makeText(ConfigEventoActivity.this, "Dados do evento salvos com sucesso",
                Toast.LENGTH_SHORT).show();
    }

    // Verifica se os campos obrigatórios estão preenchidos
    private boolean verificaCampos() {

        if (edtNomeEvento.getText().toString().trim().equals("")) {
            edtNomeEvento.setError("Campo Nome do Evento é obrigatório");
            return false;
        }
        else {
            return true;
        }
    }



    public void atualizaTela () {
        tvNomeEvento.setText(nome);
        edtNomeEvento.setHint(nome);
    }

    public void apagarLista (){

// Envia alerta de confirmação
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirmação")
                .setMessage("Deseja apagar todos os dados do evento?" )
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        realm.beginTransaction();
                        realm.deleteAll();
                        realm.commitTransaction();;

                        Toast.makeText(ConfigEventoActivity.this, "Dados do evento apagados com sucesso",
                                Toast.LENGTH_SHORT).show();

                    }
                })
                .setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //do things
                    }
                });

        AlertDialog alert = builder.create();
        alert.show();


    }

}
