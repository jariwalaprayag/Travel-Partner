package com.androidbroadcast.reciever;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SignUp extends Activity {
    DataController mydb;
    EditText email;
    EditText pwd;
    EditText cpwd;
    TextView msg;
    Button signUp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

    }

    public void saveLogin(View view) {
        Log.d("Clicked", "signUp: ");
        EditText newEmail = (EditText) findViewById(R.id.emailView);
        EditText pwd = (EditText) findViewById(R.id.passwordView);
        EditText firstName = (EditText) findViewById(R.id.firstNameView);
        EditText lastName = (EditText) findViewById(R.id.lastNameView);
        EditText cpwd = (EditText) findViewById(R.id.cpasswordView);
        TextView msg = (TextView) findViewById(R.id.msg);
        String email = newEmail.getText().toString();
        String pwdEdit = pwd.getText().toString();
        String cpwdEdit = cpwd.getText().toString();
        String nameEdit = firstName.getText().toString() + " " + lastName.getText().toString();
        Log.d("details", "saveLogin: " + email + pwdEdit);

        if (cpwd.getText().length() > 0 && firstName.getText().length() > 0 && lastName.getText().length() > 0 && pwd.getText().length() > 0 && newEmail.getText().length() > 0) {
            if (isValidEmail(newEmail.getText().toString())) {
                if (pwd.getText().toString().length() >= 8) {
                    if (checkPass(pwd.getText().toString())) {
                        if (cpwd.getText().toString().equals(pwd.getText().toString())) {
                            DataController dataController = new DataController(getBaseContext());
                            dataController.open();
                            Cursor rs = dataController.retrieve(email);
                            if (!rs.moveToFirst()) {
                                long retValue = dataController.insert(nameEdit, email, pwdEdit);
                                dataController.close();
                                Log.d("checking..", "saveLogin: " + retValue + msg.getText());
                                if (retValue != -1) {
                                    Context context = getApplicationContext();
                                    //msg.setText("Registered successfully");
                                    Toast.makeText(getApplicationContext(), "Registered successfully", Toast.LENGTH_LONG).show();
                                    sendConfirmation(newEmail.getText().toString(), firstName.getText().toString());
                                    Intent intent = new Intent(SignUp.this, GooglePlacesExample.class);
                                    startActivity(intent);
                                    //Log.d("checing..", "saveLogin: " + retValue + msg.getText());

                                }
                            } else {
                                Toast.makeText(getApplicationContext(), "User already registered.Please Sign In", Toast.LENGTH_LONG).show();
                                Intent i = new Intent(SignUp.this, SignInActivity.class);
                                startActivity(i);
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Password Does Not Match", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Password Must Have One Capital Letter, Lower Letter and Number.", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Password CANNOT be Smaller than 8 Digits.", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(getApplicationContext(), "Please Enter Valid E-Mail", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(getApplicationContext(), "Please Enter all Fields", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        SignUp.this.finish();
    }

    private static boolean checkPass(String str) {
        char ch;
        boolean capitalFlag = false;
        boolean lowerCaseFlag = false;
        boolean numberFlag = false;
        for (int i = 0; i < str.length(); i++) {
            ch = str.charAt(i);
            if (Character.isDigit(ch)) {
                numberFlag = true;
            } else if (Character.isUpperCase(ch)) {
                capitalFlag = true;
            } else if (Character.isLowerCase(ch)) {
                lowerCaseFlag = true;
            }
            if (numberFlag && capitalFlag && lowerCaseFlag)
                return true;
        }
        return false;
    }

    static boolean isValidEmail(String email) {
        String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
        return email.matches(regex);
    }

    public void sendConfirmation(String sendEmail, String firstName) {
        try {
            String email_message = "Greetings " + firstName + "," + "\n" + " " + "\n" + "Thank you for Signing up for TravelPartner. We hope you love our application and is useful to you on your next trip." + "\n" + " " + "\n" + "With Love from Developers.";
            GMailSender sender = new GMailSender("travelpartner.usa@gmail.com", "Vrtheboss11");
            sender.sendMail("SignUp Confirmation from TravelPartner", email_message,
                    "travelpartner.usa@gmail.com", sendEmail);
        } catch (Exception e) {
            //"Thank you for Signing up for TravelPartner." + '\n' + " " + '\n' +"With Love from Developers."
            Log.e("SendMail", e.getMessage(), e);
        }
    }
}





