package com.bitgig.bitgig.ui;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bitgig.bitgig.MainActivity;
import com.bitgig.bitgig.R;
import com.bitgig.bitgig.util.PrefUtils;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by irvin on 2/7/2015.
 */
public class WelcomeActivity extends Activity implements View.OnClickListener {

    private Button fbLoginButton;
    private Dialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PrefUtils.markTosAccepted(WelcomeActivity.this);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.fragment_welcome_activity);

        fbLoginButton = (Button)findViewById(R.id.bFBLogin);
        fbLoginButton.setOnClickListener(this);

        ParseUser currentUser = ParseUser.getCurrentUser();
        if((currentUser != null) && ParseFacebookUtils.isLinked(currentUser)){
            showMainActivity();
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ParseFacebookUtils.finishAuthentication(requestCode,resultCode,data);
    }

    private void loginWithFB(){
        progressDialog = ProgressDialog.show(WelcomeActivity.this, "", "Logging in...", true);
        List<String> permissions = Arrays.asList("public_profile", "email");

        ParseFacebookUtils.logIn(permissions,this, new LogInCallback() {
            @Override
            public void done(ParseUser parseUser, ParseException e) {
                progressDialog.dismiss();
                if (parseUser == null) {
                    Log.d("myApp", "Uh oh. The user cancelled the Facebook login.");
                } else if (parseUser.isNew()) {
                    Log.d("myApp", "User signed up and logged in through Facebook!");
                    showMainActivity();
                } else {
                    Log.d("myApp", "User logged in through Facebook!");
                    showMainActivity();
                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_splash_screen, menu);
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showMainActivity(){
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.bFBLogin:
                loginWithFB();
                break;

        }
    }
}
