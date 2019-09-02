package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MojAdapter extends ArrayAdapter<KnjigaZaView>
{
    private Context mContext;
    private int mResource;
    public MojAdapter(Context context, int resource, ArrayList<KnjigaZaView> objects)
    {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
         String klijent = getItem(position).getKlijent();
         int godina = getItem(position).getGodina();
         String opis = getItem(position).getOpis();
         int id_knjiga = getItem(position).getId_knjiga();

        KnjigaZaView knjigaZaView = new KnjigaZaView(id_knjiga,klijent,godina,opis);

        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource,parent,false);

        TextView tv_klijent = (TextView) convertView.findViewById(R.id.textViewKlijent);
        TextView tv_opis = (TextView) convertView.findViewById(R.id.textViewOpis);
        TextView tv_godina = (TextView) convertView.findViewById(R.id.textViewGodina);

        tv_klijent.setText(klijent);
        tv_opis.setText(opis);
        tv_godina.setText(Integer.toString(godina));

        return convertView;
    }
}
