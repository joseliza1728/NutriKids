package com.example.nutrikids.actividades;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.nutrikids.R;
import com.example.nutrikids.clases.Usuario;

public class InicioActivity extends AppCompatActivity {
    Usuario usuario;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);
        usuario =(Usuario) getIntent().getSerializableExtra("usuario");
        Thread t_inicio=new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    sleep(3000);
                }catch (InterruptedException e){
                    e.printStackTrace();

                }finally {
                    Intent i_login=new Intent(getApplicationContext(), LoginActivity.class);
                    i_login.putExtra("usuario",usuario);
                    startActivity(i_login);
                    finish();
                }
            }
        };
        t_inicio.start();
    }
}