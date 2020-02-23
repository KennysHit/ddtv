package com.example.ddtv;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaDataSource;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DownloadFileListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.util.V;


public class UvListAdapter extends RecyclerView.Adapter< UvListAdapter.UvViewHolder >{
    private List<Video> data;
    private Context context;

    public UvListAdapter ( Context context , List<Video> data) {
        this.data = data;
        if(data==null)
            this.data = new ArrayList<> ( );
        this.context = context;
    }
    /**
     * 定义点击事件接口
     */
    public interface OnItemClickListener{
        void onClick ( int position );
    }
    public interface OnItemLongClickListener{
        void onLongClick ( int position );
    }

    private  OnItemClickListener listener;
    private OnItemLongClickListener longListener;

    public void setOnItemClickListener ( OnItemClickListener listener ) {
        this.listener = listener;
    }
    public void setOnItemLongClickListener ( OnItemLongClickListener longListener ) {
        this.longListener = longListener;
    }

    /**
     * 找到电影行对应的xml
     */
    public UvViewHolder onCreateViewHolder ( ViewGroup viewGroup , int i ) {
        View container = LayoutInflater.from ( viewGroup.getContext () ).inflate (
                R.layout.uvlist_card ,viewGroup,false );
        return new UvViewHolder (container);
    }
    /**
     * 填充每一行内容
     */
    public void onBindViewHolder ( UvViewHolder uvViewHolder , final int i ) {
        uvViewHolder.bind ( data.get ( i ) );
        uvViewHolder.itemView.setOnClickListener ( new View.OnClickListener ( ) {
            @Override
            public void onClick ( View v ) {
                if ( listener != null ){
                    listener.onClick ( i );
                }
            }
        } );
        uvViewHolder.itemView.setOnLongClickListener ( new View.OnLongClickListener ( ) {
            @Override
            public boolean onLongClick ( View v ) {
                longListener.onLongClick ( i );
                return true;
            }
        } );
    }

    public int getItemCount () {
        return data.size ();
    }

    public class UvViewHolder extends RecyclerView.ViewHolder{

        private TextView title;
        private ImageView imageView;

        public UvViewHolder ( View container) {
            super(container);
            title = (TextView) container.findViewById ( R.id.uv_card_title );
            imageView = (ImageView) container.findViewById ( R.id.uv_card_image );
        }

        public void bind( Video video ) {

            System.out.println ( "video: " + video.getObjectId () );
            BmobQuery<Video> query = new BmobQuery<Video>();
            query.getObject(video.getObjectId (), new QueryListener<Video> () {
                @Override
                public void done(Video object, BmobException e) {
                    if(e==null){
                        title.setText ( object.getTitle () );
                        object.getImg ().download ( new DownloadFileListener ( ) {
                            @Override
                            public void done ( String s , BmobException e ) {
                                System.out.println ( s );
                                if ( e==null ) {
                                    Log.i ( "Log:" , "下载成功" );
                                    Bitmap bitmap = BitmapFactory.decodeFile ( s );
                                    imageView.setImageBitmap ( bitmap );
                                }else {
                                    Log.i ( "Log:" , "下载失败" + e.getMessage () );
                                }
                            }

                            @Override
                            public void onProgress ( Integer integer , long l ) {

                            }
                        } );
//                        Glide.with ( context )
//                                .load ( object.getImg ().getFileUrl ())
//                                .diskCacheStrategy ( DiskCacheStrategy.ALL )
//                                .into ( imageView );


                    }else{
                        Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                    }
                }

            });

        }
    }
}

