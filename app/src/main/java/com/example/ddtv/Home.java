package com.example.ddtv;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;

import java.util.List;

import cn.bmob.v3.BmobUser;


public class Home extends AppCompatActivity {

    private NavigationView navigationView;
    private View headerView;
    private RecyclerView movieList;
    private MovieListAdapter movieAdapter;
    private List<jsonObject> data;
    private TextView userName;
    private User user = BmobUser.getCurrentUser ( User.class );

    public static final String EXTRA_MESSAGE = "com.example.ddtv.MESSAGE";
    @Override
    protected void onCreate ( Bundle savedInstanceState ) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_home );

        initView ( );
        initData ();

        userName.setText ( user.getUsername () );
        System.out.println ( data.size () );
        movieAdapter = new MovieListAdapter(this,this.data);
        movieList.setLayoutManager ( new LinearLayoutManager ( this ) );//设置列表样式
        movieList.setAdapter ( movieAdapter );
        movieAdapter.setOnItemClickListener ( new MovieListAdapter.OnItemClickListener ( ) {
            @Override
            public void onClick ( int position ) {
                Intent intent = new Intent ( Home.this,TvAction.class );
                intent.putExtra ( EXTRA_MESSAGE, data.get ( position ).getTitle ()+"@@@"+data.get ( position ).getUrl ());
                startActivity ( intent );
            }
        } );
        movieAdapter.setOnItemLongClickListener ( new MovieListAdapter.OnItemLongClickListener ( ) {
            @Override
            public void onLongClick ( int position ) {
                Log.d("info",data.get ( position ).getTitle ()+"的Url："+data.get ( position ).getUrl ());
                Toast.makeText ( Home.this,data.get ( position ).getTitle ()+"的Url："+data.get ( position ).getUrl (),Toast.LENGTH_LONG).show ();
            }
        } );


    }
    private void initView () {
        navigationView = ( NavigationView ) findViewById ( R.id.navigation );
        headerView = navigationView.getHeaderView ( 0 );
        userName = ( TextView ) headerView.findViewById ( R.id.nav_userName );
        navigationView.setItemIconTintList ( null );
        movieList = findViewById ( R.id.main_movieList );
    }

    private void initData() {
        jsonGeter jg = new jsonGeter (this);
        this.data = jg.getJson ( "data.json" );
    }
}

