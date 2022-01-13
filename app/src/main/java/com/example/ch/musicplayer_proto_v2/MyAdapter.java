package com.example.ch.musicplayer_proto_v2;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by ch on 2017-09-22.
 */

/*listviewactivity에 보여주는 class
listitem_audio.xml이 ui
    */
public class MyAdapter extends BaseAdapter{
    List<MusicDB> list;
    LayoutInflater inflater;
    Activity activity;
    private final Uri artworkUri = Uri.parse("content://media/external/audio/albumart");


    public MyAdapter(Activity activity, List<MusicDB>list){
        this.list = list;
        this.activity = activity;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount(){
        return list.size();
    }

    @Override
    public MusicDB getItem(int position){
        return list.get(position);
    }

    @Override
    public long getItemId(int position){
        return position;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent){
        if(convertView == null){
            convertView = inflater.inflate(R.layout.listitem_audio, parent, false);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            convertView.setLayoutParams(layoutParams);
        }
        ImageView imageView = (ImageView)convertView.findViewById(R.id.img_albumart);
      //  Bitmap albumImage = getAlbumImage(activity, Integer.parseInt(list.get(position).getAlbumId(),170));
        //imageView.setImageBitmap(albumImage);
        Uri albumArtUri = ContentUris.withAppendedId(artworkUri, list.get(position).mAlbumId);
        Picasso.with(convertView.getContext()).load(albumArtUri).error(R.drawable.empty_albumart).into(imageView);

        TextView title = (TextView)convertView.findViewById(R.id.music_title);
        title.setText(list.get(position).mTitle);

        TextView subtitle = (TextView)convertView.findViewById(R.id.music_subtitle);
        subtitle.setText(list.get(position).mArtist);

        TextView duration = (TextView)convertView.findViewById(R.id.music_duration);
        duration.setText(new SimpleDateFormat("mm:ss").format(list.get(position).mDuration));
        CheckBox checkBox = (CheckBox)convertView.findViewById(R.id.checkbox);
        return convertView;
    }
    public int getItemPosition(int position)
    {
        return list.get(position).nowPosition;
    }
}
