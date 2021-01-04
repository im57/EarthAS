package ericson.lg.mobile.earthas;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class BluetoothActivity extends AppCompatActivity  {

    private Bluetooth bluetooth;

    private FloatingActionButton fabOnOff;
    private Button btnPaired;
    private Button btnSearch;

    private TextView tvStatus;

    private ListView lvPaired;
    private ArrayAdapter adapterPaired;
    private ListView lvSearch;
    private ArrayAdapter adapterSearch;

    private Boolean isBluetoothOn = false;
    private Boolean isEnabled = false;

    private List<String> listPairedDevices;
    private Set<BluetoothDevice> pairedDevices;
    private List<String> listSearchDevices;
    private List<BluetoothDevice> searchDevices;
    private int selectDevice = -1;

    private final static int BT_REQUEST_ENABLE = 1;

    BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);

        bluetooth = new Bluetooth();

        fabOnOff = findViewById(R.id.fab_bluetooth_onoff);
        btnPaired = findViewById(R.id.btn_bluetooth_paired);
        btnSearch = findViewById(R.id.btn_bluetooth_search);

        tvStatus = findViewById(R.id.text_bluetooth_status);

        lvPaired = findViewById(R.id.list_bluetooth_paired);
        listPairedDevices = new ArrayList<String>();
        adapterPaired = new ArrayAdapter(this, android.R.layout.simple_list_item_1, listPairedDevices);
        lvPaired.setAdapter(adapterPaired);

        lvSearch = findViewById(R.id.list_bluetooth_search);
        listSearchDevices = new ArrayList<String>();
        adapterSearch = new ArrayAdapter(this, android.R.layout.simple_list_item_1, listSearchDevices);
        lvSearch.setAdapter(adapterSearch);

        searchDevices = new ArrayList<>();

        //블루투스 브로드캐스트 리시버 등록
        //리시버1
        IntentFilter stateFilter = new IntentFilter();
        stateFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED); //BluetoothAdapter.ACTION_STATE_CHANGED : 블루투스 상태변화 액션
        registerReceiver(mBluetoothStateReceiver, stateFilter);

        //리시버2
        IntentFilter searchFilter = new IntentFilter();
        searchFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED); //BluetoothAdapter.ACTION_DISCOVERY_STARTED : 블루투스 검색 시작
        searchFilter.addAction(BluetoothDevice.ACTION_FOUND); //BluetoothDevice.ACTION_FOUND : 블루투스 디바이스 찾음
        searchFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED); //BluetoothAdapter.ACTION_DISCOVERY_FINISHED : 블루투스 검색 종료
        searchFilter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        registerReceiver(mBluetoothSearchReceiver, searchFilter);
        ////////////////////

        lvPaired.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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

        lvSearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                BluetoothDevice device = searchDevices.get(i);

                try {
                    //선택한 디바이스 페어링 요청
                    Method method = device.getClass().getMethod("createBond", (Class[]) null);
                    method.invoke(device, (Object[]) null);
                    selectDevice = i;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        fabOnOff.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                bluetoothOnOff();
            }
        });

        btnPaired.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                listPairedDevices();
            }
        });

        btnSearch.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("eeeeeeeeeee","click");

                //btnSearch.setEnabled(false);
                if(mBluetoothAdapter.isDiscovering()){
                    mBluetoothAdapter.cancelDiscovery();
                }
                mBluetoothAdapter.startDiscovery();
                Log.d("eeeeeeeeeee","end");

            }
        });
    }

    //블루투스 상태변화 BroadcastReceiver
    BroadcastReceiver mBluetoothStateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //BluetoothAdapter.EXTRA_STATE : 블루투스의 현재상태 변화
            int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, -1);

            //블루투스 활성화
            if(state == BluetoothAdapter.STATE_ON){
                tvStatus.setText("블루투스 ON");
            }
            //블루투스 활성화 중
            else if(state == BluetoothAdapter.STATE_TURNING_ON){
                tvStatus.setText("블루투스 활성화 중...");
            }
            //블루투스 비활성화
            else if(state == BluetoothAdapter.STATE_OFF){
                tvStatus.setText("블루투스 OFF");
            }
            //블루투스 비활성화 중
            else if(state == BluetoothAdapter.STATE_TURNING_OFF){
                tvStatus.setText("블루투스 비활성화 중...");
            }
        }
    };

    //블루투스 검색결과 BroadcastReceiver
    BroadcastReceiver mBluetoothSearchReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("eeeeeeeeeeeeee","receiveeeee");

            String action = intent.getAction();
            switch(action){
                //블루투스 디바이스 검색
                case BluetoothAdapter.ACTION_DISCOVERY_STARTED:
                    Log.d("eeeeeeeeeeeeee","start");
                    listSearchDevices.clear();
                    searchDevices.clear();
                    Toast.makeText(BluetoothActivity.this, "블루투스 검색 시작", Toast.LENGTH_SHORT).show();
                    break;
                //블루투스 디바이스 찾음
                case BluetoothDevice.ACTION_FOUND:
                    Log.d("eeeeeeeeeeeeee","found");
                    //검색한 블루투스 디바이스의 객체를 구한다
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    //데이터 저장
                    //Map map = new HashMap();
                    //map.put("name", device.getName()); //device.getName() : 블루투스 디바이스의 이름
                    //map.put("address", device.getAddress()); //device.getAddress() : 블루투스 디바이스의 MAC 주소
                    listSearchDevices.add(device.getName());
                    //리스트 목록갱신
                    adapterSearch.notifyDataSetChanged();

                    //블루투스 디바이스 저장
                    searchDevices.add(device);

                    break;
                //블루투스 디바이스 검색 종료
                case BluetoothAdapter.ACTION_DISCOVERY_FINISHED:
                    Log.d("eeeeeeeeeeeeee","finish");
                    Toast.makeText(BluetoothActivity.this, "블루투스 검색 종료", Toast.LENGTH_SHORT).show();
                    btnSearch.setEnabled(true);
                    break;
                //블루투스 디바이스 페어링 상태 변화
                case BluetoothDevice.ACTION_BOND_STATE_CHANGED:
                    Log.d("eeeeeeeeeeeeee","change");
                    BluetoothDevice paired = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    if(paired.getBondState()==BluetoothDevice.BOND_BONDED){
                        //데이터 저장
//                        Map map2 = new HashMap();
//                        map2.put("name", paired.getName()); //device.getName() : 블루투스 디바이스의 이름
//                        map2.put("address", paired.getAddress()); //device.getAddress() : 블루투스 디바이스의 MAC 주소
                        listPairedDevices.add(paired.getName());
                        //리스트 목록갱신
                        adapterPaired.notifyDataSetChanged();

                        //검색된 목록
                        if(selectDevice != -1){
                            searchDevices.remove(selectDevice);

                            listSearchDevices.remove(selectDevice);
                            adapterSearch.notifyDataSetChanged();
                            selectDevice = -1;
                        }
                    }
                    break;
            }
        }
    };

    //
    @Override
    protected void onStop() {
        super.onStop();
        Log.d("deviceeeeeeeeeeeeeeeee", "ghcnf");
        unregisterReceiver(mBluetoothStateReceiver);
        unregisterReceiver(mBluetoothSearchReceiver);
    }
    //

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
                isEnabled = false;
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

                    adapterPaired.notifyDataSetChanged();
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
                } else if (resultCode == RESULT_CANCELED) { // 블루투스 활성화를 취소를 클릭하였다면
                    Toast.makeText(getApplicationContext(), "취소", Toast.LENGTH_LONG).show();
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}