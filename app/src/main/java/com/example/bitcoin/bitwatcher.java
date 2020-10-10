package com.example.bitcoin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class bitwatcher extends AppCompatActivity {

    public static final String api_endpoint="https://api.coindesk.com/v1/bpi/currentprice.json";
    private OkHttpClient okHttpClient=new OkHttpClient();
    private ProgressDialog progressDialog;
    private TextView txt,txt1,txt2;
    public Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bitwatcher);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        txt=(TextView)findViewById(R.id.txt);
        txt1=(TextView)findViewById(R.id.txt1);
        txt2=(TextView)findViewById(R.id.txt2);
        button=(Button)findViewById(R.id.button);
        progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("API Loading");
        progressDialog.setMessage("Plwase wait till it is loading...");

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                load();
            }
        });
    }



    public void load(){
        Request request=new Request.Builder().url(api_endpoint).build();
        progressDialog.show();

        okHttpClient.newCall(request).enqueue(new Callback(){
            @Override
          public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.i("sss:","error");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                final String body= Objects.requireNonNull(response.body()).string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                        parseBpiResponse(body);
                    }
                });
            }
        });
    }

    private void parseBpiResponse(String body) {
        try{

            StringBuilder stringBuilder=new StringBuilder();
            JSONObject jsonObject=new JSONObject(body);
            JSONObject timepbject=jsonObject.getJSONObject("time");
            stringBuilder.append(timepbject.getString("updated"));
            txt.setText(stringBuilder.toString());

            StringBuilder stringBuilder2=new StringBuilder();
            JSONObject bpiObject = jsonObject.getJSONObject("bpi");

            JSONObject usiObject= bpiObject.getJSONObject("USD");

            stringBuilder2.append(usiObject.getString("rate")).append("$");

            txt1.setText(stringBuilder2.toString());

            StringBuilder stringBuilder3=new StringBuilder();
            JSONObject eurObject=bpiObject.getJSONObject("EUR");
            stringBuilder3.append(eurObject.getString("rate")).append(eurObject.getString("symbol"));


            txt2.setText(stringBuilder3.toString());


        }
        catch (Exception e){

        }
    }
}
