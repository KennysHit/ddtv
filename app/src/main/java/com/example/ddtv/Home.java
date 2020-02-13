package com.example.ddtv;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.util.V;


public class Home extends AppCompatActivity {

    private NavigationView navigationView;
    private View headerView;
    private TextView userName;
    private User user = BmobUser.getCurrentUser ( User.class );
    private Button logOut;
    public static String LOCAL_VIDEO_URI;
    @Override
    protected void onCreate ( Bundle savedInstanceState ) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_home );

        initView ();
        userName.setText ( user.getUsername () );

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected( MenuItem item) {
                switch ( item.getItemId () ){
                    case R.id.single_1:
                        getLocalVideo ();
                        break;
                    case R.id.single_2:
                        Log.d ("log: ", "收藏视频！" );
                        break;
                }
                //在这里处理item的点击事件
                return true;
            }
        });

        Fragment tvlist = new TvList ();
        getSupportFragmentManager ().beginTransaction ().add ( R.id.home, tvlist ).commit ();

        logOut.setOnClickListener ( new View.OnClickListener ( ) {
            @Override
            public void onClick ( View view ) {
                user.logOut ();
                Intent intent = new Intent ( Home.this, MainActivity.class );
                startActivity ( intent );
                finish ();
            }
        } );
    }

    private void initView(){
        navigationView = ( NavigationView ) findViewById ( R.id.navigation );
        headerView = navigationView.getHeaderView ( 0 );
        userName = ( TextView ) headerView.findViewById ( R.id.nav_userName );
        navigationView.setItemIconTintList ( null );
        logOut = ( Button ) headerView.findViewById ( R.id.nav_logout );
    }

    @Override
    public boolean onKeyDown ( int keyCode , KeyEvent event ) {
        setRequestedOrientation ( ActivityInfo.SCREEN_ORIENTATION_PORTRAIT );
        return super.onKeyDown ( keyCode , event );
    }

    private void getLocalVideo() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        /* 开启Pictures画面Type设定为image */
        //intent.setType("image/*");
        // intent.setType("audio/*"); //选择音频
        intent.setType("video/*"); //选择视频 （mp4 3gp 是android支持的视频格式）

        // intent.setType("video/*;image/*");//同时选择视频和图片

        /* 使用Intent.ACTION_GET_CONTENT这个Action */
        intent.setAction(Intent.ACTION_GET_CONTENT);
        /* 取得相片后返回本画面 */
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult ( requestCode , resultCode , data );
        if ( requestCode == 1 ) {
            if ( resultCode == RESULT_OK ) {
                LOCAL_VIDEO_URI = data.getData ().toString ();

                Intent intent = new Intent ( this, Upload.class );
                startActivity ( intent );

            }
        }
    }

}

