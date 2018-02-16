package br.com.italo.agenda;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import br.com.italo.agenda.adapter.AlunosAdapter;
import br.com.italo.agenda.dao.AlunoDao;
import br.com.italo.agenda.dto.AlunosSync;
import br.com.italo.agenda.modelo.Aluno;
import br.com.italo.agenda.retrofit.RetrofitInicializador;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListaAlunosActivity extends AppCompatActivity {

    private ListView listaAluno;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_alunos);

         listaAluno = (ListView) findViewById(R.id.lista_alunos);

         listaAluno.setOnItemClickListener(new AdapterView.OnItemClickListener() {
             @Override
             public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                 Aluno aluno = (Aluno) listaAluno.getItemAtPosition(position);
                 //Toast.makeText(ListaAlunosActivity.this, "Aluno "+ aluno.getNome() + " Clique longo", Toast.LENGTH_SHORT).show();
                Intent intentPastForm = new Intent(ListaAlunosActivity.this, FormularioActivity.class);
                intentPastForm.putExtra("aluno",aluno);
                startActivity(intentPastForm);
             }
         });


        Button novoAluno = (Button)findViewById(R.id.novo_aluno);
        novoAluno.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.makeText(ListaAlunosActivity.this, "Click: ListaAlunosActivity", Toast.LENGTH_LONG).show();
                Intent intentForm = new Intent(ListaAlunosActivity.this, FormularioActivity.class);
                startActivity(intentForm);

            }
        });

        registerForContextMenu(listaAluno);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.menu_enviar_notas:
                new EnviaAlunostask(this).execute();
                break;
            case R.id.menu_baixar_provas:
                Intent vaiParaProvas = new Intent(this, ProvasActivity.class);
                startActivity(vaiParaProvas);
                break;
            case R.id.menu_mapa:
                Intent vaiParaMapa = new Intent(this,MapaActivity.class);
                startActivity(vaiParaMapa);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void carregaLista() {
        AlunoDao dao = new AlunoDao(this);
        List<Aluno> alunos = dao.buscaAluno();

        for (Aluno aluno:alunos) {
            Log.i("ID do aluno:",String.valueOf(aluno.getId()));
        }

        dao.close();


        AlunosAdapter adapter = new AlunosAdapter(this, alunos);
        listaAluno.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Call<AlunosSync> call = new RetrofitInicializador().getAlunoService().lista();

        call.enqueue(new Callback<AlunosSync>() {
            @Override
            public void onResponse(Call<AlunosSync> call, Response<AlunosSync> response) {
                AlunosSync alunoSync = response.body();
                AlunoDao dao = new AlunoDao(ListaAlunosActivity.this);
                dao.sincroniza(alunoSync.getAlunos());
                dao.close();
                carregaLista();
            }

            @Override
            public void onFailure(Call<AlunosSync> call, Throwable t) {
                Log.e("onFailure chamado", t.getMessage());
            }
        });

        carregaLista();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, final ContextMenu.ContextMenuInfo menuInfo) {

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        final Aluno aluno = (Aluno) listaAluno.getItemAtPosition(info.position);

        MenuItem itemLigar = menu.add("Ligar");

        itemLigar.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                if(ActivityCompat.checkSelfPermission(ListaAlunosActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(ListaAlunosActivity.this, new String[]{Manifest.permission.CALL_PHONE},123);
                }else{
                    Intent intentLigar = new Intent(Intent.ACTION_VIEW);
                    intentLigar.setData(Uri.parse("tel:" + aluno.getTelefone()));
                    startActivity(intentLigar);
                }
                return false;
            }
        });



        MenuItem itemSMS = menu.add("Enviar sms");
        Intent intentSMS = new Intent(Intent.ACTION_VIEW);
        intentSMS.setData(Uri.parse("sms:"+ aluno.getTelefone()));
        itemSMS.setIntent(intentSMS);

        MenuItem itemMapa = menu.add("Visualizar no mapa");
        Intent intentMapa = new Intent(Intent.ACTION_VIEW);

        intentMapa.setData(Uri.parse("geo:0:0?q="+ aluno.getEndereco()));

        itemMapa.setIntent(intentMapa);



        MenuItem itemSite = menu.add("Visitar site");
        Intent intentSite = new Intent(Intent.ACTION_VIEW);

        String site = aluno.getSite();
        if(!site.startsWith("http://")){
            site = "http://" + site;
        }

        intentSite.setData(Uri.parse(site));
        itemSite.setIntent(intentSite);
       MenuItem  deletar =  menu.add("Deletar");
        deletar.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                AlunoDao dao = new AlunoDao(ListaAlunosActivity.this);
                dao.deleta(aluno);
                dao.close();
                carregaLista();
                Toast.makeText(ListaAlunosActivity.this, "Deletar aluno: "+aluno.getNome(), Toast.LENGTH_SHORT).show();
                return false;
            }
        });
    }

}
