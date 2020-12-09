package com.id.socketio;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

public class ChatFragment extends Fragment {
    private Button setNickName;
    private EditText userNickName;
    private EditText newRoomNumber;
    private Boolean hasConnection = false;
    private Socket RoomSocket;
    {
        try {
            RoomSocket = IO.socket("http://10.0.2.2:3001/room");
        } catch (URISyntaxException e) {}
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.activity_add_chat,container,false);
        userNickName = view.findViewById(R.id.userNickName);
        setNickName = view.findViewById(R.id.setNickName);
        newRoomNumber=view.findViewById(R.id.newRoomNumber);
        RoomSocket.connect();


        RoomSocket.on("existedRoom",onNewRoom);


        userNickName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().trim().length() > 0) {
                    setNickName.setEnabled(true);
                    Log.i(ChatActivity.TAG, "onTextChanged: ABLED");
                } else {
                    Log.i(ChatActivity.TAG, "onTextChanged: DISABLED");
                    setNickName.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });


        setNickName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject newRoom=new JSONObject();
                try {
                    newRoom.put("roomnumber",newRoomNumber.getText().toString());
                    newRoom.put("user",userNickName.getText().toString());
                    RoomSocket.emit("newRoom",newRoom);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

        if(savedInstanceState != null){
            hasConnection = savedInstanceState.getBoolean("hasConnection");
        }

        if(hasConnection){

        }else {
            RoomSocket.connect();

        }
        RoomSocket.on("existedRoom",onNewRoom);


        userNickName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().trim().length() > 0) {
                    setNickName.setEnabled(true);
                    Log.i(ChatActivity.TAG, "onTextChanged: ABLED");
                } else {
                    Log.i(ChatActivity.TAG, "onTextChanged: DISABLED");
                    setNickName.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });


        setNickName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject newRoom=new JSONObject();
                try {
                    newRoom.put("roomnumber",newRoomNumber.getText().toString());
                    newRoom.put("user",userNickName.getText().toString());
                    RoomSocket.emit("newRoom",newRoom);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });


        return view;
    };
    Emitter.Listener onNewRoom = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    String exist;
                    try {
                        exist=data.getString("exist");
                        if(exist.equals("false")){

                            Intent intent = new Intent(getActivity(), ChatActivity.class);
                            intent.putExtra("username", userNickName.getText().toString());
                            intent.putExtra("roomNumber",newRoomNumber.getText().toString());
                            startActivity(intent);
                        }else{

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            });
        }};
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("hasConnection", hasConnection);
        userNickName = getActivity().findViewById(R.id.userNickName);
        setNickName = getActivity().findViewById(R.id.setNickName);
        newRoomNumber=getActivity().findViewById(R.id.newRoomNumber);


        /*if(savedInstanceState != null){
            hasConnection = savedInstanceState.getBoolean("hasConnection");
        }*/

        if(hasConnection){

        }else {}

    }




}
