package com.potatoprogrammers.doit.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.potatoprogrammers.doit.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserActivityFragment extends Fragment {

    public UserActivityFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_user_activity, container, false);
    }

}
