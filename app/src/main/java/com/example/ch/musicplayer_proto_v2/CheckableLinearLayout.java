package com.example.ch.musicplayer_proto_v2;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.LinearLayout;

/**
 * Created by ch on 2017-09-24.
 */

public class CheckableLinearLayout extends LinearLayout implements Checkable {
    public CheckableLinearLayout(Context context, AttributeSet attrs){
        super(context, attrs);
    }
    @Override
    public boolean isChecked(){
        CheckBox checkBox = (CheckBox)findViewById(R.id.checkbox);
        return checkBox.isChecked();
    }
    @Override
    public void toggle(){
        CheckBox checkBox = (CheckBox)findViewById(R.id.checkbox);
        setChecked(checkBox.isChecked() ? false : true);
    }
    @Override
    public void setChecked(boolean checked){
        CheckBox checkBox = (CheckBox)findViewById(R.id.checkbox);
        if(checkBox.isChecked() != checked){
            checkBox.setChecked(checked);
        }
    }
}