package org.cacophonie.cacompagnon.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import org.cacophonie.cacompagnon.R;

public class LoginActivity extends AppCompatActivity {

    public static int LOGIN_INTENT_CODE = 1;
    public static String LOGIN_INTENT_SESSION_KEY = "org.cacophonie.cacompagnon.activity.LoginActivity.session_key";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Enable the back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Create a WebView and configure it
        WebView webview = new WebView(this);
        WebSettings webSettings = webview.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setAppCacheEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setSupportMultipleWindows(true);

        //Cookie manager for the webview
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);

        // display the webview
        setContentView(webview);
        webview.setWebViewClient(new LoginWebViewClient());
        webview.loadUrl("https://smaiz.fr/vanilla/entry/signin");
    }

    private class LoginWebViewClient extends WebViewClient {
        private String cookies;

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Log.d("Login", url);
            if (url.equals("https://smaiz.fr/vanilla/")) {
                // If we've been redirected here, we are logged in
                Intent intent = new Intent();
                // Let's return the cookies to the main activity
                intent.putExtra(LOGIN_INTENT_SESSION_KEY, cookies);
                setResult(RESULT_OK, intent);
                finish();
                return true;
            }
            return false;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            cookies = CookieManager.getInstance().getCookie(url);
        }
    }
}
