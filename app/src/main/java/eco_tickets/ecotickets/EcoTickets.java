package eco_tickets.ecotickets;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class EcoTickets extends Application {

        private String nomeEvento;

    public String getNomeEvento() {
        return nomeEvento;
    }

    public void setNomeEvento(String nomeEvento) {
        this.nomeEvento = nomeEvento;
    }

    @Override
        public void onCreate() {
        super.onCreate();

        // The Realm file will be located in package's "files" directory.
        RealmConfiguration realmConfig = new RealmConfiguration.Builder(this).build();
// Limpa o banco de dados
//        Realm.deleteRealm(realmConfig);
        Realm.setDefaultConfiguration(realmConfig);

        nomeEvento = "Evento Legal";

    }

}
