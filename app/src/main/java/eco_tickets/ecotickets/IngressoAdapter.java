package eco_tickets.ecotickets;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class IngressoAdapter extends BaseAdapter {

    private LayoutInflater inflater;

    private List<Ingresso> ingressos = null;

    public IngressoAdapter(Context context) {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
            currentView = inflater.inflate(R.layout.ingresso_list_item, parent, false);
        }

        Ingresso ingresso = ingressos.get(position);

        if (ingresso != null) {
            ((TextView) currentView.findViewById(R.id.txt_nome)).setText(ingresso.getNome());
            ((TextView) currentView.findViewById(R.id.txt_documento)).setText(ingresso.getDocumento());
        }

        return currentView;
    }

}
