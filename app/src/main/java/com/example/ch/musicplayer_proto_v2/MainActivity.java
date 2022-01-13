package com.example.ch.musicplayer_proto_v2;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        //implements NavigationView.OnNavigationItemSelectedListener{
    {
    public static ArrayList<MusicDB> musicList;
     ArrayList<Integer> playList = null;
//    private final Uri artworkUri = Uri.parse("content://media/external/audio/albumart");
    private ArrayList<String> musicName = null;
    ImageView imageView;

    AudioManager am;
    MusicPlayer player;
    float musicSpeed = 1.0f;

    TextView dispTxt;
    ImageButton playBtn;
    ImageButton pauseBtn;
    ImageButton loopBtn;

    ImageButton volumeUp, volumeDown;
    TextView volumeTxt;
    ListView showListView;

    FabLayoutControl fabLayoutControl;
    FloatingActionButton fab;
    boolean isFABVisible = false;

    boolean isChanged = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

         fab = (FloatingActionButton) findViewById(R.id.fab);
        /*
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(player.isLoop){
                    player.isLoop = false;
                } else{
                    player.isLoop = true;
                }
                player.player.setLooping(player.isLoop);
            }
        });
        */
        fabLayoutControl = new FabLayoutControl(fab, fab.getResources());
        fab.setImageBitmap(FabLayoutControl.textAsBitmap("x"+Integer.toString((int)musicSpeed),20, Color.WHITE));

//        try {
            /*
            fabLayoutControl.addSubFAB((LinearLayout) findViewById(R.id.fabLayout1), (FloatingActionButton) findViewById(R.id.fab1), R.dimen.standard_55);
            fabLayoutControl.addSubFAB((LinearLayout) findViewById(R.id.fabLayout2), (FloatingActionButton) findViewById(R.id.fab2), R.dimen.standard_100);
            fabLayoutControl.addSubFAB((LinearLayout) findViewById(R.id.fabLayout3), (FloatingActionButton) findViewById(R.id.fab3), R.dimen.standard_145);*/
            FloatingActionButton fab1 = (FloatingActionButton)findViewById(R.id.fab1);
            FloatingActionButton fab2 = (FloatingActionButton)findViewById(R.id.fab2);
            FloatingActionButton fab3 = (FloatingActionButton)findViewById(R.id.fab3);
            FloatingActionButton fab4 = (FloatingActionButton)findViewById(R.id.fab4);

            fab1.setImageBitmap(FabLayoutControl.textAsBitmap("x1",20,Color.WHITE));
            fab2.setImageBitmap(FabLayoutControl.textAsBitmap("x1.2",15,Color.WHITE));
            fab3.setImageBitmap(FabLayoutControl.textAsBitmap("x1.5",15,Color.WHITE));
            fab4.setImageBitmap(FabLayoutControl.textAsBitmap("x2",20,Color.WHITE));

            LinearLayout fabLayout1 = (LinearLayout)findViewById(R.id.fabLayout1);
            LinearLayout fabLayout2 = (LinearLayout)findViewById(R.id.fabLayout2);
            LinearLayout fabLayout3 = (LinearLayout)findViewById(R.id.fabLayout3);
            LinearLayout fabLayout4 = (LinearLayout)findViewById(R.id.fabLayout4);

            fabLayoutControl.addSubFAB(fabLayout1, fab1, R.dimen.standard_255);
            fabLayoutControl.addSubFAB(fabLayout2, fab2, R.dimen.standard_300);
            fabLayoutControl.addSubFAB(fabLayout3,fab3, R.dimen.standard_345);
            fabLayoutControl.addSubFAB(fabLayout4,fab4, R.dimen.standard_390);
            fabLayoutControl.setTranslationY(R.dimen.standard_200);
        /*} catch(Exception e)
        {
            Toast.makeText(this,e.toString(), Toast.LENGTH_LONG).show();
        }*/


        dispTxt = (TextView)findViewById(R.id.dispTxt);
        imageView = (ImageView)findViewById(R.id.albumimage);
        imageView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if(!isFABVisible){
                    fab.show();
                    isFABVisible = true;
                }
                else{
                    //fab.setVisibility(View.GONE);
                    fabLayoutControl.closeFABMenu();
                    fab.hide();
                    isFABVisible = false;
                }
            }
        });

        player = new MusicPlayer();

        am = (AudioManager)getSystemService(AUDIO_SERVICE);

        pauseBtn = (ImageButton)findViewById(R.id.pause);
        playBtn = (ImageButton)findViewById(R.id.play);
        loopBtn = (ImageButton)findViewById(R.id.loopBtn);

        player.setMusicProgress((SeekBar)findViewById(R.id.seekBar));
        player.setView(this, dispTxt,imageView);
        player.setButton(playBtn,pauseBtn);

        /*
        ContentResolver cr = getContentResolver();
        Cursor mCr = cr.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,null,null,null,null);
        SimpleCursorAdapter Adapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_1, mCr,
                new String[]{MediaStore.MediaColumns.DISPLAY_NAME},
                new int[] {android.R.id.text1});
        list.setAdapter(Adapter);
        list.setOnItemClickListener(mItemClickListener);
        Adapter.notifyDataSetChanged();
*/
        //NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        //navigationView.setNavigationItemSelectedListener(this);
        showListView = (ListView)findViewById(R.id.musiclist);
        //showListView.setOnItemClickListener(mItemClickListener);

        /*
        SeekBar volumeSeek = (SeekBar)findViewById(R.id.volumeSeek);
        volumeSeek.setMax(am.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
        volumeSeek.setProgress(am.getStreamVolume(AudioManager.STREAM_MUSIC));
        volumeSeek.setOnSeekBarChangeListener(onVolumeSeek);
*/
        volumeTxt = (TextView)findViewById(R.id.volumeTxt);
        volumeUp = (ImageButton) findViewById(R.id.volumeUp);
        volumeDown = (ImageButton)findViewById(R.id.volumeDown);

        //< NotFoundException : TextView.setText(int)일 때 발생
        try {
            volumeTxt.setText(Integer.toString(am.getStreamVolume(AudioManager.STREAM_MUSIC)));
        }catch (Exception e){
            Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
        }
        volumeUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    int volume = Integer.parseInt((String) volumeTxt.getText());
                    if (volume < 20)
                        am.setStreamVolume(AudioManager.STREAM_MUSIC, ++volume, 0);
                    volumeTxt.setText(Integer.toString(volume));
                }
                catch(Exception e)
                {
                    Toast.makeText(getBaseContext(), e.toString(), Toast.LENGTH_LONG).show();
                }
            }
        });

        volumeDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                int volume = Integer.parseInt((String) volumeTxt.getText());
                if (volume > 0)
                    am.setStreamVolume(AudioManager.STREAM_MUSIC, --volume, 0);
                volumeTxt.setText(Integer.toString(volume));
            }
                catch(Exception e)
                {
                    Toast.makeText(getBaseContext(), e.toString(), Toast.LENGTH_LONG).show();
                }
            }
        });

        getMusicList();
        player.setMusicList(musicList);
    }


    public void onClicked(View view){
        switch(view.getId()){
            case R.id.pause:
            case R.id.play:
                player.play_pause();
                break;
            case R.id.previous:
                player.previous();
                break;
            case R.id.next:
                player.next();
                break;
        }
    }

    public void onFABClicked(View view){
        switch(view.getId()){
            case R.id.fab1:
            case R.id.fab4:
                musicSpeed = (view.getId() == R.id.fab1) ? 1.0f : 2.0f;
                fab.setImageBitmap(FabLayoutControl.textAsBitmap("x"+Integer.toString((int)musicSpeed),20, Color.WHITE));
                break;
            case R.id.fab2:
            case R.id.fab3:
                musicSpeed = (view.getId() == R.id.fab2) ? 1.2f : 1.5f;
                fab.setImageBitmap(FabLayoutControl.textAsBitmap("x"+Float.toString(musicSpeed),15, Color.WHITE));
                break;
        }
    }


    public void onLoopClicked(View view){
        switch(view.getId()){
            case R.id.loopBtn:
                if(player.isLoop){
                    player.isLoop = false;
                } else{
                    player.isLoop = true;
                }
                player.player.setLooping(player.isLoop);
                break;
        }
    }
    public void onRestart(){
        super.onRestart();
        if(isChanged) {
            musicName = new ArrayList<>();
            for (int i = 0; i < playList.size(); i++) {
                musicName.add(musicList.get(playList.get(i)).mTitle);
            }
            ArrayAdapter<String> Adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, musicName);
            showListView.setAdapter(Adapter);
            showListView.setOnItemClickListener(mItemClickListener);
            Adapter.notifyDataSetChanged();
        }
    }

    public void onDestroy(){
        super.onDestroy();
        player.release();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.actionbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {   //actionbar event 처리
        switch (item.getItemId()) {
            case R.id.add_action:
                try{
                Intent intent = new Intent(MainActivity.this, ListViewActivity.class);
                if(playList != null)
                intent.putIntegerArrayListExtra("checkedList",playList);
                startActivityForResult(intent, 0);      //실행할 activity intent를 0으로 함
                //startActivity(intent);
                } catch(Exception e){
                    Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.wifi_search_action:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){  //생성된 activity에게서 정보 받아옴
        switch(requestCode){
            case 0:         //0번 intent : ListView Activity
                if(resultCode == RESULT_OK) {
                    isChanged = data.getBooleanExtra("isChanged", false);
                    if (isChanged) {
                        try {
                            //playList = (ArrayList<Integer>) data.getIntegerArrayListExtra("playList");

                            try {
                                playList.addAll((ArrayList<Integer>) data.getIntegerArrayListExtra("playList"));
                            }catch(Exception e){
                                playList = (ArrayList<Integer>) data.getIntegerArrayListExtra("playList");
                            }

                            /*
                            ArrayList<Integer> tempList = new ArrayList<>();
                            tempList.addAll(playList);
                            tempList.addAll((ArrayList<Integer>)data.getIntegerArrayListExtra("playList"));
                            //playList = (ArrayList<Integer>) tempList.clone();
                            playList = tempList;
                            */

                            String temp = "";
                            for (int i = 0; i < playList.size(); i++)
                                temp += playList.get(i) + ", ";

                            Toast.makeText(this, temp+"\n playlist changed", Toast.LENGTH_LONG).show();

                            player.setPlayList(playList);
                        } catch (Exception e) {
                            Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
                        }
                    }
                    else
                        Toast.makeText(this, "playlist not changed", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    ///음악 db 생성
    public void getMusicList(){
        musicList = new ArrayList<>();
        String[] projection = new String[]{
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.ALBUM_ID,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.DATA
        };
        Cursor cursor = getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, projection, null ,null, null);
        while(cursor.moveToNext()){
            musicList.add(MusicDB.bindCursor(cursor));
        }
        cursor.close();

        for(int i = 0; i<musicList.size();i++)
        {
            musicList.get(i).nowPosition = i;
        }
    }
/*
    SeekBar.OnSeekBarChangeListener onVolumeSeek = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            am.setStreamVolume(AudioManager.STREAM_MUSIC,progress,0);
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    };
*/
    AdapterView.OnItemClickListener mItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
            try {
                //player.play(playList.get(position));
                //player.play(8);
                player.setThisPosition(playList.get(position));
                player.play();
            } catch (Exception e){
                Toast.makeText(getApplicationContext(), "position : "+position+"\nlist : "+playList.get(position)+"\n"+e.toString(), Toast.LENGTH_LONG).show();
            }
        }
    };

    /*
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id)
        {
            case R.id.musiclist:
                showListView.callOnClick();
                break;
        }
        Toast.makeText(getApplicationContext(),Integer.toString(id),Toast.LENGTH_LONG);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }*/
}
