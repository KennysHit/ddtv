package com.example.ddtv;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.LinkedList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class UVList extends Fragment {
    private View view;
    private Button toTv;
    private RecyclerView uvList;
    private UvListAdapter uvAdapter;
    private Handler handler;
    private List<Video> data;

    @Nullable
    @Override
    public View onCreateView ( @NonNull LayoutInflater inflater , @Nullable ViewGroup container , @Nullable Bundle savedInstanceState ) {
        view = inflater.inflate ( R.layout.fragment_uvlist , container , false );

        initView ( );
        initData ();

        handler = new Handler ( ){
            @Override
            public void handleMessage ( Message msg ) {
                switch ( msg.what ){
                    case 1:
                        data = (List< Video> ) msg.obj;
                        System.out.println ( "data: " + data );
                        uvAdapter = new UvListAdapter ( getActivity (), data );
                        uvList.setLayoutManager ( new GridLayoutManager ( getActivity (), 2 ) );
                        uvList.setAdapter ( uvAdapter );
                }
            }
        };


        toTv.setOnClickListener ( new View.OnClickListener ( ) {
            @Override
            public void onClick ( View view ) {
                Fragment tvlist = new TvList ( );
                getActivity ( ).getSupportFragmentManager ( ).beginTransaction ( ).replace ( R.id.home , tvlist ).commit ( );
            }
        } );

        return view;
    }

    public void initView () {
        toTv = ( Button ) view.findViewById ( R.id.to_tvlist );
        uvList = ( RecyclerView ) view.findViewById ( R.id.uv_list );
    }

    public void initData(){
        BmobQuery<Video> bmobQuery = new BmobQuery<Video>();
        bmobQuery.addQueryKeys("objectId");
        bmobQuery.findObjects(new FindListener<Video> () {
            @Override
            public void done(List<Video> object, BmobException e) {
                if(e==null && object!=null){
                    Log.i("bmob", "查询成功：共" + object.size() + "条数据。");
                    System.out.println ( "object: " + object );
                    Message message = handler.obtainMessage();
                    message.what = 1;
                    if ( object!=null ) {
                        message.obj = object;
                        handler.sendMessage ( message );
                        //注意：这里的Person对象中只有指定列的数据。
                    }
                }else{
                    Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                }
            }
        });
    }
}
