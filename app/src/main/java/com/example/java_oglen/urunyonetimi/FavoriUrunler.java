package com.example.java_oglen.urunyonetimi;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;



public class FavoriUrunler extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public FavoriUrunler() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FavoriUrunler.
     */
    // TODO: Rename and change types and number of parameters
    public static FavoriUrunler newInstance(String param1, String param2) {
        FavoriUrunler fragment = new FavoriUrunler();
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

    ListView lview;
    TextView tvbaslik, tvfiyat;
    ArrayList<String> baslikim = new ArrayList<>();
    ArrayList<String> fiyatim = new ArrayList<>();
    BaseAdapter ba;
    LayoutInflater li;
    Boolean favoriyok = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_favori_urunler, container, false);
        lview = (ListView) v.findViewById(R.id.lvfavori);

        SQLiteDatabase fOku = new DB(v.getContext()).getReadableDatabase();


        //query ile sorgumuzu gerçekleştiririz standart sql sorgusu
        String query = "SELECT * FROM favoriler";

        //sql data base bize cursor yapsısında bir veri dönüşü yapacaktır.Yapmış olduğumuz bu sorguyu belirtiriz
        // Cursor Veritabanı nesneleri içerisinde satır bazlı hareket etmemizi sağlar
        Cursor cr = fOku.rawQuery(query, null);

        //Database de ulaşmak istediğim verilere ulaşmamı sağlıyor
        int uid = cr.getColumnIndex("kid");
        int ubaslik = cr.getColumnIndex("ubaslik");
        int ufiyat = cr.getColumnIndex("ufiyat");


        //data base dolu ise ekrana birşeyler yazdırıyoruz
        if (cr != null) {

            //çektiğimiz verileri sırayla okumamızı sağlıyor
            while (cr.moveToNext()) {

                String baslik = cr.getString(ubaslik);
                baslikim.add(baslik);
                fiyatim.add(cr.getString(ufiyat));

            }

            lview = (ListView) v.findViewById(R.id.lvfavori);
            li = LayoutInflater.from(v.getContext());

            ba = new BaseAdapter() {
                @Override
                public int getCount() {
                    return baslikim.size();
                }

                @Override
                public Object getItem(int position) {
                    return null;
                }

                @Override
                public long getItemId(int position) {
                    return 0;
                }

                @Override
                public View getView(int i, View view, ViewGroup parent) {
                    if (view == null) {
                        view = li.inflate(R.layout.favoritem, null);
                    } else {
                        //Log.e("olusturulmuş","view favori ürünler");
                    }

                    tvbaslik = (TextView) view.findViewById(R.id.item_baslik);
                    tvfiyat = (TextView) view.findViewById(R.id.item_fiyat);

                    tvbaslik.setText(baslikim.get(i));
                    tvfiyat.setText(fiyatim.get(i));

                    return view;
                }
            };
            lview.setAdapter(ba);
        }
        return v;
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
