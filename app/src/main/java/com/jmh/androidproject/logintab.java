package com.jmh.androidproject;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link logintab#newInstance} factory method to
 * create an instance of this fragment.
 */
public class logintab extends Fragment {

    EditText edit_email, edit_pwd;
    Button btn_login;
    float v =0;

    //dialog
    Dialog dialog;
    TextView btn_pwd_find;



    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public logintab() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment logintab.
     */
    // TODO: Rename and change types and number of parameters
    public static logintab newInstance(String param1, String param2) {
        logintab fragment = new logintab();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_logintab, container,false);

        edit_email = root.findViewById(R.id.edit_email);
        edit_pwd = root.findViewById(R.id.edit_pwd);
        btn_login = root.findViewById(R.id.btn_login);
        btn_pwd_find = root.findViewById(R.id.btn_pwd_find);

        edit_email.setTranslationX(800);
        edit_pwd.setTranslationX(800);
        btn_pwd_find.setTranslationX(800);
        btn_login.setTranslationX(800);

        edit_email.setAlpha(v);
        edit_pwd.setAlpha(v);
        btn_pwd_find.setAlpha(v);
        btn_login.setAlpha(v);

        edit_email.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(300).start();
        edit_pwd.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(500).start();
        btn_pwd_find.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(500).start();
        btn_login.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(700).start();

        //dialog
        dialog = new Dialog(getActivity());
        btn_pwd_find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showfindpassworddialog();

            }
        });

        //???????????? ?????? ????????? ?????????
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getActivity(), MyPageActivity.class);

                String email = edit_email.getText().toString();
                String pwd = edit_pwd.getText().toString();

                String result = "email="+email+"&pwd="+pwd;

                new LoginTask().execute(result);

            }
        });
        return root;
    }//oncreate()

    //----------dialog start----------
    private void showfindpassworddialog() {
        dialog.setContentView(R.layout.dialog_findpassword);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        EditText find_email, find_name;
        Button btn_find;
        ImageButton btnclose = dialog.findViewById(R.id.btnclose);
        find_email = dialog.findViewById(R.id.find_email);
        find_name = dialog.findViewById(R.id.find_name);
        btn_find = dialog.findViewById(R.id.btn_find);

        btnclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        //???????????? ?????? ????????? ?????????
        btn_find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getActivity(),Login_SignupActivity.class);

                String email = find_email.getText().toString();
                String name = find_name.getText().toString();

                String result = "email="+email+"&name="+name;

                new FindTask().execute(result);

            }
        });

        dialog.show();
    }
    //----------dialog end----------

    //????????? ?????? Async?????????
    class LoginTask extends AsyncTask<String, Void, String> {

        public String ip = Ip.ip;
        String sendMsg, receiveMsg;
        String serverip = "http://" + ip + ":9090/controller/login.do";

        @Override
        protected String doInBackground(String... strings) {

            String str = "";

            try {

                URL url = new URL(serverip);

                //?????? ????????? ????????????
                HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                conn.setRequestMethod("POST");
                OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());

                //osw?????? ????????? ???????????? ?????? ????????? ????????? ??????????????? ??????
                //regi.do?id=a&pwd=1111
                sendMsg = strings[0];

                //????????? ????????? ?????? ??????
                osw.write(sendMsg);
                osw.flush();//??????????????? ????????? ??????????????? ???????????? ?????????

                //??????????????? ???????????? ????????? ???????????? ??????
                //conn.getResponseCode() : 200?????? ??????
                //                         404, 500???... ?????????
                if( conn.getResponseCode() == conn.HTTP_OK ){


                    InputStreamReader tmp = new InputStreamReader(conn.getInputStream(), "UTF-8");
                    BufferedReader reader = new BufferedReader(tmp);

                    //???????????? ????????? JSON????????? ???????????? ?????????
                    //{res:[{'result':'success'}]}
                    receiveMsg = reader.readLine();
                    Log.i("MY", receiveMsg);

                    //JSON????????? ????????? ?????? ?????????
                    JSONArray jarray = new JSONObject(receiveMsg).getJSONArray("res");

                    //jarray : [{'result':'success'}]
                    JSONObject jObject = jarray.getJSONObject(0);

                    //jObject : {'result':'success'}
                    str = jObject.optString("result");
                }
            }catch (Exception e){
            }
            return str;
        }

        @Override
        protected void onPostExecute(String s) {

            if( s.equalsIgnoreCase("success") ){
                //Toast.makeText(LoginActivity.this, "????????? ??????!", Toast.LENGTH_SHORT).show();

                //????????? ????????? ?????????????????? ??????
                Intent i = new Intent(getActivity(), MyPageActivity.class);

                i.putExtra("message", edit_email.getText().toString());
                i.putExtra("login", "ok");

                startActivity(i);

                finish();

            }else{
                Toast.makeText(getActivity(), "???????????? ??????????????? ???????????? ??????", Toast.LENGTH_SHORT).show();
            }
        }
    }//Task

    private void finish() {
    }

    //??????????????????
    class FindTask extends AsyncTask<String, Void, String> {

        public String ip = Ip.ip;
        String sendMsg, receiveMsg;
        String serverip = "http://" + ip + ":9090/controller/find.do";

        @Override
        protected String doInBackground(String... strings) {

            String str = "";

            try {

                URL url = new URL(serverip);

                //?????? ????????? ????????????
                HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                conn.setRequestMethod("POST");
                OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());

                //osw?????? ????????? ???????????? ?????? ????????? ????????? ??????????????? ??????
                //regi.do?id=a&pwd=1111
                sendMsg = strings[0];

                //????????? ????????? ?????? ??????
                osw.write(sendMsg);
                osw.flush();//??????????????? ????????? ??????????????? ???????????? ?????????

                //??????????????? ???????????? ????????? ???????????? ??????
                //conn.getResponseCode() : 200?????? ??????
                //                         404, 500???... ?????????
                if( conn.getResponseCode() == conn.HTTP_OK ){


                    InputStreamReader tmp = new InputStreamReader(conn.getInputStream(), "UTF-8");
                    BufferedReader reader = new BufferedReader(tmp);

                    //???????????? ????????? JSON????????? ???????????? ?????????
                    //{res:[{'result':'success'}]}
                    receiveMsg = reader.readLine();
                    Log.i("MY", receiveMsg);

                    //JSON????????? ????????? ?????? ?????????
                    JSONArray jarray = new JSONObject(receiveMsg).getJSONArray("res");

                    //jarray : [{'result':'success'}]
                    JSONObject jObject = jarray.getJSONObject(0);

                    //jObject : {'result':'success'}
                    str = jObject.optString("result");
                }

            }catch (Exception e){
            }
            return str;
        }

        @Override
        protected void onPostExecute(String s) {

            if( s.equalsIgnoreCase("success") ){
                Toast.makeText(getActivity(), "???????????? ?????? ??????????????? ??????????????????.", Toast.LENGTH_SHORT).show();

                Intent i = new Intent(getActivity(), Login_SignupActivity.class);

                startActivity(i);

                finish();

            }else{
                Toast.makeText(getActivity(), "???????????? ??????????????? ???????????? ??????", Toast.LENGTH_LONG).show();
            }
        }
    }//Task
}