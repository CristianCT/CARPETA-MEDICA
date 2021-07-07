package com.example.carpetamedica0;


import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

public class Adaptador extends BaseAdapter {

    private Context context;
    private ArrayList<Entidad> listItems;

    public Adaptador(Context context, ArrayList<Entidad> listItems) {
        this.context = context;
        this.listItems = listItems;
    }

    @Override
    public int getCount() {
        return listItems.size();
    }

    @Override
    public Object getItem(int position) {
        return listItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Entidad Item = (Entidad) getItem(position);

        convertView = LayoutInflater.from(context).inflate(R.layout.item , null);
        ImageView imgFoto = (ImageView) convertView.findViewById(R.id.imgFoto);
        TextView titulo = (TextView) convertView.findViewById(R.id.titulo);
        TextView descripcion = (TextView) convertView.findViewById(R.id.contenido);

        imgFoto.setImageResource(Item.getImgFoto());
        titulo.setText(Item.getTitulo());
        descripcion.setText(Item.getContenido());

        convertView.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {

                openFolder();

                System.out.println("--------------------------------------------------------------------------");
                System.out.println("--------------------------------------------------------------------------");
                System.out.println("--------------------------------------------------------------------------");
            }
        });

        return convertView;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void openFolder(){

        Toast.makeText(context, "Refresque sus documentos y revise su almacenamiento", Toast.LENGTH_LONG).show();
    }
}

