package com.example.myapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import org.apache.http.HttpConnection;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class EditActivity extends AppCompatActivity
{
    String Json = new String();
    int id_knjiga;
    private ListView lv;
    Button btn;
    private Knjiga knjiga;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_activity);

        Bundle extras = getIntent().getExtras();
        if(extras!=null)
        {
            id_knjiga = extras.getInt("id_knjiga");
        }
        String jsonKnj = new String();
        knjiga = new Knjiga();
        try
        {
             jsonKnj = getJSONObjectFromURL(false);
             knjiga = JSONObject2Knjiga(new JSONObject(jsonKnj));
        }
        catch(Throwable t){}
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy.");
        ((TextView)findViewById(R.id.textViewKlijent)).setText("Klijent: "+knjiga.klijent);
        ((EditText)findViewById(R.id.editTextDatumOd)).setText(knjiga.datumOd);
        ((EditText)findViewById(R.id.editTextDatumDo)).setText(knjiga.datumDo);
        ((EditText)findViewById(R.id.editText4)).setText(knjiga.opis);

        btn = findViewById(R.id.buttonSave);
        btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                try
                {
                    SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy.");
                    boolean isEdited = false;
                    if((((EditText) findViewById(R.id.editTextDatumOd)).getText().toString()) != knjiga.datumOd)
                    {
                        isEdited = true;
                        knjiga.datumOd = (((EditText) findViewById(R.id.editTextDatumOd)).getText().toString());
                    }
                    if((((EditText) findViewById(R.id.editTextDatumDo)).getText().toString()) != knjiga.datumDo)
                    {
                        isEdited = true;
                        knjiga.datumDo = (((EditText) findViewById(R.id.editTextDatumDo)).getText().toString());
                    }
                    if((((EditText) findViewById(R.id.editText4)).getText().toString()) != knjiga.opis)
                    {
                        isEdited = true;
                        knjiga.opis = (((EditText) findViewById(R.id.editText4)).getText().toString());
                    }
                    if(isEdited)
                    {
                        HttpURLConnection urlConnection = null;

                        android.net.Uri.Builder builder = new android.net.Uri.Builder();
                        builder.scheme("http")
                                .encodedAuthority("192.168.1.131:1990")
                                .appendPath("WebForm1.aspx")
                                .appendQueryParameter("tip", "editKnjiga")
                                .appendQueryParameter("editedKnjiga", Knjiga2JSONObject(knjiga).toString() );

                        String json = Knjiga2JSONObject(knjiga).toString();
                        URL url = new URL(builder.build().toString());



                        urlConnection = (HttpURLConnection) url.openConnection();
                        urlConnection.setRequestMethod("GET");
                        urlConnection.setReadTimeout(100000 /* milliseconds */);
                        urlConnection.setConnectTimeout(150000 /* milliseconds */);

                        urlConnection.setDoOutput(true);

                        urlConnection.connect();

                        BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));

                        char[] buffer = new char[1024];

                        String response = new String();

                        StringBuilder sb = new StringBuilder();
                        String line;
                        while ((line = br.readLine()) != null) {
                            sb.append(line + "\n");
                        }
                        br.close();

                        response = sb.toString();

                       /* try {
                            //urlConnection.connect();
                            urlConnection.setDoOutput(true);
                            urlConnection.setChunkedStreamingMode(0);

                            OutputStream out = new BufferedOutputStream(urlConnection.getOutputStream());
                            //writeStream(out);

                            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                            //readStream(in);
                        } finally {
                            urlConnection.disconnect();
                        }*/
                       if(response.toUpperCase().startsWith("OK"))
                       {
                           new AlertDialog.Builder(EditActivity.this)
                                   .setTitle("Izmjena")
                                   .setMessage("Izmjena uspjesna")
                                   .setPositiveButton("ok", new DialogInterface.OnClickListener()
                                       {
                                       @Override
                                       public void onClick(DialogInterface dialogInterface, int i)
                                           {
                                                EditActivity.this.finish();
                                           }
                                       })

                                   .show();
                       }
                    }
                }
                catch(Exception exc)
                {
                    Log.w("Error",exc.getMessage().toString());
                }
            }
        });


    }

    public String getJSONObjectFromURL(boolean Save) throws IOException, JSONException {

        HttpURLConnection urlConnection = null;

        android.net.Uri.Builder builder = new android.net.Uri.Builder();
        builder.scheme("http")
                .encodedAuthority("192.168.1.131:1990")
                .appendPath("WebForm1.aspx")
                .appendQueryParameter("tip", "getKnjiga")
                .appendQueryParameter("id_knjiga", String.valueOf(id_knjiga));
        /*if(Save)
        {
                builder.appendQueryParameter("id_knjiga", String.valueOf(id_knjiga));
        }*/


        URL url = new URL(builder.build().toString());

        urlConnection = (HttpURLConnection) url.openConnection();

        urlConnection.setRequestMethod("GET");
        urlConnection.setReadTimeout(10000 /* milliseconds */);
        urlConnection.setConnectTimeout(15000 /* milliseconds */);

        urlConnection.setDoOutput(true);

        urlConnection.connect();

        BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));

        char[] buffer = new char[1024];

        String jsonString = new String();

        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line + "\n");
        }
        br.close();

        jsonString = sb.toString();

        System.out.println("JSON: " + jsonString);

        //return new JSONObject(jsonString);}
        return jsonString;
    }
    public class Knjiga{
        public int godina;
        public String datumOd;
        public String datumDo;
        public String opis;
        public String klijent;
        public int id_klijent;
        public int id_knjiga;
        public String getOpis() {
            return opis;
        }
    }
    public Knjiga JSONObject2Knjiga(JSONObject obj)
    {
        Knjiga knj = new Knjiga();
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy.");
            try
            {
                knj.godina = obj.getInt("godina");
                knj.id_knjiga = obj.getInt("id_knjiga");
                knj.id_klijent = obj.getInt("id_klijent");
                knj.datumOd = obj.getString("datumOd");
                knj.datumDo = obj.getString("datumDo");
                knj.opis = obj.getString("opis");
                knj.klijent = obj.getString("klijent");
            }
            catch(Throwable t){}
        return knj;
    }
    public JSONObject Knjiga2JSONObject(Knjiga _knjiga)
        {
            JSONObject knjiga = new JSONObject();
            try
            {
                knjiga.put("id_knjiga",_knjiga.id_knjiga)  ;
                knjiga.put("id_klijent",_knjiga.id_klijent);
                knjiga.put("godina", _knjiga.godina);
                knjiga.put("datumOd", _knjiga.datumOd);
                knjiga.put("datumDo", _knjiga.datumDo);
                knjiga.put("opis", _knjiga.opis);
                knjiga.put("klijent",_knjiga.klijent);
            }
            catch(Throwable T){}
            return knjiga;
        }

}
