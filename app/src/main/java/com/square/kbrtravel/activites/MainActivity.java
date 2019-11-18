package com.square.kbrtravel.activites;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.square.kbrtravel.R;
import com.square.kbrtravel.utilities.AlertUtils;
import com.square.kbrtravel.utilities.NetworkUtils;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class MainActivity extends AppCompatActivity {
    WebView webView;
    AlertDialog alertDialog;

    TextView tvNoInternetConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ((MainActivity) this).getSupportActionBar().hide();
        webView = findViewById(R.id.webview);
        tvNoInternetConnection = findViewById(R.id.tv_back_no_internet_connection);

        initViews("https://kbrtravel.com/");
    }


    private void initViews(String url) {
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.setWebViewClient(new HelloWebViewClient());
        if (alertDialog == null) {
            alertDialog = AlertUtils.customProgressDialog(MainActivity.this);
            alertDialog.show();
        }
        webView.loadUrl(url);

        if (!NetworkUtils.isNetworkConnected(this)) {
            tvNoInternetConnection.setVisibility(View.VISIBLE);
            showSweetDialog();
        }

        webView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {

            }
        });
    }

    private class HelloWebViewClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            if (alertDialog == null) {
                alertDialog = AlertUtils.customProgressDialog(MainActivity.this);
                alertDialog.show();
            }
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            if (!NetworkUtils.isNetworkConnected(MainActivity.this)) {
                tvNoInternetConnection.setVisibility(View.VISIBLE);
                showSweetDialog();

            }

            view.loadUrl(url);


            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            if (alertDialog != null) {
                alertDialog.dismiss();
            }
            super.onPageFinished(view, url);
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_BACK:
                    if (webView.canGoBack()) {
                        webView.goBack();
                    } else {
                        finish();
                    }
                    return true;
            }

        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }


    private void showSweetDialog() {
        SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(MainActivity.this, SweetAlertDialog.ERROR_TYPE);
        sweetAlertDialog.setConfirmText("Refresh")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        startActivity(new Intent(MainActivity.this, MainActivity.class));
                    }
                })
                .setTitleText("Oops...")
                .setContentText("No internet connection!")
                .setOnKeyListener(new DialogInterface.OnKeyListener() {
                    @Override
                    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                        finish();
                        return false;
                    }
                });

        sweetAlertDialog.setCancelButton("Close", new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                finish();
            }
        });
        sweetAlertDialog.setCancelable(false);
        sweetAlertDialog.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}