package org.cacophonie.cacompagnon.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.webkit.WebView;

import org.cacophonie.cacompagnon.R;

public class LoginActivity extends AppCompatActivity {

    public static int LOGIN_INTENT_CODE = 1;
    public static String LOGIN_INTENT_SESSION_KEY = "org.cacophonie.cacompagnon.activity.LoginActivity.session_key";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Enable the back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Create a WebView and display it
        WebView webview = new WebView(this);
        setContentView(webview);
        webview.loadUrl("https://smaiz.fr/Caconnexion/login.php");

        // Handle login
    }
}
