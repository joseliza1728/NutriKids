package com.example.nutrikids.adaptadores;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nutrikids.R;
import com.example.nutrikids.actividades.LoginActivity;
import com.example.nutrikids.actividades.VistaActivity;
import com.example.nutrikids.clases.Receta;
import com.example.nutrikids.fragmentos.EditarRecetaFragment;
import com.loopj.android.http.Base64;
import java.util.List;

public class RecetaAdapter extends RecyclerView.Adapter<RecetaAdapter.ViewHolder>{




    private List<Receta> lista_receta;

    public RecetaAdapter(List<Receta> lista_receta) {
        this.lista_receta = lista_receta;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_receta, parent, false);
        return new ViewHolder(view);
    }
    @RequiresApi(api=Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Receta receta=lista_receta.get(position);
        holder.txt_nombre.setText(receta.getNombre());


        String s_imagen = receta.getFoto();
        byte[] image_byte = Base64.decode(s_imagen, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(image_byte, 0, image_byte.length);
        holder.iv_foto_receta.setImageBitmap(bitmap);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder,  int position, @NonNull List<Object> payloads) {
        Receta receta = lista_receta.get(position);
        holder.txt_nombre.setText(receta.getNombre());


        holder.setOnClickListeners();

        String iv_foto_receta = receta.getFoto();
        byte[] image_byte = Base64.decode(iv_foto_receta, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(image_byte, 0, image_byte.length);
        holder.iv_foto_receta.setImageBitmap(bitmap);
        holder.cv_item_receta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i_editar = new Intent(v.getContext(), EditarRecetaFragment.class);
                i_editar.putExtra("id", lista_receta.get(position).getId_receta());
                v.getContext().startActivity(i_editar);
                //Toast.makeText(v.getContext(), "ID: "+lista_receta.get(position).getId(), Toast.LENGTH_SHORT).show();
            }
        });

        super.onBindViewHolder(holder, position, payloads);
    }


    @Override
    public int getItemCount() {
        return lista_receta.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        Context context;
        CardView cv_item_receta;
        TextView txt_nombre;
        ImageView iv_foto_receta;
        Button but_item;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            context=itemView.getContext();

            cv_item_receta = itemView.findViewById(R.id.cv_item_receta);
            txt_nombre = itemView.findViewById(R.id.lbl_receta_titulo_comida);
            iv_foto_receta = itemView.findViewById(R.id.iv_item_receta);
            but_item=(Button) itemView.findViewById(R.id.itm_btn_ingresar);

        }
        void setOnClickListeners(){

            but_item.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.itm_btn_ingresar:
                    Intent intent = new Intent(context, VistaActivity.class);
                    context.startActivity(intent);
                    break;
            }
        }
    }
}
