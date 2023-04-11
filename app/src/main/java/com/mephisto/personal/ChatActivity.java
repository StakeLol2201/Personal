package com.mephisto.personal;

import androidx.annotation.NonNull;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ChatActivity extends BaseActivity {

    private EditText messageEditText;
    private List<Message> messages;
    private MessageAdapter messageAdapter;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        messageEditText = findViewById(R.id.message_edit_text);
        Button sendButton = findViewById(R.id.send_button);
        ListView messagesListView = findViewById(R.id.messages_list_view);

        messages = new ArrayList<>();
        messageAdapter = new MessageAdapter(this, messages);
        messagesListView.setAdapter(messageAdapter);

        sendButton.setOnClickListener(view -> {

            String content = messageEditText.getText().toString();
            @SuppressLint("SimpleDateFormat") String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            Message message = new Message(content, timestamp);
            messages.add(message);
            messageAdapter.notifyDataSetChanged();
            messageEditText.setText("");

        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.disconnect) {
            signOut();
        }

        return super.onOptionsItemSelected(item);
    }

    private void signOut() {
        showProgressDialog();
        FirebaseAuth.getInstance().signOut();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        updateUI(user);
    }

    private void updateUI(FirebaseUser user) {
        if (user == null) {
            Intent launchIntent = new Intent(this, MainActivity.class);
            startActivity(launchIntent);
        }
    }

}