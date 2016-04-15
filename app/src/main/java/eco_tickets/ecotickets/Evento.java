package eco_tickets.ecotickets;

import io.realm.RealmObject;

public class Evento extends RealmObject {

    private String nomeEvento;

// TODO Permitir cadastrar vários evento
//    private List<Ingresso>;

    public String getNomeEvento() {
        return nomeEvento;
    }

    public void setNomeEvento(String nomeEvento) {
        this.nomeEvento = nomeEvento;
    }
}
