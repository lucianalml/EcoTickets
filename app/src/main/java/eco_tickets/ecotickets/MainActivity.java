package eco_tickets.ecotickets;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import io.realm.Realm;

public class MainActivity extends AppCompatActivity {

    Button btnGerar, btnListar, btnCheckin;
    TextView tvNomeEvento;

    private String nomeEvento;

    private Realm realm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        realm = Realm.getDefaultInstance();

        setUI();
        setActions();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setUI(){

        btnGerar = (Button) findViewById(R.id.btn_Gerar );
        btnCheckin = (Button) findViewById(R.id.btn_Checkin );
        tvNomeEvento = (TextView) findViewById(R.id.txt_evento);

// Recupera o nome do evento cadastrado se houver
        Evento evento = realm.where(Evento.class).findFirst();

// Se não há nenhum evento cadastrado insere um com o nome inicial
        if ( evento == null ) {
            nomeEvento = ((EcoTickets) this.getApplication()).getNomeEvento();
            realm.beginTransaction();
            evento = realm.createObject(Evento.class);
            evento.setNomeEvento(nomeEvento);
            realm.commitTransaction();
        } else {

// Recupera o nome
            nomeEvento = evento.getNomeEvento();
        }

        tvNomeEvento.setText(nomeEvento);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close(); // Remember to close Realm when done.
    }


    @Override
    protected void onResume() {
        super.onResume();
        atualizaTela();
    }

    private void setActions(){

        btnGerar.setOnClickListener( new View.OnClickListener() {
            public void onClick(View v) {
                gerarIngressos(v);
            }
        });
        btnCheckin.setOnClickListener( new View.OnClickListener() {
            public void onClick(View v) {
                fazerCheckIn(v);
            }
        });

    }

    public void atualizaTela() {
        // Atualiza nome do evento
        Evento evento = realm.where(Evento.class).findFirst();
        if ( evento != null ){
            nomeEvento = evento.getNomeEvento();
        }
        tvNomeEvento.setText(nomeEvento);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_config:
                configurarEvento();
                return true;

            case R.id.action_listar:
                listarConvidados();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void listarConvidados() {
        Intent intent = new Intent(this, ListarActivity.class);
        startActivity(intent);
    }

    public void configurarEvento() {
        Intent intent = new Intent(this, ConfigEventoActivity.class);
        startActivity(intent);
    }

    public void gerarIngressos(View view) {
        Intent intent = new Intent(this, GerarActivity.class);
        startActivity(intent);
    }

    public void fazerCheckIn(View view) {
        Intent intent = new Intent(this, ScannerActivity.class);
        startActivity(intent);
    }

}
