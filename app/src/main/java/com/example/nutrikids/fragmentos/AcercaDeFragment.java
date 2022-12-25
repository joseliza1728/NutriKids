package com.example.nutrikids.fragmentos;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.nutrikids.R;
import com.example.nutrikids.actividades.BienvenidaActivity;
import com.example.nutrikids.actividades.MenuActivity;
import com.example.nutrikids.clases.Usuario;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AcercaDeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AcercaDeFragment extends Fragment implements View.OnClickListener {
    Button btn_volver;
    Usuario usuario;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AcercaDeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AcercaDeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AcercaDeFragment newInstance(String param1, String param2) {
        AcercaDeFragment fragment = new AcercaDeFragment();
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
        View frg_acerca=inflater.inflate(R.layout.fragment_acerca_de, container, false);

        btn_volver=frg_acerca.findViewById(R.id.frag_acercade_btn_volver);

        btn_volver.setOnClickListener(this);
        cargar_datos_usuario();
        return frg_acerca;
    }
    private void cargar_datos_usuario() {
        Bundle datos =getArguments();
        usuario= (Usuario)datos.getSerializable("usuario");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.frag_acercade_btn_volver:
                volver();
                break;

        }
    }

    private void volver() {
        Intent i_bienvenida = new Intent(getContext(), BienvenidaActivity.class);
        i_bienvenida.putExtra("usuario",usuario);
        startActivity(i_bienvenida);
    }
}