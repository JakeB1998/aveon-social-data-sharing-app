package com.example.madcompetition.activties.fragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.madcompetition.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SwipeTorefreshFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {



    private SwipeRefreshLayout mSwipeRefreshLayout;

    private SwipeRefreshLayout.OnRefreshListener mListener;
    private ListView mListView;

    public SwipeTorefreshFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        mSwipeRefreshLayout = (SwipeRefreshLayout) inflater.inflate(R.layout.fragment_swipe_torefresh, container, false);

        if (mListener != null) {
            mSwipeRefreshLayout.setOnRefreshListener(mListener);
        }




        // Retrieve the ListView
        mListView = mSwipeRefreshLayout.findViewById(android.R.id.list);

        return  mSwipeRefreshLayout;
    }


    public SwipeRefreshLayout getSwipeRefreshLayout()
    {
        return mSwipeRefreshLayout;
    }


    public void registerOnRefreshListener(SwipeRefreshLayout.OnRefreshListener listener)
    {
        this.mListener = listener;

        if (mSwipeRefreshLayout != null)
        {
            mSwipeRefreshLayout.setOnRefreshListener(mListener);
        }

    }

    public void refreshFinished()
    {
        if (mSwipeRefreshLayout != null)
        {
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }






    @Override
    public void onRefresh() {
        Log.i(this.getClass().getName(), "App swiped to refresh");
      mSwipeRefreshLayout.setRefreshing(false);
    }
}
