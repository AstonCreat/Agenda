package br.com.italo.agenda;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;


import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

import br.com.italo.agenda.dao.AlunoDao;
import br.com.italo.agenda.modelo.Aluno;


/**
 * Created by italo on 10/02/2018.
 */

public class MapaFragment extends SupportMapFragment implements OnMapReadyCallback {

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {


        LatLng posicaoDaEscola =  pegaCOordenadaDoEndereco("Qr 106 conjunto 2,Samambaia, Brasilia");
        if(posicaoDaEscola != null){
            CameraUpdate update = CameraUpdateFactory.newLatLngZoom(posicaoDaEscola,20);
            googleMap.moveCamera(update);

        }

        AlunoDao alunoDao = new AlunoDao((getContext()));
        for (Aluno aluno : alunoDao.buscaAluno()){
            LatLng coordenada = pegaCOordenadaDoEndereco(aluno.getEndereco());
            if (coordenada != null){
                MarkerOptions marcador = new MarkerOptions();
                marcador.position(coordenada);
                marcador.title(aluno.getNome());
                marcador.snippet(String.valueOf(aluno.getNota()));
                googleMap.addMarker(marcador);
            }
            alunoDao.close();
            new Localizador(getContext(),googleMap);

        }
    }

    private LatLng pegaCOordenadaDoEndereco(String endereco){
        try {
            Geocoder geocoder = new Geocoder(getContext());
            List<Address> resultado = geocoder.getFromLocationName(endereco, 1);
            if(!resultado.isEmpty()){
               LatLng posicao =  new LatLng(resultado.get(0).getLatitude(), resultado.get(0).getLongitude());
               return posicao;
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }


}
