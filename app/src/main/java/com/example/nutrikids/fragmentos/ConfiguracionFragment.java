package com.example.nutrikids.fragmentos;

import android.content.Intent;
import android.os.Bundle;

import androidx.core.util.PatternsCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.nutrikids.R;
import com.example.nutrikids.actividades.BienvenidaActivity;
import com.example.nutrikids.actividades.LoginActivity;
import com.example.nutrikids.clases.Hash;
import com.example.nutrikids.clases.Usuario;
import com.example.nutrikids.sqlite.Sesion;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.BaseJsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import cz.msebera.android.httpclient.Header;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ConfiguracionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ConfiguracionFragment extends Fragment implements View.OnClickListener {
    EditText txt_correo,txt_clave_anterior, txt_clave;
    Button btn_cerrarSesion, btn_actualizar;
    Usuario usuario;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ConfiguracionFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ConfiguracionFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ConfiguracionFragment newInstance(String param1, String param2) {
        ConfiguracionFragment fragment = new ConfiguracionFragment();
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
        View frg_conf=inflater.inflate(R.layout.fragment_configuracion, container, false);

        txt_correo=frg_conf.findViewById(R.id.frg_conf_txt_correo);
        txt_clave_anterior=frg_conf.findViewById(R.id.frg_conf_txt_clave_anterior);
        txt_clave=frg_conf.findViewById(R.id.frg_conf_txt_clave);
        btn_actualizar=frg_conf.findViewById(R.id.frg_conf_btn_actualizar);
        btn_cerrarSesion=frg_conf.findViewById(R.id.frg_conf_btn_cerrarSesion);

        btn_actualizar.setOnClickListener(this);
        btn_cerrarSesion.setOnClickListener(this);
        cargar_datos_usuario();

        return frg_conf;
    }

    private void cargar_datos_usuario() {
        Bundle datos =getArguments();
        usuario= (Usuario)datos.getSerializable("usuario");
        txt_correo.setText(usuario.getCorreo());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.frg_conf_btn_actualizar:
                actualizar_datos();
                break;

            case R.id.frg_conf_btn_cerrarSesion:
                cerrar_sesion();
                break;
        }

    }

    private void actualizar_datos() {

        Sesion sesion =new Sesion(getContext());
        Hash hash = new Hash();

        if(txt_correo.getText().toString().isEmpty()||!PatternsCompat.EMAIL_ADDRESS.matcher(txt_correo.getText().toString()).matches()) {
            Toast.makeText(getContext(), "Ingrese un correo válido", Toast.LENGTH_SHORT).show();
            return;
        }
        String s_clave_ant=hash.StringToHash(txt_clave_anterior.getText().toString(),"SHA1");
        String s_clave_grd = sesion.extraer_dato_usuario("CLAVE");
        if(!txt_clave_anterior.getText().toString().isEmpty()) {
            if (!s_clave_ant.equals(s_clave_grd)) {
                Toast.makeText(getContext(), "La clave anterior no es correcta", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        if (txt_clave.getText().toString().isEmpty() && !txt_clave_anterior.getText().toString().isEmpty()){
            Toast.makeText(getContext(), "La nueva clave no puede ser vacía", Toast.LENGTH_SHORT).show();
            return;
        }
        //actualizo los datos de sesion e SQLite y BD externa
        String s_clave_new=hash.StringToHash(txt_clave.getText().toString(),"SHA1");
        sesion.actualizar_dato_usuario(1,"CLAVE",s_clave_new);

        AsyncHttpClient ahc_actualizar_auto = new AsyncHttpClient();
        String s_url = "http://receta-upn-test.atwebpages.com/ws/actualiza_clave_liza.php";
        RequestParams params = new RequestParams();
        params.add("id_usuario",String.valueOf(usuario.getId_usuario()));
        params.add("correo",usuario.getCorreo());
        params.add("clave",hash.StringToHash(txt_clave.getText().toString().trim(),"SHA1"));
        ahc_actualizar_auto.post(s_url, params, new BaseJsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String rawJsonResponse, Object response) {
                if(statusCode == 200){
                    int ret_val = rawJsonResponse.length() == 0 ? 0 : Integer.parseInt(rawJsonResponse);
                    if(ret_val == 1){
                        Toast.makeText(getContext(), "Datos Actualizados!!!", Toast.LENGTH_SHORT).show();
                        sesion.eliminar_usuario(usuario.getId_usuario());
                        sesion.agregar_usuario(usuario.getId_usuario(),usuario.getCorreo(),hash.StringToHash(txt_clave.getText().toString().trim(),"SHA1"));
                        Intent i_bienvenida= new Intent(getContext(),BienvenidaActivity.class);
                        i_bienvenida.putExtra("usuario",usuario);
                        startActivity(i_bienvenida);
                    }
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


    }

    private void cerrar_sesion() {
        Sesion sesion= new Sesion(getContext());
        sesion.eliminar_usuario(usuario.getId_usuario());
        //destruir historial
        getActivity().finishAffinity();
        //cargar actividad login
        Intent i_login = new Intent(getContext(), LoginActivity.class);
        startActivity(i_login);
    }
}