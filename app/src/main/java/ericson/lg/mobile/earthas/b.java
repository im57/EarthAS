package ericson.lg.mobile.earthas;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class b extends AppCompatActivity {
    BluetoothAdapter mBluetoothAdapter;
    private static final int PERMISSIONS = 1;
    BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                Log.d("ghcn", "ghcnf3");
                //bluetooth device found
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                Log.d("device was searched", device.getName());
                Toast.makeText(b.this,"Found device " + device.getName(), Toast.LENGTH_SHORT).show();
            }
        }
    };

    protected void create() {

//        ActivityCompat.requestPermissions(this,
//                new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
//                        Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSIONS);
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        mBluetoothAdapter.startDiscovery();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothDevice.ACTION_FOUND);
        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                    Log.d("ghcn", "ghcnf3");
                    //bluetooth device found
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    Log.d("device was searched", device.getName());
                    Toast.makeText(b.this,"Found device " + device.getName(), Toast.LENGTH_SHORT).show();
                }
            }
        }, intentFilter);
    }


    protected void stop() {
        Log.d("ghcn", "ghcnf");
        unregisterReceiver(mReceiver);
    }

}
