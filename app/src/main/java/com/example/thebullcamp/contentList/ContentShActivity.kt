package com.example.thebullcamp.contentList

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebView
import android.widget.Toast
import com.example.thebullcamp.R

class ContentShActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_content_sh)

        //intent로 넘겨준 값을 받아 온다
        val getUrl = intent.getStringExtra("url")

        //받아온 값을 webView 로 로드하게 만든다
        val webView : WebView = findViewById(R.id.webView)
        webView.loadUrl(getUrl.toString())

    }

}