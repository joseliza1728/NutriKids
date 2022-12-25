package com.example.nutrikids.actividades;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.example.nutrikids.R;
import com.example.nutrikids.clases.InterfaceMenu;
import com.example.nutrikids.clases.Usuario;
import com.example.nutrikids.fragmentos.AcercaDeFragment;
import com.example.nutrikids.fragmentos.AgregarRecetaFragment;
import com.example.nutrikids.fragmentos.ConfiguracionFragment;
import com.example.nutrikids.fragmentos.ListaRecetasFragment;

public class MenuActivity extends AppCompatActivity implements InterfaceMenu {
    Fragment[] fragments;
    Usuario usuario;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        fragments=new Fragment[4];
        fragments[0]=new ListaRecetasFragment();
        fragments[1]=new AgregarRecetaFragment();
        fragments[2]=new AcercaDeFragment();
        fragments[3]=new ConfiguracionFragment();
        usuario =(Usuario) getIntent().getSerializableExtra("usuario");
        int i_id_boton=getIntent().getIntExtra("ID_boton",-1);
        on_click_menu(i_id_boton);
    }

    @Override
    public void on_click_menu(int i_id_button) {
        Bundle datos = new Bundle();
        datos.putSerializable("usuario", usuario);
        fragments[i_id_button].setArguments(datos);
        FragmentManager fm= getSupportFragmentManager();
        FragmentTransaction ft= fm.beginTransaction();
        ft.replace(R.id.men_rel_menu, fragments[i_id_button]);
        ft.commit();
    }
}