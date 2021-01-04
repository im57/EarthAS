package ericson.lg.mobile.earthas;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Set;
import java.util.UUID;

public class Bluetooth {
    private final static int BT_MESSAGE_READ = 2;
    private final static int BT_CONNECTING_STATUS = 3;
    final static UUID BT_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");    //phone to arduino
    //final static UUID BT_UUID = UUID.fromString("8CE255C0-200A-11E0-AC64-0800200C9A66");    //phone to phone
    private UUID uuid;
    //0000110a-0000-1000-8000-00805f9b34fb // phone
    //0000111e-0000-1000-8000-00805f9b34fb // airpods


    private ConnectedBluetoothThread threadConnectedBluetooth;
    private BluetoothDevice bluetoothDevice;
    private BluetoothSocket bluetoothSocket;

    private Set<BluetoothDevice> pairedDevices = null;

    BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

    private Handler bluetoothHandler = new Handler(){
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

    public Boolean isEnabled(){
        return bluetoothAdapter.isEnabled();
    }

    public void bluetoothOff() {
        if(bluetoothAdapter != null) {
            if (bluetoothAdapter.isEnabled()) {
                bluetoothAdapter.disable();
            }
        }
    }

    //find bluetooth
    public Set<BluetoothDevice> listPairedDevices() {
        if(bluetoothAdapter != null)  {
            if (bluetoothAdapter.isEnabled()) {
                pairedDevices = bluetoothAdapter.getBondedDevices();
            }
        }

        return pairedDevices;
    }

    //connect bluetooth
    Boolean connectSelectedDevice(String selectedDeviceName) {
        for(BluetoothDevice tempDevice : pairedDevices) {
            if (selectedDeviceName.equals(tempDevice.getName())) {
                bluetoothDevice = tempDevice;
                break;
            }
        }
        try {
            uuid = bluetoothDevice.getUuids()[0].getUuid();
            Log.d("errrrrrrrrrrrrrrrrrrrrrrr", uuid.toString());
            //bluetoothSocket = bluetoothDevice.createRfcommSocketToServiceRecord(BT_UUID);
            bluetoothAdapter.cancelDiscovery();
            //bluetoothSocket = bluetoothDevice.createRfcommSocketToServiceRecord(uuid);
            bluetoothSocket = bluetoothDevice.createInsecureRfcommSocketToServiceRecord(uuid);
            bluetoothSocket.connect();
            Log.d("errrrrrrrrrrrrrrrrrr","connect err333333333");

            threadConnectedBluetooth = new ConnectedBluetoothThread(bluetoothSocket);
            Log.d("errrrrrrrrrrrrrrrrrr","connect err444444444");
            threadConnectedBluetooth.start();
            Log.d("errrrrrrrrrrrrrrrrrr","connect err5555555555");
            bluetoothHandler.obtainMessage(BT_CONNECTING_STATUS, 1, -1).sendToTarget();
        } catch (IOException e) {
            Log.d("errrrrrrrrrrrrrrrrrr","connect err");
            return false;
        }

        return true;
    }

    //thread
    public class ConnectedBluetoothThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        private final static int BT_MESSAGE_READ = 2;

        public ConnectedBluetoothThread(BluetoothSocket socket) {
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
                Log.d("errrrrrrrrrrrrrrrrrr","socket err");
                //Toast.makeText(getApplicationContext(), "소켓 연결 중 오류가 발생했습니다.", Toast.LENGTH_LONG).show();
            }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {
            byte[] buffer = new byte[1024];
            int bytes;

            //receive data (always listening)
            while (true) {
                try {
                    bytes = mmInStream.available();
                    if (bytes != 0) {
                        buffer = new byte[1024];
                        SystemClock.sleep(100);
                        bytes = mmInStream.available();
                        bytes = mmInStream.read(buffer, 0, bytes);
                        bluetoothHandler.obtainMessage(BT_MESSAGE_READ, bytes, -1, buffer).sendToTarget();
                    }
                } catch (IOException e) {
                    Log.d("errrrrrrrrrrrrrrrrrr","io err");
                    break;
                }
            }
        }

        public void write(String str) {
            byte[] bytes = str.getBytes();
            try {
                mmOutStream.write(bytes);
            } catch (IOException e) {
            }
        }

        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
            }
        }
    }
}
