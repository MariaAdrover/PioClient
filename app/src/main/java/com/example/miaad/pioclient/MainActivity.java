package com.example.miaad.pioclient;

import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener{
    private TextView texto;
    private Button leftButton;
    private Button rightButton;
    private Button upButton;
    private Button downButton;

    private Button conect;
    private Button action;

    private EditText ipInfo;
    private EditText portInfo;

    private String ip;
    private int puerto;
    private Socket sk;
    private BufferedReader entrada;
    private PrintWriter salida;

    private boolean con;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .permitAll().build());

        con = false;

        texto = (TextView) findViewById(R.id.text);

        leftButton = (Button) findViewById(R.id.left);
        rightButton = (Button) findViewById(R.id.right);
        upButton = (Button) findViewById(R.id.up);
        downButton = (Button) findViewById(R.id.down);

        leftButton.setOnTouchListener(this);
        rightButton.setOnTouchListener(this);
        upButton.setOnTouchListener(this);
        downButton.setOnTouchListener(this);

        conect = (Button) findViewById(R.id.conect);
        conect.setOnClickListener(this);

        action = (Button) findViewById(R.id.accion);
        action.setOnClickListener(this);

        ipInfo = (EditText) findViewById(R.id.ip);
        portInfo = (EditText) findViewById(R.id.port);
    }

    @Override
    public void onClick(View v) {
        if (((Button)v).getText().toString().equalsIgnoreCase("action")) {
            talk(v);
        } else {
            if (!con) {
                conectar();
            }
        }
    }

    private void talk(View v) {
        String data = ((Button) v).getText().toString();
        salida.println(data);
        try {
            String confirmation = entrada.readLine();
            if (!confirmation.equalsIgnoreCase("ok")) {
                this.con = false;
            }

        } catch (Exception e) {
            texto.setText("disconnected");
            this.con = false;
        }

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        if ((event.getAction() == MotionEvent.ACTION_UP) || (event.getAction() == MotionEvent.ACTION_DOWN)) {
            talk(v);
        }

        return true;
    }

    private void conectar() {
        this.ip = this.ipInfo.getText().toString();
        this.puerto = Integer.parseInt(this.portInfo.getText().toString());

        try {
            this.connect();
        } catch (Exception e) {
            texto.setText("couldn't connect...");
            con = false;
        }
    }

    private void connect() throws Exception{
        texto.setText("connecting");
        this.sk = new Socket(ip, puerto);
        this.entrada = new BufferedReader(new InputStreamReader(sk.getInputStream()));
        this.salida = new PrintWriter(new OutputStreamWriter(sk.getOutputStream()), true);
        texto.setText("connected");
        con = true;
    }
}
