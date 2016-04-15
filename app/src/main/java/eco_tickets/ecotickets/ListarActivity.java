package eco_tickets.ecotickets;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.List;

import io.realm.Realm;

public class ListarActivity extends AppCompatActivity {

    private List<Ingresso> ingressoList;
    private RecyclerView mRecyclerView;
    private IngressoAdapter mAdapter;

    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar);

        realm = Realm.getDefaultInstance();

        ingressoList = realm.where(Ingresso.class).findAll();

        mRecyclerView = (RecyclerView) findViewById(R.id.lst_ingressos);

        mAdapter = new IngressoAdapter(ingressoList);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mAdapter);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close(); // Remember to close Realm when done.
    }

}

