package com.example.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.StrictMode;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class TestActivity extends AppCompatActivity {
    String json;
    JSONArray jsonLista;
    ArrayList<String> prostaLista = new ArrayList<String>();
    ArrayList<Knjiga> listaKnjiga = new ArrayList<Knjiga>();
    ArrayList<KnjigaZaView> listaKnjigaView = new ArrayList<KnjigaZaView>();
    ProgressDialog pd;
    //{
        //listaKnjiga = new ArrayList<Knjiga>();
    //}
    private ListView lv;
    private ProgressBar pg;
    private SwipeRefreshLayout swipe;
    MojAdapter adapter;
    private EditText pretragaEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_activity);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        swipe = findViewById(R.id.swipe);
        pretragaEditText = findViewById(R.id.pretragaEditText);


        Bundle extras = getIntent().getExtras();
        if(extras!=null){
            json = extras.getString("knjige");
            if(json.charAt(0)=='{'){
                try{
                    JSONObject jsonObjekat = new JSONObject(json);
                }
                catch(Throwable t){
                    Log.e("JSON", "Malformed JSON: \"" + json + "\"");
                }
            }
            if(json.charAt(0)=='['){
                try{
                    jsonLista = new JSONArray(json);
                    JSONArray2KnjigaArray(jsonLista);
                }
                catch(Throwable t){
                    Log.e("JSON", "Malformed JSON: \"" + json + "\"");
                }
            }
        }

        getProstaLista(listaKnjiga);
        getKnjigeZaView(listaKnjiga);



        lv = findViewById(R.id.listviewmain);
        adapter = new MojAdapter(this,R.layout.adapter_view_layout,listaKnjigaView);

        lv.setAdapter(adapter);





        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
            //your codes here
        }
        lv.setClickable(true);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                KnjigaZaView o = (KnjigaZaView) lv.getItemAtPosition(i);

                Intent intent = new Intent(getApplicationContext(),EditActivity.class);
                intent.putExtra("id_knjiga", o.id_knjiga);
                startActivity(intent);
            }
        });

        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
        {
            @Override
            public void onRefresh()
            {
                //refreshData();
                OdvojeniThread thread = new OdvojeniThread();
                thread.start();
                //adapter.notifyDataSetChanged();
                //swipe.setRefreshing(false);
            }
        });
        pretragaEditText.setOnFocusChangeListener(new View.OnFocusChangeListener()
        {
            @Override
            public void onFocusChange(View view, boolean b)
            {
                switch(pretragaEditText.getText().toString())
                {
                    case "Pretraga...":
                        pretragaEditText.setText("");
                        break;
                    case "":
                        pretragaEditText.setText("Pretraga...");
                        break;
                }
            }
        });

        pretragaEditText.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {
                String search = pretragaEditText.getText().toString();
                //adapter.
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {

            }

            @Override
            public void afterTextChanged(Editable editable)
            {

            }
        });



    }
    public class OdvojeniThread extends Thread{

        @Override
        public void run()
        {
            try{
                jsonLista = new JSONArray(getJSONObjectFromURL());
            }
            catch(Throwable tr){}
            JSONArray2KnjigaArray(jsonLista);
            getProstaLista(listaKnjiga);
            getKnjigeZaView(listaKnjiga);

            Handler test_handler = new Handler(Looper.getMainLooper());
            test_handler.post(new Runnable()
            {
                @Override
                public void run()
                {
                    adapter.notifyDataSetChanged();
                    swipe.setRefreshing(false);
                }
            });
        }
    }

    private void refreshData()
    {
        try{
        jsonLista = new JSONArray(getJSONObjectFromURL());
        }
        catch(Throwable tr){}
        JSONArray2KnjigaArray(jsonLista);
        getProstaLista(listaKnjiga);
        getKnjigeZaView(listaKnjiga);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public class KlijentZaSerijalizaciju
    {
        public String naziv;
        public String puni_naziv ;
        public String pdv ;
        public String pib ;
        public String direktor;

        public String getNaziv() {
            return naziv;
        }

        public void setNaziv(String naziv) {
            this.naziv = naziv;
        }

        public String getPuni_naziv() {
            return puni_naziv;
        }

        public void setPuni_naziv(String puni_naziv) {
            this.puni_naziv = puni_naziv;
        }

        public String getPdv() {
            return pdv;
        }

        public void setPdv(String pdv) {
            this.pdv = pdv;
        }

        public String getPib() {
            return pib;
        }

        public void setPib(String pib) {
            this.pib = pib;
        }

        public String getDirektor() {
            return direktor;
        }

        public void setDirektor(String direktor) {
            this.direktor = direktor;
        }


    }


    public KlijentZaSerijalizaciju JSONtoKlijent(JSONObject jsonObject)throws JSONException {
        KlijentZaSerijalizaciju klijent = new KlijentZaSerijalizaciju();
        klijent.puni_naziv = jsonObject.getString("puni_naziv");
        klijent.naziv = jsonObject.getString("naziv");
        klijent.direktor = jsonObject.getString("direktor");
        klijent.pib = jsonObject.getString("pib");
        klijent.pdv = jsonObject.getString("pdv");

        return klijent;
    }
    public void RefreshSmece (KlijentZaSerijalizaciju klijent){
        TextView textViewNaziv = (TextView) findViewById(R.id.textViewNaziv);
        textViewNaziv.setText("Naziv: "+klijent.naziv);
        TextView textViewNazivPuni = (TextView) findViewById(R.id.textViewNazivPuni);
        textViewNazivPuni.setText("Naziv puni: "+klijent.puni_naziv);
        TextView textViewPib = (TextView) findViewById(R.id.textViewPib);
        textViewPib.setText("Pib: "+klijent.pib);
        TextView textViewPdv = (TextView) findViewById(R.id.textViewPdv);
        textViewPdv.setText("Pdv: " +klijent.pdv);
        TextView textViewDirektor = (TextView) findViewById(R.id.textViewDirektor);
        textViewDirektor.setText("Direktor: "+klijent.direktor);
    }
    public class Knjiga{
        public int godina;
        public Date datumOd;
        public Date datumDo;
        public String opis;
        public String klijent;
        public int id_klijent;
        public int id_knjiga;
        public String getOpis() {
            return opis;
        }
    }

    public void JSONArray2KnjigaArray(JSONArray jsonLista){
        listaKnjiga.clear();
        if(jsonLista!=null){
            for(int i=0;i<jsonLista.length();i++){
                Knjiga knj = new Knjiga();
                try{
                    knj.godina = jsonLista.getJSONObject(i).getInt("godina");
                    SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy.");
                    knj.datumOd = format.parse(jsonLista.getJSONObject(i).getString("datumOd"));
                    knj.datumDo = format.parse(jsonLista.getJSONObject(i).getString("datumDo"));
                    knj.opis = jsonLista.getJSONObject(i).getString("opis");
                    knj.id_klijent=jsonLista.getJSONObject(i).getInt("id_klijent");
                    knj.klijent = jsonLista.getJSONObject(i).getString("klijent");
                    knj.id_knjiga = jsonLista.getJSONObject(i).getInt("id_knjiga");
                    listaKnjiga.add(knj);
                }
                catch(Throwable t){

                }
            }
        }
    }
    public void getProstaLista(ArrayList<Knjiga> lista){

        for(Knjiga k:listaKnjiga)
        {
            prostaLista.add(k.opis);
        }
    }
    public void getKnjigeZaView(ArrayList<Knjiga> lista){
        listaKnjigaView.clear();
        for (Knjiga k:listaKnjiga){
            listaKnjigaView.add(new KnjigaZaView(k.id_knjiga,k.klijent,k.godina,k.opis));
        }
    }

    public String getJSONObjectFromURL() throws IOException, JSONException {

        HttpURLConnection urlConnection = null;

        EditText editText = (EditText) findViewById(R.id.editText);
        android.net.Uri.Builder builder = new android.net.Uri.Builder();
        builder.scheme("http")
                .encodedAuthority("192.168.1.131:1990")
                .appendPath("WebForm1.aspx")
                .appendQueryParameter("klijent", "11")
                .appendQueryParameter("tip", "knj");


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

}
