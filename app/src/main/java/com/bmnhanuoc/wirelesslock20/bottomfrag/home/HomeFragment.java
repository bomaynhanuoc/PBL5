package com.bmnhanuoc.wirelesslock20.bottomfrag.home;

import android.app.Notification;
import android.app.NotificationManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
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

import com.bmnhanuoc.wirelesslock20.R;
import com.bmnhanuoc.wirelesslock20.service.ApiService;
import com.bumptech.glide.Glide;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;
import static android.content.Context.NOTIFICATION_SERVICE;

public class HomeFragment extends Fragment {

    private static final int NOTIFICATION_ID_NGUOI_LA = 1;
    private static final int NOTIFICATION_ID_NGUOI_QUEN = 2;


    private Handler timerHandler = new Handler();
    private boolean shouldRun = true;

    public Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            if (shouldRun) {
                sendPostToCheck();
                timerHandler.postDelayed(this, 2000);
            }
        }
    };
    @Override
    public void onResume() {
        super.onResume();
        timerHandler.postDelayed(timerRunnable, 0);
    }
    @Override
    public void onPause() {
        super.onPause();
        shouldRun = false;
        timerHandler.removeCallbacksAndMessages(timerRunnable);
    }

    TextView textView;
    EditText editText;
    ImageView imageView;
    Button btn1,btn;
    String string1 = "don't have data yet";
    String string2 = "not exist in db";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_home,container,false);


        imageView = root.findViewById((R.id.imageView2));
        btn = root.findViewById(R.id.button);
        btn1 = root.findViewById(R.id.button1);
        textView = root.findViewById(R.id.url_to_be_send);
        editText = root.findViewById(R.id.name_to_be_send);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendPostToOpenDoor();
            }
        });
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToDataBase();
            }
        });

        return root;
    }

    public void sendPostToOpenDoor() {
        ApiService.apiService.sendOpenDoorPost().enqueue(new Callback<PostString>() {
            @Override
            public void onResponse(Call<PostString> call, Response<PostString> response) {
                if(response.isSuccessful()) {
                    Toast.makeText(getContext(), "Door opened!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Login require", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<PostString> call, Throwable t) {

            }
        });
    }

    public void addToDataBase(){
        String url = textView.getText().toString().trim();
        String name = editText.getText().toString().trim();
        AddUser addUser = new AddUser(name,url);

        ApiService.apiService.addUser(addUser).enqueue(new Callback<AddUser>() {
            @Override
            public void onResponse(Call<AddUser> call, Response<AddUser> response) {
                if(response.isSuccessful()) {
                    Toast.makeText(getContext(),"Adding success",Toast.LENGTH_LONG).show();
                    editText.setVisibility(View.GONE);
                    btn1.setVisibility(View.GONE);
                }
            }
            @Override
            public void onFailure(Call<AddUser> call, Throwable t) {
                Toast.makeText(getContext(), "Fail to add, try again.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void sendPostToCheck(){
        ApiService.apiService.sendPost().enqueue(new Callback<PostString2>() {
            @Override
            public void onResponse(Call<PostString2> call, Response<PostString2> response) {
                if (response.isSuccessful()){
                    Log.i(TAG, "post submitted to API." + response.body().toString());
                        if (response.body().getName().trim().equals(string1)) {
                            return;
                        } else if (response.body().getName().trim().equals(string2)) {
                            Toast.makeText(getContext(),"Có một người lạ xuất hiện trước nhà bạn.",Toast.LENGTH_LONG).show();
                            sendNotiNguoiLa();
                            Glide.with(HomeFragment.this).load(response.body().getImageUrl()).into(imageView);
                            textView.setText(response.body().getImageUrl());
                            btn1.setVisibility(View.VISIBLE);
                            editText.setVisibility(View.VISIBLE);
                            return;
                        } else {
                            Toast.makeText(getContext(),response.body().getName().trim()
                                + " đã đến nhà bạn, cửa đã tự động mở.",Toast.LENGTH_LONG).show();
                            sendNotiNguoiQuen();
                            Glide.with(HomeFragment.this).load(response.body().getImageUrl()).into(imageView);
                            return;
                        }
                } else {
                    Toast.makeText( getContext(),"Please log in first", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<PostString2> call, Throwable t) {
                Toast.makeText(getContext(),"Please enter the IP first",Toast.LENGTH_LONG).show();
            }
        });
    }

    private void sendNotiNguoiQuen() {
        Notification notification = new Notification.Builder(getContext())
                .setContentTitle("Có người quen đến nhà bạn")
                .setContentText("Cửa đã tự động mở!")
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .build();
        NotificationManager notificationManager = (NotificationManager) requireContext().getSystemService(NOTIFICATION_SERVICE);
        if (notificationManager!= null) {
            notificationManager.notify(NOTIFICATION_ID_NGUOI_QUEN, notification);
        }
    }

    private void sendNotiNguoiLa() {
        Notification notification = new Notification.Builder(getContext())
                .setContentTitle("Có người lạ đến nhà bạn")
                .setContentText("Kiểm tra ngay đấy là ai!")
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .build();
        NotificationManager notificationManager = (NotificationManager) requireContext().getSystemService(NOTIFICATION_SERVICE);
        if (notificationManager!= null) {
            notificationManager.notify(NOTIFICATION_ID_NGUOI_LA, notification);
        }
    }
}
