package com.example.ddtv;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;



public class UvListAdapter extends RecyclerView.Adapter< UvListAdapter.UvViewHolder >{
    private List<jsonObject> data;
    private Context context;

    public UvListAdapter ( Context context , List<jsonObject> data) {
        if(data==null)
            this.data = new ArrayList<> ( );
        this.data = data;
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
        uvViewHolder.bind ( );
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

        public void bind( ) {

        }
    }
}

