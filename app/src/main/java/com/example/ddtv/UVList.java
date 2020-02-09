package com.example.ddtv;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class UVList extends Fragment {
    private View view;
    private Button toTv;
    @Nullable
    @Override
    public View onCreateView ( @NonNull LayoutInflater inflater , @Nullable ViewGroup container , @Nullable Bundle savedInstanceState ) {
        view = inflater.inflate ( R.layout.fragment_svlist, container, false );
        initView ();

        toTv.setOnClickListener ( new View.OnClickListener ( ) {
            @Override
            public void onClick ( View view ) {
                Fragment tvlist = new TvList ();
                getActivity ().getSupportFragmentManager ().beginTransaction ().replace ( R.id.home, tvlist ).commit ();
            }
        } );
        return view;
    }

    public void initView(){
        toTv = (Button)view.findViewById ( R.id.to_tvlist );
    }
}
