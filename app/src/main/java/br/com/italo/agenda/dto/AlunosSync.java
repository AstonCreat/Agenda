package br.com.italo.agenda.dto;

import java.util.List;

import br.com.italo.agenda.modelo.Aluno;

/**
 * Created by italo on 15/02/2018.
 */

public class AlunosSync {

    private List<Aluno> alunos;

    private String momentoDaUltimaModificacao;

    public List<Aluno> getAlunos() {
        return alunos;
    }


    public String getMomentoDaUltimaModificacao() {
        return momentoDaUltimaModificacao;
    }


}
