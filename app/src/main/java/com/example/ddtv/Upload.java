package com.example.ddtv;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
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
import java.io.IOException;
import java.util.List;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadBatchListener;
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
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();//实例化MediaMetadataRetriever对象
        mmr.setDataSource(file.getAbsolutePath());
        Bitmap bitmap=mmr.getFrameAtTime();//获得视频第一帧的Bitmap对象
        img.setImageBitmap ( bitmap );
        System.out.println ( FileUtils.getFilePathByUri ( this, uri ) );

        up.setOnClickListener ( new View.OnClickListener ( ) {
            @Override
            public void onClick ( View v ) {
                final String[] filePaths = new String[2];
                filePaths[0] = FileUtils.getFilePathByUri ( Upload.this, uri );
                filePaths[1] = saveBitmap ( bitmap );
                BmobFile.uploadBatch(filePaths, new UploadBatchListener() {

                    @Override
                    public void onSuccess(List<BmobFile> files,List<String> urls) {
                        //1、files-上传完成后的BmobFile集合，是为了方便大家对其上传后的数据进行操作，例如你可以将该文件保存到表中
                        //2、urls-上传文件的完整url地址
                        if(urls.size()==filePaths.length){//如果数量相等，则代表文件全部上传完成
                            Video video = new Video ();
                            video.setAuthor ( BmobUser.getCurrentUser ( User.class ).getUsername () );
                            video.setFile ( files.get ( 0 ) );
                            video.setImg ( files.get ( 1 ) );
                            video.setTitle ( title.getText ().toString ().trim () );
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

                    @Override
                    public void onError(int statuscode, String errormsg) {
                        Toast.makeText ( Upload.this, "上传失败：" + statuscode + errormsg, Toast.LENGTH_SHORT ).show ();
                    }

                    @Override
                    public void onProgress(int curIndex, int curPercent, int total,int totalPercent) {
                        //1、curIndex--表示当前第几个文件正在上传
                        //2、curPercent--表示当前上传文件的进度值（百分比）
                        //3、total--表示总的上传文件数
                        //4、totalPercent--表示总的上传进度（百分比）
                    }
                });
            }
        } );

    }
    private void initView(){
        title = (TextView) findViewById ( R.id.upload_title );
        img = (ImageView) findViewById ( R.id.upload_bitmap );
        up = ( Button ) findViewById ( R.id.upload_up );
    }


    /** 保存方法 */
    public String saveBitmap(Bitmap bitmap) {
        Log.e("TAG:", "保存图片");
        File f = new File(Environment.getExternalStorageDirectory(), "123.jpg");
        if (f.exists()) {
            f.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(f);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
            Log.i("TAG", "已经保存");
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println ( f.getAbsolutePath () );
        return f.getAbsolutePath ();
    }
}
