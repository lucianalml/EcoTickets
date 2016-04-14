package eco_tickets.ecotickets;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

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

        mRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), mRecyclerView, new ClickListener() {


            @Override
            public void onClick(View view, int position) {

                Ingresso modifiedIngresso = ingressoList.get(position);

                // Acquire the RealmObject matching the name of the clicked Ingresso.
                Ingresso ingresso = realm.where(Ingresso.class).equalTo("qrCode", modifiedIngresso.getQrCode()).findFirst();

                CheckBox checked = (CheckBox) view.findViewById(R.id.cb_checkedIn);

                realm.beginTransaction();
                ingresso.setChecked(checked.isChecked());
                realm.commitTransaction();

                Toast.makeText(getApplicationContext(), "Checkin alterado"  , Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongClick(View view, int position) {

                

                final Ingresso modifiedIngresso = ingressoList.get(position);
                final int item_position = position;

// Recupera o objeto do realm
                final Ingresso ingresso = realm.where(Ingresso.class).equalTo("qrCode", modifiedIngresso.getQrCode()).findFirst();

                AlertDialog.Builder builder = new AlertDialog.Builder(ListarActivity.this);

                builder.setTitle("Confirmação");
                builder.setMessage("Deseja deletar" + ingresso.getNome() + "?");

                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {

// Remove do banco de dados
                        realm.beginTransaction();
                        ingresso.removeFromRealm();
                        realm.commitTransaction();


                        ingressoList.remove(ingressoList.get(item_position));
                        // Do nothing but close the dialog
                        dialog.dismiss();
                    }

                });

                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing
                        dialog.dismiss();
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();



            }
        }));

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close(); // Remember to close Realm when done.
    }

    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private ListarActivity.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ListarActivity.ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {

                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        }

    }

}

