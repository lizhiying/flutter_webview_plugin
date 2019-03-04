package com.flutter_webview_plugin;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by lejard_h on 20/12/2017.
 */

public class BrowserClient extends WebViewClient {

    private Context mContext;
    private Dialog progressDialog;

    public BrowserClient(Context ctx) {
        super();
        mContext = ctx;
    }

    private int running = 0; // Could be public if you want a timer to check.

//    @Override
//    public boolean shouldOverrideUrlLoading(WebView webView, String urlNewString) {
//        running++;
//        webView.loadUrl(urlNewString);
//
//        showProgress("loading...");
//        return true;
//    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);

        running = Math.max(running, 1); // First request move it to 1.

        Map<String, Object> data = new HashMap<>();
        data.put("url", url);
        data.put("type", "startLoad");
        FlutterWebviewPlugin.channel.invokeMethod("onState", data);
//        showProgress("loading...");
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        Map<String, Object> data = new HashMap<>();
        data.put("url", url);

        FlutterWebviewPlugin.channel.invokeMethod("onUrlChanged", data);

        data.put("type", "finishLoad");
        FlutterWebviewPlugin.channel.invokeMethod("onState", data);

//        if(--running == 0) { // just "running--;" if you add a timer.
//            removeProgress();
//        }

    }

    @Override
    public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
        super.onReceivedHttpError(view, request, errorResponse);
        Map<String, Object> data = new HashMap<>();
        data.put("url", request.getUrl().toString());
        data.put("code", Integer.toString(errorResponse.getStatusCode()));
        FlutterWebviewPlugin.channel.invokeMethod("onHttpError", data);
    }

//    public void showProgress(String message) {
//        if (progressDialog == null) {
//            progressDialog = CustomProgressDialog.create(mContext, message);
//            progressDialog.setCancelable(true);
//            progressDialog.setCanceledOnTouchOutside(true);
//            progressDialog.show();
//        }
//
//        if (!progressDialog.isShowing()) {
//            progressDialog.show();
//        }
//    }
//
//    public void removeProgress() {
//        if (progressDialog == null) {
//            return;
//        }
//        if (progressDialog.isShowing()) {
//            progressDialog.dismiss();
//        }
//
//    }

}