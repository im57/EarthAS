package ericson.lg.mobile.earthas;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class BluetoothActivity extends AppCompatActivity  {

    private Bluetooth bluetooth;

    private FloatingActionButton fabOnOff;
    private Button btnConnect;

    private TextView tvStatus;

    private ListView lvBluetooth;
    private ArrayAdapter adapter;

    private Boolean isBluetoothOn = false;
    private Boolean isEnabled = false;

    private List<String> listPairedDevices;
    private Set<BluetoothDevice> pairedDevices;

    private final static int BT_REQUEST_ENABLE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);

        bluetooth = new Bluetooth();

        fabOnOff = findViewById(R.id.fab_bluetooth_onoff);
        btnConnect = findViewById(R.id.btn_bluetooth_connect);

        tvStatus = findViewById(R.id.text_bluetooth_status);

        lvBluetooth = findViewById(R.id.list_bluetooth);
        listPairedDevices = new ArrayList<String>();
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, listPairedDevices);
        lvBluetooth.setAdapter(adapter);

        lvBluetooth.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                AlertDialog.Builder builder = new AlertDialog.Builder(BluetoothActivity.this);
                builder.setTitle(listPairedDevices.get(i) + "에 연결하시겠습니까?")
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(bluetooth.connectSelectedDevice(listPairedDevices.get(i))){
                                    Toast.makeText(getApplicationContext(), listPairedDevices.get(i) + "에 연결됐습니다.", Toast.LENGTH_LONG).show();
                                }else{
                                    Toast.makeText(getApplicationContext(), "블루투스 연결 중 오류가 발생했습니다.", Toast.LENGTH_LONG).show();
                                }
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
    }

    //bluetooth on/off setting
    public void onResume() {
        super.onResume();

        isEnabled = bluetooth.isEnabled();

        tvStatus.setTextColor(getColor(R.color.colorPrimaryRed));
        fabOnOff.setBackgroundColor(getColor(R.color.sub_color));

        if(bluetooth.bluetoothAdapter == null) {
            tvStatus.setText("블루투스를 지원하지 않는 기기");
        } else {
            if (isEnabled) {
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

    public void bluetoothOn() {
        if(bluetooth.bluetoothAdapter == null) {
            Toast.makeText(getApplicationContext(), "블루투스를 지원하지 않는 기기입니다.", Toast.LENGTH_LONG).show();
        }
        else {
            if (isEnabled) {
                Toast.makeText(getApplicationContext(), "블루투스가 이미 활성화 되어 있습니다.", Toast.LENGTH_LONG).show();
            }
            else {
                Intent intentBluetoothEnable = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(intentBluetoothEnable, BT_REQUEST_ENABLE);
            }

            fabOnOff.setSupportBackgroundTintList(ColorStateList.valueOf(getColor(R.color.sub_color)));
        }
    }

    public void bluetoothOff() {
        if(bluetooth.bluetoothAdapter == null) {
            Toast.makeText(getApplicationContext(), "블루투스를 지원하지 않는 기기입니다.", Toast.LENGTH_LONG).show();
        }
        else{
            if (isEnabled) {
                bluetooth.bluetoothOff();
                Toast.makeText(getApplicationContext(), "블루투스가 비활성화 되었습니다.", Toast.LENGTH_SHORT).show();
                tvStatus.setText("블루투스 OFF");
            }
            else {
                Toast.makeText(getApplicationContext(), "블루투스가 이미 비활성화 되어 있습니다.", Toast.LENGTH_SHORT).show();
            }

            fabOnOff.setSupportBackgroundTintList(ColorStateList.valueOf(getColor(R.color.colorAccent)));
        }
    }

    void listPairedDevices() {
        if(bluetooth.bluetoothAdapter == null) {
            Toast.makeText(getApplicationContext(), "블루투스를 지원하지 않는 기기입니다.", Toast.LENGTH_LONG).show();
        } else {
            if (isEnabled) {
                pairedDevices = bluetooth.listPairedDevices();

                if (pairedDevices.size() > 0) {
                    listPairedDevices.clear();

                    for (BluetoothDevice device : pairedDevices) {
                        listPairedDevices.add(device.getName());
                    }

                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getApplicationContext(), "페어링된 장치가 없습니다.", Toast.LENGTH_LONG).show();
                }
            }
            else {
                Toast.makeText(getApplicationContext(), "블루투스가 비활성화 되어 있습니다.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case BT_REQUEST_ENABLE:
                if (resultCode == RESULT_OK) { // 블루투스 활성화를 확인을 클릭하였다면
                    Toast.makeText(getApplicationContext(), "블루투스 활성화", Toast.LENGTH_LONG).show();
                    tvStatus.setText("블루투스 ON");
                } else if (resultCode == RESULT_CANCELED) { // 블루투스 활성화를 취소를 클릭하였다면
                    Toast.makeText(getApplicationContext(), "취소", Toast.LENGTH_LONG).show();
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}