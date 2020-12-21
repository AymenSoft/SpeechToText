package com.app.speechtotext;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

import static android.Manifest.permission.RECORD_AUDIO;

public class MainActivity extends AppCompatActivity {

    TextView tvText;
    Button btnVoice;

    SpeechRecognizer speechRecognizer;

    boolean isListening;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        isListening = false;

        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);

        tvText = findViewById(R.id.tv_text);
        btnVoice = findViewById(R.id.btn_voice);

        final Intent speechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle params) {
                Log.e("onReadyForSpeech", "yes");
            }

            @Override
            public void onBeginningOfSpeech() {
                Log.e("onBeginningOfSpeech", "yes");
            }

            @Override
            public void onRmsChanged(float rmsdB) {
                Log.e("onRmsChanged", "yes");
            }

            @Override
            public void onBufferReceived(byte[] buffer) {
                Log.e("onBufferReceived", "yes");
            }

            @Override
            public void onEndOfSpeech() {
                Log.e("onEndOfSpeech", "yes");
            }

            @Override
            public void onError(int error) {
                Log.e("onError", error+"");
            }

            @Override
            public void onResults(Bundle results) {
                ArrayList<String> data = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                tvText.setText(data.get(0));
            }

            @Override
            public void onPartialResults(Bundle partialResults) {
                Log.e("onPartialResults", "yes");
            }

            @Override
            public void onEvent(int eventType, Bundle params) {
                Log.e("onEvent", "yes");
            }
        });

        btnVoice.setOnClickListener(v -> {
            btnVoice.setText(isListening?"start":"stop");
            if (isListening) {
                speechRecognizer.stopListening();
            }else {
                speechRecognizer.startListening(speechRecognizerIntent);
            }
            isListening = !isListening;
        });

        if(ContextCompat.checkSelfPermission(this,RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED){
            checkPermission();
        }

    }

    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions(this,new String[]{RECORD_AUDIO},1);
        }
    }


    @Override
    protected void onDestroy() {
        speechRecognizer.destroy();
        super.onDestroy();
    }
}