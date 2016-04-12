package eco_tickets.ecotickets;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import io.realm.Realm;

public class CheckInActivity extends AppCompatActivity {

    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

/*
        setContentView(R.layout.activity_gerar);
        setUI();
        setActions();
        // Create the Realm configuration
        RealmConfiguration realmConfig = new RealmConfiguration.Builder(getApplicationContext()).build();
//        Realm.deleteRealm(realmConfig); // Testes -> Exclui o banco de dados da execução anterior
        realm = Realm.getInstance(realmConfig);
*/

        lerQrCode();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close(); // Remember to close Realm when done.
    }

    private void lerQrCode() {


    }

}
