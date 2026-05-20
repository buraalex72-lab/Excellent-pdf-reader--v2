package com.pdfreader;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.widget.Button;
import android.widget.Toast;
import com.github.barteksc.pdfviewer.PDFView;
import java.util.Locale;

public class MainActivity extends Activity implements TextToSpeech.OnInitListener {
    PDFView pdfView; Button selectBtn, speakBtn; TextToSpeech tts; Uri pdfUri;
    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pdfView = findViewById(R.id.pdfView);
        selectBtn = findViewById(R.id.selectBtn);
        speakBtn = findViewById(R.id.speakBtn);
        tts = new TextToSpeech(this, this);
        selectBtn.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("application/pdf");
            startActivityForResult(intent, 100);
        });
        speakBtn.setOnClickListener(v -> {
            if (tts != null) tts.speak("PDF loaded. Tap Select PDF first.", TextToSpeech.QUEUE_FLUSH, null, null);
        });
    }
    @Override protected void onActivityResult(int req, int res, Intent data) {
        super.onActivityResult(req, res, data);
        if (req == 100 && res == RESULT_OK && data != null) {
            pdfUri = data.getData();
            pdfView.fromUri(pdfUri).load();
            Toast.makeText(this, "PDF Loaded", Toast.LENGTH_SHORT).show();
        }
    }
    @Override public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) tts.setLanguage(Locale.US);
    }
    @Override protected void onDestroy() {
        if (tts != null) tts.shutdown();
        super.onDestroy();
    }
}
