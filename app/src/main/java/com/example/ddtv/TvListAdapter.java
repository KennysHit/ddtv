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



public class TvListAdapter extends RecyclerView.Adapter< TvListAdapter.TvViewHolder >{
    private List<jsonObject> data;
    private Context context;

    public TvListAdapter ( Context context , List<jsonObject> data) {
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
    public TvViewHolder onCreateViewHolder ( ViewGroup viewGroup , int i ) {
        View container = LayoutInflater.from ( viewGroup.getContext () ).inflate (
                R.layout.tvlisit_card ,viewGroup,false );
        return new TvViewHolder (container);
    }
    /**
     * 填充每一行内容
     */
    public void onBindViewHolder ( TvViewHolder tvViewHolder , final int i ) {
        tvViewHolder.bind ( data.get ( i ), i);
        tvViewHolder.itemView.setOnClickListener ( new View.OnClickListener ( ) {
            @Override
            public void onClick ( View v ) {
                if ( listener != null ){
                    listener.onClick ( i );
                }
            }
        } );
        tvViewHolder.itemView.setOnLongClickListener ( new View.OnLongClickListener ( ) {
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

    public class TvViewHolder extends RecyclerView.ViewHolder{
        private TextView moveName;
        private TextView exp;
        private ImageView imageView;

        private int[] img={
                R.drawable.c1,
                R.drawable.c2,
                R.drawable.c3,
                R.drawable.c4,
                R.drawable.c5,
                R.drawable.c6,
                R.drawable.js,
                R.drawable.zj,
                R.drawable.df,
                R.drawable.hn,
        };
        public TvViewHolder ( View container) {
            super(container);
            moveName = container.findViewById(R.id.tv_card_title );
            imageView = container.findViewById(R.id.tv_card_img );
            exp = container.findViewById ( R.id.tv_card_exp );
        }

        public void bind(jsonObject jo ,int i) {
            this.moveName.setText(jo.getTitle ());
            this.exp.setText ( jo.getQuality () );
            imageView.setImageResource ( img[i] );
        }
    }
}

