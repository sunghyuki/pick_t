package com.example.pickt;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ChattingActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    EditText textView;
    Button button;
    ArrayList<ChattingClass> myChatArrayList;
    public static final int REQUEST_CODE_CHATROOM=101;
    Button button_back;
    TextView nickname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        nickname=(TextView)findViewById(R.id.chat_nick);
        Intent intent=getIntent();
        String text=intent.getStringExtra("nickname");
        nickname.setText(text);



        LinearLayoutManager layoutManager=new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);
        recyclerView=(RecyclerView)findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(layoutManager);
        button_back=(Button)findViewById(R.id.button_back);
        button_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        myChatArrayList=new ArrayList<>();
        final ChatAdapter adapter=new ChatAdapter(myChatArrayList);
        button=(Button)findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textView=(EditText) findViewById(R.id.editTextTextMultiLine);
                final String chat = textView.getText().toString();
                myChatArrayList.add(new ChattingClass(chat));
                adapter.notifyDataSetChanged();
                /*new Thread(new Runnable() {
                    @Override
                    public void run() {
                        send(chat);

                    }
                }).start();*/

            }
        });
        recyclerView.setAdapter(adapter);
    }
    /*public void send(String data) {
        int portNumber=5001;
        try {
            Socket sock=new Socket("localhost",portNumber);
            ObjectOutputStream outstream=new ObjectOutputStream(sock.getOutputStream());
            outstream.writeObject(data);
            outstream.flush();
            sock.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }*/
}