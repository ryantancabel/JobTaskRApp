package ict376.murdoch.edu.au.jobtaskrapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SignUpCallback;


public class Tech_SignUp extends Fragment {
    protected Button signUp1;
    protected TextView Name1;
    protected TextView userName1;
    protected TextView Email1;
    protected TextView pass1;
    protected TextView address1;
    protected Button registeredLink1;
    public View fra;


    public static Tech_SignUp newInstance(){

        Tech_SignUp fragmen_lg = new Tech_SignUp();
        return fragmen_lg;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fra = inflater.inflate(R.layout.fragment_tech__sign_up, container, false);
        return fra;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);


        View upContainer = getActivity().findViewById(R.id.areaFragment);

        boolean isVisible = upContainer != null && upContainer.getVisibility() == View.VISIBLE;

        if (isVisible) {
            signUp1 = (Button) fra.findViewById(R.id.signUpbtn);
            Name1 = (TextView) fra.findViewById(R.id.name);
            userName1 = (TextView) fra.findViewById(R.id.userName);
            Email1 = (TextView) fra.findViewById(R.id.Email);
            pass1 = (TextView) fra.findViewById(R.id.pass);
            address1 = (TextView) fra.findViewById(R.id.address);

            registeredLink1 = (Button) fra.findViewById(R.id.regist);

            registeredLink1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent home = MainActivity.newIntent(getActivity());
                    startActivity(home);
                }
            });

            signUp1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String uName = Name1.getText().toString();
                    String UserName = userName1.getText().toString();
                    String userEmail = Email1.getText().toString();
                    String Password = pass1.getText().toString();
                    String Address = address1.getText().toString();

                    ParseUser user = (ParseUser) new ParseObject("UserSignUpTable_Siddik");

                    user.setUsername(UserName);
                    user.setPassword(Password);
                    user.setEmail(userEmail);
                    user.put("Name", uName);
                    user.put("uAddress", Address);
                    user.signUpInBackground(new SignUpCallback() {
                        @Override
                        public void done(ParseException e) {

                            if (e == null) {
                                Toast.makeText(getActivity(), String.format("Sucessful Sign Up!"), Toast.LENGTH_SHORT).show();
                                // Intent ProfilePage = UserAccountActivity.newIntent(getActivity());
                                //startActivity(ProfilePage);
                            } else {
                                ParseUser.logOut();
                                Toast.makeText(getActivity(), String.format("Sign Up failed"), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }
            });

            ParseInstallation.getCurrentInstallation().saveInBackground();
        }
    }



}
