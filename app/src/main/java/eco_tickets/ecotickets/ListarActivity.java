package eco_tickets.ecotickets;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import io.realm.Realm;

public class ListarActivity extends AppCompatActivity {

    private ListView mListView;
    private IngressoAdapter mAdapter;

    private Realm realm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar);

        realm = Realm.getDefaultInstance();

    }


    @Override
    public void onResume() {
        super.onResume();

        // Recupera os ingressos pela 1a vez
        if(mAdapter == null) {
            List<Ingresso> ingressos = realm.where(Ingresso.class).findAll();

            //This is the GridView adapter
            mAdapter = new IngressoAdapter(this);
            mAdapter.setData(ingressos);

            //This is the GridView which will display the list of cities
            mListView = (ListView) findViewById(R.id.lst_ingressos);
            mListView.setAdapter(mAdapter);
            mListView.setClickable(true);

// :(
            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    Toast.makeText(getApplicationContext(), "Clica fdp", Toast.LENGTH_SHORT).show();

                    atualizaIngresso(position);
                }
            });

            mAdapter.notifyDataSetChanged();
            mListView.invalidate();
        }
    }


/*
    @Override
    public void onItemClick(AdapterViewCompat<?> parent, View view, int position, long id) {
        Toast.makeText(getApplicationContext(), "Clica fdp", Toast.LENGTH_SHORT).show();
    }
*/

    public class IngressoAdapter extends BaseAdapter {

        private LayoutInflater mInflater;

        public List<Ingresso> ingressos = null;

        public IngressoAdapter(Context context) {
            mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        public void setData(List<Ingresso> details) {
            this.ingressos = details;
        }

        @Override
        public int getCount() {
            if (ingressos == null) {
                return 0;
            }
            return ingressos.size();
        }

        @Override
        public Object getItem(int position) {
            if (ingressos == null || ingressos.get(position) == null) {
                return null;
            }
            return ingressos.get(position);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int position, View currentView, ViewGroup parent) {

            if (currentView == null) {
                currentView = mInflater.inflate(R.layout.ingresso_list_item, parent, false);
            }

            Ingresso ingresso = ingressos.get(position);

            if (ingresso != null) {
                ((TextView) currentView.findViewById(R.id.txt_nome)).setText(ingresso.getNome());
                ((TextView) currentView.findViewById(R.id.txt_documento)).setText(ingresso.getDocumento());
                ((CheckBox) currentView.findViewById(R.id.cb_checkedIn)).setChecked(ingresso.isChecked());

            }

            return currentView;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close(); // Remember to close Realm when done.
    }

/*
    public void updateIngressos() {

        // Pull all the cities from the realm
        RealmResults<Ingresso> ingressos = realm.where(Ingresso.class).findAll();

        // Put these items in the Adapter
        mAdapter.setData(ingressos);
        mAdapter.notifyDataSetChanged();
        mListView.invalidate();
    }
*/

    public void atualizaIngresso ( int position) {

        Ingresso modifiedIngresso = (Ingresso)mAdapter.getItem(position);

        // Acquire the RealmObject matching the name of the clicked Ingresso.
        Ingresso ingresso = realm.where(Ingresso.class).equalTo("qrCode", modifiedIngresso.getQrCode()).findFirst();

        // Create a transaction to increment the vote count for the selected City in the realm
        realm.beginTransaction();
        ingresso.setChecked(modifiedIngresso.isChecked());
        realm.commitTransaction();

// ??
//        updateIngressos();


    }

//    @Override
//    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//    }
}
