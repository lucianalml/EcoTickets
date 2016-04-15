package eco_tickets.ecotickets;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button btnGerar, btnListar, btnCheckin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setUI();
        setActions();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setUI(){

        btnGerar = (Button) findViewById(R.id.btn_Gerar );
        btnListar = (Button) findViewById(R.id.btn_ListarConvidados );
        btnCheckin = (Button) findViewById(R.id.btn_Checkin );

    }

    private void setActions(){

        btnGerar.setOnClickListener( new View.OnClickListener() {
            public void onClick(View v) {
                gerarIngressos(v);
            }
        });
        btnListar.setOnClickListener( new View.OnClickListener() {
            public void onClick(View v) {
                listarConvidados(v);
            }
        });

        btnCheckin.setOnClickListener( new View.OnClickListener() {
            public void onClick(View v) {
                fazerCheckIn(v);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }


    public void gerarIngressos(View view) {
        Intent intent = new Intent(this, GerarActivity.class);
        startActivity(intent);
    }

    public void listarConvidados(View view) {
        Intent intent = new Intent(this, ListarActivity.class);
        startActivity(intent);
    }

    public void fazerCheckIn(View view) {
        Intent intent = new Intent(this, ScannerActivity.class);
        startActivity(intent);
    }

}
