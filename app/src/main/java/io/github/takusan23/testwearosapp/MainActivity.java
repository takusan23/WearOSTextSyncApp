package io.github.takusan23.testwearosapp;

import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.android.gms.wearable.MessageClient;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.Wearable;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    String datapath = "/message";
    int num = 1;

    Button sendButton;
    EditText editText;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sendButton = findViewById(R.id.sendButton);
        editText = findViewById(R.id.editText);
        textView = findViewById(R.id.textview);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String message = editText.getText().toString();
                //テキストビュー
                textView.append(editText.getText().toString() + "\n");

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        //Node(接続先？)検索
                        Task<List<Node>> nodeListTask =
                                Wearable.getNodeClient(getApplicationContext()).getConnectedNodes();
                        try {
                            List<Node> nodes = Tasks.await(nodeListTask);
                            for (Node node : nodes) {
                                //sendMessage var1 は名前
                                //sendMessage var2 はメッセージ
                                Task<Integer> sendMessageTask =
                                        Wearable.getMessageClient(MainActivity.this).sendMessage(node.getId(), datapath, message.getBytes());

                                Integer result = Tasks.await(sendMessageTask);
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        }

                    }
                }).start();


            }
        });

    }
}

