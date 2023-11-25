package com.example.insquare;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;

//도로명 주소 API Page
public class WebViewActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        WebView webView = findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.addJavascriptInterface(new BridgeInterface(), "Android");
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                //android -> javascript 함수 호출
                webView.loadUrl("javascript:sample2_execDaumPostcode();");
            }
        });
        //최초 웹뷰 로드
        webView.loadUrl("https://test-b8854.firebaseapp.com");
    }
    private class BridgeInterface{
        @JavascriptInterface
        public void processDATA(String data){
            // 카카오 주소 검색 api 결과값이 브릿지 통로를 통해 전달 (from javascript)
            Intent intent = new Intent();
            intent.putExtra("data", data);
            setResult(RESULT_OK, intent);
            finish();
        }
    }

}