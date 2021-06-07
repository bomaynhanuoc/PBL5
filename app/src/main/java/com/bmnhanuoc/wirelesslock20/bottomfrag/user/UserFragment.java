package com.bmnhanuoc.wirelesslock20.bottomfrag.user;


import android.app.Notification;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bmnhanuoc.wirelesslock20.MainActivity;
import com.bmnhanuoc.wirelesslock20.R;
import com.bmnhanuoc.wirelesslock20.login.LoginActivity;

import static android.content.Context.NOTIFICATION_SERVICE;

public class UserFragment extends Fragment {
    EditText ip;
    ImageView imageView;
    TextView textView;
    public Button button,button2,button3;
    static String IP;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_user,container,false);


        imageView = root.findViewById(R.id.image1);
        textView = root.findViewById(R.id.text1);
        button = root.findViewById(R.id.btn1);
        button2 = root.findViewById(R.id.btn2);
        button3 = root.findViewById(R.id.testnoti);
        ip = root.findViewById(R.id.ipedit);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
//                imageView.setVisibility(View.INVISIBLE);
//                textView.setVisibility(View.INVISIBLE);
//                button.setVisibility(View.INVISIBLE);
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IP = ip.getText().toString().trim();
                if (IP.length() > 0){
                    Toast.makeText(getContext(),"IP saved", Toast.LENGTH_SHORT).show();
                    button.setVisibility(View.VISIBLE);
                } else {
                    Toast.makeText(getContext(),"Enter your IP", Toast.LENGTH_SHORT).show();
                }
            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Notification notification = new Notification.Builder(getContext())
                        .setContentTitle("Có người lạ đến nhà bạn")
                        .setContentText("Kiểm tra ngay đấy là ai!")
                        .setSmallIcon(R.mipmap.ic_launcher_round)
                        .build();
                NotificationManager notificationManager = (NotificationManager) requireContext().getSystemService(NOTIFICATION_SERVICE);
                if (notificationManager!= null) {
                    notificationManager.notify(0, notification);
                }
            }
        });


        return root;

    }
    public static String printIP(){
        return IP;
    }
}
