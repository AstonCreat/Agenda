package br.com.italo.agenda.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;
import android.widget.Switch;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import br.com.italo.agenda.modelo.Aluno;

/**
 * Created by italo on 02/02/2018.
 */

public class AlunoDao extends SQLiteOpenHelper{


    public AlunoDao(Context context) {
        super(context, "Agenda", null, 4);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE Alunos(id CHAR(36) PRIMARY KEY, " +
                "nome TEXT NOT NULL, " +
                "endereco TEXT, " +
                "telefone TEXT, " +
                "site TEXT, " +
                "nota REAL, " +
                "caminhoFoto TEXT);";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "";
        switch (oldVersion){
            case 1:
                sql = "ALTER TABLE Alunos ADD COLUMN caminhoFoto TEXT";
            db.execSQL(sql);
            case 2 :
                String criandoTabelaNova = "CREATE TABLE Alunos_novo " +
                        "(id CHAR(36) PRIMARY KEY," +
                        "nome TEXT NOT NULL, " +
                        "endereco TEXT, " +
                        "telefone TEXT, " +
                        "site TEXT," +
                        "nota REAL, " +
                        "caminhoFoto TEXT);";
                db.execSQL(criandoTabelaNova);

                String inserirAlunoNaTabelaNova = "INSERT INTO Alunos_novo" +
                        "(id, nome, endereco, telefone, site, nota, caminhoFoto) " +
                        "SELECT id, nome, endereco, telefone, site, nota, caminhoFoto " +
                        "FROM Alunos";
                db.execSQL(inserirAlunoNaTabelaNova);

                String removendoTabelAntiga = "DROP TABLE Alunos";
                db.execSQL(removendoTabelAntiga);

                String alterarNomeDaTabelaNova = "ALTER TABLE Alunos_novo RENAME TO Alunos";
                db.execSQL(alterarNomeDaTabelaNova);

            case 3:
                String buscaAlunos = "SELECT * FROM Alunos";
                Cursor cursor = db.rawQuery(buscaAlunos, null);

                List<Aluno> alunos = populaAlunos(cursor);

                String atualizaIDALuno = "UPDATE Alunos SET id=? WHERE id=?";

                for (Aluno aluno:alunos) {
                    db.execSQL(atualizaIDALuno, new String[] {geraUUID(), aluno.getId()});
                }
        }

    }

    private String geraUUID() {
        return UUID.randomUUID().toString();
    }

    public void insere(Aluno aluno) {
        SQLiteDatabase db = getWritableDatabase();
        insereIDSeNecessario(aluno);
        ContentValues dados = pegaDadosDOAluno(aluno);
         db.insert("Alunos", null, dados);
    }

    private void insereIDSeNecessario(Aluno aluno) {
        if (aluno.getId() == null){
            aluno.setId(geraUUID());
        }
    }

    @NonNull
    private ContentValues pegaDadosDOAluno(Aluno aluno) {
        ContentValues dados = new ContentValues();
        dados.put("id",aluno.getId());
        dados.put("nome",aluno.getNome());
        dados.put("endereco",aluno.getEndereco());
        dados.put("telefone",aluno.getTelefone());
        dados.put("site",aluno.getSite());
        dados.put("nota",aluno.getNota());
        dados.put("caminhoFoto",aluno.getCaminhoFoto());
        return dados;
    }

    public List<Aluno> buscaAluno() {

        String sql = "SELECT * FROM Alunos;";
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery(sql,null);

        List<Aluno> alunos = populaAlunos(c);
        c.close();

        return alunos;
    }

    @NonNull
    private List<Aluno> populaAlunos(Cursor c) {
        List<Aluno> alunos = new ArrayList<Aluno>();
        while(c.moveToNext()){
            Aluno aluno = new Aluno();
            aluno.setId(c.getString(c.getColumnIndex("id")));
            aluno.setNome(c.getString(c.getColumnIndex("nome")));
            aluno.setEndereco(c.getString(c.getColumnIndex("endereco")));
            aluno.setTelefone(c.getString(c.getColumnIndex("telefone")));
            aluno.setSite(c.getString(c.getColumnIndex("site")));
            aluno.setNota(c.getDouble(c.getColumnIndex("nota")));
            aluno.setCaminhoFoto(c.getString(c.getColumnIndex("caminhoFoto")));

            alunos.add(aluno);
        }
        return alunos;
    }

    public void deleta(Aluno aluno) {

        SQLiteDatabase db = getWritableDatabase();

        String[] params = {aluno.getId().toString()};
        db.delete("Alunos", "id = ?",params);
    }

    public void alterar(Aluno aluno) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues dados = pegaDadosDOAluno(aluno);

        String[] params = {aluno.getId().toString()};
        db.update("Alunos",dados,"id=?",params);

    }

    public void sincroniza(List<Aluno> alunos) {

        for (Aluno aluno:alunos) {
            if (existe(aluno)){
                alterar(aluno);
            }else {
                insere(aluno);
            }
        }
    }

    private boolean existe(Aluno aluno) {
        SQLiteDatabase db = getReadableDatabase();
        String existe = "SELECT id FROM Alunos WHERE id=? LIMIT 1";
        Cursor cursor = db.rawQuery(existe, new String[]{aluno.getId()});
        int qtd = cursor.getCount();
        cursor.close();
        return qtd > 0;

    }
}
