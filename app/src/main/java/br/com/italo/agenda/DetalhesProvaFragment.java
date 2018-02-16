package br.com.italo.agenda;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import br.com.italo.agenda.modelo.Prova;


public class DetalhesProvaFragment extends Fragment {

    private TextView campoMateria;
    private TextView campoData;
    private ListView listaTopicos;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_detalhes_prova, container, false);

         campoMateria = (TextView)view.findViewById(R.id.detalhe_prova_materia);
         campoData = (TextView)view.findViewById(R.id.detalhe_prova_data);
         listaTopicos = (ListView)view.findViewById(R.id.detalhe_prova_topicos);

         Bundle parametros =  getArguments();
        if (parametros != null) {
            Prova prova = (Prova) parametros.getSerializable("prova");
            populaCampos(prova);
        }


        return view;
    }

    public void populaCampos(Prova prova){
        campoMateria.setText(prova.getMateria());
        campoData.setText(prova.getData());

        ArrayAdapter<String> adapterTopicos = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1,prova.getTopicos());
        listaTopicos.setAdapter(adapterTopicos);
    }

}
