package com.mephisto.personal;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mephisto.personal.Classes.BaseActivity;
import com.mephisto.personal.Classes.Message;
import com.mephisto.personal.Classes.MessageAdapter;

import com.theokanning.openai.OpenAiService;
import com.theokanning.openai.completion.chat.ChatCompletionChoice;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.completion.chat.ChatMessageRole;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ChatActivity extends BaseActivity {

    private EditText messageEditText;
    private List<Message> messages;
    private MessageAdapter messageAdapter;
    private List<ChatCompletionChoice> choices;

    final OpenAiService service = new OpenAiService("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");
    String chatGPTMessage = "";

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

            String timestamp = "User";
            Message[] message = {new Message(content, timestamp)};
            messages.add(message[0]);
            messageAdapter.notifyDataSetChanged();
            messageEditText.setText("");

            final List<ChatMessage> messagesChatGPT = new ArrayList<>();
            final ChatMessage systemMessage = new ChatMessage(ChatMessageRole.SYSTEM.value(), content);

            messagesChatGPT.add(systemMessage);
            ChatCompletionRequest chatcompletionRequest = ChatCompletionRequest
                    .builder()
                    .model("gpt-3.5-turbo")
                    .messages(messagesChatGPT)
                    .n(1)
                    .maxTokens(75)
                    .logitBias(new HashMap<>())
                    .build();

            //PROBAR CREACIÓN DE API PROPIA

            try {
                choices = service.createChatCompletion(chatcompletionRequest).getChoices();
            } catch (Exception e) {
                Log.d("GPTException", e.toString());
            }

            for (ChatCompletionChoice choice: choices
            ) {
                ChatMessage responseChatGPT = choice.getMessage();
                chatGPTMessage = responseChatGPT.getContent();
            }

            String userChatGPT = "ChatGPT";

            message = new Message[]{new Message(chatGPTMessage, userChatGPT)};
            messages.add(message[0]);
            messageAdapter.notifyDataSetChanged();

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