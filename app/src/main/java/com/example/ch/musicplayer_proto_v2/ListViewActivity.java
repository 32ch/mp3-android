package com.example.ch.musicplayer_proto_v2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

import static com.example.ch.musicplayer_proto_v2.MainActivity.musicList;

/**
 * Created by ch on 2017-09-21.
 */

public class ListViewActivity extends AppCompatActivity{
    MyAdapter adapter;
    ListView listView;
    ArrayList<Integer> playList = null;
    ArrayList<MusicDB> tempList;
    boolean isAllChecked = false;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listview_activity);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //뒤로가기
        getSupportActionBar().setDisplayShowTitleEnabled(false);    //이름 없애기

        listView = (ListView)findViewById(R.id.list);

        Intent intent = getIntent();
        ArrayList<Integer> checkedList = null;
        if((checkedList = (ArrayList<Integer>) intent.getIntegerArrayListExtra("checkedList"))!=null) {

            Collections.sort(checkedList,
            new Comparator<Integer>(){      //익명 클래스 사용 : 오름차순
                @Override
                public int compare(Integer i1, Integer i2){
                    return i1.compareTo(i2);
                }
            });

            String temp = "";
            for(int i = 0;i<checkedList.size();i++)
                temp += checkedList.get(i)+", ";
            Toast.makeText(getApplicationContext(), temp, Toast.LENGTH_LONG).show();

            try {
                tempList = (ArrayList<MusicDB>)musicList.clone();
                int check = checkedList.size();
                int count = 0;
                Iterator<MusicDB> iter = tempList.iterator();
                /*
                for (int i = musicList.size(); i<0; i--) {
                    if(count <0)
                        break;
                    if (checkedList.get(count-1) == i){
                        count--;
                        tempList.remove(i);
                    }

                }*/
                while(iter.hasNext()){
                    MusicDB db = iter.next();
                    if( count> check-1)
                        break;
                    if(db.nowPosition == checkedList.get(count)){
                        count++;
                        iter.remove();
                    }
                }

                //ListView listView = (ListView)findViewById(R.id.list);
                //final MyAdapter adapter = new MyAdapter(this, musicList);
                adapter = new MyAdapter(this, tempList);
            } catch(Exception e)
            {
                Toast.makeText(getBaseContext(), e.toString(),Toast.LENGTH_LONG).show();
            }
        }
        else
            adapter = new MyAdapter(this, musicList);
        listView.setAdapter(adapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                Toast.makeText(getApplicationContext(), ""+(position),
                        Toast.LENGTH_SHORT).show();

                adapter.notifyDataSetChanged();
                if(isAllChecked)
                    isAllChecked = false;
            }
        });

    }

    public boolean onCreateOptionsMenu(Menu menu){
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.list_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int count=0;
        switch(item.getItemId()){
            case android.R.id.home:     ///홈키 눌렀을 경우
                finish();
                break;
            case R.id.add:
                count = adapter.getCount();
                //ArrayList<Integer> playList = new ArrayList<Integer>();
                playList = new ArrayList<Integer>();
                for(int i=0; i<count;i++){
                    if(listView.isItemChecked(i)){
                        playList.add(adapter.getItemPosition(i));
                    }
                    else
                        continue;
                }

                String temp="";
                for(int i=0; i<playList.size();i++)
                    temp+=playList.get(i)+", ";
                /*
                Toast.makeText(getApplicationContext(), temp,
                        Toast.LENGTH_SHORT).show();*/
                Intent intent = getIntent();
                if(!playList.isEmpty()){
                    intent.putExtra("isChanged",true);
                    intent.putIntegerArrayListExtra("playList",playList);
                }
                else
                    intent.putExtra("isChanged",false);
                setResult(RESULT_OK, intent);
                finish();
                break;
            case R.id.check_all:
                count = adapter.getCount();
                if(!isAllChecked) {     //not checked all
                    for (int i = 0; i < count; i++) {
                        listView.setItemChecked(i, true);
                    }
                    isAllChecked = true;
                }
                else
                {
                    for (int i = 0; i < count; i++) {
                        listView.setItemChecked(i, false);
                    }
                    isAllChecked = false;
                }
                break;
        }
        return true;
    }
}
