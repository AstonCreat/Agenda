package br.com.italo.agenda.services;

import java.util.List;

import br.com.italo.agenda.dto.AlunosSync;
import br.com.italo.agenda.modelo.Aluno;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by italo on 13/02/2018.
 */

public interface AlunoService {

    @POST("aluno")
    Call<Void> insere(@Body Aluno aluno);


    @GET("aluno")
    Call<AlunosSync> lista();
}
