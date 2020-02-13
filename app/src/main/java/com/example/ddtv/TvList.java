package com.example.ddtv;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.util.List;

public class TvList extends Fragment {

    private View view;
    private RecyclerView tvList;
    private TvListAdapter TvAdapter;
    private List<jsonObject> data;
    private Button toUV;
    public static String EXTRA_MESSAGE = null;
    @Nullable
    @Override
    public View onCreateView ( @NonNull LayoutInflater inflater , @Nullable ViewGroup container , @Nullable Bundle savedInstanceState ) {
        view = inflater.inflate ( R.layout.fragment_tvlist, container, false );

        initView ();
        initData ();

        System.out.println ( data.size () );
        TvAdapter = new TvListAdapter (getActivity (),this.data);
        tvList.setLayoutManager ( new LinearLayoutManager ( getActivity () ) );//设置列表样式
        tvList.setAdapter ( TvAdapter );
        TvAdapter.setOnItemClickListener ( new TvListAdapter.OnItemClickListener ( ) {
            @Override
            public void onClick ( int position ) {
                EXTRA_MESSAGE = data.get ( position ).getTitle ()+"@@@"+data.get ( position ).getUrl ();
                Fragment tvaction = new TvAction ();
                getActivity ().getSupportFragmentManager ().beginTransaction ().replace ( R.id.home, tvaction ).commit ();
            }
        } );
        TvAdapter.setOnItemLongClickListener ( new TvListAdapter.OnItemLongClickListener ( ) {
            @Override
            public void onLongClick ( int position ) {
                Log.d("info",data.get ( position ).getTitle ()+"的Url："+data.get ( position ).getUrl ());
                Toast.makeText ( getContext (),data.get ( position ).getTitle ()+"的Url："+data.get ( position ).getUrl (),Toast.LENGTH_LONG).show ();
            }
        } );
        toUV.setOnClickListener ( new View.OnClickListener ( ) {
            @Override
            public void onClick ( View view ) {
                Fragment uvlist = new UVList ();
                getActivity ().getSupportFragmentManager ().beginTransaction ().replace ( R.id.home, uvlist ).commit ();
            }
        } );
        return view;
    }
    private void initView () {
        tvList = view.findViewById ( R.id.tvlist );
        toUV = view.findViewById ( R.id.to_uvlist );
    }

    private void initData() {
        jsonGeter jg = new jsonGeter (getContext ());
        this.data = jg.getJson ( "data.json" );
    }
}
