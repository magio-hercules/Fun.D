package com.project.study.studyproject;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;

public class GalleryDialogFragment extends DialogFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.dialog_gallery, container);


        Bundle args = getArguments();

        String url = args.getString("url");

        TextView textView = view.findViewById(R.id.textView);

        textView.setText(url);


        WebView webView = view.findViewById(R.id.webView);

        WebSettings webSettings = webView.getSettings();

        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);

        webSettings.setJavaScriptEnabled(true);

        webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);

        webView.loadUrl(url);


        return view;

    }
}
