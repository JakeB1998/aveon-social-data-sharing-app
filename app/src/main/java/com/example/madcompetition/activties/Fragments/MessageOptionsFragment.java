package com.example.madcompetition.activties.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.example.madcompetition.BackEnd.messaging.system.Conversation;
import com.example.madcompetition.BackEnd.messaging.system.Message;
import com.example.madcompetition.BackEnd.Interfaces.FragmentDestroyedCallback;
import com.example.madcompetition.BackEnd.messaging.system.MessageHandler;
import com.example.madcompetition.R;
import com.example.madcompetition.activties.UIElements.Listeners.TouchDragListener;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MessageOptionsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.

 * create an instance of this fragment.
 */
public class MessageOptionsFragment extends Fragment  {

    private FragmentDestroyedCallback callback;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private float y;
    private float x;


    private final Fragment activity = this;

    private ImageButton backBtn;
    private ImageButton selfDestructBtn;
    private ImageButton markImportantBtn;

    private View dragViewAnchor;

    private LinearLayout fragmentView;
    private Message focusedMessage;

    private OnFragmentInteractionListener mListener;
    private Conversation conversation;

    private View focusedView;

    /**
     *
     * @param x
     * @param y
     * @param focusedView
     * @param message
     */
    public MessageOptionsFragment(Float x, Float y, View focusedView, Message message, FragmentDestroyedCallback callback, Conversation conversation) {
        // Required empty public constructor
        this.y = y;
        this.x = x;
        this.focusedView = focusedView;
        this.focusedMessage = message;
        this.callback = callback;
        this.conversation = conversation;

    }


    /**
     *
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    /**
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    float dX, dY;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        setFragmentView((LinearLayout)inflater.inflate(R.layout.fragment_message_options, container, false));
        fragmentView.setX(x);
        fragmentView.setY(y - 200);
        backBtn = fragmentView.findViewById(R.id.BackBtn);
        selfDestructBtn = fragmentView.findViewById(R.id.SelfDestructBtn);
        markImportantBtn = fragmentView.findViewById(R.id.MarkImportantBtn);
//        dragViewAnchor = fragmentView.findViewById(R.id.DragViewAnchor);
        fragmentView.setOnTouchListener(new TouchDragListener());



        removeBtns();
        handleClickEvents();

        addBtns();


        return getFragmentView();
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;

    }

    public LinearLayout getFragmentView() {
        return fragmentView;
    }

    public void setFragmentView(LinearLayout fragmentView) {
        this.fragmentView = fragmentView;
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    /**
     * Removes pre initlized buttons from layout
     * This is called in the beggining.
     * some view will be readded as needed
     */
    private void removeBtns()
    {
        fragmentView.removeView(markImportantBtn);
        fragmentView.removeView(selfDestructBtn);

    }

    private void addBtns()
    {
        fragmentView.addView(markImportantBtn);

        if (focusedMessage != null)
        {
            if (focusedMessage.isMessageSelfDestructed() == false)
            {
                fragmentView.addView(selfDestructBtn);

            }
        }


    }

    /**
     * This handles on click events for the fragment options buttons
     */
    private void handleClickEvents()
    {
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Log.i("Fragment", "Back button clciked");
                getActivity().getSupportFragmentManager().beginTransaction().remove(activity).commit();
                if (callback != null)
                {
                    callback.onCall();
                }
            }
        });

        markImportantBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if (focusedView != null)
                {
                    if (focusedMessage != null) {
                        if (1 == 1) {  // focusedMessage.isMessageSelfDestructed() == false
                            Log.i("Fragment", "Focused Message : " + focusedMessage.toString());
                            if (focusedMessage.isImportant())
                            {

                            }
                            else
                            {
                                focusedMessage.setImportant(true);
                            }
                           // MessageHandler.updateMessageObject(focusedMessage);
                            if (callback != null)
                            {
                                callback.onCall();
                            }
                        }
                        else
                        {
                            Log.i("Fragment", "Focused Message already self destructed");
                        }


                    }
                    else
                    {
                        Log.e("Fragment", "Focused message is null");
                    }

                }
                else
                {
                    Log.e("Fragment","Focused View is null");
                    // focused view is null
                }


            }
        });

        selfDestructBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if (focusedView != null)
                {
                    if (focusedMessage != null) {
                        if (1 == 1) {  // focusedMessage.isMessageSelfDestructed() == false
                            Log.i("Fragment", "Focused Message : " + focusedMessage.toString());
                            if (focusedMessage.isMessageSelfDestructed())
                            {
                                //focusedMessage.setMessageSelfDestructed(false);
                            }
                            else
                            {
                                focusedMessage.setMessageSelfDestructed(true);
                                MessageHandler.sendMessage(focusedMessage, conversation);
                            }



                           //MessageHandler.updateMessageObject(focusedMessage);
                            if (callback != null)
                            {
                                callback.onCall();
                            }
                        }
                        else
                        {
                            Log.i("Fragment", "Focused Message already self destructed");
                        }

                        getFragmentManager().beginTransaction().remove(MessageOptionsFragment.this).commit();


                    }
                    else
                    {
                        Log.e("Fragment", "Focused message is null");
                    }

                }
                else
                {
                    Log.e("Fragment","Focused View is null");
                    // focused view is null
                }
            }
        });

    }
}
