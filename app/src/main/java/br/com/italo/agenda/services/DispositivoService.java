package br.com.italo.agenda.services;

import android.telecom.Call;

import retrofit2.http.Header;
import retrofit2.http.POST;

/**
 * Created by italo on 21/02/2018.
 */

public interface DispositivoService {

    @POST("firebase/dispositivo")
    retrofit2.Call<Void> enviaToken(@Header("token") String token);

}
