package br.com.italo.agenda;

import android.graphics.Bitmap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;

import br.com.italo.agenda.foto.CarregadorDeFoto;
import br.com.italo.agenda.modelo.Aluno;

/**
 * Created by italo on 02/02/2018.
 */

public class FormHelp {
    private final EditText campNome;
    private final EditText campEnd;
    private final EditText campTel;
    private final EditText campSite;
    private final RatingBar campNota;
    private final ImageView campoFoto;
    private Aluno aluno;

    public FormHelp(FormularioActivity formActivity){

        this.campNome = (EditText) formActivity.findViewById(R.id.form_nome);

        this.campEnd = (EditText) formActivity.findViewById(R.id.form_endereco);


        this.campTel = (EditText) formActivity.findViewById(R.id.form_telefone);


        this.campSite = (EditText) formActivity.findViewById(R.id.form_site);

        this.campNota = (RatingBar) formActivity.findViewById(R.id.form_ratingBar);

        this.campoFoto = (ImageView)formActivity.findViewById(R.id.form_foto);

        aluno = new Aluno();

    }

    public Aluno pegaDadoAluno() {

        aluno.setNome(campNome.getText().toString());
        aluno.setEndereco(campEnd.getText().toString());
        aluno.setTelefone(campTel.getText().toString());
        aluno.setSite(campSite.getText().toString());
        aluno.setNota(Double.valueOf(campNota.getProgress()));
        aluno.setCaminhoFoto((String) campoFoto.getTag());

        return aluno;
    }

    public void preencheFormulario(Aluno aluno) {
        campNome.setText(aluno.getNome());
        campEnd.setText(aluno.getEndereco());
        campTel.setText(aluno.getTelefone());
        campSite.setText(aluno.getSite());
        campNota.setProgress(aluno.getNota().intValue());
        carregarImagem(aluno.getCaminhoFoto());

        this.aluno = aluno;
    }

    public void carregarImagem(String caminhoFoto) {
        if (caminhoFoto != null){
            //Bitmap bitmap = BitmapFactory.decodeFile(caminhoFoto);

            Bitmap bitmap1 = CarregadorDeFoto.carrega(caminhoFoto);

            //Bitmap bitmapReduzido = Bitmap.createScaledBitmap(bitmap1,300,300,true);
            campoFoto.setImageBitmap(bitmap1);
            //foto.setScaleType(ImageView.ScaleType.FIT_XY);
            campoFoto.setTag(caminhoFoto);
        }

    }
}
