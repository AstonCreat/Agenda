package br.com.italo.agenda.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import br.com.italo.agenda.ListaAlunosActivity;
import br.com.italo.agenda.R;
import br.com.italo.agenda.foto.CarregadorDeFoto;
import br.com.italo.agenda.modelo.Aluno;

/**
 * Created by italo on 05/02/2018.
 */

public class AlunosAdapter extends BaseAdapter {
    private final List<Aluno> alunos;
    private final Context context;

    public AlunosAdapter(Context context, List<Aluno> alunos) {
        this.context =context;
        this.alunos = alunos;
    }

    @Override
    public int getCount() {
        return alunos.size();
    }

    @Override
    public Object getItem(int position) {
        return alunos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Aluno aluno = alunos.get(position);

        LayoutInflater inflater = LayoutInflater.from(context);

        View view = convertView;
        if (view == null){
            view =inflater.inflate(R.layout.list_item,parent,false);
        }


        ImageView campoFoto = (ImageView) view.findViewById(R.id.item_foto);
        TextView campoNome = (TextView) view.findViewById(R.id.item_nome);
        TextView campoTelefone = (TextView) view.findViewById(R.id.item_telefone);
        TextView campoSite = (TextView) view.findViewById(R.id.item_site);
        TextView campoNota = (TextView) view.findViewById(R.id.item_nota);


            String caminhoFoto = aluno.getCaminhoFoto();
        if (caminhoFoto != null){
            Bitmap bitmap1 = CarregadorDeFoto.carrega(caminhoFoto);
            //Bitmap bitmapReduzido = Bitmap.createScaledBitmap(bitmap1,100,100,true);
            campoFoto.setImageBitmap(bitmap1);
            campoFoto.setScaleType(ImageView.ScaleType.FIT_XY);
        }

        campoNome.setText(aluno.getNome());
        campoTelefone.setText( aluno.getTelefone());
        campoSite.setText( aluno.getSite());
        campoNota.setText("Nota: " + aluno.getNota().toString());

        return view;
    }
}
