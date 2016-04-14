package eco_tickets.ecotickets;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import java.util.List;

public class IngressoAdapter extends RecyclerView.Adapter<IngressoAdapter.MyViewHolder> {

    private List<Ingresso> ingressosList;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView nome, documento;
        public CheckBox checked;

        public MyViewHolder(View view) {
            super(view);
            nome = (TextView) view.findViewById(R.id.txt_nome);
            documento = (TextView) view.findViewById(R.id.txt_documento);
            checked = (CheckBox) view.findViewById(R.id.cb_checkedIn);
        }
    }


    public IngressoAdapter(List<Ingresso> ingressos) {
        this.ingressosList = ingressos;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.ingresso_list_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Ingresso ingresso = ingressosList.get(position);
        holder.nome.setText(ingresso.getNome());
        holder.documento.setText(ingresso.getDocumento());
        holder.checked.setChecked((ingresso.isChecked()));
    }

    @Override
    public int getItemCount() {
        return ingressosList.size();
    }
}