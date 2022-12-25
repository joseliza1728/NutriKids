package com.example.nutrikids.fragmentos;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.nutrikids.R;
import com.example.nutrikids.actividades.BienvenidaActivity;
import com.example.nutrikids.clases.Usuario;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.BaseJsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.io.ByteArrayOutputStream;

import cz.msebera.android.httpclient.Header;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AgregarRecetaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AgregarRecetaFragment extends Fragment implements View.OnClickListener{
    EditText txt_nombre,txt_descripcion,txt_ingredientes,txt_preparacion;
    Button btn_cancel, btn_agregar,btn_subirimg;
    ImageView jiv_foto_receta;
    Usuario usuario;
    private static final int REQUEST_CODE_PERMISSION = 1;
    private static final int REQUEST_CODE_GALLERY = 2;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AgregarRecetaFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AgregarRecetaFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AgregarRecetaFragment newInstance(String param1, String param2) {
        AgregarRecetaFragment fragment = new AgregarRecetaFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View frg_agre = inflater.inflate(R.layout.fragment_agregar_receta, container, false);

        txt_nombre =frg_agre.findViewById(R.id.frg_agregarRece_txt_nombre);
        txt_descripcion =frg_agre.findViewById(R.id.frg_agregarRece_txt_descripcion);
        txt_ingredientes =frg_agre.findViewById(R.id.frg_agregarRece_txt_ingredientes);
        txt_preparacion=frg_agre.findViewById(R.id.frg_agregarRece_txt_preparacion);


        btn_cancel=frg_agre.findViewById(R.id.frg_agregarRece_btn_cancelar);
        btn_agregar=frg_agre.findViewById(R.id.frg_agregarRece_btn_agregar);
        btn_subirimg=frg_agre.findViewById(R.id.frg_agregarRece_btn_subirimg);
        jiv_foto_receta=frg_agre.findViewById(R.id.agr_iv_foto_receta);


        btn_cancel.setOnClickListener(this);
        btn_agregar.setOnClickListener(this);
        btn_subirimg.setOnClickListener(this);
        cargar_datos_usuario();
        return frg_agre;
    }
    private void cargar_datos_usuario() {
        Bundle datos =getArguments();
        usuario= (Usuario)datos.getSerializable("usuario");
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.frg_agregarRece_btn_agregar:
                edit_rece();
                break;
            case R.id.frg_agregarRece_btn_cancelar:
                cancelar_receta();
                break;
            case R.id.frg_agregarRece_btn_subirimg:
                elegir_foto();
                break;
        }

    }





    private void edit_rece() {

        //validar campos a registrar
        if(!validar_formulario()){
            Toast.makeText(getContext(), "Rellene todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        //guardarlos en base de datos
        AsyncHttpClient ahc_agregar = new AsyncHttpClient();
        String s_url_re = "http://receta-upn-test.atwebpages.com/ws/Receta.php";
        RequestParams params = new RequestParams();
        params.add("nombre",txt_nombre.getText().toString().trim());
        params.add("descripcion",txt_descripcion.getText().toString().trim());
        params.add("ingredientes",txt_ingredientes.getText().toString().trim());
        params.add("preparacion",txt_preparacion.getText().toString().trim());
        params.add("foto", image_view_to_base64(jiv_foto_receta));
        System.out.println(image_view_to_base64(jiv_foto_receta));




        ahc_agregar.post(s_url_re,params,new BaseJsonHttpResponseHandler(){


            @Override
            public void onSuccess(int statusCode, Header[] headers, String rawJsonResponse, Object response) {
                if(statusCode == 200){
                    int i_ret_val = rawJsonResponse.length()==0 ? 0 : Integer.parseInt(rawJsonResponse);
                    if(i_ret_val==1){
                        Toast.makeText(getContext(),"Receta agregada con éxito",Toast.LENGTH_SHORT).show();
                        Intent i_bienvenida = new Intent(getContext(), BienvenidaActivity.class);
                        i_bienvenida.putExtra("nombre",  "RECETA AGREGADA");
                        startActivity(i_bienvenida);
                    }
                    else
                        Toast.makeText(getContext(),"error al agregar receta",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, String rawJsonData, Object errorResponse) {
                Toast.makeText(getContext(), "Error: "+statusCode, Toast.LENGTH_SHORT).show();
            }

            @Override
            protected Object parseResponse(String rawJsonData, boolean isFailure) throws Throwable {
                return null;
            }
        });

        //cargar el fragmento ReporteReceta

    }

    private String image_view_to_base64(ImageView jiv_foto_receta) {

        Bitmap bitmap = ((BitmapDrawable)jiv_foto_receta.getDrawable()).getBitmap();

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);

        byte[] byteArray = stream.toByteArray();
        String foto = Base64.encodeToString(byteArray, Base64.DEFAULT);

        return  foto;


    }


    private void cancelar_receta() {
        Intent i_bienvenida = new Intent(getContext(), BienvenidaActivity.class);
        i_bienvenida.putExtra("usuario",usuario);
        startActivity(i_bienvenida);
    }



    private void elegir_foto() {

        requestPermissions(
                new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},
                REQUEST_CODE_PERMISSION);

        Toast.makeText(getContext(), "tengo permisos", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(getContext(), "No se puede acceder al almacenamiento externo", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(getContext(), "Debe elegir una imagen", Toast.LENGTH_SHORT).show();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private boolean validar_formulario() {
        if(txt_nombre.getText().toString().isEmpty()
                || txt_descripcion.getText().toString().isEmpty()
                || txt_ingredientes.getText().toString().isEmpty()
                || txt_preparacion.getText().toString().isEmpty()) {
            Toast.makeText(getContext(), "Rellene todos los formularios", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }








}