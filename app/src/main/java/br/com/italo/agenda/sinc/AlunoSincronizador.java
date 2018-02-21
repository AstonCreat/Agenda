package br.com.italo.agenda.sinc;

import android.content.Context;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;

import br.com.italo.agenda.dao.AlunoDao;
import br.com.italo.agenda.dto.AlunosSync;
import br.com.italo.agenda.event.AtualizarListaAlunoEvent;
import br.com.italo.agenda.retrofit.RetrofitInicializador;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AlunoSincronizador {
    private final Context context;
    private EventBus bus = EventBus.getDefault();

    public AlunoSincronizador(Context context) {
        this.context = context;
    }

    public void buscaAlunos() {
        Call<AlunosSync> call = new RetrofitInicializador().getAlunoService().lista();

        call.enqueue(new Callback<AlunosSync>() {
            @Override
            public void onResponse(Call<AlunosSync> call, Response<AlunosSync> response) {
                AlunosSync alunoSync = response.body();
                AlunoDao dao = new AlunoDao(context);
                dao.sincroniza(alunoSync.getAlunos());
                dao.close();
                bus.post(new AtualizarListaAlunoEvent());
            }

            @Override
            public void onFailure(Call<AlunosSync> call, Throwable t) {
                Log.e("onFailure chamado", t.getMessage());
                bus.post(new AtualizarListaAlunoEvent());
            }
        });
    }
}