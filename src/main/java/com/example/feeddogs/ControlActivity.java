package com.example.feeddogs;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class ControlActivity extends AppCompatActivity {

    public static UUID m_myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    public static BluetoothSocket m_bluetoothSocket = null;
    public static ProgressDialog m_progress;
    public static BluetoothAdapter m_bluetoothAdapter;
    public static boolean  m_isConnected = false;
    public static String m_address;
    public static TextView msgReceived;

    private Button feed180;
    private Button feed60;
    private Button feed90;
    private Button dis;
    private ReadData readData = new ReadData();
    private Thread t = new Thread(readData);



    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.control_layout);

        m_address = getIntent().getStringExtra(MainActivity.EXTRA_ADDRESS);
        new ConnectToDevice(this).execute();

        feed180 = findViewById(R.id.control_feed);
        feed60 = findViewById(R.id.feed60);
        feed90 = findViewById(R.id.feed90);
        dis = findViewById(R.id.control_disconnect);
        msgReceived = findViewById(R.id.control_msg_received);

        feed180.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mesaj = "180";
                if(m_bluetoothSocket != null){
                    try{
                        m_bluetoothSocket.getOutputStream().write(mesaj.getBytes());
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        });

        feed60.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mesaj = "60";
                if(m_bluetoothSocket != null){
                    try{
                        m_bluetoothSocket.getOutputStream().write(mesaj.getBytes());
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        });

        feed90.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mesaj = "90";
                if(m_bluetoothSocket != null){
                    try{
                        m_bluetoothSocket.getOutputStream().write(mesaj.getBytes());
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        });

        dis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(m_bluetoothSocket != null){
                    try {
                        m_bluetoothSocket.close();
                        m_bluetoothSocket = null;
                        m_isConnected = false;
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                }
                finish();
            }
        });

    }


    private class ConnectToDevice extends AsyncTask<Void,Void,String>{

        private boolean connectSuccess = true;
        private Context context;


        public ConnectToDevice(Context c){
            this.context = c;
        }

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            m_progress = ProgressDialog.show(context, "Connecting...", "please wait");

        }

        @Override
        protected String doInBackground(Void... voids) {
            try{
                if(m_bluetoothSocket == null || !m_isConnected){
                    m_bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                    BluetoothDevice device = m_bluetoothAdapter.getRemoteDevice(m_address);
                    m_bluetoothSocket = device.createInsecureRfcommSocketToServiceRecord(m_myUUID);
                    BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                    m_bluetoothSocket.connect();
                }
            }
            catch (Exception e){
                connectSuccess = false;
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result){
            super.onPostExecute(result);
            if(!connectSuccess){
                Log.i("data","couldn't connect");
            }
            else {
                m_isConnected = true;


            }

            t.start();
            m_progress.dismiss();
           
        }
    }

    private class ReadData implements Runnable{



        @Override
        public void run() {
            byte[] buffer;
            int bytes;

            while (m_isConnected){
                try {

                    buffer = new byte[1024];
                    bytes = m_bluetoothSocket.getInputStream().read(buffer);
                    byte[] buffer2 = new byte[bytes];
                    for(int i=0; i<bytes; i++){
                        buffer2[i]=buffer[i];
                    }

                    String msg = new String(buffer2, StandardCharsets.UTF_8);

                    msgReceived.post(new Runnable() {
                        @Override
                        public void run() {
                            msgReceived.setText(msg);
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

        }
    }



}
