package com.bmnhanuoc.wirelesslock20.login;

import android.util.Log;
import android.util.Patterns;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bmnhanuoc.wirelesslock20.MainActivity;
import com.bmnhanuoc.wirelesslock20.R;
import com.bmnhanuoc.wirelesslock20.service.ApiService;
import com.bmnhanuoc.wirelesslock20.bottomfrag.user.SigninInfo;

import java.util.Timer;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;

public class LoginTabFragment extends Fragment {

    Timer timer;
    EditText email,pass;
    TextView forgetpass;
    Button login;
    float v=0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.login_tab_fragment,container,false);

        email = root.findViewById(R.id.email);
        pass = root.findViewById(R.id.password);
        forgetpass = root.findViewById(R.id.forgetpass);
        login = root.findViewById(R.id.loginbtn);

        email.setTranslationY(300);
        pass.setTranslationY(300);
        forgetpass.setTranslationY(300);
        login.setTranslationY(300);

        email.setAlpha(v);
        pass.setAlpha(v);
        forgetpass.setAlpha(v);
        login.setAlpha(v);

        email.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(300).start();
        pass.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(500).start();
        forgetpass.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(500).start();
        login.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(700).start();



        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String stringemail = email.getText().toString().trim();
                String stringpassword = pass.getText().toString().trim();
                if(TextUtils.isEmpty(stringemail)){
                    email.setError("Please enter your email");
                    return;
                }
                if(TextUtils.isEmpty(stringpassword)){
                    pass.setError("Please enter your password");
                    return;
                }
                if(stringpassword.length() < 6 || stringpassword.length() > 16){
                    pass.setError("Password have to be 6-16 characters");
                }
                if(Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()){
                } else {
                    email.setError("Please enter a valid email");
                }
                sendLoginRequest(stringemail,stringpassword);

            }
        });

        return root;
    }

    private void sendLoginRequest(String stringemail,String stringpassword) {

        SigninInfo signinInfo = new SigninInfo(stringemail,stringpassword);

        ApiService.apiService.logIn(signinInfo).enqueue(new Callback<SigninInfo>() {
            @Override
            public void onResponse(Call<SigninInfo> call, Response<SigninInfo> response) {
                if (response.code() == 200) {
                    Toast.makeText(getContext(), "Login success!", Toast.LENGTH_SHORT).show();
                    getActivity().finish();

                } else {
                    Toast.makeText(getContext(), "Invalid username or password", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SigninInfo> call, Throwable t) {
                Toast.makeText(getContext(), "Check your connection and IP number!", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
