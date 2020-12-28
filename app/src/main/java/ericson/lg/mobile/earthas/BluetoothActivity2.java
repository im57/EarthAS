package ericson.lg.mobile.earthas;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class BluetoothActivity2 extends AppCompatActivity  {
/*
    private FloatingActionButton fabOnOff;
    private Button btnConnect;

    private TextView tvStatus;

    private ListView lvBluetooth;
    private ArrayAdapter adapter;

    private Boolean isBluetoothOn = false;

    private BluetoothAdapter bluetoothAdapter;
    private Set<BluetoothDevice> pairedDevices;
    private List<String> listPairedDevices;

    private Handler bluetoothHandler;
    private ConnectedBluetoothThread threadConnectedBluetooth;
    private BluetoothDevice bluetoothDevice;
    private BluetoothSocket bluetoothSocket;

    private final static int BT_REQUEST_ENABLE = 1;
    private final static int BT_MESSAGE_READ = 2;
    private final static int BT_CONNECTING_STATUS = 3;
    final static UUID BT_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");    //phone to arduino
    //final static UUID BT_UUID = UUID.fromString("8CE255C0-200A-11E0-AC64-0800200C9A66");    //phone to phone

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);

        fabOnOff = findViewById(R.id.fab_bluetooth_onoff);
        btnConnect = findViewById(R.id.btn_bluetooth_connect);

        tvStatus = findViewById(R.id.text_bluetooth_status);

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        lvBluetooth = findViewById(R.id.list_bluetooth);
        listPairedDevices = new ArrayList<String>();
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, listPairedDevices);
        lvBluetooth.setAdapter(adapter);

        lvBluetooth.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                AlertDialog.Builder builder = new AlertDialog.Builder(BluetoothActivity2.this);
                builder.setTitle(listPairedDevices.get(i) + "에 연결하시겠습니까?")
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                connectSelectedDevice(listPairedDevices.get(i));
                            }
                        }).setNegativeButton("취소", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).show();
            }
        });

        fabOnOff.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                bluetoothOnOff();
            }
        });

        btnConnect.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                listPairedDevices();
            }
        });

        bluetoothHandler = new Handler(){
            public void handleMessage(android.os.Message msg){
                if(msg.what == BT_MESSAGE_READ){
                    String readMessage = null;
                    try {
                        readMessage = new String((byte[]) msg.obj, "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    Log.d("receiveeeeeeeeeeeeeeee", readMessage);
                }
            }
        };
    }

    //bluetooth on/off setting
    public void onResume() {
        super.onResume();

        tvStatus.setTextColor(getColor(R.color.colorPrimaryRed));
        fabOnOff.setBackgroundColor(getColor(R.color.sub_color));

        if(bluetoothAdapter == null) {
            tvStatus.setText("블루투스를 지원하지 않는 기기");
        } else {
            if (bluetoothAdapter.isEnabled()) {
                tvStatus.setText("블루투스 ON");
                isBluetoothOn = true;
                fabOnOff.setSupportBackgroundTintList(ColorStateList.valueOf(getColor(R.color.sub_color)));
            } else {
                tvStatus.setText("블루투스 OFF");
                isBluetoothOn = false;
                fabOnOff.setSupportBackgroundTintList(ColorStateList.valueOf(getColor(R.color.colorAccent)));
            }
        }
    }

    public void bluetoothOnOff() {
        if(isBluetoothOn){
            bluetoothOff();
            isBluetoothOn = false;
        } else {
            bluetoothOn();
            isBluetoothOn = true;
        }
    }

*/
}