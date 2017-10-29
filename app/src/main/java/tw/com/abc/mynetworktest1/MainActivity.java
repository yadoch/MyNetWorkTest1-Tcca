package tw.com.abc.mynetworktest1;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    private ConnectivityManager cmgr;
    private MyNetworkStateReceiver receiver;
    private File root;
    private EditText mAccount,mPassword,mRealname;
    private String inputAccount,inputPassword,inputRealname;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    1);

        }else{
            init();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults.length > 0
                && grantResults[0]==PackageManager.PERMISSION_GRANTED){
            init();
        }
    }

    private void init() {
        cmgr=(ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        receiver= new MyNetworkStateReceiver();
        IntentFilter filter = new IntentFilter();

        //　透過AddAction 可以Receiver 多個不同的ACTION
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(receiver,filter);

        root = Environment.getExternalStorageDirectory();

        mAccount = (EditText) findViewById(R.id.account);
        mPassword = (EditText) findViewById(R.id.password);
        mRealname = (EditText) findViewById(R.id.realname);

    }


    public void test1(View view){
        Log.i("geoff","network:"+isConnectNetwork());
        Log.i("geoff","Wifi:"+isWifiConnectNetwork());
    }
    private boolean isWifiConnectNetwork(){
        NetworkInfo wifiInfo = cmgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        return wifiInfo.isConnected();
    }

    private boolean isConnectNetwork(){
        NetworkInfo info =cmgr.getActiveNetworkInfo();
        boolean isConnect = info != null && info.isConnectedOrConnecting();
        return isConnect;
    }

    private class  MyNetworkStateReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i("geoff","NetworkStatus:"+ isConnectNetwork());
        }
    }

    @Override
    public void finish() {
        super.finish();
        // 釋放recevice
        unregisterReceiver(receiver);
    }
    public void reg(View view){
        inputAccount = mAccount.getText().toString();
        inputPassword = mPassword.getText().toString();
        inputAccount = mRealname.getText().toString();

        new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                String urlString = "http://10.0.1.8/brad03.php?account="+inputAccount
                        +"&passwd="+inputPassword
                        +"&realname="+inputAccount;
                    URL url = null;

                    url = new URL(urlString);
                    HttpURLConnection conn =(HttpURLConnection) url.openConnection();
                    conn.connect();
                    conn.getInputStream();

                } catch (Exception e) {
                    Log.i("geoff",e.toString());
                }            }
        }.start();
    }

    public void login (View view){

        new Thread(){
            @Override
            public void run() {
                super.run();
                inputAccount = mAccount.getText().toString();
                inputPassword = mPassword.getText().toString();
                String urlString = "http://10.0.1.8/check.php";

                try {

                    URL url = new URL(urlString);
                    HttpURLConnection conn =(HttpURLConnection) url.openConnection();
                    conn.setReadTimeout(3000);
                    conn.setConnectTimeout(3000);
                    conn.setRequestMethod("POST");
                    conn.setDoInput(true);
                    conn.setDoOutput(true);

                    //Post 的相關動作,可以透過Mutil part Utility 取代
                    ContentValues data = new ContentValues();
                    data.put("account",inputAccount);
                    data.put("passwd",inputPassword);
                    String qs = queryString(data);

                    //送出
                    OutputStream os = conn.getOutputStream();
                    BufferedWriter writer= new BufferedWriter(new OutputStreamWriter(os));
                    w

                    conn.connect();
                    conn.getInputStream();

                } catch (Exception e) {
                    Log.i("geoff",e.toString());
                }            }
        }.start();
    }
    private String queryString (ContentValues data){
        return null;
    }
}
