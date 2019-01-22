package com.potatoprogrammers.doit.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.potatoprogrammers.doit.R;

public abstract class AbstractFragment extends Fragment {
    protected void swapFragment(Fragment fragment, Bundle args) {
        if(args!=null && !args.isEmpty()) {
            fragment.setArguments(args);
        }

        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content_frame, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}
