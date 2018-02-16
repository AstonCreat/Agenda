package br.com.italo.agenda;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;

import br.com.italo.agenda.dao.AlunoDao;
import br.com.italo.agenda.foto.CarregadorDeFoto;
import br.com.italo.agenda.modelo.Aluno;
import br.com.italo.agenda.retrofit.RetrofitInicializador;
import br.com.italo.agenda.task.InsereAlunoTask;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FormularioActivity extends AppCompatActivity {

    public static final int CODIGO_CAMERA = 567;
    private FormHelp help;
    private String caminhoFoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario);

        this.help = new FormHelp(this);

        final Intent intent = getIntent();
        Aluno aluno = (Aluno) intent.getSerializableExtra("aluno");
        if (aluno != null){
            help.preencheFormulario(aluno);
        }

        Button btnFoto = (Button)findViewById(R.id.btn_capture);
        btnFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ActivityCompat.checkSelfPermission(FormularioActivity.this, Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(FormularioActivity.this, new String[]{Manifest.permission.CAMERA},CODIGO_CAMERA);

                }else{
                    Intent intentCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    caminhoFoto = getExternalFilesDir(null)+"/"+System.currentTimeMillis()+".jpg";
                    File arqFoto =  new File(caminhoFoto);
                    intentCamera.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(arqFoto));
                    startActivityForResult(intentCamera, CODIGO_CAMERA);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK){
            if (requestCode == CODIGO_CAMERA){
                help.carregarImagem(caminhoFoto);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_form, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_form_ok:
                Aluno aluno= help.pegaDadoAluno();
                AlunoDao dao = new AlunoDao(this);
                if (aluno.getId() != null){
                    dao.alterar(aluno);
                }else{
                    dao.insere(aluno);
                }
                dao.close();

                //new InsereAlunoTask(aluno).execute();

                Call call = new RetrofitInicializador().getAlunoService().insere(aluno);

                call.enqueue(new Callback() {
                    @Override
                    public void onResponse(Call call, Response response) {
                        Log.i("onResponse","Requisicao com sucesso");
                    }

                    @Override
                    public void onFailure(Call call, Throwable t) {
                        Log.e("onFailure","Requisicao falhou");
                    }
                });

                Toast.makeText(FormularioActivity.this, "Aluno "+aluno.getNome()+ " salvo!", Toast.LENGTH_SHORT).show();

                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }


}
