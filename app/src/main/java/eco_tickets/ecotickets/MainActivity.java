package eco_tickets.ecotickets;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button btnGerar, btnListar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setUI();
        setActions();
    }

    private void setUI(){

        btnGerar = (Button) findViewById(R.id.btn_Gerar );
        btnListar = (Button) findViewById(R.id.btn_ListarConvidados );

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

    }


    public void gerarIngressos(View view) {
        Intent intent = new Intent(this, GerarActivity.class);
        startActivity(intent);
    }

    public void listarConvidados(View view) {
        Intent intent = new Intent(this, ListarActivity.class);
        startActivity(intent);
    }

}
