package com.sourabhverma.audiofy.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.sourabhverma.audiofy.R;
import com.sourabhverma.audiofy.adapter.sliderAdapter;
import com.sourabhverma.audiofy.helperClasses.SignUpHelper;
import com.sourabhverma.audiofy.models.slideItem;
import com.sourabhverma.audiofy.utils.Constants;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.sourabhverma.audiofy.utils.Constants.RC_SIGN_IN;

public class WelcomeActivity extends AppCompatActivity {

    private ViewPager2 viewPager2;

    private final Handler sliderHandler = new Handler();

    private Button signInButton;

    private SignUpHelper signUpHelper;

    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        //<<<<<=====  Initialisation  ======>>>>>
        viewPager2 = findViewById(R.id.viewPagerImageSlider);
        signInButton = findViewById(R.id.signinbuttonwithgoogle);
        signUpHelper = SignUpHelper.retrofit.create(SignUpHelper.class);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // <<<<<<<<<<======= Initialize slideItem array ==========>>>>>>>>>>>
        List<slideItem> slideItems = new ArrayList<>();
        slideItems.add(new slideItem(R.drawable.lata_mangeskar_ji));
        slideItems.add(new slideItem(R.drawable.kishor_kumar));
        slideItems.add(new slideItem(R.drawable.shankar_mahadevan));
        slideItems.add(new slideItem(R.drawable.arjit));

        initViewPager(slideItems);

    }

    private void initViewPager(List<slideItem> slideItems) {

        viewPager2.setAdapter(new sliderAdapter(slideItems));

        viewPager2.setClipToPadding(false);
        viewPager2.setClipChildren(false);
        viewPager2.setOffscreenPageLimit(3);
        viewPager2.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_ALWAYS);

        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(10));
        compositePageTransformer.addTransformer((page, position) -> {
            float r = 1 - Math.abs(position);
            page.setScaleY(0.80f + r*0.20f);
        });

        viewPager2.setPageTransformer(compositePageTransformer);

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);

                sliderHandler .removeCallbacks(sliderRunnable);
                sliderHandler.postDelayed(sliderRunnable, 3000);

            }
        });


    }

    private final Runnable sliderRunnable = new Runnable() {
        @Override
        public void run() {

            if (viewPager2.getCurrentItem() == 3) {
                viewPager2.setCurrentItem(0);
            } else {
                viewPager2.setCurrentItem(viewPager2.getCurrentItem() + 1);
            }


        }
    };



    @Override
    protected void onPause() {
        super.onPause();

        sliderHandler.removeCallbacks(sliderRunnable);

    }

    @Override
    protected void onResume() {
        super.onResume();

        sliderHandler.postDelayed(sliderRunnable, 3000);

    }

    public void signInButton(View view) {

        Log.d(Constants.TAG, "signInButton: ");
        signInButton.setEnabled(false);
        signIn();

    }

    private void signIn() {

        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(@org.jetbrains.annotations.NotNull Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            assert account != null;
            Log.d(Constants.TAG, "handleSignInResult: " + "\t" + account.getEmail() + "\t" + account.getId() + "\t" + account.getGivenName()  + "\t" + account.getFamilyName()  + "\t" + account.getDisplayName()
                    + "\t" + account.getPhotoUrl());

            try {
                createUser(account.getId(),account.getEmail(),account.getGivenName(),account.getFamilyName(),account.getDisplayName(), Objects.requireNonNull(account.getPhotoUrl()).toString());
            } catch (Exception e) {
                e.printStackTrace();
                createUser(account.getId(),account.getEmail(),account.getGivenName(),account.getFamilyName(),account.getDisplayName(), null);
            }

        } catch (ApiException e) {
            Log.d(Constants.TAG, "handleSignInResult: " + e);
            signInButton.setEnabled(true);

//            Intent intent = new Intent(WelcomeActivity.this, HomeActivity.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            startActivity(intent);

        }
    }

    private void createUser(String id, String email, String firstname, String lastname, String fullname, String photourl ) {

        Call<Void> userCall = signUpHelper.createUser(id,email,firstname,lastname,fullname,photourl);

        userCall.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NotNull Call<Void> call, @NotNull Response<Void> response) {
                if (response.isSuccessful()){
                    Log.d(Constants.TAG, "onResponse: " + response.code());
                    Intent intent = new Intent(WelcomeActivity.this, HomeActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                } else {
                    Log.d(Constants.TAG, "onResponse: 2");
                    signInButton.setEnabled(true);
                }
            }

            @Override
            public void onFailure(@NotNull Call<Void> call, @NotNull Throwable t) {
                Log.d(Constants.TAG, "onResponse: "+ " \n" + t);
                signInButton.setEnabled(true);
            }
        });

    }


}