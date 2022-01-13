package com.example.ch.musicplayer_proto_v2;

import android.animation.Animator;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static android.graphics.Paint.ANTI_ALIAS_FLAG;

/**
 * Created by ch on 2017-11-25.
 */

public class FabLayoutControl {
    boolean isFABOpen=false;
    FloatingActionButton mainFAB;
    ArrayList<FloatingActionButton> subFABList;
    ArrayList<LinearLayout> subFABLayoutList;
    ArrayList<Integer> dimensionList;
    View fabBGLayout;
    Resources appResource;
    int count = 0;
    private int closeTranslationY_ID;
    /*
    FabLayoutControl(){
        subFABList = new ArrayList<>();
        subFABLayoutList = new ArrayList<>();
        dimensionList = new ArrayList<>();
    }*/
    FabLayoutControl(FloatingActionButton mainFAB, Resources appResource)
    {
        this.mainFAB = mainFAB;
        mainFAB.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if(!isFABOpen){
                    showFABMenu();
                }else{
                    closeFABMenu();
                }
            }
        });
        subFABList = new ArrayList<>();
        subFABLayoutList = new ArrayList<>();
        dimensionList = new ArrayList<>();
        this.appResource = appResource;
    }

    public void addSubFAB(LinearLayout layout, FloatingActionButton fab, int dimension){
        subFABList.add(fab);
        subFABLayoutList.add(layout);
        dimensionList.add(dimension);
        count++;
    }
/*
    public void addSubFAB(int layoutID, int fabID, int dimension){
        subFABList.add((LinearLayout)findViewByID());
        subFABLayoutList.add(layout);
        dimensionList.add(dimension);
        count++;
    }*/

    public void setTranslationY(int Id){
        //this.tY = translationY;
        closeTranslationY_ID = Id;
    }
    public FloatingActionButton getSubFAB(int num)
    {
        return subFABList.get(num);
    }

    public void showFABMenu(){
        isFABOpen=true;
        /*
        fabLayout1.setVisibility(View.VISIBLE);
        fabLayout2.setVisibility(View.VISIBLE);
        fabLayout3.setVisibility(View.VISIBLE);*/
        /*
        Iterator<LinearLayout> iter = subFABLayoutList.iterator();
        while(iter.hasNext()){
            iter.next().setVisibility(View.VISIBLE);
        }*/
        for(int i=0;i<count;i++)
        {
            subFABLayoutList.get(i).setVisibility(View.VISIBLE);
        }

        if(fabBGLayout != null)
        fabBGLayout.setVisibility(View.VISIBLE);

        //mainFAB.animate().rotationBy(180);
        /*
        fabLayout1.animate().translationY(-getResources().getDimension(R.dimen.standard_55));
        fabLayout2.animate().translationY(-getResources().getDimension(R.dimen.standard_100));
        fabLayout3.animate().translationY(-getResources().getDimension(R.dimen.standard_145));
        */
        /*
        while(iter.hasNext()){
            iter.next().animate().translationY(-appResource.getDimension());
        }*/
        for(int i=0;i<count;i++)
        {
            subFABLayoutList.get(i).animate().translationY(-appResource.getDimension(dimensionList.get(i)));
            //subFABLayoutList.get(i).animate().translationY(-mainFAB.getResources().getDimension(dimensionList.get(i)));
        }
    }

    public void closeFABMenu(){
        isFABOpen=false;
        if(fabBGLayout != null)
            fabBGLayout.setVisibility(View.GONE);
        //mainFAB.animate().rotationBy(-180);
        /*
        fabLayout1.animate().translationY(0);
        fabLayout2.animate().translationY(0);
        fabLayout3.animate().translationY(0).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                if(!isFABOpen){
                    fabLayout1.setVisibility(View.GONE);
                    fabLayout2.setVisibility(View.GONE);
                    fabLayout3.setVisibility(View.GONE);
                }

            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });*/
        try{
            for(int i=0;i<count-1;i++){
                //subFABLayoutList.get(i).animate().translationY(0);
                //subFABLayoutList.get(i).animate().translationY(-appResource.getDimension(R.dimen.standard_145));
                subFABLayoutList.get(i).animate().translationY(-appResource.getDimension(closeTranslationY_ID));
            }
            //subFABLayoutList.get(count-1).animate().translationY(0).setListener(new Animator.AnimatorListener() {
            subFABLayoutList.get(count-1).animate().translationY(-appResource.getDimension(closeTranslationY_ID)).setListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {

                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    if(!isFABOpen){
                        for (int i=0;i<count;i++){
                            subFABLayoutList.get(i).setVisibility(View.GONE);
                        }
                    }
                }

                @Override
                public void onAnimationCancel(Animator animator) {

                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            });
        }
        catch (Exception e) {

            //float mainY = mainFAB.getTranslationY();
            for (int i = 0; i < count - 1; i++) {
                subFABLayoutList.get(i).animate().translationY(0);
                //subFABLayoutList.get(i).animate().translationY(mainY);
            }
            subFABLayoutList.get(count-1).animate().translationY(0).setListener(new Animator.AnimatorListener() {
         //   subFABLayoutList.get(count - 1).animate().translationY(mainY).setListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {

                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    if (!isFABOpen) {
                        for (int i = 0; i < count; i++) {
                            subFABLayoutList.get(i).setVisibility(View.GONE);
                        }
                    }
                }

                @Override
                public void onAnimationCancel(Animator animator) {

                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            });
        }
    }

    public static Bitmap textAsBitmap(String text, float textSize, int textColor) {
        Paint paint = new Paint(ANTI_ALIAS_FLAG);
        paint.setTextSize(textSize);
        paint.setColor(textColor);
        paint.setTextAlign(Paint.Align.LEFT);
        float baseline = -paint.ascent(); // ascent() is negative
        int width = (int) (paint.measureText(text) + 0.0f); // round
        int height = (int) (baseline + paint.descent() + 0.0f);
        Bitmap image = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(image);
        canvas.drawText(text, 0, baseline, paint);
        return image;
    }
}
