package com.example.ddtv;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadFileListener;

public class Upload extends AppCompatActivity {
    private TextView title;
    private ImageView img;
    private Button up;

    @SuppressLint("WrongThread")
    @Override
    protected void onCreate ( Bundle savedInstanceState ) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_upload );

        initView ();
        Uri uri = Uri.parse ( Home.LOCAL_VIDEO_URI );

        File file =  new File(FileUtils.getFilePathByUri ( this, uri ));
        MediaMetadataRetriever mmr=new MediaMetadataRetriever();//实例化MediaMetadataRetriever对象
        mmr.setDataSource(file.getAbsolutePath());
        Bitmap bitmap=mmr.getFrameAtTime();//获得视频第一帧的Bitmap对象
        img.setImageBitmap ( bitmap );
        System.out.println ( FileUtils.getFilePathByUri ( this, uri ) );


        BmobFile bmobFile = new BmobFile ( file );
        up.setOnClickListener ( new View.OnClickListener ( ) {
            @Override
            public void onClick ( View view ) {
                bmobFile.uploadblock ( new UploadFileListener ( ) {
                    @Override
                    public void done ( BmobException e ) {
                        if ( e==null ){
                            Video video = new Video ();
                            video.setTitle ( title.getText ().toString ().trim () );
                            video.setAuthor ( BmobUser.getCurrentUser ( User.class ).getUsername () );
                            video.setFile ( bmobFile  );
                            video.save ( new SaveListener< String > ( ) {
                                @Override
                                public void done ( String s , BmobException e ) {
                                    if ( e==null ){
                                        Toast.makeText ( Upload.this, "上传成功！", Toast.LENGTH_SHORT ).show ();
                                        Intent intent = new Intent ( Upload.this, Home.class );
                                        startActivity ( intent );
                                        finish ();
                                    }else {
                                        Toast.makeText ( Upload.this, "上传失败："+e.getMessage (), Toast.LENGTH_SHORT ).show ();
                                        Log.e("e: ",e.getMessage ());
                                    }
                                }
                            } );
                        }
                    }
                } );

            }
        } );

    }
    private void initView(){
        title = (TextView) findViewById ( R.id.upload_title );
        img = (ImageView) findViewById ( R.id.upload_bitmap );
        up = ( Button ) findViewById ( R.id.upload_up );
    }
}
