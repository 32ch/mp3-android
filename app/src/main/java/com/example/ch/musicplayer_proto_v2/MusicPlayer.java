package com.example.ch.musicplayer_proto_v2;

import android.content.ContentUris;
import android.content.Context;
import android.media.AudioManager;
import android.media.Image;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by ch on 2017-11-10.
 */

public class MusicPlayer{

    MediaPlayer player;
    int thisPosition;
    ArrayList<Integer> playList;
    ArrayList<MusicDB> musicList;
    boolean wasPlaying;
    boolean isLoop = false;
    TextView dispTxt;
    ImageView imageView;
    private final Uri artworkUri = Uri.parse("content://media/external/audio/albumart");
    Context mainView;
    SeekBar musicProgress;
    ImageButton playBtn, pauseBtn;

    MusicPlayer(){
        player = new MediaPlayer();
        player.setOnCompletionListener(mComplete);
        player.setOnSeekCompleteListener(mOnSeekCoplete);
    }

    void setMusicProgress(SeekBar seekBar){
        musicProgress = seekBar;
        musicProgress.setOnSeekBarChangeListener(onPlaySeek);
        progressHandler.sendEmptyMessageDelayed(0,200);
        musicProgress.setMin(0);
        musicProgress.setProgress(0);
    }

    void setView(Context context, TextView textView, ImageView imageView)
    {
        mainView = context;
        dispTxt = textView;
        this.imageView = imageView;
    }

    void setButton(ImageButton playBtn, ImageButton pauseBtn)
    {
        this.playBtn = playBtn;
        this.pauseBtn = pauseBtn;
    }

    void setMusicList(ArrayList<MusicDB> musicList){
        this.musicList = musicList;
    }

    void setPlayList(ArrayList<Integer> playList)
    {
        this.playList =playList;
    }

    void play_pause(){
        if(player.isPlaying() == false){
            player.start();
            playBtn.setVisibility(View.INVISIBLE);
            pauseBtn.setVisibility(View.VISIBLE);
        }
        else{
            player.pause();
            playBtn.setVisibility(View.VISIBLE);
            pauseBtn.setVisibility(View.INVISIBLE);
        }
    }

    void previous(){
        if (!(thisPosition==0)) {
            //mCr.moveToPrevious();
            thisPosition--;
        } else {
            //dispTxt.setText("this is the first");
            //mCr.moveToLast();
            thisPosition = playList.size()-1;
            //break;
        }
        play();
    }

    void next(){
        if(!(thisPosition == (playList.size()-1))){
            thisPosition++;
        }
        else{
            thisPosition=0;
            //break;
        }
        play();
    }

    void setThisPosition(int thisPosition)
    {
        this.thisPosition = thisPosition;
    }
    void play(){
        String path = musicList.get(playList.get(thisPosition)).mDataPath;
        try {
            player.reset();
            player.setDataSource(path);
            player.prepare();
            String temp = musicList.get(playList.get(thisPosition)).mTitle+" - "+musicList.get(playList.get(thisPosition)).mArtist;
            Uri albumArtUri = ContentUris.withAppendedId(artworkUri, musicList.get(playList.get(thisPosition)).mAlbumId);
            Picasso.with(mainView).load(albumArtUri).error(R.drawable.empty_albumart).into(imageView);
            musicProgress.setMax(player.getDuration());
            musicProgress.setProgress(0);
            dispTxt.setText(temp);
            player.start();
            playBtn.setVisibility(View.INVISIBLE);
            pauseBtn.setVisibility(View.VISIBLE);
        }catch(Exception e) {
            dispTxt.setText(e.getMessage());
        }
    }

    void play(int position)
    {
        thisPosition = position;
        //this.play();
        String path = musicList.get(playList.get(position)).mDataPath;
        try {
            player.reset();
            player.setDataSource(path);
            player.prepare();
            String temp = musicList.get(playList.get(position)).mTitle+" - "+musicList.get(playList.get(position)).mArtist;
            Uri albumArtUri = ContentUris.withAppendedId(artworkUri, musicList.get(playList.get(position)).mAlbumId);
            Picasso.with(mainView).load(albumArtUri).error(R.drawable.empty_albumart).into(imageView);
            musicProgress.setMax(player.getDuration());
            musicProgress.setProgress(0);
            dispTxt.setText(temp);
            player.start();
            playBtn.setVisibility(View.INVISIBLE);
            pauseBtn.setVisibility(View.VISIBLE);
        }catch(Exception e) {
            dispTxt.setText(e.getMessage());
        }
    }
    void release()
    {
        if(player != null){
            player.release();
            player = null;
        }    }
    //곡 완료 후 다음곡으로
    MediaPlayer.OnCompletionListener mComplete = new MediaPlayer.OnCompletionListener(){
        @Override
        public void onCompletion(MediaPlayer mediaPlayer){
            try {
                player.reset();
                //if(!mCr.isAfterLast())
                if((thisPosition != (playList.size()-1))) {
                    thisPosition++;
                } else{
                    thisPosition = 0;
                }
                String path = musicList.get(playList.get(thisPosition)).mDataPath;
                String temp = musicList.get(playList.get(thisPosition)).mTitle+" - "+musicList.get(playList.get(thisPosition)).mArtist;
                dispTxt.setText(temp);
                Uri albumArtUri = ContentUris.withAppendedId(artworkUri, musicList.get(playList.get(thisPosition)).mAlbumId);
                Picasso.with(mainView).load(albumArtUri).error(R.drawable.empty_albumart).into(imageView);
                player.setDataSource(path);
                player.prepare();
                musicProgress.setMax(player.getDuration());
                player.start();
                playBtn.setVisibility(View.INVISIBLE);
                pauseBtn.setVisibility(View.VISIBLE);

            }catch(Exception e){
                dispTxt.setText(e.getMessage());
            }
        }
    };

    //seekbar 이동했을때
    MediaPlayer.OnSeekCompleteListener mOnSeekCoplete = new MediaPlayer.OnSeekCompleteListener() {
        @Override
        public void onSeekComplete(MediaPlayer mediaPlayer) {
            if(wasPlaying){
                player.start();
            }
        }
    };

    Handler progressHandler = new Handler(){
        public void handleMessage(Message msg){
            if(player == null) return;
            if(player.isPlaying()){
                musicProgress.setProgress(player.getCurrentPosition());
            }
            progressHandler.sendEmptyMessageDelayed(0,200);
        }
    };
    SeekBar.OnSeekBarChangeListener onPlaySeek = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if(fromUser){
                player.seekTo(progress);
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            wasPlaying = player.isPlaying();
            if(wasPlaying){
                player.pause();
            }
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    };
}
