package com.example.lgosc;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import com.illposed.osc.OSCMessage;
import com.illposed.osc.OSCPortOut;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class MainActivity extends AppCompatActivity {
    private MyTask Mytask;
    /* access modifiers changed from: private */
    public String arg;
    /* access modifiers changed from: private */
    public String myIP = "192.168.1.1";
    /* access modifiers changed from: private */
    public int myPort = 5005;
    /* access modifiers changed from: private */
    public OSCPortOut oscPortOut;

    public class MyTask extends AsyncTask<Void, Void, Void> {
        public MyTask() {
        }

        /* access modifiers changed from: protected */
        public Void doInBackground(Void... arg0) {
            try {
                MainActivity.this.oscPortOut = new OSCPortOut(InetAddress.getByName(MainActivity.this.myIP), MainActivity.this.myPort);
            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (Exception e2) {
            }
            try {
                OSCMessage message = new OSCMessage("/flyto");
                message.addArgument(MainActivity.this.arg);
                MainActivity.this.oscPortOut.send(message);
            } catch (IOException e3) {
                e3.printStackTrace();
            }
            return null;
        }
    }

    public void LosAngeles(View view) {
        this.arg = "LosAngeles";
        this.Mytask = new MyTask();
        this.Mytask.execute(new Void[0]);
    }

    public void Madrid(View view) {
        this.arg = "Madrid";
        this.Mytask = new MyTask();
        this.Mytask.execute(new Void[0]);
    }

    public void Dubai(View view) {
        this.arg = "Dubai";
        this.Mytask = new MyTask();
        this.Mytask.execute(new Void[0]);
    }

    public void SanFransisco(View view) {
        this.arg = "SanFransisco";
        this.Mytask = new MyTask();
        this.Mytask.execute(new Void[0]);
    }

    public void NewYork(View view) {
        this.arg = "NewYork";
        this.Mytask = new MyTask();
        this.Mytask.execute(new Void[0]);
    }

    public void Tokyo(View view) {
        this.arg = "Tokyo";
        this.Mytask = new MyTask();
        this.Mytask.execute(new Void[0]);
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) C0262R.layout.activity_main);
    }
}
