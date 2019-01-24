package com.potatoprogrammers.doit.fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.potatoprogrammers.doit.R;
import com.potatoprogrammers.doit.models.User;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class PlanDayDetailsFragment extends AbstractFragment {
    private Date date;
    private EditText notes;

    public PlanDayDetailsFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_plan_day_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        date = (Date) getArguments().getSerializable("date");
        handleNotes();
    }

    @Override
    public void onResume(){
        super.onResume();
    }

    private void handleNotes() {
        notes = getView().findViewById(R.id.notesEditText);
        notes.setText(queryNotesFromDate(date));

        notes.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                User.getLoggedInUser().getNotes().put(getDateAsString(date), notes.getText().toString());
                this.updateUserInDatabase();
            }
        });
    }

    private String getDateAsString(Date date) {
        return new SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(date);
    }

    private String queryNotesFromDate(Date date) {
        return queryNotesFromDate(getDateAsString(date));
    }

    private String queryNotesFromDate(String date) {
        Map<String, String> userNotes = User.getLoggedInUser().getNotes();
        if (!userNotes.containsKey(date)) {
            userNotes.put(date, "");
        }
        return userNotes.get(date);
    }
}
