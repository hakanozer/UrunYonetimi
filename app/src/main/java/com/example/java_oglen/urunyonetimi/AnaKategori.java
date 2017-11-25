package com.example.java_oglen.urunyonetimi;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;

import java.util.ArrayList;

import EnumsProperty.KategoriPro;
import EnumsProperty.KategorilerEnum;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AnaKategori.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AnaKategori#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AnaKategori extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    ListView anaKategoriLs;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    Button btnYenile;

    private OnFragmentInteractionListener mListener;

    public AnaKategori() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AnaKategori.
     */
    // TODO: Rename and change types and number of parameters
    public static AnaKategori newInstance(String param1, String param2) {
        AnaKategori fragment = new AnaKategori();
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
    private View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        String url = "http://jsonbulut.com/json/companyCategory.php?ref=cb226ff2a31fdd460087fedbb34a6023";
        view  = inflater.inflate(R.layout.fragment_ana_kategori, container, false);
        anaKategoriLs = (ListView) view.findViewById(R.id.AnaKategoriListView);
        btnYenile = (Button) view.findViewById(R.id.btnYenile);
        btnYenile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "http://jsonbulut.com/json/companyCategory.php?ref=cb226ff2a31fdd460087fedbb34a6023";
                new jsonData(url, view.getContext()).execute();
            }
        });
        new jsonData(url, this.getContext()).execute();
        return view;
    }



    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
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
            Log.d("Gelen Data ", data);
            try {
                JSONObject obj = new JSONObject(data);
                boolean durum = obj.getJSONArray("Kategoriler").getJSONObject(0).getBoolean("durum");
                String mesaj = obj.getJSONArray("Kategoriler").getJSONObject(0).getString("mesaj");
                if (durum ) {
                    // kullanıcı kayıt başarılı
                    Toast.makeText(cnx, mesaj, Toast.LENGTH_SHORT).show();
                    JSONArray arr = obj.getJSONArray("Kategoriler").getJSONObject(0).getJSONArray("Categories");
                    final ArrayList<String> ustKat = new ArrayList<>();
                    final ArrayList<KategoriPro> tumKat = new ArrayList<>();
                    for(int i = 0; i<arr.length(); i++) {
                        KategoriPro kt = new KategoriPro();
                        JSONObject ns = arr.getJSONObject(i);
                        kt.setCatogryId(ns.getString(""+KategorilerEnum.CatogryId));
                        kt.setCatogryName(ns.getString(""+KategorilerEnum.CatogryName));
                        kt.setTopCatogryId(ns.getString(""+KategorilerEnum.TopCatogryId));
                        tumKat.add(kt);
                    }


                    // üst kategoriler ayrılıyor
                    for (KategoriPro kr : tumKat) {
                        if(kr.getTopCatogryId().equals("0")) {
                            ustKat.add(kr.getCatogryName());
                        }
                    }

                    Log.d("ArrayList" ,ustKat.toString() );
                    ArrayAdapter<String> anaKatArrAdp=new ArrayAdapter<String>( view.getContext() , android.R.layout.simple_list_item_1, android.R.id.text1, ustKat);
                    anaKategoriLs.setAdapter(anaKatArrAdp);
                    anaKategoriLs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            // üst kat id yi yakala
                            String ustid = "";
                            for (KategoriPro kr : tumKat) {
                                if(ustKat.get(i).equals(kr.getCatogryName())) {
                                    ustid = kr.getCatogryId();
                                    break;
                                }
                            }

                            final ArrayList<String> altLs = new ArrayList<>();
                            for(KategoriPro kr : tumKat) {
                                if(kr.getTopCatogryId().equals(ustid)) {
                                    altLs.add(kr.getCatogryName());
                                }
                            }
                            ArrayAdapter<String> altKatArrAdp=new ArrayAdapter<String>( view.getContext() , android.R.layout.simple_list_item_1, android.R.id.text1,altLs );
                            anaKategoriLs.setAdapter(altKatArrAdp);
                            anaKategoriLs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    //Toast.makeText(cnx, altLs.get(i), Toast.LENGTH_SHORT).show();
                                    for( KategoriPro krt : tumKat) {
                                        if(krt.getCatogryName().equals(altLs.get(i))) {
                                            String altKatid = krt.getCatogryId();
                                            //Toast.makeText(cnx, altKatid, Toast.LENGTH_SHORT).show();
                                            Intent ii = new Intent(cnx , UrunList.class);
                                            ii.putExtra("kid", altKatid);
                                            startActivity(ii);
                                            //getActivity().finish();
                                        }
                                    }
                                }
                            });
                        }
                    });


                }else {
                    // kullanıcı kayıt başarısız
                    Toast.makeText(cnx, mesaj, Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


            // yükleme ekranını tamamla
            pro.dismiss();
        }


    }
}
