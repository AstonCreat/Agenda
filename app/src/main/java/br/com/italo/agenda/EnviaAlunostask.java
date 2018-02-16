package br.com.italo.agenda;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import java.util.List;
import br.com.italo.agenda.converter.AlunoConverter;
import br.com.italo.agenda.dao.AlunoDao;
import br.com.italo.agenda.modelo.Aluno;

/**
 * Created by italo on 06/02/2018.
 */

public class EnviaAlunostask extends AsyncTask<Void, Void, String> {
    private Context context;
    private ProgressDialog dialog;
    AlunoDao dao;

    public EnviaAlunostask(Context context) {
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        dialog =  ProgressDialog.show(context, "Aguarde", "Enviando alunos...",true,true);
         dao = new AlunoDao(context);
    }

    @Override
    protected String doInBackground(Void... params) {


        List<Aluno> alunos = dao.buscaAluno();
        dao.close();

        AlunoConverter conversor = new AlunoConverter();
        String json = conversor.conversorParajson(alunos);

        WebClient client = new WebClient();
        String resposta = client.post(json);
         return resposta;
    }

    @Override
    protected void onPostExecute(String resposta) {
        dialog.dismiss();
        Toast.makeText(context, resposta , Toast.LENGTH_SHORT).show();
    }
}
