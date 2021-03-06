package com.snc.zero.webview;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.snc.zero.log.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * WebView Helper
 *
 * @author mcharima5@gmail.com
 * @since 2018
 */
public class WebViewHelper {
    private static final String TAG = WebViewHelper.class.getSimpleName();

    private static final String SCHEME_FILE = "file://";
    private static final String SCHEME_ASSET = "file:///android_asset";

    public static WebView addWebView(Context context, ViewGroup parentView) {
        WebView webView = newWebView(context);
        parentView.addView(webView);
        return webView;
    }

    public static void removeWebView(WebView webView) {
        if (null != webView) {
            webView.setWebChromeClient(null);
            webView.setWebViewClient(null);
            webView.destroy();
        }
    }

    private static WebView newWebView(Context context) {
        WebView webView = new WebView(context);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        webView.setLayoutParams(params);
        //webView.setBackgroundColor(Color.TRANSPARENT);
        //webView.setBackgroundResource(android.R.color.white);

        // setup
        setup(webView);

        return webView;
    }

    @SuppressLint({"SetJavaScriptEnabled", "JavascriptInterface", "AddJavascriptInterface"})
    private static void setup(WebView webView) {
        WebSettings settings = webView.getSettings();

        settings.setJavaScriptEnabled(true);
        settings.setGeolocationEnabled(true);
        settings.setUseWideViewPort(true);
        settings.setSupportZoom(true);
        settings.setBuiltInZoomControls(true);
        settings.setDisplayZoomControls(false);
        settings.setAppCacheEnabled(false);
        settings.setAllowFileAccess(true);
        settings.setAllowContentAccess(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            settings.setAllowFileAccessFromFileURLs(true);
            settings.setAllowUniversalAccessFromFileURLs(true);
        }
    }

    public static void loadUrl(final WebView webView, final String uriString) {
        final Map<String, String> extraHeaders = new HashMap<>();
        extraHeaders.put("Platform", "A");

        if (uriString.startsWith(SCHEME_FILE) && !uriString.startsWith(SCHEME_ASSET)) {
            List<String> permissions = new ArrayList<>();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                // Dangerous Permission
                permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
            }

            TedPermission.with(webView.getContext())
                    .setPermissionListener(new PermissionListener() {
                        @Override
                        public void onPermissionGranted() {
                            Logger.i(TAG, "[WEBVIEW] onPermissionGranted()");
                            webView.loadUrl(uriString, extraHeaders);
                        }

                        @Override
                        public void onPermissionDenied(List<String> deniedPermissions) {
                            Logger.e(TAG, "[WEBVIEW] onPermissionDenied()..." + deniedPermissions.toString());
                        }
                    })
                    .setPermissions(permissions.toArray(new String[] {}))
                    .check();
        } else {
            webView.loadUrl(uriString, extraHeaders);
        }
    }

}
