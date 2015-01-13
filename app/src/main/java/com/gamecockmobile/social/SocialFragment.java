package com.gamecockmobile.social;

import com.gamecockmobile.R;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebViewFragment;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.net.Uri;

public class SocialFragment extends Fragment {

    private WebView mWebView;
    private ProgressBar mSpinner;
    public static final String baseURL = "https://twitter.com";

    private static final String widgetInfo = "<a class=\"twitter-timeline\" href=\"https://twitter.com/UofSC\" data-widget-id=\"488422368528699392\">Tweets by @UofSC</a>"
            + "<script>!function(d,s,id){var js,fjs=d.getElementsByTagName(s)[0],p=/^http:/.test(d.location)?\'http\':\'https\';if(!d.getElementById(id)){js=d.createElement(s);js.id=id;js.src=p+\"://platform.twitter.com/widgets.js\";fjs.parentNode.insertBefore(js,fjs);}}(document,\"script\",\"twitter-wjs\");</script>";

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        ActionBar actionBar = getActivity().getActionBar();
        actionBar.setTitle("Events");

        View view = inflater.inflate(R.layout.social_fragment, container, false);

        // WebView webView = (WebView) getActivity().findViewById(R.id.social_webview);
        // webView.getSettings().setDomStorageEnabled(true);
        // webView.getSettings().setJavaScriptEnabled(true);

        // webView.loadDataWithBaseURL(baseURL, widgetInfo, "text/html", "UTF-8", null);

        return view;
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        WebView mWebView = (WebView) getActivity().findViewById(R.id.social_webview);

        if (mWebView != null) {
            mWebView.getSettings().setDomStorageEnabled(true);
            mWebView.getSettings().setJavaScriptEnabled(true);

            mWebView.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url){
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    view.getContext().startActivity(intent);
                    return true;
                }
                public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                    Toast.makeText(getActivity(), "Can't connect to Twitter feed", Toast.LENGTH_SHORT).show();
                }
            });

            mWebView.loadDataWithBaseURL(baseURL, widgetInfo, "text/html", "UTF-8", null);
        }
    }
}
