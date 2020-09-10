package com.example.workncardio;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.example.databases.UserDets;
import com.example.databases.UserInfo;
import com.example.workncardio.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

import androidx.annotation.NonNull;

public class filler extends View {
    Paint defaultPaint,filledPaint;
    int percentage;
    int step;
    Calendar calendar=Calendar.getInstance();
    DatabaseReference steps= FirebaseDatabase.getInstance().getReference("Users");
    public filler(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.filler, 0, 0);

        try {
            percentage = a.getInteger(R.styleable.filler_percent, 0);

        } finally {
            a.recycle();
        }

        init();
    }


    public void init(){
        defaultPaint=new Paint();
        defaultPaint.setColor(Color.LTGRAY);

        filledPaint=new Paint();
        filledPaint.setColor(Color.GREEN);



    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int mWIdth=getWidth();
        int mHeight=getHeight();
        canvas.drawCircle(mWIdth/2,mHeight/2,mWIdth/2,defaultPaint);
        if(percentage<=50){
            canvas.drawArc(0,0,mWIdth,mHeight,90-(float)percentage*90/50,2*(percentage*90/50),false,filledPaint);
        }else{
            canvas.drawArc(0,0,mWIdth,mHeight,(float)(-(percentage-50)*90/50),180+2*((percentage-50)*90/50),false,filledPaint);
        }

        Paint trial=new Paint();
        trial.setColor(Color.RED);
        trial.setTextSize(72);
        canvas.drawText(String.valueOf(step),mWIdth/2-36,mHeight/2+24,trial);



    }

    public int getPercentage() {
        return percentage;
    }

    public void setPercentage(Context context,int steps) {
        step=steps;
        this.steps.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    UserInfo info = snapshot1.getValue(UserInfo.class);
                    if (info.getName().equals(Homescreen.USER_NAME)) {
                        percentage = step * 100 / info.getBaseStepGoal();
                        invalidate();
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}

