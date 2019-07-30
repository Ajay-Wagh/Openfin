package com.example.aj.openfin;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class welcome extends AppCompatActivity {

    private int[] layouts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        if(!sharedPreferences.getBoolean("firstrun",true))
        {
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
            finish();
        }

        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        setContentView(R.layout.activity_welcome);
        final ViewPager viewPager =(ViewPager)findViewById(R.id.viewpager);
        final LinearLayout dotslayout=(LinearLayout)findViewById(R.id.linearLayout);
        final Button skip=(Button)findViewById(R.id.skip);

        layouts = new int[]{
                R.layout.welcome1,
                R.layout.welcome2,
                R.layout.welcome3};

        addbottomdot(0,dotslayout);
        changeStatusBarColor();

        final MyViewPagerAdapter myViewPagerAdapter = new MyViewPagerAdapter();
        viewPager.setAdapter(myViewPagerAdapter);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                switch (i)
                {
                    case 0:
                        addbottomdot(0,dotslayout);
                        break;
                    case 1:
                        addbottomdot(1,dotslayout);
                        break;
                    case 2:
                        addbottomdot(2,dotslayout);
                        break;
                }

            }

            @Override
            public void onPageScrollStateChanged(int i) {


            }
        });


        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(welcome.this,login.class));
                finish();
            }
        });







    }

    private void addbottomdot(int currentdot,final LinearLayout linearLayout)
    {
        TextView temp;
        int i=0;
        for(i=0;i<linearLayout.getChildCount();i++)
        {
            temp=(TextView)linearLayout.getChildAt(i);
            temp.setTextColor(getResources().getColor(R.color.dotinactive));
        }
        temp=(TextView)linearLayout.getChildAt(currentdot);
        temp.setTextColor(getResources().getColor(R.color.dotactive));
    }


    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setNavigationBarColor(Color.TRANSPARENT);
        }
    }


    public class MyViewPagerAdapter extends PagerAdapter {
        private LayoutInflater layoutInflater;

        public MyViewPagerAdapter() {
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View view = layoutInflater.inflate(layouts[position], container, false);
            container.addView(view);

            return view;
        }

        @Override
        public int getCount() {
            return layouts.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }
    }


}
