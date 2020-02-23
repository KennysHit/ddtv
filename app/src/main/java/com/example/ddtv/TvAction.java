package com.example.ddtv;

import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

public class TvAction extends Fragment {
    private PlayerView playerView;
    private SimpleExoPlayer player;
    private MediaSource mediaSource;
    private Button fill;
    private View view;
    private Button back;

    @Nullable
    @Override
    public View onCreateView ( @NonNull LayoutInflater inflater , @Nullable ViewGroup container , @Nullable Bundle savedInstanceState ) {
        view = inflater.inflate ( R.layout.fragment_tvaction , container, false );

        String getMessage = TvList.EXTRA_MESSAGE;
        String[] msg = getMessage.split ( "@@@" );

        if ( getMessage != null ) {
            initView ();
            initExo ( msg[1].toString ().trim () );
        }

        fill.setOnClickListener ( new View.OnClickListener ( ) {
            @Override
            public void onClick ( View v ) {
                getActivity ().setRequestedOrientation( ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            }
        } );
        back.setOnClickListener ( new View.OnClickListener ( ) {
            @Override
            public void onClick ( View view ) {
                Fragment tvlist = new TvList ();
                getActivity ().getSupportFragmentManager ().beginTransaction ().replace ( R.id.home, tvlist ).commit ();
            }
        } );
        return view;
    }

    private void initExo ( String WebPosition ) {
        /**
         * 创建播放器
         */
        DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();// 得到默认合适的带宽
        TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);// 创建跟踪的工厂
        TrackSelector trackSelector = new DefaultTrackSelector (videoTrackSelectionFactory);// 创建跟踪器

        player = ExoPlayerFactory.newSimpleInstance(getContext (), trackSelector);
        playerView.setPlayer(player);// 绑定player
        /**
         * 准备player
         */
        // 生成通过其加载媒体数据的DataSource实例
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory (getContext (),
                Util.getUserAgent(getActivity (), "ExoPlayer"), bandwidthMeter);
        mediaSource = new HlsMediaSource.Factory(dataSourceFactory).createMediaSource( Uri.parse ( WebPosition ));// 创建要播放的媒体的MediaSource
        player.prepare(mediaSource);// 准备播放器的MediaSource
        player.setPlayWhenReady(true);// 当准备完毕后直接播放
    }

    public void onDestroy (){
        super.onDestroy ();
        myRelase ();
    }
    public void onStop (){
        super.onStop ();
        myRelase ();
    }
    private void myRelase(){
        if(player != null){
            player.release ();
            player = null;
            mediaSource = null;
        }

    }
    private void initView(){
        playerView = (PlayerView) view.findViewById ( R.id.action_player );
        fill = ( Button ) view.findViewById ( R.id.action_fill );
        back = ( Button ) view.findViewById ( R.id.action_back );
    }


}
