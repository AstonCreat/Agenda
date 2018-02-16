package br.com.italo.agenda;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.List;

import br.com.italo.agenda.modelo.Prova;

/**
 * Created by italo on 09/02/2018.
 */

public class ListaProvasFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_provas, container, false);

        List<String> topicoPort = Arrays.asList("Sujeito","Objeto direto","Objeto indireto");
        Prova provaPort = new Prova("Portugues","25/06/2017",topicoPort);

        List<String>topicoMAt = Arrays.asList("Equacoes de segundo grau", "Trigonometria");
        Prova provaMAt = new Prova("Matematica", "27/05/2016", topicoMAt);

        List<Prova> provas = Arrays.asList(provaPort, provaMAt);

        ArrayAdapter<Prova> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, provas);


        ListView lista =(ListView) view.findViewById(R.id.provas_lista);
        lista.setAdapter(adapter);

        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Prova prova = (Prova) parent.getItemAtPosition(position);
                Toast.makeText(getContext(), "Clicou na prova de " + prova, Toast.LENGTH_SHORT).show();

           ProvasActivity provasActivity = (ProvasActivity) getActivity();
           provasActivity.selecionaProva(prova);

            }
        });

        return view;
    }
}
