package com.example.nutrikids.actividades;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nutrikids.R;
import com.example.nutrikids.clases.Hash;
import com.example.nutrikids.clases.Usuario;
import com.example.nutrikids.sqlite.Sesion;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.BaseJsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;

import cz.msebera.android.httpclient.Header;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    EditText txt_correo, txt_clave;
    CheckBox chk_recordar;
    Button btn_ingresar, btn_salir;
    TextView lbl_registrate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        txt_correo=findViewById(R.id.log_txt_correo);
        txt_clave=findViewById(R.id.log_txt_clave);
        chk_recordar=findViewById(R.id.log_chk_recordar);
        btn_ingresar=findViewById(R.id.log_btn_ingresar);
        btn_salir=findViewById(R.id.log_btn_salir);
        lbl_registrate=findViewById(R.id.log_lbl_registrate);

        btn_ingresar.setOnClickListener(this);
        btn_salir.setOnClickListener(this);
        lbl_registrate.setOnClickListener(this);

        validar_si_recordo_sesion();
    }

    private void validar_si_recordo_sesion() {
        Sesion sesion=new Sesion(getApplicationContext());
        if(sesion.recordo_sesion()){
            iniciar_sesion(sesion.extraer_dato_usuario("CORREO"),
                    sesion.extraer_dato_usuario("CLAVE"), true);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.log_btn_ingresar:
                iniciar_sesion(txt_correo.getText().toString().trim(),txt_clave.getText().toString().trim(),false);
                break;
            case R.id.log_btn_salir:
                salir_aplicacion();
                break;
            case R.id.log_lbl_registrate:
                registrar_usuario();
                break;
        }

    }

    private void iniciar_sesion(String correo, String clave, boolean b_recordo){
        activar_formulario(false);
        Hash hash = new Hash();
        clave=(b_recordo == true ? clave : hash.StringToHash(clave,"SHA1"));

        AsyncHttpClient ahc_login = new AsyncHttpClient();
        String s_url="http://receta-upn-test.atwebpages.com/ws/login.php";
        RequestParams params = new RequestParams();
        params.add("correo",correo);
        params.add("clave",clave);

        ahc_login.post(s_url, params, new BaseJsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String rawJsonResponse, Object response) {
                if(statusCode==200){
                    try {
                        JSONArray jsonArray = new JSONArray(rawJsonResponse);
                        if(jsonArray.length()>0){
                            int id_usuario = jsonArray.getJSONObject(0).getInt("id_usuario");
                            if(id_usuario == -1){
                                Toast.makeText(getApplicationContext(), "Credenciales incorrectas", Toast.LENGTH_SHORT).show();
                            }else{
                                Usuario usuario = new Usuario();
                                usuario.setId_usuario(id_usuario);
                                usuario.setNombre(jsonArray.getJSONObject(0).getString("nombre"));
                                usuario.setApellidos(jsonArray.getJSONObject(0).getString("apellidos"));
                                usuario.setSexo(jsonArray.getJSONObject(0).getString("sexo").charAt(0));
                                usuario.setCorreo(jsonArray.getJSONObject(0).getString("correo"));
                                //guardar los datos del JSON a la clase
                                if(chk_recordar.isChecked()){
                                    //guardar correo y la clave SQLite
                                    Sesion sesion = new Sesion(getApplicationContext());
                                    sesion.agregar_usuario(usuario.getId_usuario(),usuario.getCorreo(), hash.StringToHash(txt_clave.getText().toString().trim(),"SHA1"));
                                    //.makeText(getApplicationContext(), "Clave: "+ usuario.getNombre(), Toast.LENGTH_SHORT).show();
                                    //Toast.makeText(getApplicationContext(), "Clave: "+ usuario.getClave(), Toast.LENGTH_SHORT).show();
                                    //Toast.makeText(getApplicationContext(), "Clave: "+ hash.StringToHash(txt_clave.getText().toString().trim(),"SHA1"), Toast.LENGTH_SHORT).show();
                                    Toast.makeText(getApplicationContext(), "Recordó sesión", Toast.LENGTH_SHORT).show();
                                }
                                Intent i_bienvenida= new Intent(getApplicationContext(),BienvenidaActivity.class);
                                i_bienvenida.putExtra("usuario",usuario);
                                startActivity(i_bienvenida);
                                finish();
                            }
                        }
                    } catch (JSONException e){
                        e.printStackTrace();
                    }


                }
                activar_formulario(true);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, String rawJsonData, Object errorResponse) {
                activar_formulario(true);
                Toast.makeText(getApplicationContext(), "Error: "+statusCode, Toast.LENGTH_SHORT).show();

            }

            @Override
            protected Object parseResponse(String rawJsonData, boolean isFailure) throws Throwable {
                return null;
            }
        });
    }

    private void activar_formulario(boolean b) {
        txt_correo.setEnabled(b);
        txt_clave.setEnabled(b);
        chk_recordar.setEnabled(b);
        btn_ingresar.setEnabled(b);
        btn_salir.setEnabled(b);

    }

    private void salir_aplicacion(){
        System.exit(0);
    }
    private void registrar_usuario(){
        Intent i_registro=new Intent(this, RegistroActivity.class);
        startActivity(i_registro);
    }


}