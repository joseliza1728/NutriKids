package com.example.nutrikids.actividades;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.PatternsCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.nutrikids.R;
import com.example.nutrikids.clases.Hash;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.BaseJsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import cz.msebera.android.httpclient.Header;

public class RegistroActivity extends AppCompatActivity implements View.OnClickListener {
    EditText txt_nombre, txt_apellidos, txt_correo, txt_clave, txt_conf_clave;
    RadioButton rbt_nodef, rbt_femenino, rbt_masculino;
    RadioGroup rdg_sexo;
    CheckBox chk_terminos;
    Button btn_registrar, btn_cancelar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        //EDITTEXTS
        txt_nombre=findViewById(R.id.reg_txt_nombre);
        txt_apellidos=findViewById(R.id.reg_txt_apellido);
        txt_correo=findViewById(R.id.reg_txt_correo);
        txt_clave=findViewById(R.id.reg_txt_clave);
        txt_conf_clave=findViewById(R.id.reg_txt_clave_confirmar);

        //RADIOBUTTONS
        rdg_sexo=findViewById(R.id.reg_rb_sexo);
        rbt_nodef=findViewById(R.id.reg_rb_ND);
        rbt_masculino=findViewById(R.id.reg_rb_M);
        rbt_femenino=findViewById(R.id.reg_rb_F);

        //CHECKBOX
        chk_terminos=findViewById(R.id.reg_chk_terminos);

        //BUTTONS
        btn_registrar=findViewById(R.id.reg_btn_registrar);
        btn_cancelar=findViewById(R.id.reg_btn_cancelar);


        /* chk_terminos.setOnClickListener(this);*/
        btn_registrar.setOnClickListener(this);
        btn_cancelar.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.reg_btn_registrar:
                registrar();
                break;
            case R.id.reg_btn_cancelar:
                cancelar();

        }
    }

    private void registrar() {

        //validar campos a registrar
        if(!validar_formulario()){
            Toast.makeText(this, "Rellene todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        //guardarlos en base de datos
        AsyncHttpClient ahc_agregar = new AsyncHttpClient();
        String s_url_re = "http://receta-upn-test.atwebpages.com/ws/usuario.php";
        RequestParams params = new RequestParams();
        Hash hash = new Hash();
        char c_sexo = 'X';
        params.add("nombre",txt_nombre.getText().toString().trim());
        params.add("apellidos",txt_apellidos.getText().toString().trim());

        int rbt_sel=rdg_sexo.getCheckedRadioButtonId();
        switch (rbt_sel){
            case R.id.reg_rb_ND: c_sexo = 'X'; break;
            case R.id.reg_rb_M: c_sexo = 'M'; break;
            case R.id.reg_rb_F: c_sexo = 'F'; break;
        }
        params.add("sexo",String.valueOf(c_sexo));
        params.add("correo",txt_correo.getText().toString().trim());
        params.add("clave",hash.StringToHash(txt_clave.getText().toString(),"SHA1"));


        ahc_agregar.post(s_url_re,params,new BaseJsonHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, Header[] headers, String rawJsonResponse, Object response) {
                if(statusCode == 200){
                    int ret_val = rawJsonResponse.length() == 0 ? 0 : Integer.parseInt(rawJsonResponse);
                    if(ret_val == 1){
                        Toast.makeText(getApplicationContext(), "Usuario registrado!!!", Toast.LENGTH_SHORT).show();
                        finish();
                        Intent i_registro= new Intent(getApplicationContext(),LoginActivity.class);
                        i_registro.putExtra("nombre","txt_nombre");
                        startActivity(i_registro);
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, String rawJsonData, Object errorResponse) {
                Toast.makeText(getApplicationContext(), "Error al registrar Usuario "+statusCode, Toast.LENGTH_SHORT).show();
            }

            @Override
            protected Object parseResponse(String rawJsonData, boolean isFailure) throws Throwable {
                return null;
            }
        });
    }

    private void cancelar(){
        finish();
        Intent i_login=new Intent(this, LoginActivity.class);
        startActivity(i_login);
    }


    private boolean validar_formulario() {
        if(txt_nombre.getText().toString().isEmpty()
                || txt_apellidos.getText().toString().isEmpty()
                || txt_correo.getText().toString().isEmpty()
                || txt_clave.getText().toString().isEmpty()
                || txt_conf_clave.getText().toString().isEmpty()
        ) {
            Toast.makeText(this, "Rellene todos los formularios", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(!txt_clave.getText().toString().equals(txt_conf_clave.getText().toString())){
            Toast.makeText(this, "Las claves no son iguales", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(!chk_terminos.isChecked()){
            Toast.makeText(this, "Debe aceptar los términos y condiciones", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}