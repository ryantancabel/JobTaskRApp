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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class Client_SignUp extends Fragment{

    public Client_SignUp() {

    }

    protected Button signUp1;
    protected TextView Name1;
    protected TextView userName1;
    protected TextView Email1;
    protected TextView pass1;
    protected TextView address1;
    protected TextView Postcode1;
    protected TextView Suburb1;
    protected Spinner  State1;


    protected Button registeredLink1;

    public View fra;


    public static Client_SignUp newInstance() {

        Client_SignUp fragmen_lg = new Client_SignUp();
        return fragmen_lg;
    }


    public static Client_SignUp newInstance(String param1, String param2) {
        Client_SignUp fragment = new Client_SignUp();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fra = inflater.inflate(R.layout.fragment_client__sign_up, container, false);
        //    Parse.initialize(getActivity(), getString(R.string.back4app_app_id), getString(R.string.back4app_client_key));


        signUp1 = (Button) fra.findViewById(R.id.signUpbtn);
        Name1 = (TextView) fra.findViewById(R.id.name);
        userName1 = (TextView) fra.findViewById(R.id.userName);
        Email1 = (TextView) fra.findViewById(R.id.Email);
        pass1 = (TextView) fra.findViewById(R.id.pass);
        address1 = (TextView) fra.findViewById(R.id.address);
        Postcode1  = (TextView) fra.findViewById(R.id.postCode);
        Suburb1  = (TextView) fra.findViewById(R.id.suburb);
        State1  = (Spinner) fra.findViewById(R.id.state);

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
                String Postcode = Postcode1.getText().toString();
                String Suburb  = Suburb1.getText().toString();
                String StateName = State1.getSelectedItem().toString();


                ParseUser user = new ParseUser();

                user.setUsername(UserName);
                user.setPassword(Password);
                user.setEmail(userEmail);
                user.put("Name", uName);
                user.put("Address",Address);
                user.put("Postcode", Postcode);
                user.put("Suburb",Suburb);
                user.put("State", StateName);



                user.signUpInBackground(new SignUpCallback() {
                    @Override
                    public void done(ParseException e) {

                        if (e == null) {
                            Toast.makeText(getActivity(), String.format("Sucessful Sign Up!"), Toast.LENGTH_SHORT).show();

                            Intent ProfilePage = SidePanelActivity.newIntent(getActivity());
                            startActivity(ProfilePage);

                        } else {
                            ParseUser.logOut();
                            AlertDialog.Builder alerBuilder = new AlertDialog.Builder(getActivity());
                            alerBuilder.setMessage(e.getMessage());
                            alerBuilder.setTitle("Error, Singup Failed!");
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

        ParseInstallation.getCurrentInstallation().saveInBackground();

        return fra;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public static Intent newIntent(Context packageContext) {
        Intent i = new Intent(packageContext, LoginFragment.class);
        return i;

    }

}
