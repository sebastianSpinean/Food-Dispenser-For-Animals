package com.example.feeddogs;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.jetbrains.anko.DialogsKt.toast;


public class MainActivity extends AppCompatActivity {

    private BluetoothAdapter m_bluetoothAdapter = null;
    private Set<BluetoothDevice> m_pairedDevices;
    private Button b;
    private ListView lst;
    private int REQUEST_ENABLE_BLUETOOTH = 1;

    public static String EXTRA_ADDRESS = "Device_address";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        m_bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if(m_bluetoothAdapter == null){
            Context context = getApplicationContext();
            CharSequence text = "Acest dispozitiv nu suporta Blurtooth";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
            return;
        }
        if(!m_bluetoothAdapter.isEnabled()){
            Intent enableBluetoothIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBluetoothIntent,REQUEST_ENABLE_BLUETOOTH);
        }

       b = findViewById(R.id.select_device_refresh);
       b.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               m_pairedDevices = m_bluetoothAdapter.getBondedDevices();
               List<BluetoothDevice> list = new ArrayList<>();

               if(!m_pairedDevices.isEmpty()){
                   for(BluetoothDevice device : m_pairedDevices){
                       list.add(device);
                       Log.i("device",""+device);
                   }
               }
               else {
                   Context context = getApplicationContext();
                   CharSequence text = "Nu s-au gasit dispozitive imperecheate";
                   int duration = Toast.LENGTH_SHORT;
                   Toast toast = Toast.makeText(context, text, duration);
                   toast.show();
               }

               lst = findViewById(R.id.select_device_list);

               ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, list);
               lst.setAdapter(adapter);
               lst.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                   @Override
                   public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                     BluetoothDevice device = list.get(position);
                     String address = device.getAddress();

                     Intent intent = new Intent(getApplicationContext(),ControlActivity.class);
                     intent.putExtra(EXTRA_ADDRESS,address);
                     startActivity(intent);

                   }
               });

           }
       });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_ENABLE_BLUETOOTH){
            if(resultCode == Activity.RESULT_OK){
                if(m_bluetoothAdapter.isEnabled()){
                    Context context = getApplicationContext();
                    CharSequence text = "Bluetooth has been enabled";
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }
                else {
                    Context context = getApplicationContext();
                    CharSequence text = "Bluetooth has been disabled";
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }

            }
            else if(resultCode == Activity.RESULT_CANCELED){
                Context context = getApplicationContext();
                CharSequence text = "Bluetooth enabling has been canceled";
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }
        }
    }
}