package com.jmh.androidproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import vo.DetailInfoVO;

public class ListPageActivity extends AppCompatActivity {
    //Tab
    TabLayout tabs;
    ViewPager2 views;
    ListPageAdapter listPageAdapter;
    float v =0;

    //back button
    ImageView back_button;

    FloatingActionButton btn_t;

    ReviewAdapter reviewAdapter;

    static String bid = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //status bar remove
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_list_page);


        bid = getIntent().getStringExtra("bid");

         /* img_d = findViewById(R.id.img_d);
        txt_d = findViewById(R.id.txt_d);
        txt_de = findViewById(R.id.txt_de);
        txt_det = findViewById(R.id.txt_det);
        txt_detail = findViewById(R.id.txt_detail);
        btn_t = findViewById(R.id.btn_t);

        list = new ArrayList<DetailInfoVO>();
        parserDe = new ParserDe();
        new KopisAsync().execute();*/

//-----TAB START-------------------------------------------------------------------------------------//
        tabs = findViewById(R.id.tabs);
        views = findViewById(R.id.views);

        FragmentManager fm = getSupportFragmentManager();
        listPageAdapter = new ListPageAdapter(fm, getLifecycle());
        views.setAdapter(listPageAdapter);

        tabs.addTab(tabs.newTab().setText("정보 보기"));
        tabs.addTab(tabs.newTab().setText("리뷰 보기"));
        tabs.setTabGravity(TabLayout.GRAVITY_FILL);

        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                views.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }

        });

        views.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                tabs.selectTab(tabs.getTabAt(position));
            }
        });

        tabs.setTranslationY(300);

        tabs.setAlpha(v);

        tabs.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(100).start();


        //back button
        back_button = findViewById(R.id.back_button);
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ListPageActivity.this, HomePageActivity.class);
                startActivity(i);
            }

            });

    }//oncreate()

}