package com.jmh.androidproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import java.util.ArrayList;

import vo.DetailInfoVO;
import vo.ReservationVO;

public class Ticket_RevActivity extends AppCompatActivity {

    ImageView back_button;
    Button btn_buy;
    ReservationDe reservationDe;
    ArrayList<ReservationVO> list;
    String bid = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //status bar remove
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_ticket_rev);

        //back button
        back_button = findViewById(R.id.back_button);
        //구매
        btn_buy = findViewById(R.id.btn_buy);

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(Ticket_RevActivity.this, ListPageActivity.class);
                i.putExtra("bid", bid);
                startActivity(i);
            }
        });
        //구매
        btn_buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Ticket_RevActivity.this, MyPageActivity.class);
                startActivity(i);
            }
        });

        bid = getIntent().getStringExtra("bid");
        Log.i("re", "bid : " + bid);
        list = new ArrayList<ReservationVO>();
        reservationDe = new ReservationDe();
        new KopisAsync().execute();

    }//onCreate()

    class KopisAsync extends AsyncTask<String, Void, ArrayList<ReservationVO>> {

        @Override
        protected ArrayList<ReservationVO> doInBackground(String... strings) {
            return reservationDe.connectKopis(list, ListPageActivity.bid);
        }

        @Override
        protected void onPostExecute(ArrayList<ReservationVO> infoVOS) {
            Log.i("re", "res size : " + infoVOS.size());

        }
    }
}