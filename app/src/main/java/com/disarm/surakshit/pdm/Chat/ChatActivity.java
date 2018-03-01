package com.disarm.surakshit.pdm.Chat;

import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.disarm.surakshit.pdm.Chat.Holders.IncomingAudioHolders;
import com.disarm.surakshit.pdm.Chat.Holders.IncomingVideoHolders;
import com.disarm.surakshit.pdm.Chat.Holders.OutgoingAudioHolders;
import com.disarm.surakshit.pdm.Chat.Holders.OutgoingVideoHolders;
import com.disarm.surakshit.pdm.R;
import com.disarm.surakshit.pdm.Util.Params;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.github.javiersantos.materialstyleddialogs.enums.Duration;
import com.github.javiersantos.materialstyleddialogs.enums.Style;
import com.squareup.picasso.Picasso;
import com.stfalcon.chatkit.commons.ImageLoader;
import com.stfalcon.chatkit.messages.MessageHolders;
import com.stfalcon.chatkit.messages.MessageInput;
import com.stfalcon.chatkit.messages.MessagesList;
import com.stfalcon.chatkit.messages.MessagesListAdapter;

import java.io.File;

public class ChatActivity extends AppCompatActivity implements MessageHolders.ContentChecker<Message> {
    MessagesList messagesList;
    MessageInput messageInput;
    ImageLoader load;
    String number;
    MessagesListAdapter<Message> messagesListAdapter;
    private final byte CONTENT_AUDIO=1,CONTENT_VIDEO=2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        messagesList = (MessagesList) findViewById(R.id.messagesList);
        messageInput = (MessageInput) findViewById(R.id.input);

        load = new ImageLoader() {
            @Override
            public void loadImage(ImageView imageView, String url) {
                File im = Environment.getExternalStoragePublicDirectory(url);
                if(im.exists()) {
                    Picasso.with(ChatActivity.this).load(im).resize(800,1000).centerCrop().into(imageView);
                }
            }
        };
        MessageHolders holders = new MessageHolders();
        holders.registerContentType(CONTENT_AUDIO,
                IncomingAudioHolders.class,R.layout.chat_incoming_audio,
                OutgoingAudioHolders.class,R.layout.chat_outgoing_audio,
                this);
        holders.registerContentType(CONTENT_VIDEO,
                IncomingVideoHolders.class,R.layout.chat_incoming_video,
                OutgoingVideoHolders.class,R.layout.chat_outgoing_video,
                this);
        number = getIntent().getStringExtra("number");
        messagesListAdapter = new MessagesListAdapter<Message>(Params.SOURCE_PHONE_NO,holders,load);
        messagesListAdapter.setOnMessageClickListener(new MessagesListAdapter.OnMessageClickListener<Message>() {
            @Override
            public void onMessageClick(Message message) {

                //Define what to do with msg touch events
                //Video touch and Audio touch are already defined in the holders

                if(message.isImage()){
                    Intent i = new Intent(ChatActivity.this, ImageViewActivity.class);
                    i.putExtra("url",message.getImageUrl());
                    startActivity(i);
                }
            }
        });
        messageInput.setAttachmentsListener(new MessageInput.AttachmentsListener() {
            @Override
            public void onAddAttachments() {
                View view = getLayoutInflater().inflate(R.layout.dialog_attachment,null);
                MaterialStyledDialog materialDialog = new MaterialStyledDialog.Builder(ChatActivity.this)
                        .setTitle(R.string.attachment)
                        .setCustomView(view,10,20,10,20)
                        .withDialogAnimation(true, Duration.FAST)
                        .setCancelable(true)
                        .setStyle(Style.HEADER_WITH_TITLE)
                        .withDarkerOverlay(true)
                        .build();

                Window window = materialDialog.getWindow();
                WindowManager.LayoutParams wlp = window.getAttributes();
                wlp.gravity = Gravity.BOTTOM;
                wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
                window.setAttributes(wlp);
                materialDialog.show();
            }
        });

        messagesList.setAdapter(messagesListAdapter);
        Author me = new Author(Params.SOURCE_PHONE_NO,"Naman");
        Author other = new Author(number,"Anuj");
        Message msg = new Message("1",me,"text");
        msg.setText("Hi there!!!");
        Message msg2 = new Message("2",me,"image");
        msg2.setImageurl("test.jpg");
        Message msg3 = new Message("3",other,"text");
        msg3.setText("This looks yum ");
        Message msg4 = new Message("4",me,"text");
        msg4.setText("Yeah!!! Come over my place to have it");
        Message msg5 = new Message("5",other,"video");
        msg5.setUrl("test.mp4");
        Message msg6 = new Message("6",me,"text");
        msg6.setText("come fast...");
        Message msg7 = new Message("7",other,"audio");
        msg7.setUrl("test.mp3");
        Message msg8 = new Message("8",other,"map");
        msg8.setImageurl("map.png");


        addMessage(msg);
        addMessage(msg2);
        addMessage(msg3);
        addMessage(msg4);
        addMessage(msg5);
        addMessage(msg6);
        addMessage(msg7);
        addMessage(msg8);


    }

    @Override
    public boolean hasContentFor(Message message, byte type) {
        if(type == CONTENT_AUDIO){
            return message.isAudio();
        }
        if(type == CONTENT_VIDEO){
            return message.isVideo();
        }
        return false;
    }

    private void addMessage(Message msg){
        messagesListAdapter.addToStart(msg,true);
    }
}
