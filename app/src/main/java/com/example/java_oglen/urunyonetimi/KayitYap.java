package com.example.java_oglen.urunyonetimi;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;

import android.support.v7.app.AppCompatActivity;


//import static android.content.Context.MODE_PRIVATE;

public class KayitYap extends AppCompatActivity {

    SharedPreferences sha;
    static SharedPreferences.Editor edit;

    static EditText mail, parola, ad, soyad,tel;
    Button gonder;
    String email, password, name, surname,phone;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kayit_yap);

        soyad = (EditText) findViewById(R.id.etsurname);
        ad = (EditText) findViewById(R.id.etname);
        mail = (EditText) findViewById(R.id.etmail);
        parola = (EditText) findViewById(R.id.etpassword);
        gonder = (Button) findViewById(R.id.btnGonder);
        tel = (EditText) findViewById(R.id.etphone);

        sha = getSharedPreferences("urunxml",MODE_PRIVATE);
        edit = sha.edit();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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


    public void kayitJson(View v) {
        name = ad.getText().toString();
        phone = tel.getText().toString();
        surname = soyad.getText().toString();
        email = mail.getText().toString();
        password = parola.getText().toString();

        String url = "http://jsonbulut.com/json/userRegister.php?ref=cb226ff2a31fdd460087fedbb34a6023&" +
                "userName=" + name + "&" +
                "userSurname=" + surname + "&" +
                "userPhone="+phone+"&" +
                "userMail=" + email +"&"+
                "userPass=" + password;
        new jsonDataCagis(url, this).execute();
    }

}

class jsonDataCagis extends AsyncTask<Void, Void, Void> {



    public static final String ID = "id";
    String url = "";
    String data = "";
    Context cnx;
    ProgressDialog pd;

    static String kid;

    public jsonDataCagis(String url, Context cnx) {
        this.url = url;
        this.cnx = cnx;
        pd = new ProgressDialog(cnx);
        pd.setMessage("İşlem gerçekleşiyor Lütfen bekleyiniz :) ");
        pd.show();
    }

    //
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    //
    @Override
    protected Void doInBackground(Void... params) {

        try {
            data = Jsoup.connect(url).ignoreContentType(true).get().body().text();
        } catch (Exception ex) {
            Log.e("data json hatası", "doınBackground", ex);
        }


        return null;
    }

    //
    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

        //grafiksel ozelliği olan işlemler bu bolumde yapılır
        Log.d("gelen data : ", data);
        try {
            JSONObject obj = new JSONObject(data);
            boolean durum = obj.getJSONArray("user").getJSONObject(0).getBoolean("durum");
            String mesaj = obj.getJSONArray("user").getJSONObject(0).getString("mesaj");
            if (durum) {
                //kayıt basarılı
                Toast.makeText(cnx, mesaj, Toast.LENGTH_SHORT).show();
                kid = obj.getJSONArray("user").getJSONObject(0).getString("kullaniciId");
                Log.d("kid = ", kid);

                String name = KayitYap.ad.getText().toString();
                String phone = KayitYap.tel.getText().toString();
                String surname = KayitYap.soyad.getText().toString();
                String email = KayitYap.mail.getText().toString();

                KayitYap.edit.putString("kid", kid);
                KayitYap.edit.putString("name", name);
                KayitYap.edit.putString("phone", phone);
                KayitYap.edit.putString("surname", surname);
                KayitYap.edit.putString("email", email);
                KayitYap.edit.commit();// yazma işlemi bitti

                //Intent i = new Intent(KayitYap.this, MainActivity.class);
                //startActivity(i);

                //0Intent intent = new Intent(cnx, UrunAyrinti.class);
                //intent.putExtra(ID, kid);
                //cnx.startActivity(intent);

            } else {
                //kayıt basarısız
                Toast.makeText(cnx, mesaj, Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //yükleme ekranını tamamla
        pd.dismiss();
    }
}
