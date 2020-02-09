package com.example.ddtv;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.google.android.material.navigation.NavigationView;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.util.V;


public class Home extends AppCompatActivity {

    private NavigationView navigationView;
    private View headerView;
    private TextView userName;
    private User user = BmobUser.getCurrentUser ( User.class );
    private Button logOut;
    @Override
    protected void onCreate ( Bundle savedInstanceState ) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_home );

        initView ();
        userName.setText ( user.getUsername () );

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
}

