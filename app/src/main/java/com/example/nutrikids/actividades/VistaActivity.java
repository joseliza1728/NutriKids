package com.example.nutrikids.actividades;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.nutrikids.R;
import com.example.nutrikids.clases.Usuario;
import com.example.nutrikids.fragmentos.EditarRecetaFragment;
import com.example.nutrikids.fragmentos.ListaRecetasFragment;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.BaseJsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.io.ByteArrayOutputStream;

import cz.msebera.android.httpclient.Header;

public class VistaActivity extends AppCompatActivity implements View.OnClickListener {
    EditText txt_nombre,txt_descripcion,txt_ingredientes,txt_preparacion;
    ImageView jiv_foto_receta;
    Button btn_editar, btn_regresar,btn_subirimg;
    Usuario usuario;
    int i_ID = -1;

    private static final int REQUEST_CODE_PERMISSION = 1;
    private static final int REQUEST_CODE_GALLERY = 2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vista);

        txt_nombre =findViewById(R.id.vista_txt_nombre);
        txt_descripcion =findViewById(R.id.vista_txt_desc);
        txt_ingredientes =findViewById(R.id.vista_txt_ingred);
        txt_preparacion=findViewById(R.id.vista_txt_prepa);
        btn_editar =findViewById(R.id.vista_btn_editar);
        btn_regresar=findViewById(R.id.vista_btn_regresar);
        btn_subirimg=findViewById(R.id.vista_btn_subirimg);
        jiv_foto_receta=findViewById(R.id.vista_iv_foto_receta);

        btn_editar.setOnClickListener(this);
        btn_regresar.setOnClickListener(this);
        btn_subirimg.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.vista_btn_editar:
                frag_editar();
                break;
            case R.id.vista_btn_regresar:
                regresar_vista();
                break;
            case R.id.vista_btn_subirimg:
                elegir_foto();
                break;

        }
    }

    private void regresar_vista() {

        Intent i_bienvenida = new Intent(this,BienvenidaActivity.class);
        i_bienvenida.putExtra("usuario",usuario);
        startActivity(i_bienvenida);
        finish();

    }

    private void frag_editar() {

        if(!validar_formulario()){
            Toast.makeText(getApplicationContext(), "Rellene todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        AsyncHttpClient ahc_actualizar_auto = new AsyncHttpClient();
        String s_url = "http://gsqupn.atwebpages.com/S13/ActualizarReceta.php";
        RequestParams params = new RequestParams();
        params.add("nombre", txt_nombre.getText().toString().trim());
        params.add("descripcion", txt_descripcion.getText().toString().trim());
        params.add("ingredientes", txt_ingredientes.getText().toString().trim());
        params.add("preparacion", txt_preparacion.getText().toString().trim());
        params.add("foto", image_view_to_base64(jiv_foto_receta));
        params.add("id_receta", String.valueOf(i_ID));
        ahc_actualizar_auto.post(s_url, params, new BaseJsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String rawJsonResponse, Object response) {
                if(statusCode == 200){
                    int ret_val = rawJsonResponse.length() == 0 ? 0 : Integer.parseInt(rawJsonResponse);
                    if(ret_val == 1){
                        Toast.makeText(getApplicationContext(), "Datos Actualizados!!!", Toast.LENGTH_SHORT).show();
                        regresar_vista();
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, String rawJsonData, Object errorResponse) {
                Toast.makeText(getApplicationContext(), "Error al actualizar Receta "+statusCode, Toast.LENGTH_SHORT).show();
            }

            @Override
            protected Object parseResponse(String rawJsonData, boolean isFailure) throws Throwable {
                return null;
            }
        });

    }

    private boolean validar_formulario() {
        if(txt_nombre.getText().toString().isEmpty()
                || txt_descripcion.getText().toString().isEmpty()
                || txt_ingredientes.getText().toString().isEmpty()
                || txt_preparacion.getText().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Rellene todos los formularios", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private String image_view_to_base64(ImageView jiv_foto_auto) {
        Bitmap bitmap = ((BitmapDrawable)jiv_foto_auto.getDrawable()).getBitmap();

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);

        byte[] byteArray = stream.toByteArray();
        String imagen = android.util.Base64.encodeToString(byteArray, android.util.Base64.DEFAULT);

        return  imagen;
    }


    private void elegir_foto() {


        Intent i_file_chooser = new Intent(Intent.ACTION_PICK);
        i_file_chooser.setType("image/*");
        startActivityForResult(i_file_chooser, REQUEST_CODE_GALLERY);

        Toast.makeText(getApplicationContext(), "tengo permisos", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == REQUEST_CODE_PERMISSION){
            if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Intent i_file_chooser = new Intent(Intent.ACTION_PICK);
                i_file_chooser.setType("image/*");
                startActivityForResult(i_file_chooser, REQUEST_CODE_GALLERY);
            }
            else
                Toast.makeText(getApplicationContext(), "No se puede acceder al almacenamiento externo", Toast.LENGTH_SHORT).show();
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == REQUEST_CODE_GALLERY){
            if(resultCode == RESULT_OK && data != null){
                Uri uri = data.getData();
                jiv_foto_receta.setImageURI(uri);
            }
            else
                Toast.makeText(getApplicationContext(), "Debe elegir una imagen", Toast.LENGTH_SHORT).show();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }














}





