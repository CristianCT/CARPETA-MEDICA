package com.example.carpetamedica0;

import android.os.Bundle;
import android.view.View;
import android.view.Menu;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static android.os.Environment.getExternalStorageDirectory;

public class profileUsuario extends AppCompatActivity {

    private ListView lvItems;
    private Adaptador adaptador;
    private AppBarConfiguration mAppBarConfiguration;
    private JSONObject myresponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_usuario);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Actualizando Carpeta Medica", Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();

                //En esta parte se van a actualizar los archivos
                try {
                    download_documents(myresponse.getString("historial") , myresponse.getString("documents"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow, R.id.compartir , R.id.nav_papelera , R.id.contacto , R.id.configuracion , R.id.exit)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        Bundle bundle = getIntent().getExtras();
        String usuario = null;
        if(bundle != null){
            usuario = bundle.getString("myrequest");
            try {
                myresponse = new JSONObject(usuario);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.profile_usuario, menu);
        configUser();
        printDocuments();
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    private void configUser(){

        TextView name = (TextView) findViewById(R.id.name_user);
        TextView email = (TextView) findViewById(R.id.email_user);

         try {
            name.setText(myresponse.getString("name") + " " + myresponse.getString("lastname"));
            email.setText(myresponse.getString("email"));
            Toast.makeText(getApplicationContext(), myresponse.getString("name"), Toast.LENGTH_LONG).show();

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void download_documents(String historial , String documentos){

        try {

            JSONArray jsonArray = new JSONArray(historial);
            JSONArray jsonArray1 = new JSONArray(documentos);

            File directorio = new File(getExternalStorageDirectory(), "Carpeta medica");

            if (!directorio.exists()) {

                directorio.mkdirs();
            }

            for (int i = 0 ; i < jsonArray.length(); i++){

                JSONObject doc = new JSONObject();
                doc = (JSONObject) jsonArray.get(i);

                JSONObject doc1 = new JSONObject();
                doc1 = (JSONObject) jsonArray1.get(i);

                File file = new File(directorio , doc1.getString("name_doc") + doc.getString("originalname").substring(doc.getString("originalname").indexOf(".")));
                if (!file.exists()) {

                    try {

                        file.createNewFile();
                        DescargarDocumentosAsyncTask downloadFile = new DescargarDocumentosAsyncTask(this);
                        downloadFile.setUrl("https://carpetamedica.herokuapp.com/uploads/" + doc.getString("originalname").replace(" ", "%20"));
                        downloadFile.setOutputFile(file);
                        downloadFile.loadInBackground();

                    } catch (IOException e) {
                        Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
                    }
                }
            }
        } catch (JSONException e) {
            Toast.makeText(getApplicationContext(), e.toString() , Toast.LENGTH_LONG).show();
        }
    }

    private void printDocuments(){

        lvItems = findViewById(R.id.lista);
        adaptador = new Adaptador(this , getArrayList());
        lvItems.setAdapter(adaptador);
    }

    private ArrayList<Entidad> getArrayList(){
        JSONArray arrayDocuments = null;
        JSONArray arrayHistorial = null;

        //En esta parte se agregan los objetos que se van a mostrar

        //Obtenemos Los arrays que van a ser recorridos
        try {
            String arrayStringDocuments = myresponse.getString("documents");
            arrayDocuments = new JSONArray(arrayStringDocuments); //Procedemos a convertir el String en un JSONArray
            String arrayStringHistorial = myresponse.getString("historial");
            arrayHistorial  = new JSONArray(arrayStringHistorial);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ArrayList<Entidad> listItems = new ArrayList<>();

        //Recorremos los Arrays con un ciclo for
        for(int x = 0; x < arrayHistorial.length() ; x++){

            try {
                JSONObject docReq = (JSONObject) arrayHistorial.get(x);
                JSONObject docReq_1 = (JSONObject) arrayDocuments.get(x);

                if(docReq.getString("originalname").substring(docReq.getString("originalname").indexOf(".") + 1 , docReq.getString("originalname").indexOf(".") + 4).toUpperCase().equals("PDF")){

                    listItems.add(new Entidad(R.drawable.pdf , docReq_1.getString("name_doc") , "Tamaño: "+ docReq.getString("size").substring(0 , docReq.getString("size").length() - 3) + "KB        Codificación: " + docReq.getString("encoding")));

                }else if(docReq.getString("originalname").substring(docReq.getString("originalname").indexOf(".") + 1 , docReq.getString("originalname").indexOf(".") + 4).toUpperCase().equals("DOC")){

                    listItems.add(new Entidad(R.drawable.doc , docReq_1.getString("name_doc") , "Tamaño: "+ docReq.getString("size").substring(0 , docReq.getString("size").length() - 3) + "KB        Codificación: " + docReq.getString("encoding")));
                }else{

                    listItems.add(new Entidad(R.drawable.img , docReq_1.getString("name_doc") , "Tamaño: "+ docReq.getString("size").substring(0 , docReq.getString("size").length() - 3) + "KB        Codificación: " + docReq.getString("encoding")));
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return listItems;
    }
}
