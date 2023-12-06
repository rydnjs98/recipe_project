package com.example.recipe_project;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;


import android.webkit.WebChromeClient;

import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

import java.net.URISyntaxException;

public class Webview  extends AppCompatActivity {
    private String TAG = Webview.class.getSimpleName();

    private WebView webView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

        webView = (WebView) findViewById(R.id.webview);

        webView.setWebViewClient(new WebViewClient());  // 새 창 띄우기 않기
        webView.setWebChromeClient(new WebChromeClient());
        //webView.setDownloadListener(new DownloadListener(){...});  // 파일 다운로드 설정

        webView.getSettings().setLoadWithOverviewMode(true);  // WebView 화면크기에 맞추도록 설정 - setUseWideViewPort 와 같이 써야함
        webView.getSettings().setUseWideViewPort(true);  // wide viewport 설정 - setLoadWithOverviewMode 와 같이 써야함

        webView.getSettings().setSupportZoom(false);  // 줌 설정 여부
        webView.getSettings().setBuiltInZoomControls(false);  // 줌 확대/축소 버튼 여부

        webView.getSettings().setJavaScriptEnabled(true); // 자바스크립트 사용여부
//        webview.addJavascriptInterface(new AndroidBridge(), "android");
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true); // javascript가 window.open()을 사용할 수 있도록 설정
        webView.getSettings().setSupportMultipleWindows(true); // 멀티 윈도우 사용 여부

        webView.getSettings().setDomStorageEnabled(true);  // 로컬 스토리지 (localStorage) 사용여부


        Intent intent = getIntent();
        String receivedMessage = intent.getStringExtra("Ing_link");


        //웹페이지 호출
//        webView.loadUrl("http://www.naver.com");




        if (receivedMessage.startsWith("intent:")) {
            try {
                Intent intents = Intent.parseUri(receivedMessage, Intent.URI_INTENT_SCHEME);
                Intent existPackage = getPackageManager().getLaunchIntentForPackage(intents.getPackage());
                if (existPackage != null) {
                    startActivity(intents);
                } else {
                    Intent marketIntent = new Intent(Intent.ACTION_VIEW);
                    marketIntent.setData(Uri.parse("market://details?id=" + intents.getPackage()));
                    startActivity(marketIntent);
                }

            }catch (URISyntaxException uriSyntaxException) {
                uriSyntaxException.printStackTrace();
                // URL 구문 오류 처리
            } catch (ActivityNotFoundException activityNotFoundException) {
                activityNotFoundException.printStackTrace();
                // 앱 실행 오류 처리
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {

            webView.loadUrl(receivedMessage);
        }
    }







}
