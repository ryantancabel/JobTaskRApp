package ict376.murdoch.edu.au.jobtaskrapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseUser;

public class LoginFragment extends Fragment {

    public LoginFragment() {

    }

    protected Button login_button2;
    protected Button registedButton2;
    protected TextView username2;
    protected TextView password2;
    public View lg_fra;

    public static LoginFragment newInstance(){
        LoginFragment fragmen_lg = new LoginFragment();
        return fragmen_lg;
    }

    public static LoginFragment newInstance(String param1, String param2) {
        LoginFragment fragment = new LoginFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        lg_fra = inflater.inflate(R.layout.fragment_login, container, false);

        Parse.initialize(getActivity(), getString(R.string.back4app_app_id), getString(R.string.back4app_client_key));

        login_button2 = (Button) lg_fra.findViewById(R.id.login_button);
        username2 = (TextView) lg_fra.findViewById(R.id.username);
        password2 = (TextView) lg_fra.findViewById(R.id.password);
        registedButton2 = (Button) lg_fra.findViewById(R.id.registerBtn);

        login_button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String usedID = username2.getText().toString();
                String passwordLg = password2.getText().toString();



                ParseUser.logInInBackground(usedID, passwordLg, new LogInCallback() {
                    @Override
                    public void done(ParseUser parseUser, ParseException e) {
                        if (parseUser != null) {

                            Toast.makeText(getActivity(), String.format("Login_Success"), Toast.LENGTH_SHORT).show();

                            Intent ProfilePage = SidePanelActivity.newIntent(getActivity());
                            startActivity(ProfilePage);

                        } else {

                            AlertDialog.Builder alerBuilder = new AlertDialog.Builder(getActivity());
                            alerBuilder.setMessage(e.getMessage());
                            alerBuilder.setTitle("Error, Invalid Login attempt");
                            alerBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            });

                            AlertDialog alertbox = alerBuilder.create();
                            alertbox.show();
                        }
                    }
                });
            }
        });

        registedButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ProfilePage = SignUpActivity.newIntent(getActivity());
                startActivity(ProfilePage);
            }
        });

        ParseInstallation.getCurrentInstallation().saveInBackground();

        return lg_fra;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public static Intent newIntent(Context packageContext){
        Intent i = new Intent(packageContext, LoginFragment.class);
        return i;
    }
}
