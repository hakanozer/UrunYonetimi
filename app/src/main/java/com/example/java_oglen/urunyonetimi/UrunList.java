package com.example.java_oglen.urunyonetimi;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;

import java.util.ArrayList;

import EnumsProperty.KategoriPro;
import EnumsProperty.KategorilerEnum;

public class UrunList extends AppCompatActivity {

    ListView urunListView;
    BaseAdapter urunBaseAdapter;
    LayoutInflater urunLayoutInflater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_urun_list);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        urunListView = (ListView) findViewById(R.id.urunListView);
        urunLayoutInflater = LayoutInflater.from(this);



        String katid = getIntent().getExtras().getString("kid");
        String url = "http://jsonbulut.com/json/product.php?ref=cb226ff2a31fdd460087fedbb34a6023&start=1&count=2&categoryId="+katid;
        new jsonData(url, this).execute();
    }


    class jsonData extends AsyncTask<Void, Void, Void> {

        String url = "";
        String data = "";
        ProgressDialog pro;
        Context cnx;
        public jsonData(String url, Context cnx) {
            this.url = url;
            this.cnx = cnx;
            pro = new ProgressDialog(cnx);
            pro.setMessage("İşlem Yapılıor, Lütfen Bekleyiniz");
            pro.show();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {

                data = Jsoup.connect(url).ignoreContentType(true).get().body().text();
            }catch (Exception ex) {
                Log.e("Data Json Hatası", "doInBackground: ",ex );
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            // grafiksel özelliği olan işlemler bu bölümde yapılır.
            try {
                //Log.d("TumData", data);
                JSONObject obj = new JSONObject(data);
                boolean durum = obj.getJSONArray("Products").getJSONObject(0).getBoolean("durum");
                String mesaj = obj.getJSONArray("Products").getJSONObject(0).getString("mesaj");
                if ( !obj.getJSONArray("Products").getJSONObject(0).isNull("bilgiler") ) {
                    final JSONArray arr = obj.getJSONArray("Products").getJSONObject(0).getJSONArray("bilgiler");
                    urunBaseAdapter = new BaseAdapter() {
                        @Override
                        public int getCount() {
                            return arr.length();
                        }

                        @Override
                        public Object getItem(int i) {
                            return null;
                        }

                        @Override
                        public long getItemId(int i) {
                            return 0;
                        }

                        @Override
                        public View getView(int i, View view, ViewGroup viewGroup) {

                            if(view == null) {
                                view = urunLayoutInflater.inflate(R.layout.urunlistcustomrow, null);
                            }

                            JSONObject obj = null;
                            try {
                                obj = arr.getJSONObject(i);
                            ImageView img = (ImageView) view.findViewById(R.id.urunResimRow);
                            TextView txtBaslik = (TextView) view.findViewById(R.id.urunBaslikRow);
                            TextView txtFiyat = (TextView) view.findViewById(R.id.urunFiyatRow);

                            txtBaslik.setText(obj.getString("productName"));
                            txtFiyat.setText(obj.getString("price"));

                            // resim datası çözümleniyor
                                boolean rDurum = obj.getBoolean("image");
                                if (rDurum) {
                                    String url = obj.getJSONArray("images").getJSONObject(0).getString("normal");
                                    Picasso.with(cnx).load(url).into(img);
                                }

                            }
                            catch (JSONException e) {
                                e.printStackTrace();
                            }

                            return view;
                        }
                    };

                    urunListView.setAdapter(urunBaseAdapter);
                    urunListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                           // Toast.makeText(cnx, "Seçim " + i, Toast.LENGTH_SHORT).show();
                            try {
                                Intent uruni = new Intent(UrunList.this, UrunAyrinti.class);
                                UrunAyrinti.uobj = arr.getJSONObject(i);
                                startActivity(uruni);
                               // finish();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });

                }else {
                    Toast.makeText(cnx, "data yok", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


            // yükleme ekranını tamamla
            pro.dismiss();
        }


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
}
