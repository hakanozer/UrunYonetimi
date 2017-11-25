package com.example.java_oglen.urunyonetimi;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;

import java.io.IOException;


public class Profil extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public Profil() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Profil.
     */
    // TODO: Rename and change types and number of parameters
    public static Profil newInstance(String param1, String param2) {
        Profil fragment = new Profil();
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

    View view;
    EditText userName, userSurname, userMail, userPhone, userPass;
    Button btnGuncelle;
    static SharedPreferences sha;
    static SharedPreferences.Editor edit;
    static String userId;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_profil, container, false);

        userName = (EditText) view.findViewById(R.id.txtuserName);
        userSurname = (EditText) view.findViewById(R.id.txtuserSurname);
        userMail = (EditText) view.findViewById(R.id.txtuserMail);
        userPhone = (EditText) view.findViewById(R.id.txtuserPhone);
        userPass = (EditText) view.findViewById(R.id.txtuserPass);
        btnGuncelle = (Button) view.findViewById(R.id.btnGuncelle);

        sha = view.getContext().getSharedPreferences("urunxml", Context.MODE_PRIVATE);
        edit = sha.edit();
        userName.setText(sha.getString("name", ""));
        userSurname.setText(sha.getString("surname", ""));
        userMail.setText(sha.getString("email", ""));
        userPhone.setText(sha.getString("phone", ""));
        userPass.setText(sha.getString("userPass", ""));
        userId = sha.getString("kid","");

        btnGuncelle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String n = userName.getText().toString().trim();
                String s = userSurname.getText().toString().trim();
                String m = userMail.getText().toString().trim();
                String p = userPhone.getText().toString().trim();
                String ss = userPass.getText().toString().trim();

                if (ss.equals("")) {
                    Toast.makeText(view.getContext(), "Lütfen Şifre Belirleyiniz !", Toast.LENGTH_SHORT).show();
                    userPass.requestFocus();
                }else {
                    String url =  "http://jsonbulut.com/json/userSettings.php?ref=cb226ff2a31fdd460087fedbb34a6023&userName="+n+"&userSurname="+s+"&userMail="+m+"&userPhone="+p+"&userPass="+ss+"&userId="+userId+"";
                    Log.d("URL " , url);
                    new jsonGuncelle(url,view.getContext()).execute();
                }

            }
        });
        return view;
    }


    class jsonGuncelle extends AsyncTask<Void,Void,Void > {
        String url = "";
        String data = "";
        Context cnx;
        ProgressDialog pd;




        public jsonGuncelle(String url, Context cnx) {
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

                    String n = userName.getText().toString().trim();
                    String s = userSurname.getText().toString().trim();
                    String m = userMail.getText().toString().trim();
                    String p = userPhone.getText().toString().trim();

                    Profil.edit.putString("kid", userId);
                    Profil.edit.putString("name", n);
                    Profil.edit.putString("phone", p);
                    Profil.edit.putString("surname", s);
                    Profil.edit.putString("email", m);
                    Profil.edit.commit();// yazma işlemi bitti

                    /*if(cnx instanceof Activity){
                        ((Activity)cnx).finish(); }
                        */


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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
