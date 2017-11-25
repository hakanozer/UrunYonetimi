package com.example.java_oglen.urunyonetimi;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;

import java.io.IOException;

import static android.content.Context.MODE_PRIVATE;

public class KullaniciGiris extends AppCompatActivity {

    SharedPreferences sha;
    static SharedPreferences.Editor edit;

    EditText etmail , etpassword;
    String stmail ,stpassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kullanici_giris);

        etmail = findViewById(R.id.txtGirisAdi);
        etpassword = findViewById(R.id.txtGirisSifre);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        sha = getSharedPreferences("urunxml",MODE_PRIVATE);
        edit = sha.edit();

    }


    public void kayitYap(View v) {
        Intent i = new Intent(KullaniciGiris.this, KayitYap.class);
        startActivity(i);
    }

    public void geri() {
        onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //http://jsonbulut.com/json/userLogin.php?ref=cb226ff2a31fdd460087fedbb34a6023&userEmail=b@b.com&userPass=123456&face=no


    public void girisJson(View v) {
        stmail = etmail.getText().toString();
        stpassword = etpassword.getText().toString();

        String url = "http://jsonbulut.com/json/userLogin.php?ref=cb226ff2a31fdd460087fedbb34a6023&" +
                "userEmail="+stmail+
                "&userPass="+stpassword+"" +
                "&face=no";

        new JsonData(url, this).execute();
    }

}

class JsonData extends AsyncTask<Void, Void, Void> {
    String url = "";
    String data = "";
    Context cnx;
    ProgressDialog pd;




    public JsonData(String url, Context cnx) {
        this.url = url;
        this.cnx = cnx;
        pd = new ProgressDialog(cnx);
        pd.setMessage("İşlem gerçekleşiyor azıcık bekle");
        pd.show();


    }




    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Void doInBackground(Void... params) {

        try {
            data = Jsoup.connect(url).ignoreContentType(true).get().body().text();
        } catch (IOException ex) {
            Log.e("data json hatası", "doınBackground", ex);
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

        try {
            JSONObject jo = new JSONObject(data);
            boolean durum = jo.getJSONArray("user").getJSONObject(0).getBoolean("durum");
            String mesaj = jo.getJSONArray("user").getJSONObject(0).getString("mesaj");
            if (durum) {
                //giriş basarılı
                Toast.makeText(cnx, mesaj, Toast.LENGTH_SHORT).show();
                JSONObject obj = jo.getJSONArray("user").getJSONObject(0).getJSONObject("bilgiler");
                String kid = obj.getString("userId");
                String userName = obj.getString("userName");
                String userSurname = obj.getString("userSurname");
                String userEmail = obj.getString("userEmail");
                String userPhone = obj.getString("userPhone");

                KullaniciGiris.edit.putString("kid", kid);
                KullaniciGiris.edit.putString("name", userName);
                KullaniciGiris.edit.putString("phone", userPhone);
                KullaniciGiris.edit.putString("surname", userSurname);
                KullaniciGiris.edit.putString("email", userEmail);
                KullaniciGiris.edit.commit();// yazma işlemi bitti
                ((Activity)cnx).finish();


            } else {
                //kayıt basarısız
                Toast.makeText(cnx, mesaj, Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException ex) {
            Log.e("data json hatası", "doınBackground", ex);
        }
        pd.dismiss();
    }
}


