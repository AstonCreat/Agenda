package br.com.italo.agenda.retrofit;

import br.com.italo.agenda.modelo.Aluno;
import br.com.italo.agenda.services.AlunoService;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

/**
 * Created by italo on 13/02/2018.
 */

public class RetrofitInicializador {

    private final Retrofit retrofit;

    public RetrofitInicializador(){

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder client = new OkHttpClient.Builder();
        client.addInterceptor(interceptor);

        retrofit = new Retrofit.Builder().baseUrl("http://192.168.1.104:8080/api/")
                    .addConverterFactory(JacksonConverterFactory.create())
                    .client(client.build())
                    .build();
    }

    public AlunoService getAlunoService() {

        return retrofit.create(AlunoService.class);
    }
}
