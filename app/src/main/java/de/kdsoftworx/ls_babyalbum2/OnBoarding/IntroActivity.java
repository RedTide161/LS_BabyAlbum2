package de.kdsoftworx.ls_babyalbum2.OnBoarding;

import android.content.Intent;
import android.os.Bundle;

import com.cuneytayyildiz.onboarder.OnboarderActivity;
import com.cuneytayyildiz.onboarder.OnboarderPage;
import com.cuneytayyildiz.onboarder.utils.OnboarderPageChangeListener;

import java.util.Arrays;
import java.util.List;

import de.kdsoftworx.ls_babyalbum2.Activity.AccountActivity;
import de.kdsoftworx.ls_babyalbum2.Data.User;
import de.kdsoftworx.ls_babyalbum2.Data.UserLokalStore;
import de.kdsoftworx.ls_babyalbum2.R;

public class IntroActivity extends OnboarderActivity implements OnboarderPageChangeListener {

    @Override
    protected void onResume() {
        super.onResume();

        UserLokalStore userLokalStore = UserLokalStore.getInstance(this);

        // Check if it´s necessary to show OnBoarding-Intro
        if (userLokalStore.getUserLoggedIn() && userLokalStore.getIntroStatus()){
           // start AccountActivity
            startActivity(new Intent(IntroActivity.this, AccountActivity.class));

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        UserLokalStore userLokalStore = UserLokalStore.getInstance(this);
        User user = userLokalStore.getLoggedInUser();

        List<OnboarderPage> pages;
        pages = Arrays.asList(
                // Welcome screen
                new OnboarderPage.Builder()
                        .title(getString(R.string.txt_welcome_title )+ " " + user.getUsername())
                        .description(getString(R.string.txt_welcome_2))
                        .imageResourceId(R.drawable.logobw_hell1)
                        .backgroundColorId(R.color.colorblue)
                        .titleColorId(R.color.colorPrimary)
                        .descriptionColorId(R.color.colorPrimary)
                        .multilineDescriptionCentered(true)
                        .build(),

                // Tips Text screen
                new OnboarderPage.Builder()
                        .title(getString(R.string.txt_tips_title))
                        .description(getString(R.string.txt_tips))
                        .imageResourceId(R.drawable.logobw_hell1)
                        .backgroundColorId(R.color.colorgreen)
                        .titleColorId(R.color.colorPrimary)
                        .descriptionColorId(R.color.colorPrimary)
                        .multilineDescriptionCentered(true)
                        .build(),

                // Add screen
                new OnboarderPage.Builder()
                        .title(getString(R.string.txt_add_title))
                        .description(getString(R.string.txt_add))
                        .imageResourceId(R.drawable.intro_add_book)
                        .backgroundColorId(R.color.colororange)
                        .titleColorId(R.color.colorGrey)
                        .descriptionColorId(R.color.colorGrey)
                        .multilineDescriptionCentered(true)
                        .build(),

                // Edit screen
                new OnboarderPage.Builder()
                        .title(getString(R.string.txt_edit_titel))
                        .description(getString(R.string.txt_edit))
                        .imageResourceId(R.drawable.intro_edit_bookname)
                        .backgroundColorId(R.color.colorlavender)
                        .titleColorId(R.color.colorGrey)
                        .descriptionColorId(R.color.colorGrey)
                        .multilineDescriptionCentered(true)
                        .build(),

                // Delete screen
                new OnboarderPage.Builder()
                        .title(getString(R.string.txt_delete_title))
                        .description(getString(R.string.txt_delete))
                        .imageResourceId(R.drawable.intro_delete_book)
                        .backgroundColorId(R.color.colorpink)
                        .titleColorId(R.color.colorPrimary)
                        .descriptionColorId(R.color.colorPrimary)
                        .multilineDescriptionCentered(true)
                        .build(),

                // Delete screen
        new OnboarderPage.Builder()
                .title(getString(R.string.txt_start_title))
                .description(getString(R.string.txt_start))
                .imageResourceId(R.drawable.intro_click_book)
                .backgroundColorId(R.color.colorturquoise)
                .titleColorId(R.color.colorPrimary)
                .descriptionColorId(R.color.colorPrimary)
                .multilineDescriptionCentered(true)
                .build()
        );
        setOnboarderPageChangeListener(this);
        setSkipButtonTextColor(R.color.colorWhite);
        setActiveIndicatorColor(R.color.colorWhite);
        setInactiveIndicatorColor(R.color.colorGrey);
        initOnboardingPages(pages);
    }

    @Override
    public void onFinishButtonPressed() {

        // Go to Book-Area
        Intent intent = new Intent(IntroActivity.this, AccountActivity.class);
        IntroActivity.this.startActivity(intent);

        // User has finished OnBoarding -> set Satus to true = don´t show Intro
        UserLokalStore userLokalStore = UserLokalStore.getInstance(this);
        userLokalStore.setIntroStatus(true);
    }

    @Override
    public void onPageChanged(int position) {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        moveTaskToBack(true);
    }
}
