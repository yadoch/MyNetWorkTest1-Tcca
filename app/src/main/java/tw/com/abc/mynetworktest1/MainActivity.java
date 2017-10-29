package tw.com.abc.mynetworktest1;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    private ConnectivityManager cmgr;
    private MyNetworkStateReceiver receiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        receiver= new MyNetworkStateReceiver();
        IntentFilter filter = new IntentFilter();
        // 
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(receiver,filter);
        cmgr=(ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);


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
}
