package com.learnque.my.authproject;

import android.content.Intent;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Spanned;
import android.util.Patterns;
import android.view.View;
import android.text.Html;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import static android.text.Html.fromHtml;

public class LoginActivity extends AppCompatActivity {

    //declaration editText
    EditText editEmail, editPassword;

    //declaration TextInput Layout
    TextInputLayout textInputLayoutEmail, textInputLayoutPassword;

    //declaration Button
    Button btnLogin;

    //declaration SQLite Helper
    Sqlitehelper sqlitehelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        sqlitehelper = new Sqlitehelper(this);
        initCreateAccountTextView();
        initViews();

        //setClick event of login button
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //validate user input
                if( validate() ) {
                    //get values from editText
                    String email = editEmail.getText().toString();
                    String password = editPassword.getText().toString();

                    //Authenticate User
                    User currentUser = sqlitehelper.Authenticate(new User(null, null, email, password));

                    //check auth is it success or fail
                    if ( currentUser != null ){
                        Snackbar.make(btnLogin, "Successfully logged in!", Snackbar.LENGTH_LONG).show();

                        //user logged in redirect to main activity
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.putExtra(MainActivity.EXTRA_NAME, currentUser.username);
                        startActivity(intent);
                        finish();
                    } else {
                        //user logged in failed
                        Snackbar.make(btnLogin, "Failed to login!, Please try again.", Snackbar.LENGTH_LONG).show();
                    }

                }
            }
        });

    }

    // this method used to set Create account TextView text and click event( maltipal colors
    // for TextView yet not supported in Xml so i have done it programmatically)
    private void initCreateAccountTextView(){
        TextView textViewCreateAccount = (TextView) findViewById(R.id.linkRegister);
        textViewCreateAccount.setText(fromHtml("<font color='#fff'>I don't have account yet. </font><font color='0c0099'>create one</font>"));
        textViewCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent register = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(register);
            }
        });
    }

    //this method is used to connect XML views to its Objects
    private void initViews(){
        editEmail = (EditText) findViewById(R.id.editTextEmail);
        editPassword = (EditText) findViewById(R.id.editTextPassword);

        textInputLayoutEmail = (TextInputLayout) findViewById(R.id.textInput_emailLayout);
        textInputLayoutPassword = (TextInputLayout) findViewById(R.id.textInput_passwordLayout);
        btnLogin = (Button) findViewById(R.id.btnLogin);
    }

    //This method is for handling fromHtml method deprecation
    @SuppressWarnings("deprecation")
    public static Spanned fromHtml(String html){
        Spanned result;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N){
            result = Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY);
        } else {
            result = Html.fromHtml(html);
        }
        return result;
    }

    //This method is used to validate input given by user
    public boolean validate(){
        boolean valid = false;

        //get values from editText
        String email = editEmail.getText().toString();
        String password = editPassword.getText().toString();

        //Handling validation for email fields
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            valid = false;
            textInputLayoutEmail.setError("Please enter valid email!");
        } else {
            valid =  true;
            textInputLayoutEmail.setError(null);
        }

        //Handling validations for Password fields
        if (password.isEmpty()){
            valid = false;
            textInputLayoutPassword.setError("Password is required!");
        } else {
            if (password.length() > 5){
                valid = true;
                textInputLayoutPassword.setError(null);
            } else {
                valid = false;
                textInputLayoutPassword.setError("Password is too short!");
            }
        }

        return valid;
    }
}
