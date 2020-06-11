package com.example.madcompetition.activties;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import com.example.madcompetition.BackEnd.Interfaces.DataTransferCallback;
import com.example.madcompetition.BackEnd.Interfaces.ProgressDataTransferCallback;
import com.example.madcompetition.BackEnd.account.Account;
import com.example.madcompetition.BackEnd.AppManager;
import com.example.madcompetition.BackEnd.account.AccountInformation;
import com.example.madcompetition.BackEnd.messaging.system.Conversation;
import com.example.madcompetition.BackEnd.messaging.system.FileMessage;
import com.example.madcompetition.BackEnd.security.KeyContract;
import com.example.madcompetition.BackEnd.server.ftp.FileData;
import com.example.madcompetition.BackEnd.server.ftp.FtpServerConnectInterface;
import com.example.madcompetition.BackEnd.utils.Data;
import com.example.madcompetition.BackEnd.utils.FileExtensionUtils;
import com.example.madcompetition.BackEnd.utils.FileUtils;
import com.example.madcompetition.BackEnd.utils.KeyboardUtils;
import com.example.madcompetition.BackEnd.messaging.system.LocationMessage;
import com.example.madcompetition.BackEnd.messaging.system.MessageContraints;
import com.example.madcompetition.BackEnd.messaging.system.MessageHandler;
import com.example.madcompetition.BackEnd.messaging.system.PictureMessage;
import com.example.madcompetition.BackEnd.messaging.system.Message;
import com.example.madcompetition.BackEnd.utils.StringUtils;
import com.example.madcompetition.activties.Fragments.FragmentTags;
import com.example.madcompetition.activties.Fragments.ImageViewerFragment;
import com.example.madcompetition.activties.Fragments.MediaPickerFragment;
import com.example.madcompetition.activties.Fragments.MessageOptionsFragment;
import com.example.madcompetition.BackEnd.Interfaces.FragmentDestroyedCallback;
import com.example.madcompetition.BackEnd.messaging.system.MessageAction;
import com.example.madcompetition.BackEnd.messaging.system.MessageFrom;
import com.example.madcompetition.BackEnd.Interfaces.MyTimeUpdate;
import com.example.madcompetition.R;
import com.example.madcompetition.BackEnd.messaging.system.TextMessage;
import com.example.madcompetition.activties.Fragments.SwipeTorefreshFragment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ActivityConversationInterface extends AppCompatActivity implements FragmentDestroyedCallback, MyTimeUpdate, DataTransferCallback {

    private final int CHARACTER_MAX = 255;

    private final String LOG = "ConversationInterfaceActivity";
    private AppManager appManager;
    private Account currentAccount;
    private Conversation currentConversation;
    private Message[] messages;
    private boolean canSendFile;

    private HashMap<View,Message> uiMessagePairs;

    private ProgressBar progressBar;
    private ImageButton sendMessageBtn;
    private ImageButton addMediaBtn;
    private ImageButton mBackBtn;
    private ScrollView scrollViewParent;
    private ScrollView scrollViewC;

    private MediaPickerFragment fragment;


    private float touchX;
    private float touchY;


    public final int HOST_SENDER_BACKROUND_COLOR = Color.rgb(76,230,82);
    public final int HOST_RECIEVER_BACKROUND_COLOR = Color.GRAY;

    private FrameLayout myView;
    private LinearLayout scrollView;
    private FrameLayout externalMessageLayout;

    private EditText textBox;

    private TextView conversationRecipientName;
    private TextView externalMessagePreviewText;

    private ImageView externalMessagePreviewImage;

    private SwipeRefreshLayout swipeRefreshLayout;


    private Message preppedMessage;


    private AccountInformation[] recipients;

    /**
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("ConversationActivity", "On Create called");

        touchX = 0;
        touchY = 0;
        sendMessageBtn = null;
        addMediaBtn = null;
        textBox = null;

        MessageHandler.registerForUpdates(this);

       uiMessagePairs = new HashMap<>();

        appManager = AppManager.getInstance();
        currentAccount = appManager.getCurrentAccountLoggedIn();

        if (currentAccount == null)
        {
            //currentAccount = new Account();
        }
        Log.i("ActivityExtra", Integer.toString(currentAccount.getAccountID()));

        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_conversation_interface);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        myView = findViewById(R.id.main);
        scrollView = findViewById(R.id.ScrollViewContainer);
        externalMessageLayout = findViewById(R.id.ExternalMessagePreviewLayout);
        sendMessageBtn = findViewById(R.id.SendBtn);
        addMediaBtn = findViewById(R.id.AddMediaBtn);
        mBackBtn = findViewById(R.id.BackBtn);
        textBox = findViewById(R.id.MessageContainerInput);
        scrollViewParent = findViewById(R.id.ScrollViewParent);
        conversationRecipientName = findViewById(R.id.ConversationName);
        swipeRefreshLayout = findViewById(R.id.SwipeRefresh);

        swipeRefreshLayout.setEnabled(false);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadMessages(true);
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        if (appManager.getCurrentAccountLoggedIn().getSavedSettings().getAccessibilitySettings() != null) {
            if (appManager.getCurrentAccountLoggedIn().getSavedSettings().getAccessibilitySettings().isSwipeFeatureEnabled() == false) {
                swipeRefreshLayout.setEnabled(false);
            }
        }

        progressBar = findViewById(R.id.progressBar);
       progressBar.setVisibility(ProgressBar.INVISIBLE);

        externalMessagePreviewImage = findViewById(R.id.ExternalMessageImage);
        externalMessagePreviewText  = findViewById(R.id.ExternalMessagePreviewTitle);
        externalMessagePreviewImage.setOnClickListener(new ExternalImageClickHandle());

        mBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        scrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Fragment fragment = null;
                if ((fragment = getSupportFragmentManager().findFragmentByTag("MESSAGEOPTIONS")) != null)
                {
                    getSupportFragmentManager().beginTransaction().remove(fragment).commit();
                }
                return false;
            }
        });

        Bundle extras = getIntent().getExtras();
        if (extras != null)
        {
            Log.i("MyDebug", "started");
            if (extras.containsKey("Conversation")) {
                currentConversation = (Conversation) extras.getSerializable("Conversation"); // a serialized copy passed from last activity
                int index = -1;
                index = currentAccount.getConversations().indexOf(currentConversation); // finding non copy of current memory refrence inside the instance of current account
                currentConversation = currentAccount.getConversations().get(index); // getting a non coppy from the current instance of current account.
                // this allows changed effects of current conversation to update in the object current account
                recipients = currentConversation.getAccounts();
                ArrayList<Message> temp = currentConversation.getMessages();
                Log.i("MyDebug", "Has Convo Serilized");
                messages = new Message[temp.size()];
                for (int i = 0; i < messages.length; i++) {
                    messages[i] = temp.get(i);
                }


            }
        }


        if (currentConversation.getAccounts() != null)
        {
            String names = "";
            AccountInformation[] accounts = currentConversation.getAccounts();

            for (int i = 0; i < accounts.length; i++)
            {
                if (accounts[i].equals(AppManager.getInstance().getCurrentAccountLoggedIn().getAccountInformation()) == false) {

                    names += accounts[i].getTextRepresentation();

                }
            }
            conversationRecipientName.setText(names);
        }
        sendMessageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (preppedMessage != null)
                {
                    if (canSendFile)
                    {
                        Log.i(this.getClass().getName(), "Sending file");
                        Toast.makeText(ActivityConversationInterface.this,"Sending", Toast.LENGTH_LONG);
                        if (sendMessage(preppedMessage)) {
                            preppedMessage = null;
                            clearExternalGui();
                            canSendFile = false;
                            Log.i(this.getClass().getName(), "file sent");

                        }
                        else
                        {
                            Log.i(this.getClass().getName(), "file not sent");
                        }
                    }
                    else
                    {
                        Log.i(this.getClass().getName(), "must wait to send file");
                    }
                }
                else
                {
                   Log.i(this.getClass().getName(), "Prepped message is null");
                    sendMessageFromTextBox(); // checks to see if text is in textbox, if so then sends
                }
                loadMessages(true);
            }
        });
        this.loadMessages(false);
    }


    /**
     *
     * @param hasFocus
     */
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        int[] xx = new int[2];
        int[] qq = new int[2];
        scrollViewParent.getLocationOnScreen(xx);
        textBox.getLocationOnScreen(qq);

        int aa = qq[1] - xx[1];
        Log.i("Debug", Integer.toString(aa));
        scrollViewParent.getLayoutParams().height = aa;
        Log.i(this.getClass().getName(), "Parent Layout Hieght : " + myView.getLayoutParams().height);
        addMediaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String TAG = "MediaPicker";
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                if (fragmentManager.findFragmentByTag(TAG) == null) {
                    fragment = new MediaPickerFragment(ActivityConversationInterface.this
                    );

                    fragmentTransaction.add(R.id.main, fragment, TAG);
                    fragmentTransaction.commit();
//                    fragment.slide();
                    Log.i(ActivityConversationInterface.class.getName(), "doesnt exists");
                }
                else
                {
                    fragment.slide();
                    Log.i(ActivityConversationInterface.class.getName(), "Fragment exists");
                   // fragmentTransaction.replace(R.id.main, fragment, TAG);
                   // fragmentTransaction.commit();

                }

            }
        });
    }

    /**
     * Loades messages from the users saved conversation array and orders gui to be created for each message accordingly.
     * Also handles dynamically loading image bitampas without the thread blocking
     * @param interfaceCall
     */
    public void loadMessages(Boolean interfaceCall)
    {

        currentAccount = AppManager.getInstance().getCurrentAccountLoggedIn();

        Log.i(LOG,"Message Number size  : " + currentConversation.getMessages().size());
        Log.i(LOG,"Loading messages");
        scrollView.removeAllViews();
        MessageContraints cc = new MessageContraints(MessageAction.SelfDestruct, MessageFrom.TimeSent, ChronoUnit.MINUTES, 3);
        for (int i = 0; i < currentConversation.getMessages().size(); i++) {
            if (currentConversation.getMessages().get(i) instanceof TextMessage) {
                TextMessage text = (TextMessage) currentConversation.getMessages().get(i);

                if (interfaceCall == false) { // delete in final build
                    //text.setMessageSelfDestructed(false);
                }

                createTextMessageGui(text);
                //currentConversation.getMessages().get(i).setTimeSent(LocalTime.now().minusMinutes(8));
                cc.hasConstaintBeenMet(currentConversation.getMessages().get(i));
            }
            else if (currentConversation.getMessages().get(i) instanceof LocationMessage)
            {
                createLocationMessage((LocationMessage) currentConversation.getMessages().get(i));

            }
            else if(currentConversation.getMessages().get(i) instanceof PictureMessage)
            {
                Log.i(this.getClass().getName(), "created picture message gui");
                this.createPictureMessageGui((PictureMessage)currentConversation.getMessages().get(i));
            }
            else if (currentConversation.getMessages().get(i) instanceof FileMessage)
            {
                Log.i(this.getClass().getName(), "created picture message gui");
                this.createFileMessage((FileMessage) currentConversation.getMessages().get(i));

            }
            else
            {
                Log.i(this.getClass().getName(), "Message not created GUI");
            }

        }
    }

    /**
     *Create the gui related to a textual message dynamically
     * @param textNew
     */
    private void createTextMessageGui(TextMessage textNew)
    {
        final TextMessage text = textNew;
        boolean valid = false;
        if (text != null)
        {
            valid = true;

        }

        LayoutInflater inflater = (LayoutInflater)   this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final FrameLayout view = (FrameLayout)inflater.inflate(R.layout.layout_text_message,myView, false);
        final TextView myText = view.findViewById(R.id.textMessage);
            repositionMessageGui(textNew,myText,view);

        if (text.isMessageSelfDestructed() == false)
        {
            myText.setText(text.getMessage());
        }
        else if (text.isMessageSelfDestructed())
        {
            myText.setText("THIS MESSAGE HAS SELF DESTRUCTED");
        }
        else
        {
            myText.setText("BLANK");

        }
        scrollView.addView(view); // addes the cretaed view to scroll view container
        this.handleEvents(myText); // handles the events accossiated with the gui
        this.storeConnectedHashPair(myText, textNew); //pairs each gui created with the index inside of the conversation array
    }


    /**
     * Creates the gui related to a picture message dynamically
     * @param pictureMessage
     */
    private void createPictureMessageGui(final PictureMessage pictureMessage)
    {
        final View view;
        boolean valid = false;
        if (pictureMessage != null) {
            valid = true;


            LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.picture_message, myView, false);

//
            final ImageView contentImage = view.findViewById(R.id.PictureMessage);
            contentImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (pictureMessage.getSender().equals(AppManager.getInstance().getCurrentAccountLoggedIn().getAccountInformation()) == false) {
                        if (pictureMessage.isDownloaded() == false) {
                            pictureMessage.setDownloaded(true);
                            AppManager.getInstance().getCurrentAccountLoggedIn().saveAccount(ActivityConversationInterface.this);
                            FtpServerConnectInterface.getInstance().startDownloadInteraction(pictureMessage);
                            Log.i(this.getClass().getName(), "Image Clicked");

                        }
                        else
                        {

                                pictureMessage.setLoaded(true);
                                loadFileelsewhere(pictureMessage.getMessageFileData().getFile(), FileUtils.getMimeType(pictureMessage.getMessageFileData().getFile().toPath().toString()));

                        }
                    }
                    else
                    {

                            loadFileelsewhere(pictureMessage.getMessageFileData().getFile(), FileUtils.getMimeType(pictureMessage.getMessageFileData().getFile().toPath().toString()));

                    }
                }
            });
            //backroundMessage.setBackground(getDrawable(R.mipmap.default_messagesbackround_icon_foreground));

            Log.i(this.getClass().getName(), FileUtils.getMimeType(pictureMessage.getMessageFileData().getFile().toPath().toString()));
            if (pictureMessage.isMessageSelfDestruct())
            {
                Log.i(this.getClass().getName(), "Message is self =destructed");
               // contentImage.setImageBitmap(null); // replace with self destruct image
            }
            else
            {

                if (pictureMessage.getMessageFileData().getFileLength() == pictureMessage.getMessageFileData().getFile().length())
                {
                   // final InputStream imageStream =ActivityConversationInterface.this.getContentResolver().openInputStream();
                    if (pictureMessage.getPictureMessage()== null) {
                        Log.i(this.getClass().getName(), "Loading image from file");
                        Log.i(this.getClass().getName(), "File path : " + pictureMessage.getMessageFileData().getFilePath());
                    }
                    else
                    {
                        Log.i(this.getClass().getName(), "File path : " + pictureMessage.getMessageFileData().getFile().getPath());
                        Log.i(this.getClass().getName(), "Bitmap size : " + pictureMessage.getPictureMessage().getByteCount());
                    }
                }
                else
                {
                   // Log.i(this.getClass().getName(),"Size 1 : " +  pictureMessage.getMessageFileData().getFilePath().length() + "\n Size 2 : " + pictureMessage.getMessageFileData().getFile().length());
                    Log.i(this.getClass().getName(), "File created before download" + "Recorded Length : " + pictureMessage.getMessageFileData().getFileLength()
                            + "Actual file length : " + pictureMessage.getMessageFileData().getFile().length());
                    Log.i(this.getClass().getName(), "new file path : " + pictureMessage.getMessageFileData().getFile().toPath());
                }
                    this.loadBitmapFromFile(pictureMessage, contentImage);
            }

            repositionMessageGui(pictureMessage,contentImage,view);
            scrollView.addView(view);
            this.handleEvents(contentImage);
            this.storeConnectedHashPair(contentImage, pictureMessage);
        }
    }

    /**
     * 0
     * @param locationMessage
     */
    private void createLocationMessage(final LocationMessage locationMessage)
    {
        final LocationMessage text = locationMessage;
        boolean valid = false;
        if (text != null)
        {
            valid = true;

        }
        final View view;


        LayoutInflater inflater = (LayoutInflater)   this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.layout_text_message,myView, false);



        view.setPadding(0, 50,0,0);

        final TextView myText = view.findViewById(R.id.textMessage);
        myText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.i(this.getClass().getName(), "Location message clicked");

// Or map
                Uri location = Uri.parse("http://maps.google.com/maps?" + "daddr=" +Double.toString( locationMessage.getLatitude()) + "," + Double.toString(locationMessage.getLongitude()));
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, location);
                Intent chooser = Intent.createChooser(mapIntent,"open with");//maps code
                startActivity(chooser);
            }
        });

        myText.setTextColor(Color.BLUE);

        repositionMessageGui(locationMessage,myText,view);


        if (valid && text.isMessageSelfDestructed() == false)
        {
            myText.setText(text.getMessage());
        }
        else if (text.isMessageSelfDestructed())
        {
            myText.setText("THIS MESSAGE HAS SELF DESTRUCTED");
        }
        else
        {
            myText.setError("Something is wrong");

        }
        //Log.i("Mine", Integer.toString( myText.getLayout().getHeight()));
        scrollView.addView(view);

        this.handleEvents(myText);
        this.storeConnectedHashPair(myText, locationMessage);

    }

    /**
     *
     * @param message
     */
    private void createFileMessage(final FileMessage message)
    {
        Log.i(this.getClass().getName(), "File message created");
        final View view;

        boolean valid = false;
        if (message != null) {
            valid = true;

            LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.picture_message, myView,false);
            final ImageView contentImage = view.findViewById(R.id.PictureMessage);
            final TextView fileText = view.findViewById(R.id.FileNameText);
            fileText.setText(message.getMessageFileData().getFilePath());
            contentImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (1 == 1) {   //message.getSender().equals(AppManager.getInstance().getCurrentAccountLoggedIn().getAccountInformation()) == false
                        if (message.isDownloaded() == false) {
                            message.setDownloaded(true);
                            AppManager.getInstance().getCurrentAccountLoggedIn().saveAccount(ActivityConversationInterface.this);
                            FtpServerConnectInterface.getInstance().startDownloadInteraction(message);
                            Log.i(this.getClass().getName(), "file Clicked");

                        }
                        else {

                            loadFileelsewhere(message.getMessageFileData().getFile(), FileUtils.getMimeType(message.getMessageFileData().getFile().toPath().toString()));
                        }
                    }
                    else {
                        loadFileelsewhere(message.getMessageFileData().getFile(), FileUtils.getMimeType(message.getMessageFileData().getFile().toPath().toString()));

                    }
                }
            });
            //backroundMessage.setBackground(getDrawable(R.mipmap.default_messagesbackround_icon_foreground));

            if (message.isMessageSelfDestruct())
            {
                contentImage.setImageBitmap(null); // replace with self destruct image
            }
            else {
                Drawable drawable = FileExtensionUtils.retrieveFileDrawable(message.getMessageFileData().getFile().getPath(), this); // file gui
                contentImage.setImageDrawable(drawable);
            }
            repositionMessageGui(message,contentImage,view);
            scrollView.addView(view);
            this.handleEvents(contentImage);
            this.storeConnectedHashPair(contentImage, message);
        }



    }

    /**
     * Sends a textual message from the textbox in this activity
     */
    public void sendMessageFromTextBox()
    {
        Message prepped = null;
        TextMessage temp = null;
        TextMessage t = new TextMessage("NEEEEEEERRRRRRRRRDDDDD", currentAccount.getAccountInformation(), currentConversation.getAccounts());
        t.setDateSent(LocalDate.now());
        t.setTimeSent(LocalTime.now());
        if (textBox != null)
        {
            if (textBox.getText().length() > 0)
            {
                if (textBox.getText().length() <= CHARACTER_MAX) {
                    String text = textBox.getText().toString();
                    temp = new TextMessage(text, currentAccount.getAccountInformation(), currentConversation.getAccounts());
                    t.setDateSent(LocalDate.now());
                    t.setTimeSent(LocalTime.now());


                    textBox.setText(null);
                    if (sendMessage(temp))
                    {
                        this.loadMessages(true);
                        Log.i(this.getClass().getName(), "Message was sent succesfully");
                       // scrollView.scrollTo(0, scrollView.getBottom());


                    }

                     else
                    {
                        Log.i(this.getClass().getName(), "Message sent failure");

                    }
                }
                else
                {
                    // exceeds character max
                    textBox.setError("The max character count is " + CHARACTER_MAX);
                }

            }
            else
            {
                // emty
                textBox.setError("Can not send emtry message");
            }

        }

    }

    /**
     * Sends message
     * Called from send message from textbox method
     * @return
     */
    public boolean sendMessage(Message message)
    {
        boolean send = false;
        if (message != null)
        {
            message.setConversationId(currentConversation.getConversationID());
            message.setRecipeints(currentConversation.getAccounts());
            FtpServerConnectInterface.getInstance().registerOnProgressUpdates(new TransferCallbacks());
            if (MessageHandler.sendMessage(message, currentConversation))
            {
                if (message instanceof FileMessage)
                {
                    this.createFileMessage((FileMessage)message);

                }
                else if (message instanceof PictureMessage)
                {
                    this.createPictureMessageGui((PictureMessage)message);
                }
                Log.i(this.getClass().getName(), "Message id : " + message.getConversationId());
                message = null; // message was sent

                this.loadMessages(true);
                KeyboardUtils.hideKeyboard(this);
                return true;

            }
            else
            {
               Log.e(this.getClass().getName(), "Message handler failed to send message");
            }

        }
        else
        {
            Log.e(this.getClass().getName(), "prepped message is null, line 737");
        }

        return  false;
    }



    /**
     *
     * @param view
     * @param message
     */
    private void storeConnectedHashPair(View view, Message message)
    {
        uiMessagePairs.put(view,message);
    }


    /**
     *
     * @param view
     */
    private void handleEvents(final View view)
    {
        view.setClickable(true);


        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {


                final String TAG = "MESSAGEOPTIONS";
                Log.i("Input", "Long click detected and cakked");
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();


                int[] x = new int[2];
                v.getLocationOnScreen(x);
                touchX = x[0];
                touchY = x[1];
                Message message = null;
                Log.i("Hashmap", "Size : " + uiMessagePairs.size());
                if (uiMessagePairs.containsKey(v)) {
                  message =   uiMessagePairs.get(v);
                    Log.i("Hashmap", "Contains key");
                }

                MessageOptionsFragment fragment = new MessageOptionsFragment((float) x[0], (float) x[1], v, message, ActivityConversationInterface.this,currentConversation);
                if (fragmentManager.findFragmentByTag(TAG) == null) {
                    fragmentTransaction.add(R.id.main, fragment, TAG);
                    fragmentTransaction.commit();
                }
                else
                {
                    fragmentTransaction.replace(R.id.main, fragment, TAG);
                    fragmentTransaction.commit();
                }


                // fragmentTransaction.hide(fragment);
                Log.i("Fragment", "Fragment has been commited");
                return true;
            }
        });
    }

    @Override
    public void onCall() {
        Log.i("Interface", "On call called");


    }

    public void onFragmentDestroyed(Fragment fragment)
    {
        if (fragment.getClass().getName().equals(ImageViewerFragment.class.getName()))
        {
            externalMessageLayout.addView(externalMessagePreviewImage);
            externalMessagePreviewImage.setOnClickListener(new ExternalImageClickHandle());
        }
    }
    public void filterExecute()
    {

    }

    /**
     *
     * @param filePath
     */
    public void handleExternalMessageGui(String filePath)
    {
        if (externalMessageLayout != null) {
            externalMessageLayout.setVisibility(View.VISIBLE);


            if (externalMessagePreviewText != null) {
                externalMessagePreviewText.setText(filePath);

            }
            else {
                Log.e(this.getClass().getName(), "External message Preview Text is null");
            }

            if (externalMessagePreviewImage != null)
            {
                Drawable drawable = FileExtensionUtils.retrieveFileDrawable(filePath, this);
                externalMessagePreviewImage.setImageDrawable(drawable);
            }
            else
            {
                Log.e(this.getClass().getName(), "External message Preview Image is null");
            }
        }
        else {
            Log.e(this.getClass().getName(), "External message layout is null");
        }

    }

    /**
     * Clears the external gui thaytt represents both pictures and files
     */
    public void clearExternalGui()
    {
        externalMessageLayout.setVisibility(View.INVISIBLE);
        externalMessagePreviewText.setText(null);
        externalMessagePreviewImage.setImageDrawable(null);
    }

    /**
     * This is mainly used to pass prepped message objects for both file and picture messages from a fragment.
     * This is its callback method to transfer data
     * @param data
     */
    @Override
    public void TransferData(Data data) {

        Log.i(this.getClass().getName(), "Data recieved of length : " + data.length());
        if (data.contains(KeyContract.MESSAGE_KEY))
        {
             preppedMessage = ((Message)data.getObject(KeyContract.MESSAGE_KEY));
             preppedMessage.setRecipeints(currentConversation.getAccounts());
            Log.i(this.getClass().getName(), "MEssage sent from data transfter");

            if (preppedMessage instanceof FileMessage)
            {
                FileMessage fileMessage = (FileMessage)data.getObject(KeyContract.MESSAGE_KEY);
                this.handleExternalMessageGui(fileMessage.getMessageFileData().getFile().getAbsolutePath());
                canSendFile = true;



            }
            else if (preppedMessage instanceof  PictureMessage)
            {
                PictureMessage pictureMessage = (PictureMessage) data.getObject(KeyContract.MESSAGE_KEY);
                pictureMessage.prepMessage(this, progressBar, new TransferCallbacks());


            }
            else if (preppedMessage instanceof LocationMessage)
            {
                LocationMessage locationMessage = (LocationMessage)data.getObject(KeyContract.MESSAGE_KEY);
                locationMessage.setSender(AppManager.getInstance().getCurrentAccountLoggedIn().getAccountInformation());
                locationMessage.setRecipeints(currentConversation.getAccounts());
                sendMessage(locationMessage);
            }

        }
    }

    @Override
    public void update() {
         this.loadMessages(true);


    }



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


    public class ExternalImageClickHandle implements View.OnClickListener
    {
        public void onClick(View view)
        {

            if (externalMessagePreviewImage.getParent() == null)
            {
                externalMessageLayout.addView(externalMessagePreviewImage);
            }
            float x = externalMessagePreviewImage.getX();
            float y = externalMessagePreviewImage.getY();
            externalMessageLayout.removeView(externalMessagePreviewImage);

            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            ImageViewerFragment fragment = new ImageViewerFragment(externalMessagePreviewImage,
                    x,y, ActivityConversationInterface.this);
            fragmentTransaction.add(R.id.main, fragment);
            fragmentTransaction.commit();


        }

    }


    /**
     * Repositions message ui in accordance to who sends it, Host vs non host
     * @param view
     * @param backroundMessage
     */
    private void repositionMessageGui(Message message,final View view, final View backroundMessage)
    {

        final int offset = 50;
        if (message.getSender().equals(AppManager.getInstance().getCurrentAccountLoggedIn().getAccountInformation()))
        {

            backroundMessage.setX(getResources().getDisplayMetrics().widthPixels * 0.4f);
            if (view instanceof TextView) {
                backroundMessage.setBackgroundTintList((ColorStateList.valueOf(HOST_SENDER_BACKROUND_COLOR)));
            }



        }
        else
        {

            backroundMessage.setX(getResources().getDisplayMetrics().widthPixels * 0.05f);
            //backroundMessage.setLayoutParams(new FrameLayout.LayoutParams(width + 50, ViewGroup.LayoutParams.WRAP_CONTENT));
            if (view instanceof TextView) {
                backroundMessage.setBackgroundTintList((ColorStateList.valueOf(HOST_RECIEVER_BACKROUND_COLOR)));
            }



        }

        ViewTreeObserver vto = backroundMessage.getViewTreeObserver();
        vto.addOnGlobalLayoutListener (new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                float xy =  scrollViewParent.getY() - sendMessageBtn.getY();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    backroundMessage.getViewTreeObserver()
                            .removeOnGlobalLayoutListener(this);
                } else {
                    backroundMessage.getViewTreeObserver()
                            .removeGlobalOnLayoutListener(this);
                }
                int width  = backroundMessage.getWidth();
                int height = backroundMessage.getHeight();

                LinearLayout.LayoutParams params = null;
                if (view instanceof ImageView)
                {
                    params = (LinearLayout.LayoutParams)backroundMessage.getLayoutParams();
                }
                else {
                params = new LinearLayout.LayoutParams(width + 50, ViewGroup.LayoutParams.WRAP_CONTENT);


                    backroundMessage.setPadding(25,25,25,0);

                }
                params.setMargins(0,25,0,0);

                backroundMessage.setLayoutParams(new LinearLayout.LayoutParams(params));


                //view.setTextAlignment(View.TEXT_ALIGNMENT_GRAVITY);


            }
        });


    }

    /**
     * Inner class
     */
    private class SwipeRefreshFeature implements SwipeRefreshLayout.OnRefreshListener
    {
        private SwipeTorefreshFragment mSwipeRefresh;

        public void addSwipeFeature ()
        {

            FragmentManager fragmentManager = getSupportFragmentManager();

            if (fragmentManager.findFragmentByTag(FragmentTags.SWIPE_UPDATE_FRAGMENT) == null) {
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                SwipeTorefreshFragment fragment = new SwipeTorefreshFragment();
                fragmentTransaction.add(R.id.main, fragment);
                fragmentTransaction.commit();

                fragment.registerOnRefreshListener(this);

                mSwipeRefresh = fragment;
            }
            else
            {
                Log.e(this.getClass().getName(), "Attempted to open swipe to refresh fragment when one is already on");
            }

        }


        @Override
        public void onRefresh ()
        {

            loadMessages(true);
            Log.i(this.getClass().getName(), "Swipe called back overridden here");
        }
    }

    public interface MessageReadyCallback {

        public void MessageReadyCallback(FileData data);

    }


    public void loadBitmapFromFile(final PictureMessage pictureMessage, final ImageView contentImage)
    {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        final Bitmap selectedImage = BitmapFactory.decodeFile(pictureMessage.getMessageFileData().getFile().getPath());
                        pictureMessage.setPictureMessage(selectedImage);
                        contentImage.setImageBitmap(pictureMessage.getPictureMessage());
                    }
                });

            }
        });
        thread.start();
        Log.i(this.getClass().getName(), "Thread for loading bitmap started");

    }


    public void loadFileelsewhere(File file, String type)
    {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);

        PackageManager packageManager = getPackageManager();
        List<ResolveInfo> activities = packageManager.queryIntentActivities(intent, 0);
        Intent chooser = Intent.createChooser(intent, "Share");


        Uri apkURI = FileProvider.getUriForFile(
                ActivityConversationInterface.this,
                ActivityConversationInterface.this.getApplicationContext()
                        .getPackageName() + ".fileprovider", file);


        intent.setDataAndType(apkURI, type);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        boolean isIntentSafe = activities.size() > 0;
        if (isIntentSafe) {
            startActivity(chooser);
        }

    }



    private class TransferCallbacks implements ProgressDataTransferCallback, MessageReadyCallback
    {

        @Override
        public void uploadProgressUpdate(int progress) {

            Log.i(this.getClass().getName(), "Upload Progress update : " + progress);
        }

        @Override
        public void downloadProgressUpdate(int progress) {
            Log.i(this.getClass().getName(), "download Progress update : " + progress);


        }



        @Override
        public void MessageReadyCallback(FileData data) {

            handleExternalMessageGui(data.getFilePath());
            canSendFile = true;
        }






    }





}
