package com.potatoprogrammers.doit.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.potatoprogrammers.doit.R;
import com.potatoprogrammers.doit.models.User;

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

    protected void refreshFragment() {
        FragmentManager fm = getFragmentManager();
        if (fm != null) {
            fm.beginTransaction()
                    //.setReorderingAllowed(false) //in case of issues with Android version
                    .detach(this)
                    .attach(this)
                    .commit();
        }
    }

    protected void showShortToast(CharSequence cs) {
        Toast.makeText(getContext(), cs, Toast.LENGTH_SHORT).show();
    }

    protected void showShortToast(String str) {
        Toast.makeText(getContext(), str, Toast.LENGTH_SHORT).show();
    }

    protected void showShortToast(int resource) {
        if (getContext() != null) {
            Toast.makeText(getContext(), resource, Toast.LENGTH_SHORT).show();
        } else {
            Log.wtf("AbstractFragment", "No context found for the fragment");
        }
    }

    protected void updateUserInDatabase() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(User.COLLECTION)
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .set(User.getLoggedInUser(), SetOptions.merge())
                .addOnSuccessListener(aVoid -> showShortToast(R.string.sync_success))
                .addOnFailureListener(e -> showShortToast(e.getLocalizedMessage()));
    }
}
