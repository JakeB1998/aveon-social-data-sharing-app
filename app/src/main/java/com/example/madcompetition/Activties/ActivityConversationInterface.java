package com.example.madcompetition.Activties;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import com.example.madcompetition.BackEnd.Account;
import com.example.madcompetition.BackEnd.AppManager;
import com.example.madcompetition.BackEnd.Conversation;
import com.example.madcompetition.BackEnd.FileMessage;
import com.example.madcompetition.BackEnd.KeyboardUtils;
import com.example.madcompetition.BackEnd.LocationMessage;
import com.example.madcompetition.BackEnd.PictureMessage;
import com.example.madcompetition.BackEnd.Message;
import com.example.madcompetition.R;
import com.example.madcompetition.BackEnd.TextMessage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class ActivityConversationInterface extends AppCompatActivity {

    private AppManager appManager;
    private Account currentAccount;
    private Account responderAccount;
    private Conversation currentConversation;
    private Message[] messages;

    private ImageButton sendMessageBtn;
    private ScrollView scrollViewParent;


    public final int HOST_SENDER_BACKROUND_COLOR = Color.rgb(0,100,0);
    public final int HOST_RECIEVER_BACKROUND_COLOR = Color.GRAY;

    private ConstraintLayout myView;
    private LinearLayout scrollView;
    private EditText textBox;


    private Message preppedMessage;


    private Account[] recipients;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        sendMessageBtn = null;
        textBox = null;


        appManager = AppManager.getInstance();
        currentAccount = appManager.getCurrentAccountLoggedIn();

        if (currentAccount == null)
        {
            currentAccount = new Account();
        }
        Log.i("Debug", Integer.toString(currentAccount.getAccountID()));




        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation_interface);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        myView = findViewById(R.id.main);
        scrollView = findViewById(R.id.ScrollViewContainer);
        sendMessageBtn = findViewById(R.id.SendBtn);
        textBox = findViewById(R.id.MessageContainerInput);
        scrollViewParent = findViewById(R.id.ScrollViewParent);


        final int zz = scrollViewParent.getLayoutParams().height;
        
        KeyboardUtils.addKeyboardToggleListener(this, new KeyboardUtils.SoftKeyboardToggleListener()
        {
            @Override
            public void onToggleSoftKeyboard(boolean isVisible)
            {
                Log.d("keyboard", "keyboard visible: "+isVisible);
                if (isVisible)
                {
                    sendMessageBtn.setVisibility(View.VISIBLE);
                    scrollViewParent.setLayoutParams( new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT,scrollViewParent.getLayoutParams().height / 2));

                    // textBox.setY(textBox.getY() - getResources().getDisplayMetrics().heightPixels / 2);
                    //textBox.setBackgroundColor(Color.RED);
                    Log.i("Click", "Message Input is focused");
                }
                else
                {
                    sendMessageBtn.setVisibility(View.INVISIBLE);
                    scrollViewParent.setLayoutParams( new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT,zz));
                    Log.i("Click", "Message Input lost focus");

                }
            }
        });

        Bundle extras = getIntent().getExtras();



        if (extras != null)
        {
            Log.i("MyDebug", "started");
            if (extras.containsKey("Conversation")) {
                currentConversation = (Conversation) extras.getSerializable("Conversation");
                recipients = currentConversation.getAccounts();
                ArrayList<Message> temp = currentConversation.getMessages();
                Log.i("MyDebug", "Has Convo Serilized");
                messages = new Message[temp.size()];
                for (int i = 0; i < messages.length; i++) {
                    messages[i] = temp.get(i);
                }

                if (recipients.length == 2) {
                    if (recipients[0].equals(appManager.getCurrentAccountLoggedIn())) {
                        currentAccount = recipients[0];
                        responderAccount = recipients[1];
                    } else if (recipients[1].equals(appManager.getCurrentAccountLoggedIn())) {
                        currentAccount = recipients[1];
                        responderAccount = recipients[0];

                    } else {
                        // conversation does not belong to this account
                    }
                } else if (recipients.length > 2) {
                    // group message
                } else {
                    //invalid convo
                }
            }
        }

        Log.i("ID", Integer.toString(currentConversation.getConversationID()));

        for (int i = 0; i < currentConversation.getMessages().size(); i++) {
            if (currentConversation.getMessages().get(i) instanceof TextMessage) {
                createTextMessageGui((TextMessage) currentConversation.getMessages().get(i));
            }
            else if (currentConversation.getMessages().get(i) instanceof LocationMessage)
            {
                createLocationMessage((LocationMessage) currentConversation.getMessages().get(i));

            }
            Log.i("MyDebug", "text created");
        }

        /*
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

         */
    }

    private boolean dataValidation()
    {
        return  true;
    }

    private void createGui(Message message)
    {
        //createTextMessageGui((TextMessage)message);
        //createPictureMessageGui((PictureMessage)message);
        //createLocationMessage((LocationMessage)message);
        //createFileMessage((FileMessage)message);

    }

    private void createTextMessageGui(TextMessage textNew)
    {
        final TextMessage text = textNew;
        boolean valid = false;
        if (text != null)
        {
            valid = true;

        }
        View view;

        LayoutInflater inflater = (LayoutInflater)   this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.layout_text_message,null);
        final ImageView backroundMessage = view.findViewById(R.id.BackroundMessage);
//
        final TextView myText = view.findViewById(R.id.textMessage);
       //backroundMessage.setBackground(getDrawable(R.mipmap.default_messagesbackround_icon_foreground));

        view.setPadding(0,50,0,0);
        int height;
        myText.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            public void onLayoutChange( View v, int left, int top, int right, int bottom,
                                       int oldLeft, int oldTop, int oldRight, int oldBottom)
            {
                final int width = right - left;
                final int height = bottom - top;
                final int offset = 35;
                Log.i("Mine", "Size: " + Integer.toString(width));
                //myText.setLayoutParams(new ViewGroup.LayoutParams(width + 100, 300));
                   if (text.getSender().equals(currentAccount))
                   {
                       myText.setX(getResources().getDisplayMetrics().widthPixels * 0.05f);
                       backroundMessage.setX(myText.getX() - offset);
                       backroundMessage.setLayoutParams(new FrameLayout.LayoutParams(width + 50, height));
                       backroundMessage.setBackgroundColor(HOST_SENDER_BACKROUND_COLOR);

                   }
                   else if (1 == 1)
                    {
                        int newWidth =  width + 100;
                        myText.setX(getResources().getDisplayMetrics().widthPixels - newWidth);
                        backroundMessage.setX(myText.getX() - offset);
                        backroundMessage.setLayoutParams(new FrameLayout.LayoutParams(width + 50, height));
                        backroundMessage.setBackgroundColor(HOST_RECIEVER_BACKROUND_COLOR);

                    }
                   else
                   {
                       // doesnt belong
                   }
            }
            });


        if (valid)
        {
            myText.setText(text.getMessage());
        }
        else {
            myText.setText("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
                    "aaaaaaaaaaaaaaa");
        }

        scrollView.addView(view);
    }

    private void createPictureMessageGui(final PictureMessage pictureMessage)
    {
        boolean valid = false;
        if (pictureMessage != null)
        {
            valid = true;

        }
        View view;

        LayoutInflater inflater = (LayoutInflater)   this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.picture_message,null);
        final ImageView backroundMessage = view.findViewById(R.id.BackroundMessage);
//
        final TextView myText = view.findViewById(R.id.textMessage);
        //backroundMessage.setBackground(getDrawable(R.mipmap.default_messagesbackround_icon_foreground));

        view.setPadding(0,50,0,0);
        int height;
        myText.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            public void onLayoutChange( View v, int left, int top, int right, int bottom,
                                        int oldLeft, int oldTop, int oldRight, int oldBottom)
            {
                final int width = right - left;
                final int height = bottom - top;
                final int offset = 35;
                Log.i("Mine", "Size: " + Integer.toString(width));
                //myText.setLayoutParams(new ViewGroup.LayoutParams(width + 100, 300));
                if (pictureMessage.getSender().equals(currentAccount))
                {
                    myText.setX(getResources().getDisplayMetrics().widthPixels * 0.05f);
                    backroundMessage.setX(myText.getX() - offset);
                    backroundMessage.setLayoutParams(new FrameLayout.LayoutParams(width + 50, height));
                    backroundMessage.setBackgroundColor(HOST_SENDER_BACKROUND_COLOR);

                }
                else if (1 == 1)
                {
                    int newWidth =  width + 100;
                    myText.setX(getResources().getDisplayMetrics().widthPixels - newWidth);
                    backroundMessage.setX(myText.getX() - offset);
                    backroundMessage.setLayoutParams(new FrameLayout.LayoutParams(width + 50, height));
                    backroundMessage.setBackgroundColor(HOST_RECIEVER_BACKROUND_COLOR);

                }
                else
                {
                    // doesnt belong
                }
            }
        });


        if (valid)
        {
            myText.setText(pictureMessage.getMessage());
        }
        else {
            myText.setText("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
                    "aaaaaaaaaaaaaaa");
        }



    }

    private void createLocationMessage(LocationMessage locationMessage)
    {
        final LocationMessage text = locationMessage;
        boolean valid = false;
        if (text != null)
        {
            valid = true;

        }
        View view;


        LayoutInflater inflater = (LayoutInflater)   this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.layout_text_message,null);

        view.setPadding(0, 50,0,0);

        final ImageView backroundMessage = view.findViewById(R.id.BackroundMessage);
//

        final TextView myText = view.findViewById(R.id.textMessage);
        //backroundMessage.setBackground(getDrawable(R.mipmap.default_messagesbackround_icon_foreground));

        backroundMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        int height;
        myText.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            public void onLayoutChange( View v, int left, int top, int right, int bottom,
                                        int oldLeft, int oldTop, int oldRight, int oldBottom)
            {
                final int width = right - left;
                final int height = bottom - top;
                final int offset = 35;
                Log.i("Mine", "Size: " + Integer.toString(width));

                //myText.setLayoutParams(new ViewGroup.LayoutParams(width + 100, 300));
                if (text.getSender().equals(currentAccount))
                {
                    myText.setX(getResources().getDisplayMetrics().widthPixels * 0.05f);
                    backroundMessage.setX(myText.getX() - offset);
                    backroundMessage.setLayoutParams(new FrameLayout.LayoutParams(width + 50, height));
                    backroundMessage.setBackgroundColor(HOST_SENDER_BACKROUND_COLOR);


                }
                else if (1 == 1)
                {
                    int newWidth =  width + 100;
                    myText.setX(getResources().getDisplayMetrics().widthPixels - newWidth);
                    backroundMessage.setX(myText.getX() - offset);
                    backroundMessage.setLayoutParams(new FrameLayout.LayoutParams(width + 50, height));
                    backroundMessage.setBackgroundColor(HOST_RECIEVER_BACKROUND_COLOR);

                }
                else
                {
                    // doesnt belong
                }
            }
        });


        if (valid)
        {
            myText.setText(locationMessage.toString());
        }
        else {
            myText.setText("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
                    "aaaaaaaaaaaaaaa");
        }
        //Log.i("Mine", Integer.toString( myText.getLayout().getHeight()));
        scrollView.addView(view);

    }

    private void createFileMessage(FileMessage message, boolean hostSent)
    {

    }

    private boolean isHostSent(Message message)
    {
        return true;
    }

    public void pickMessageType()
    {

    }


    public void sendMessageFromTextBox()
    {
        Message prepped = null;
        TextMessage temp = null;
        TextMessage t = new TextMessage("NEEEEEEERRRRRRRRRDDDDD", currentAccount, currentConversation.getAccounts());
        t.setDateSent(LocalDate.now());
        t.setTimeSent(LocalTime.now());
        if (textBox != null)
        {
            if (textBox.getText().length() > 0)
            {
                String text = textBox.getText().toString();
                temp = new TextMessage(text, currentAccount, currentConversation.getAccounts());
                preppedMessage = temp;
                if (sendMessage())
                {
                    // message was sent
                }
                else
                {

                }

            }

        }

    }

    public boolean sendMessage()
    {
        boolean send = false;
        if (preppedMessage != null)
        {
            preppedMessage = null; // message was sent
            send = true;

        }
        else
        {

        }

        return  send;
    }











    /*
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.i("Application", "Activity state restored");
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);
        Log.i("Application", "Activity state Saved");
    }

     */

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
