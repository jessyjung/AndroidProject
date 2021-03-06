package com.jmh.androidproject;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Pattern;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link signuptab#newInstance} factory method to
 * create an instance of this fragment.
 */
public class signuptab extends Fragment {

    Button btn_login_event, btn_register, register_btn_bottom,btn_login,btn_pwd_find;
    EditText register_email, register_pwd, register_pwd_check, register_name, register_pnum,
            edit_email,edit_pwd;

    Pattern emailPatttern = Patterns.EMAIL_ADDRESS;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public signuptab() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment signuptab.
     */
    // TODO: Rename and change types and number of parameters
    public static signuptab newInstance(String param1, String param2) {
        signuptab fragment = new signuptab();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_signuptab, container, false);

        register_email = root.findViewById(R.id.register_email);
        register_pwd = root.findViewById(R.id.register_pwd);
        register_pwd_check = root.findViewById(R.id.register_pwd_check);
        register_name = root.findViewById(R.id.register_name);
        register_pnum = root.findViewById(R.id.register_pnum);
       // edit_email = root.findViewById(R.id.edit_email);


        register_btn_bottom = root.findViewById(R.id.register_btn_bottom);
        register_btn_bottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(register_email.getText().toString().length() ==0) {
                    Toast.makeText(getActivity(), "???????????? ???????????????", Toast.LENGTH_SHORT).show();

                    register_email.requestFocus();
                    return;

                }else if(!emailPatttern.matcher(register_email.getText().toString()).matches()){
                    Toast.makeText(getActivity(), "?????????????????? ???????????? ????????????.", Toast.LENGTH_SHORT).show();
                   // edit_email.requestFocus();
                    return;


                }else if (register_pwd.getText().toString().length()==0){
                    Toast.makeText(getActivity(),"??????????????? ???????????????",Toast.LENGTH_SHORT).show();
                    register_pwd.requestFocus();
                    return;

                }else if (register_pwd_check.getText().toString().length()==0){
                    Toast.makeText(getActivity(),"??????????????? ???????????????",Toast.LENGTH_SHORT).show();
                    register_pwd_check.requestFocus();
                    return;

                }else if (register_name.getText().toString().length()==0) {
                    Toast.makeText(getActivity(), "????????? ???????????????", Toast.LENGTH_SHORT).show();
                    register_name.requestFocus();
                    return;
                }else if (register_pnum.getText().toString().length()==0) {
                    Toast.makeText(getActivity(), "??????????????? ???????????????", Toast.LENGTH_SHORT).show();
                    register_pnum.requestFocus();
                    return;
                }else if (!register_pwd.getText().toString().equals(register_pwd_check.getText().toString())) {
                    Toast.makeText(getActivity(), "??????????????? ???????????? ????????????", Toast.LENGTH_SHORT).show();
                    register_pwd.setText("");
                    register_pwd_check.setText("");
                    register_pwd.requestFocus();
                    return;


                }else {



                    String email = register_email.getText().toString();
                    String pwd = register_pwd.getText().toString();
                    String name = register_name.getText().toString();
                    String pnum = register_pnum.getText().toString();
                    String result = "email=" + email + "&pwd=" + pwd + "&name=" + name + "&pnum=" + pnum;

                    new Task().execute(result);
                }
            }
        });
        return root;
    }//oncreateview

    //??????????????? Async?????????
    class Task extends AsyncTask<String, Void, String> {

        public String ip = Ip.ip;
        String sendMsg, receiveMsg;
        String serverip = "http://" + ip + ":9090/controller/register.do";

        @Override
        protected String doInBackground(String... strings) {

            String str = "";

            try {

                URL url = new URL(serverip);

                //?????? ????????? ????????????
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());

                //osw?????? ????????? ???????????? ?????? ????????? ????????? ??????????????? ??????
                //regi.do?email=a&pwd=1111&name=a&pnum=1111
                sendMsg = strings[0];


                //????????? ????????? ?????? ??????
                osw.write(sendMsg);
                osw.flush();//??????????????? ????????? ??????????????? ???????????? ?????????

                //??????????????? ???????????? ????????? ???????????? ??????
                //conn.getResponseCode() : 200?????? ??????
                //                         404, 500???... ?????????
                if (conn.getResponseCode() == conn.HTTP_OK) {


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

            } catch (Exception e) {

            }
            return str;
        }


        @Override
        protected void onPostExecute(String s) {

            if( s.equalsIgnoreCase("success") ){
                Toast.makeText(getActivity(), "??????????????? ???????????????", Toast.LENGTH_SHORT).show();
               // Intent i = new Intent(getActivity(), logintab.class);
               // getContext().startActivity(i);
                Login_SignupActivity.viewPager.setCurrentItem(0);

            }else{
                Toast.makeText(getActivity(), "???????????? ???????????????", Toast.LENGTH_SHORT).show();

            }

        }
    }//Task




}