package com.example.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Handler;
import android.os.Looper;
import android.os.StrictMode;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    Button btn;
    TextView naziv;
    JSONObject jsonObject;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
            //your codes here
        }




        btn = findViewById(R.id.button);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*ProgressBar pg = findViewById(R.id.progressBar);
                pg.setVisibility(View.VISIBLE);
                try{
                    String jsonString = getJSONObjectFromURL();
                    if(jsonString.charAt(0)=='{'){
                        jsonObject = new JSONObject(getJSONObjectFromURL());
                    }


                    EditText editTextTip = findViewById(R.id.editTextTip);
                    if(editTextTip.getText().toString().equals("knj")){
                        Intent intent = new Intent(getApplicationContext(),TestActivity.class);
                        intent.putExtra("knjige", new String(getJSONObjectFromURL()));
                        startActivity(intent);
                        pg.setVisibility(View.GONE);
                    }
                    else{
                        RefreshSmece(JSONtoKlijent(jsonObject));
                        pg.setVisibility(View.GONE);
                    }
                }
                catch(IOException e){
                    e.printStackTrace();
                }
                catch(JSONException e){
                    e.printStackTrace();
                }*/
                AsyncKnjige thread = new AsyncKnjige();
                thread.start();

            }
        });


    }
    public class AsyncKnjige extends Thread
    {
        @Override
        public void run()
        {
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable()
            {
                @Override
                public void run()
                {
                    ProgressBar pg = findViewById(R.id.progressBar);
                    pg.setVisibility(View.VISIBLE);
                    btn = findViewById(R.id.button);
                    btn.setEnabled(false);
                }
            });
            try{
                String jsonString = getJSONObjectFromURL();
                if(jsonString.charAt(0)=='{'){
                    jsonObject = new JSONObject(getJSONObjectFromURL());
                }


                EditText editTextTip = findViewById(R.id.editTextTip);
                if(editTextTip.getText().toString().equals("knj")){
                    Intent intent = new Intent(getApplicationContext(),TestActivity.class);
                    intent.putExtra("knjige", new String(getJSONObjectFromURL()));
                    startActivity(intent);

                    handler.post(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            ProgressBar pg = findViewById(R.id.progressBar);
                            pg.setVisibility(View.GONE);
                            btn = findViewById(R.id.button);
                            btn.setEnabled(true);
                        }
                    });
                }
                else{

                    handler.post(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            try
                            {
                                RefreshSmece(JSONtoKlijent(jsonObject));
                            } catch (JSONException e)
                            {
                                e.printStackTrace();
                            }
                            ProgressBar pg = findViewById(R.id.progressBar);
                            pg.setVisibility(View.GONE);
                            btn = findViewById(R.id.button);
                            btn.setEnabled(true);
                        }
                    });
                }
            }
            catch(IOException e){
                e.printStackTrace();
            }
            catch(JSONException e){
                e.printStackTrace();
            }
        }
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
    public String getJSONObjectFromURL() throws IOException, JSONException {

        HttpURLConnection urlConnection = null;

        EditText editText = (EditText) findViewById(R.id.editText);
        android.net.Uri.Builder builder = new android.net.Uri.Builder();
        builder.scheme("http")
                .encodedAuthority("192.168.1.131:1990")
                .appendPath("WebForm1.aspx")
                .appendQueryParameter("klijent", editText.getText().toString());

        EditText editTextTip = findViewById(R.id.editTextTip);
        if (editTextTip.getText().toString().equals("knj")) {
            builder.appendQueryParameter("tip", editTextTip.getText().toString());
        }

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
}
