package br.com.italo.agenda.task;

import android.os.AsyncTask;

import br.com.italo.agenda.WebClient;
import br.com.italo.agenda.converter.AlunoConverter;
import br.com.italo.agenda.modelo.Aluno;

/**
 * Created by italo on 13/02/2018.
 */

public class InsereAlunoTask extends AsyncTask{
    private final Aluno aluno;

    public InsereAlunoTask(Aluno aluno) {
        this.aluno = aluno;
    }

    @Override
    protected Object doInBackground(Object[] objects) {

       String json = new AlunoConverter().converrteParaJsonCompleto(aluno);
        new WebClient().insere(json);

        return null;
    }
}
