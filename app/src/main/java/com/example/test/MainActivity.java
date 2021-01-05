package com.example.test;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.test.Classification.Result;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "ClassificationDemo";
    private Classification client;

    private TextView resultTextView;
    private EditText inputEditText;
    private Handler handler;
    private ScrollView scrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.v(TAG, "onCreate");

        client = new Classification(getApplicationContext());
        handler = new Handler();
        Button classifyButton = findViewById(R.id.button);
        classifyButton.setOnClickListener(
                (View v) -> {
                    classify(inputEditText.getText().toString());
                });
        resultTextView = findViewById(R.id.result_text_view);
        inputEditText = findViewById(R.id.input_text);
        scrollView = findViewById(R.id.scroll_view);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.v(TAG, "onStart");
        handler.post(
                () -> {
                    client.load();
                });
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.v(TAG, "onStop");
        handler.post(
                () -> {
                    client.unload();
                });
    }

    /** Send input text to TextClassificationClient and get the classify messages. */
    private void classify(final String text) {
        handler.post(
                () -> {
                    // Run text classification with TF Lite.
                    List<Result> results = client.classify(text);
                    String pos_text = client.posTag(text);
                    float[][] input_embed = client.tokenizeInputText(pos_text);

                    // Show classification result on screen
                    showResult(text, results, pos_text, input_embed);
                });
    }

    /** Show classification result on the screen. */
    private void showResult(final String inputText, final List<Result> results, final String pos_text, final float[][] input_embed) {
        // Run on UI thread as we'll updating our app UI
        runOnUiThread(
                () -> {
                    String textToShow = "Input: " + inputText + "\n" + "Pos_Input: " + pos_text + "\nOutput:\n";

                    for (int i = 0; i < results.size(); i++) {
                        Result result = results.get(i);
                        textToShow +=
                                String.format("    %s: %s\n", result.getTitle(), result.getConfidence());
                    }
                    textToShow += "---------\n";

                    for (int r=0; r<input_embed.length; r++) {   // 행 갯수
                        for (int c=0; c<input_embed[r].length; c++) {   // 행별 열 갯수
                            textToShow += input_embed[r][c] + " ";
                        }
                        textToShow += "\n";
                    }

                    // Append the result to the UI.
                    resultTextView.append(textToShow);

                    // Clear the input text.
                    inputEditText.getText().clear();

                    // Scroll to the bottom to show latest entry's classification result.
                    scrollView.post(() -> scrollView.fullScroll(View.FOCUS_DOWN));
                });
    }
}
