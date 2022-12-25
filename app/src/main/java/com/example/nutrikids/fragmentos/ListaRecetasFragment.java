package com.example.nutrikids.fragmentos;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.nutrikids.R;
import com.example.nutrikids.adaptadores.RecetaAdapter;
import com.example.nutrikids.clases.Receta;
import com.example.nutrikids.clases.Usuario;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.BaseJsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ListaRecetasFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListaRecetasFragment extends Fragment {
    RecyclerView list_vista_recetacard;
    RecetaAdapter adapter=null;
    Usuario usuario;
    public static ArrayList<Receta> lista;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ListaRecetasFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ListaRecetasFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ListaRecetasFragment newInstance(String param1, String param2) {
        ListaRecetasFragment fragment = new ListaRecetasFragment();
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
        View v_his= inflater.inflate(R.layout.fragment_lista_recetas, container, false);
        list_vista_recetacard = v_his.findViewById(R.id.mos_rv_mostrar_receta);
        lista= new ArrayList<>();
        llenar_recetalist();
        LinearLayoutManager manager=new LinearLayoutManager(getContext());
        list_vista_recetacard.setLayoutManager(manager);

        adapter=new RecetaAdapter(lista);
        list_vista_recetacard.setAdapter(adapter);
        cargar_datos_usuario();
        return v_his;

    }
    private void cargar_datos_usuario() {
        Bundle datos =getArguments();
        usuario= (Usuario)datos.getSerializable("usuario");
    }
    private void llenar_recetalist() {


       AsyncHttpClient ahc_recetas = new AsyncHttpClient();
        String s_url = "http://receta-upn-test.atwebpages.com/ws/MostrarReceta.php";
        RequestParams params = new RequestParams();
        params.add("id_receta","-1");
        ahc_recetas.post(s_url,params, new BaseJsonHttpResponseHandler() {


            @Override
            public void onSuccess(int statusCode, Header[] headers, String rawJsonResponse, Object response) {

                if(statusCode == 200){
                    try {
                        JSONArray jsonArray= new JSONArray(rawJsonResponse);
                        lista.clear();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            lista.add(new Receta(jsonArray.getJSONObject(i).getInt("id_receta"),
                                    jsonArray.getJSONObject(i).getString("nombre"),
                                    jsonArray.getJSONObject(i).getString("descripcion"),
                                    jsonArray.getJSONObject(i).getString("ingredientes"),
                                    jsonArray.getJSONObject(i).getString("preparacion"),
                                    jsonArray.getJSONObject(i).getString("foto")
                                    ));
                        }
    adapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }


            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, String rawJsonData, Object errorResponse) {

            }

            @Override
            protected Object parseResponse(String rawJsonData, boolean isFailure) throws Throwable {
                return null;
            }
});


    }
}