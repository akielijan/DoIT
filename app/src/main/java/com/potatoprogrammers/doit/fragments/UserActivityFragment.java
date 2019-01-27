package com.potatoprogrammers.doit.fragments;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.potatoprogrammers.doit.R;
import com.potatoprogrammers.doit.glide.GlideApp;
import com.potatoprogrammers.doit.models.User;
import com.potatoprogrammers.doit.models.UserActivity;
import com.potatoprogrammers.doit.models.UserActivityStep;

import java.util.Locale;
import java.util.UUID;

import lombok.NoArgsConstructor;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
@NoArgsConstructor
public class UserActivityFragment extends AbstractFragment {
    private static final int REQUEST_GET_STEP_PICTURE = 1;
    private ImageView editUserActivity;
    private ImageView addStep;
    private ImageView deleteStep;
    private ImageView nextStep;
    private ImageView prevStep;
    private ImageView stepImage;
    private EditText stepDescription;
    private TextView activityName;
    private TextView stepNo;
    private int currentStep = 0;

    private UserActivity activity;
    private UserActivityStep activityStep;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_user_activity, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        int activityPosition = getArguments().getInt("userActivityPosition");
        activity = User.getLoggedInUser().getActivities().get(activityPosition);
        if (activity.getUserActivitySteps().isEmpty()) { //just for the old records
            activity.getUserActivitySteps().add(new UserActivityStep());
        }
        activityStep = activity.getUserActivitySteps().get(currentStep);
        initializeComponentsFromView(view);
        initializeListeners();
        setupStepButtonsVisibility();

        activityName.setText(activity.getName());
        stepNo.setText(String.format(Locale.getDefault(), "Step %d", currentStep+1));

        setupFromStepModel(activityStep);
    }

    @Override
    public void onResume() {
        super.onResume();
        setupFromStepModel(activityStep);
    }

    private void initializeListeners() {
        editUserActivity.setOnClickListener(v -> swapFragment(new EditUserActivityFragment(), getArguments()));
        activityName.setOnLongClickListener(this::renameActivity);
        stepImage.setOnLongClickListener(v -> {
            callPictureSelector(REQUEST_GET_STEP_PICTURE);
            //handle rest in onActivityResult
            return true;
        });

        stepDescription.setOnLongClickListener(v -> true);

        nextStep.setOnClickListener(this::moveToNextStep);
        prevStep.setOnClickListener(this::moveToPreviousStep);
        addStep.setOnClickListener(this::addStep);
        deleteStep.setOnClickListener(this::deleteStep);
    }

    private void setupFromStepModel(UserActivityStep step) {
        if (!TextUtils.isEmpty(step.getDescription())) {
            stepDescription.setText(step.getDescription());
        } else {
            stepDescription.setText(R.string.no_desc);
        }

        if (!TextUtils.isEmpty(step.getImageRef())) {
            setupStepImage(step.getImageRef());
        }
    }

    private void initializeComponentsFromView(@NonNull View view) {
        editUserActivity = view.findViewById(R.id.editUserActivityImageView);
        stepImage = view.findViewById(R.id.stepImageView);
        stepDescription = view.findViewById(R.id.stepDescriptionTextView);
        nextStep = view.findViewById(R.id.nextStepImageView);
        prevStep = view.findViewById(R.id.prevStepImageView);
        activityName = view.findViewById(R.id.userActivityNameTextView);
        addStep = view.findViewById(R.id.addNewStepImageView);
        deleteStep = view.findViewById(R.id.deleteNewStepImageView);
        stepNo = view.findViewById(R.id.stepNoTextView);
    }

    private void setupStepButtonsVisibility() {
        if (currentStep <= 0) {
            prevStep.setVisibility(View.INVISIBLE);
        } else {
            prevStep.setVisibility(View.VISIBLE);
        }

        if (currentStep >= activity.getUserActivitySteps().size() - 1) {
            nextStep.setVisibility(View.INVISIBLE);
            addStep.setVisibility(View.VISIBLE);
            deleteStep.setVisibility(View.VISIBLE);
        } else {
            nextStep.setVisibility(View.VISIBLE);
            addStep.setVisibility(View.INVISIBLE);
            deleteStep.setVisibility(View.INVISIBLE);
        }
    }

    private void callPictureSelector(int requestCode) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select picture"), requestCode);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_GET_STEP_PICTURE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            deleteOldImageFromStorage(activityStep.getImageRef());
            String imageUUID = UUID.randomUUID().toString();
            uploadNewImageToStorage(imageUri, imageUUID); //will update the image as well
            this.refreshFragment();
        }
    }

    private void deleteOldImageFromStorage(String imageUUID) {
        if (TextUtils.isEmpty(imageUUID)) {
            return; //nothing to delete
        }
        StorageReference ref = getImageRef(imageUUID);
        ref.delete()
                .addOnSuccessListener(aVoid -> activityStep.setImageRef(""))
                .addOnFailureListener(e -> this.showShortToast(R.string.sync_fail));
    }

    private void uploadNewImageToStorage(Uri imageUri, String imageUUID) {
        StorageReference ref = getImageRef(imageUUID);
        ref.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> {
                    activityStep.setImageRef(imageUUID);
                    setupStepImage(imageUri); //to avoid redownloading the image for the first time
                    this.updateUserInDatabase();
                })
                .addOnFailureListener(e -> this.showShortToast(R.string.sync_fail));
    }

    private void setupStepImage(Uri uri) {
        GlideApp.with(this).load(uri).into(stepImage);
    }

    private void setupStepImage(String uuid) {
        this.setupStepImage(getImageRef(uuid));
    }

    private void setupStepImage(StorageReference imageRef) {
        GlideApp.with(this).load(imageRef).into(stepImage);
    }

    private String generateImageRef(String fileUUID) {
        String userUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        return String.format(Locale.getDefault(), "%s/%s", userUid, fileUUID);
    }

    private StorageReference getImageRef(String uuid) {
        return FirebaseStorage.getInstance().getReference().child(generateImageRef(uuid));
    }

    private static boolean isActivityNameValid(String val) {
        return !TextUtils.isEmpty(val);
    }

    private void deleteStep(View v) {
        TextView label = new TextView(getContext());
        label.setText(R.string.delete_step_q);
        new AlertDialog.Builder(getContext())
                .setTitle(R.string.delete_step_title)
                .setView(new TextView(getContext()))
                .setPositiveButton(R.string.yes, (dialog, which) -> {
                    if (currentStep < activity.getUserActivitySteps().size()) {
                        activity.getUserActivitySteps().remove(currentStep);
                        currentStep--;
                        this.updateUserInDatabase();
                        this.refreshFragment();
                        this.showShortToast(R.string.deleted_step);
                    } else {
                        this.showShortToast(R.string.delete_failed);
                    }
                })
                .setNegativeButton(R.string.no, (dialog, which) -> dialog.dismiss())
                .show();
    }

    private void addStep(View v) {
        activity.getUserActivitySteps().add(new UserActivityStep());
        currentStep++;
        this.updateUserInDatabase();
        this.refreshFragment();
        setupStepButtonsVisibility();
    }

    private void moveToPreviousStep(View v) {
        if (currentStep > 0) {
            currentStep--;
            this.refreshFragment();
        }
        setupStepButtonsVisibility();
    }

    private void moveToNextStep(View v) {
        if (currentStep < activity.getUserActivitySteps().size() - 1) {
            currentStep++;
            this.refreshFragment();
        }
        setupStepButtonsVisibility();
    }

    private boolean renameActivity(View v) {
        final EditText input = new EditText(getContext());
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        new AlertDialog.Builder(getContext())
                .setTitle(R.string.rename_activity)
                .setView(input)
                .setPositiveButton(R.string.accept, (dialog, which) -> {
                    String val = input.getText().toString().trim();
                    if (isActivityNameValid(val)) {
                        activity.setName(val);
                        this.updateUserInDatabase();
                        this.refreshFragment();
                    } else {
                        Toast.makeText(getContext(), R.string.invalid_name, Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton(R.string.cancel, (dialog, which) -> dialog.cancel())
                .show();
        return true;
    }
}
