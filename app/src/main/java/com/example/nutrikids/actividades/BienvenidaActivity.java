package com.example.nutrikids.actividades;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.nutrikids.R;
import com.example.nutrikids.clases.InterfaceMenu;
import com.example.nutrikids.clases.Usuario;

public class BienvenidaActivity extends AppCompatActivity implements InterfaceMenu {
    TextView lbl_saludo;
    Usuario usuario;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bienvenida);
        lbl_saludo=findViewById(R.id.bie_lbl_saludo);
        usuario =(Usuario) getIntent().getSerializableExtra("usuario");
        lbl_saludo.setText("Bienvenido: "+ usuario.getNombre());
    }

    @Override
    public void on_click_menu(int i_id_button) {
        Intent i_menu= new Intent(this, MenuActivity.class);
        i_menu.putExtra("ID_boton",i_id_button);
        i_menu.putExtra("usuario",usuario);
        startActivity(i_menu);
    }
}