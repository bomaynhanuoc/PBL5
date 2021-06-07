package com.bmnhanuoc.wirelesslock20.login;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bmnhanuoc.wirelesslock20.R;

public class SignupTabFragment extends Fragment {
    EditText email,pass,pass2,number;
    Button signup;
    float v=0;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.signup_tab_fragment,container,false);

        email = root.findViewById(R.id.email2);
        number = root.findViewById(R.id.number);
        pass = root.findViewById(R.id.password2);
        pass2 = root.findViewById(R.id.password3);
        signup = root.findViewById(R.id.signupbtn);

        email.setTranslationY(300);
        number.setTranslationY(300);
        pass.setTranslationY(300);
        pass2.setTranslationY(300);
        signup.setTranslationY(300);

        email.setAlpha(v);
        number.setAlpha(v);
        pass.setAlpha(v);
        pass2.setAlpha(v);
        signup.setAlpha(v);

        email.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(300).start();
        number.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(400).start();
        pass.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(500).start();
        pass2.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(600).start();
        signup.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(700).start();

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String stringemail = email.getText().toString().trim();
                String stringpassword = pass.getText().toString().trim();
                String stringpassword2 = pass2.getText().toString().trim();

                if(TextUtils.isEmpty(stringemail)){
                    email.setError("Please enter a valid email");
                    return;
                }
                if(TextUtils.isEmpty(stringpassword)){
                    pass.setError("Please enter a password");
                    return;
                }
                if(TextUtils.isEmpty(stringpassword2)){
                    pass2.setError("Please confirm the password");
                    return;
                }
                if(stringpassword.length() < 6 || stringpassword.length() > 16){
                    pass.setError("Password have to be 6-16 characters");
                }
                if(stringpassword2 != stringpassword){
                    pass2.setError("Password not match");
                }
                if(Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()){

                } else {
                    email.setError("Please enter a valid email");
                }
            }
        });

        return root;
    }
}
