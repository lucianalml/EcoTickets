package eco_tickets.ecotickets;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import io.realm.Realm;

public class IngressoAdapter extends RecyclerView.Adapter<IngressoAdapter.ViewHolder> {

    private List<Ingresso> ingressosList;
    private Realm realm;

    public IngressoAdapter(List<Ingresso> ingressos) {
        this.ingressosList = ingressos;
    }

    @Override
    public IngressoAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.ingresso_list_item, parent, false);

        ViewHolder holder = new ViewHolder(v);

        return holder;

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.nome.setText(ingressosList.get(position).getNome());
        holder.documento.setText("Documento: " + ingressosList.get(position).getDocumento());
        holder.checked.setChecked((ingressosList.get(position).isChecked()));
        holder.qrCode = (ingressosList.get(position).getQrCode());

// ?
        holder.checked.setTag(new Integer(position));

        holder.checked.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                CheckBox cb = (CheckBox)v;

                int clickedPos = ((Integer)cb.getTag()).intValue();

// Recupera o ingresso selecionado e atualiza o checkin no banco de dados
                realm = Realm.getDefaultInstance();
                Ingresso ingresso = realm.where(Ingresso.class).equalTo("qrCode", ingressosList.get(clickedPos).getQrCode()).findFirst();

                realm.beginTransaction();
                ingresso.setChecked(cb.isChecked());
                realm.commitTransaction();
                realm.close();

                notifyItemChanged(clickedPos);

                Toast.makeText(v.getContext(), "Check-in atualizado.", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return ingressosList.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView nome, documento;
        public CheckBox checked;
        public String qrCode;

        public ViewHolder(View view) {
            super(view);
            nome = (TextView) view.findViewById(R.id.txt_nome);
            documento = (TextView) view.findViewById(R.id.txt_documento);
            checked = (CheckBox) view.findViewById(R.id.cb_checkedIn);

        }
    }

}