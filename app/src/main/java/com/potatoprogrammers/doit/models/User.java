package com.potatoprogrammers.doit.models;

import android.security.keystore.UserNotAuthenticatedException;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Getter
    @Setter
    private static User loggedInUser;
    public static final String COLLECTION = "users";
    private static final String TAG = "User";

    @NonNull
    private List<UserActivity> activities = new ArrayList<>();
}
