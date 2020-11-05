package com.example.madcompetition.activties.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.example.madcompetition.backend.Interfaces.FragmentDestroyedCallback;
import com.example.madcompetition.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ImageViewerFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * create an instance of this fragment.
 */
public class ImageViewerFragment extends Fragment implements Animation.AnimationListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ImageView imageView;
    private float x;
    private float y;

    private OnFragmentInteractionListener mListener;
    private FragmentDestroyedCallback callback;

    private FrameLayout myView;

    public ImageViewerFragment(ImageView image, float x, float y, FragmentDestroyedCallback callback) {
        // Required empty public constructor
        this.imageView = image;
        this.x =x;
        this.y = y;
        this.callback = callback;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    private boolean toggle;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final FrameLayout view = (FrameLayout)inflater.inflate(R.layout.fragment_image_viewer, container, false);
        myView = view;
        view.setX(x);
        view.setY(y);
        view.addView(imageView);

       final Animation hyperspaceJump = AnimationUtils.loadAnimation(getActivity().getBaseContext(), R.anim.expand_image);
        final Animation collapse = AnimationUtils.loadAnimation(getActivity().getBaseContext(), R.anim.collapse_image);




        imageView.setAnimation(hyperspaceJump);
        final Animation animation = imageView.getAnimation();



        animation.setAnimationListener(this);
        collapse.setAnimationListener(this);

        animation.start();



        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                imageView.clearAnimation();
                imageView.setAnimation(collapse);
                imageView.getAnimation().setAnimationListener(ImageViewerFragment.this);




            }
        });
        return view;
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
        callback.onFragmentDestroyed(this);
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

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {

        Log.i(ImageViewerFragment.class.getName(), "Toggle : " + toggle);
        if (toggle) {


            myView.removeView(imageView);
            getFragmentManager().beginTransaction()
                    .remove(ImageViewerFragment.this).commit();

        }
        else
        {

            toggle = true;
        }

    }

    @Override
    public void onAnimationRepeat(Animation animation) {
        Log.i(ImageViewerFragment.class.getName(), "Toggle : " + toggle);
        if (toggle) {

            getFragmentManager().beginTransaction()
                    .remove(ImageViewerFragment.this).commit();
        }
        else
        {
            toggle = true;
        }

    }

}
