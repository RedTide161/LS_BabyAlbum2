package de.kdsoftworx.ls_babyalbum2.Activity;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;


import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

import de.kdsoftworx.ls_babyalbum2.Adapter.CCollectionPagerAdapter;
import de.kdsoftworx.ls_babyalbum2.Adapter.PageTransformerAdapter;
import de.kdsoftworx.ls_babyalbum2.Data.User;
import de.kdsoftworx.ls_babyalbum2.Fragment.BaptismFragment1;
import de.kdsoftworx.ls_babyalbum2.Fragment.BaptismFragment2;
import de.kdsoftworx.ls_babyalbum2.Fragment.ChildBirthDatesFragment;
import de.kdsoftworx.ls_babyalbum2.Fragment.ChildBirthFragment;
import de.kdsoftworx.ls_babyalbum2.Fragment.ChildFirstBath1;
import de.kdsoftworx.ls_babyalbum2.Fragment.ChildFirstBath2;
import de.kdsoftworx.ls_babyalbum2.Fragment.ChildFirstPicsFragment;
import de.kdsoftworx.ls_babyalbum2.Fragment.ChildFirstSnow1;
import de.kdsoftworx.ls_babyalbum2.Fragment.ChildFirstSnow2;
import de.kdsoftworx.ls_babyalbum2.Fragment.EasterFragment1;
import de.kdsoftworx.ls_babyalbum2.Fragment.EasterFragment2;
import de.kdsoftworx.ls_babyalbum2.Fragment.FirstStepsFragment1;
import de.kdsoftworx.ls_babyalbum2.Fragment.FirstStepsFragment2;
import de.kdsoftworx.ls_babyalbum2.Fragment.HalfTitleFragment;
import de.kdsoftworx.ls_babyalbum2.Fragment.NightsFragment;
import de.kdsoftworx.ls_babyalbum2.Fragment.PregnancyFragment1;
import de.kdsoftworx.ls_babyalbum2.Fragment.PregnancyFragment2;
import de.kdsoftworx.ls_babyalbum2.Fragment.TimeIsRunningFragment1;
import de.kdsoftworx.ls_babyalbum2.Fragment.TimeIsRunningFragment2;
import de.kdsoftworx.ls_babyalbum2.Login.LoginActivity;
import de.kdsoftworx.ls_babyalbum2.PDFDocument.PdfDocumentCreater;
import de.kdsoftworx.ls_babyalbum2.R;
import de.kdsoftworx.ls_babyalbum2.Data.UserLokalStore;

public class ChildDataActivity extends AppCompatActivity {

    // Variables for Collection Pager Adapter (swipe Fragments)
    CCollectionPagerAdapter collectionPagerAdapter;
    ViewPager viewPager;
    // declare User-Objects
    UserLokalStore userLokalStore;
    User user;

    // Boolean Variable to check if Logout was clicked
    boolean logoutClicked, changeBookClicked;

    public static String Log_tag = ChildDataActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_data);

        // create a Objekt "userLokalStore" to get access to saved userdata
        userLokalStore =  UserLokalStore.getInstance(this);

        // get UserData from logged in User
        user = userLokalStore.getLoggedInUser();

        // Remove Shadow of Action Bar
        getSupportActionBar().setElevation(0);

        // Set Fragments and Tabs for swipe Fragments or select by Tabs
        viewPager = findViewById(R.id.viewPager);
        setUpViewPager(viewPager);

    }


    //------------------------------------- Options Menu --------------------------------------------------------------------------
    // Create Optionsmenu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    // Check for Clicks on Menu-Items
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_change_book){
            // clear stored Bookdata-ID
            userLokalStore.setCurrentRecordId(0);
            // clear current Page
            userLokalStore.setCurrentPage(0);
            // set changeBookClicked to know User is changing Book
            changeBookClicked = true;
            // Go to BookOverview (AccountActivity)
            startActivity(new Intent(this, AccountActivity.class));

            // destroy the current Activity from BackStack
            finish();
        }

        if (id == R.id.action_logout) {
            userLokalStore.setUserLoggedIn(false);
            userLokalStore.setCurrentPage(0);
            userLokalStore.clearAllUserData();
            // Facebook Logout ----------------------------------------------------
            LoginManager.getInstance().logOut();
            // Google Logout ------------------------------------------------------
            // Build a GoogleSignInClient with the options specified by gso.
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestEmail()
                    .build();
            final GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
            mGoogleSignInClient.signOut();

            // set logoutClicked to know User is Logging out
            logoutClicked = true;

            startActivity(new Intent(this, LoginActivity.class));

            // destroy the current Activity from BackStack
            finish();

        }

        if (id == R.id.action_order_book){
            // TEST -------------------- TEST ----------------------- TEST ---------------------- TEST ----------------------------
            //new longTermOperation().execute(this);

            longTermOperation(this);
            // TEST -------------------- TEST ----------------------- TEST ---------------------- TEST ----------------------------
        }

        return true;
    }
    //-----------------------------------------------------------------------------------------------------------------------------------


    @Override
    public void onBackPressed() {
        super.onBackPressed();

        moveTaskToBack(true);
    }

    @Override
    protected void onStop() {
        super.onStop();

        // check if user is logging out and don´t save the last shown Page
        if (!logoutClicked && !changeBookClicked) {
            int lCurrentPage = 0;

            // Get current Page of Viewpager
            lCurrentPage = viewPager.getCurrentItem();
            // Set current Page on Userdata to show last opened Page when resume to App
            userLokalStore.setCurrentPage(lCurrentPage);
        }

        // clear Variable logoutClicked
        logoutClicked = false;
        changeBookClicked = false;

    }

    //--------------------------- set Fragments on Page Adapter to swipe Fragments or click on Tabs ------------------------------------
    public void setUpViewPager(ViewPager viewPager) {
        CCollectionPagerAdapter adapter = new CCollectionPagerAdapter(getSupportFragmentManager());
        PageTransformerAdapter pageTransformer = new PageTransformerAdapter();


        // Page 1
        adapter.addFragment(new HalfTitleFragment(), getResources().getString(R.string.tab_name_ht));
        pageTransformer.addViewToParallax(new PageTransformerAdapter.ParallaxTransformInformation(R.id.cd_Titel_1, 1.75f,1.75f))
                .addViewToParallax(new PageTransformerAdapter.ParallaxTransformInformation(R.id.etChildName,-0.75f,-0.75f))
                .addViewToParallax(new PageTransformerAdapter.ParallaxTransformInformation(R.id.cd_Titel_2_1,0.5f,0.5f))
                .addViewToParallax(new PageTransformerAdapter.ParallaxTransformInformation(R.id.cd_Titel_2_2,0.35f,0.35f))
                .addViewToParallax(new PageTransformerAdapter.ParallaxTransformInformation(R.id.etCreator,-0.5f,-0.5f));


        // Page 2
        adapter.addFragment(new PregnancyFragment1(), getResources().getString(R.string.tab_name_pregnancy));
        pageTransformer.addViewToParallax(new PageTransformerAdapter.ParallaxTransformInformation(R.id.tv_intro_pregnancy, 1.75f, 1.75f))
                .addViewToParallax(new PageTransformerAdapter.ParallaxTransformInformation(R.id.tv_p_preg_knowing, 0.5f, 0.5f))
                .addViewToParallax(new PageTransformerAdapter.ParallaxTransformInformation(R.id.et_p_preg_knowing, 0.5f, 0.5f))
                .addViewToParallax(new PageTransformerAdapter.ParallaxTransformInformation(R.id.tv_p_preg_feeling, -0.5f, -0.5f))
                .addViewToParallax(new PageTransformerAdapter.ParallaxTransformInformation(R.id.et_p_preg_feeling, -0.5f, -0.5f))
                .addViewToParallax(new PageTransformerAdapter.ParallaxTransformInformation(R.id.tv_p_preg_well_being, 0.5f, 0.5f))
                .addViewToParallax(new PageTransformerAdapter.ParallaxTransformInformation(R.id.et_p_preg_well_being, 0.5f, 0.5f))
                .addViewToParallax(new PageTransformerAdapter.ParallaxTransformInformation(R.id.tv_p_preg_sex, 0.35f, 0.35f))
                .addViewToParallax(new PageTransformerAdapter.ParallaxTransformInformation(R.id.et_p_preg_sex, 0.35f, 0.35f))
                .addViewToParallax(new PageTransformerAdapter.ParallaxTransformInformation(R.id.tv_p_preg_movements, -0.75f, -0.75f))
                .addViewToParallax(new PageTransformerAdapter.ParallaxTransformInformation(R.id.et_p_preg_movements, -0.75f, -0.75f));

        // Page 3
        adapter.addFragment(new PregnancyFragment2(), "");
        pageTransformer.addViewToParallax(new PageTransformerAdapter.ParallaxTransformInformation(R.id.ll_p_mom_1, 0.5f, 0.5f))
                .addViewToParallax(new PageTransformerAdapter.ParallaxTransformInformation(R.id.ll_p_mom_2, 0.5f, 0.5f))
                .addViewToParallax(new PageTransformerAdapter.ParallaxTransformInformation(R.id.ll_p_ultrasound_pic, -0.5f, -0.5f));

        // Page 4
        adapter.addFragment(new ChildBirthDatesFragment(), getResources().getString(R.string.tab_name_cbd));
        pageTransformer.addViewToParallax(new PageTransformerAdapter.ParallaxTransformInformation(R.id.tv_intro_birthdates, 1.75f, 1.75f))
                .addViewToParallax(new PageTransformerAdapter.ParallaxTransformInformation(R.id.tv_childname, 0.5f, 0.5f))
                .addViewToParallax(new PageTransformerAdapter.ParallaxTransformInformation(R.id.et_childname_cbd, 0.5f, 0.5f))
                .addViewToParallax(new PageTransformerAdapter.ParallaxTransformInformation(R.id.tv_birthday_time, -0.5f, -0.5f))
                .addViewToParallax(new PageTransformerAdapter.ParallaxTransformInformation(R.id.et_birthday_cbd, -0.5f, -0.5f))
                .addViewToParallax(new PageTransformerAdapter.ParallaxTransformInformation(R.id.et_birth_weight_cbd, 0.5f, 0.5f))
                .addViewToParallax(new PageTransformerAdapter.ParallaxTransformInformation(R.id.tv_birth_weight_cbd, 0.5f, 0.5f))
                .addViewToParallax(new PageTransformerAdapter.ParallaxTransformInformation(R.id.tv_birth_weight_unit, 0.35f, 0.35f))
                .addViewToParallax(new PageTransformerAdapter.ParallaxTransformInformation(R.id.tv_birth_size, -0.75f, -0.75f))
                .addViewToParallax(new PageTransformerAdapter.ParallaxTransformInformation(R.id.et_birth_size_cbd, -0.75f, -0.75f))
                .addViewToParallax(new PageTransformerAdapter.ParallaxTransformInformation(R.id.tv_birth_size_unit, -0.35f, -0.35f))
                .addViewToParallax(new PageTransformerAdapter.ParallaxTransformInformation(R.id.tv_head_circumference, 0.5f, 0.5f))
                .addViewToParallax(new PageTransformerAdapter.ParallaxTransformInformation(R.id.et_head_circumference_cbd, 0.5f, 0.5f))
                .addViewToParallax(new PageTransformerAdapter.ParallaxTransformInformation(R.id.tv_head_circumference_unit, 0.35f, 0.35f))
                .addViewToParallax(new PageTransformerAdapter.ParallaxTransformInformation(R.id.tv_eye_color, -0.5f, -0.5f))
                .addViewToParallax(new PageTransformerAdapter.ParallaxTransformInformation(R.id.et_fst_textInput_4, -0.35f, -0.35f))
                .addViewToParallax(new PageTransformerAdapter.ParallaxTransformInformation(R.id.tv_hair_color, 0.5f, 0.5f))
                .addViewToParallax(new PageTransformerAdapter.ParallaxTransformInformation(R.id.et_hair_color_cbd, 0.35f, 0.35f))
                .addViewToParallax(new PageTransformerAdapter.ParallaxTransformInformation(R.id.tv_star_sign, -0.75f, -0.75f))
                .addViewToParallax(new PageTransformerAdapter.ParallaxTransformInformation(R.id.et_star_sign_cbd, -0.35f, -0.35f));

        // Page 5
        adapter.addFragment(new ChildBirthFragment(), getResources().getString(R.string.tab_name_birth));
        pageTransformer.addViewToParallax(new PageTransformerAdapter.ParallaxTransformInformation(R.id.tv_intro_birth, 1.75f, 1.75f))
                .addViewToParallax(new PageTransformerAdapter.ParallaxTransformInformation(R.id.tv_b_contraction_start, 0.5f, 0.5f))
                .addViewToParallax(new PageTransformerAdapter.ParallaxTransformInformation(R.id.et_b_contraction_start, 0.5f, 0.5f))
                .addViewToParallax(new PageTransformerAdapter.ParallaxTransformInformation(R.id.tv_b_contr_start_time, -0.5f, -0.5f))
                .addViewToParallax(new PageTransformerAdapter.ParallaxTransformInformation(R.id.et_p_preg_feeling, -0.5f, -0.5f))
                .addViewToParallax(new PageTransformerAdapter.ParallaxTransformInformation(R.id.tv_p_preg_well_being, -0.5f, -0.5f))
                .addViewToParallax(new PageTransformerAdapter.ParallaxTransformInformation(R.id.et_p_preg_well_being, -0.5f, -0.5f))
                .addViewToParallax(new PageTransformerAdapter.ParallaxTransformInformation(R.id.tv_p_preg_sex, 0.5f, 0.5f))
                .addViewToParallax(new PageTransformerAdapter.ParallaxTransformInformation(R.id.et_p_preg_sex, 0.5f, 0.5f))
                .addViewToParallax(new PageTransformerAdapter.ParallaxTransformInformation(R.id.tv_b_delivery_room_text_1, -0.5f, -0.5f))
                .addViewToParallax(new PageTransformerAdapter.ParallaxTransformInformation(R.id.et_b_delivery_room_time, -0.5f, -0.5f))
                .addViewToParallax(new PageTransformerAdapter.ParallaxTransformInformation(R.id.tv_b_delivery_room_text_2, -0.5f, -0.5f))
                .addViewToParallax(new PageTransformerAdapter.ParallaxTransformInformation(R.id.tv_b_delivery_room_text_3, 0.5f, 0.5f))
                .addViewToParallax(new PageTransformerAdapter.ParallaxTransformInformation(R.id.et_p_preg_movements, 0.5f, 0.5f))
                .addViewToParallax(new PageTransformerAdapter.ParallaxTransformInformation(R.id.tv_b_delivery_room_text_4, 0.5f, 0.5f))
                .addViewToParallax(new PageTransformerAdapter.ParallaxTransformInformation(R.id.tv_b_delivery_room_text_5, -0.5f, -0.5f))
                .addViewToParallax(new PageTransformerAdapter.ParallaxTransformInformation(R.id.tv_b_location_of_birth, 0.5f, 0.5f))
                .addViewToParallax(new PageTransformerAdapter.ParallaxTransformInformation(R.id.et_b_location_of_birth, 0.5f, 0.5f))
                .addViewToParallax(new PageTransformerAdapter.ParallaxTransformInformation(R.id.tv_b_name_midwife, -0.5f, -0.5f))
                .addViewToParallax(new PageTransformerAdapter.ParallaxTransformInformation(R.id.et_b_name_midwife, -0.5f, -0.5f))
                .addViewToParallax(new PageTransformerAdapter.ParallaxTransformInformation(R.id.tv_b_Hospital_Docs_midwife, 0.5f, 0.5f))
                .addViewToParallax(new PageTransformerAdapter.ParallaxTransformInformation(R.id.et_b_Hospital_Docs_midwife, 0.5f, 0.5f))
                .addViewToParallax(new PageTransformerAdapter.ParallaxTransformInformation(R.id.tv_b_accompaniment, -0.5f, -0.5f))
                .addViewToParallax(new PageTransformerAdapter.ParallaxTransformInformation(R.id.et_b_accompaniment, -0.5f, -0.5f));

        // Page 6
        adapter.addFragment(new ChildFirstPicsFragment(), getResources().getString(R.string.tab_name_cbd_pics));
        pageTransformer.addViewToParallax(new PageTransformerAdapter.ParallaxTransformInformation(R.id.tv_cbd_Titel, 1.75f,1.75f))
                .addViewToParallax(new PageTransformerAdapter.ParallaxTransformInformation(R.id.cl_fi_baby, 0.5f, 0.5f))
                .addViewToParallax(new PageTransformerAdapter.ParallaxTransformInformation(R.id.cl_fi_mum, -0.5f, -0.5f));

        // Page 7
        adapter.addFragment(new NightsFragment(), getResources().getString(R.string.tab_name_nights));
        pageTransformer.addViewToParallax(new PageTransformerAdapter.ParallaxTransformInformation(R.id.tv_intro_nights, 1.75f,1.75f))
                .addViewToParallax(new PageTransformerAdapter.ParallaxTransformInformation(R.id.tv_n_text_1, 0.5f, 0.5f))
                .addViewToParallax(new PageTransformerAdapter.ParallaxTransformInformation(R.id.et_n_inputText_1, 0.5f, 0.5f))
                .addViewToParallax(new PageTransformerAdapter.ParallaxTransformInformation(R.id.tv_n_text_2, -0.75f, -0.75f))
                .addViewToParallax(new PageTransformerAdapter.ParallaxTransformInformation(R.id.et_n_inputText_2, -0.75f, -0.75f))
                .addViewToParallax(new PageTransformerAdapter.ParallaxTransformInformation(R.id.tv_n_text_3, 0.75f, 0.75f))
                .addViewToParallax(new PageTransformerAdapter.ParallaxTransformInformation(R.id.et_n_inputText_3, 0.75f, 0.75f))
                .addViewToParallax(new PageTransformerAdapter.ParallaxTransformInformation(R.id.tv_n_text_4, -0.5f, -0.5f))
                .addViewToParallax(new PageTransformerAdapter.ParallaxTransformInformation(R.id.et_n_inputText_4, -0.5f, -0.5f))
                .addViewToParallax(new PageTransformerAdapter.ParallaxTransformInformation(R.id.tv_n_text_5, 0.5f, 0.5f))
                .addViewToParallax(new PageTransformerAdapter.ParallaxTransformInformation(R.id.et_n_inputText_5, 0.5f, 0.5f))
                .addViewToParallax(new PageTransformerAdapter.ParallaxTransformInformation(R.id. tv_n_text_6, -0.5f, -0.5f))
                .addViewToParallax(new PageTransformerAdapter.ParallaxTransformInformation(R.id.et_n_inputText_6, -0.5f, -0.5f));


        // Page 8
        adapter.addFragment(new ChildFirstBath1(), getResources().getString(R.string.tab_name_fb));
        pageTransformer.addViewToParallax(new PageTransformerAdapter.ParallaxTransformInformation(R.id.tv_fb_Titel, 1.75f, 1.75f))
                .addViewToParallax(new PageTransformerAdapter.ParallaxTransformInformation(R.id.tv_fb_text_1, 0.5f, 0.5f))
                .addViewToParallax(new PageTransformerAdapter.ParallaxTransformInformation(R.id.et_fb_textinput_1, 0.5f, 0.5f))
                .addViewToParallax(new PageTransformerAdapter.ParallaxTransformInformation(R.id.tv_fb_text_2, -0.5f, -0.5f))
                .addViewToParallax(new PageTransformerAdapter.ParallaxTransformInformation(R.id.et_fb_textinput_2, -0.5f, -0.5f))
                .addViewToParallax(new PageTransformerAdapter.ParallaxTransformInformation(R.id.ll_fb_baby1, 0.75f, 0.75f));


        // Page 9
        adapter.addFragment(new ChildFirstBath2(), "");
        pageTransformer.addViewToParallax(new PageTransformerAdapter.ParallaxTransformInformation(R.id.ll_fb_baby2, 0.5f, 0.5f))
                .addViewToParallax(new PageTransformerAdapter.ParallaxTransformInformation(R.id.tv_fb_text_3, -0.5f, -0.5f))
                .addViewToParallax(new PageTransformerAdapter.ParallaxTransformInformation(R.id.et_fb_textinput_3, -0.5f, -0.5f))
                .addViewToParallax(new PageTransformerAdapter.ParallaxTransformInformation(R.id.tv_fb_text_4, 0.5f, 0.5f))
                .addViewToParallax(new PageTransformerAdapter.ParallaxTransformInformation(R.id.et_fb_textinput_4, 0.5f, 0.5f))
                .addViewToParallax(new PageTransformerAdapter.ParallaxTransformInformation(R.id.tv_fb_text_5, -0.35f, -0.35f))
                .addViewToParallax(new PageTransformerAdapter.ParallaxTransformInformation(R.id.et_fb_textinput_5, -0.35f, -0.35f));

        // Page 10
        adapter.addFragment(new ChildFirstSnow1(), getResources().getString(R.string.tab_name_fs));
        pageTransformer.addViewToParallax(new PageTransformerAdapter.ParallaxTransformInformation(R.id.tv_fs_Titel, 1.75f, 1.75f))
                .addViewToParallax(new PageTransformerAdapter.ParallaxTransformInformation(R.id.tv_fs_text_1, 0.5f, 0.5f))
                .addViewToParallax(new PageTransformerAdapter.ParallaxTransformInformation(R.id.et_fs_textinput_1, 0.5f, 0.5f))
                .addViewToParallax(new PageTransformerAdapter.ParallaxTransformInformation(R.id.tv_fs_text_2, -0.5f, -0.5f))
                .addViewToParallax(new PageTransformerAdapter.ParallaxTransformInformation(R.id.et_fs_textinput_2, -0.5f, -0.5f))
                .addViewToParallax(new PageTransformerAdapter.ParallaxTransformInformation(R.id.tv_fs_text_3, 0.75f, 0.75f))
                .addViewToParallax(new PageTransformerAdapter.ParallaxTransformInformation(R.id.et_fs_textinput_3, 0.75f, 0.75f))
                .addViewToParallax(new PageTransformerAdapter.ParallaxTransformInformation(R.id.tv_fs_text_4, -0.75f, -0.75f))
                .addViewToParallax(new PageTransformerAdapter.ParallaxTransformInformation(R.id.et_fs_textinput_4, -0.75f, -0.75f));

        // Page 11
        adapter.addFragment(new ChildFirstSnow2(), "");
        pageTransformer.addViewToParallax(new PageTransformerAdapter.ParallaxTransformInformation(R.id.ll_fs_baby1, 0.5f, 0.5f))
                .addViewToParallax(new PageTransformerAdapter.ParallaxTransformInformation(R.id.ll_fs_baby2, -0.5f, -0.5f))
                .addViewToParallax(new PageTransformerAdapter.ParallaxTransformInformation(R.id.tv_fs_poem, 0.5f, 0.5f));

        // Page 12
        adapter.addFragment(new EasterFragment1(), getResources().getString(R.string.tab_name_e));
        pageTransformer.addViewToParallax(new PageTransformerAdapter.ParallaxTransformInformation(R.id.tv_intro_1_easter, 1.75f, 1.75f))
                .addViewToParallax(new PageTransformerAdapter.ParallaxTransformInformation(R.id.tv_e_easter_location, 0.5f, 0.5f))
                .addViewToParallax(new PageTransformerAdapter.ParallaxTransformInformation(R.id.et_e_easter_location, 0.5f, 0.5f))
                .addViewToParallax(new PageTransformerAdapter.ParallaxTransformInformation(R.id.tv_e_easter_gift, -0.5f, -0.5f))
                .addViewToParallax(new PageTransformerAdapter.ParallaxTransformInformation(R.id.et_e_easter_gift, -0.5f, -0.5f))
                .addViewToParallax(new PageTransformerAdapter.ParallaxTransformInformation(R.id.ll_e_easter_gift_pic, 0.75f, 0.75f));

        // Page 13
        adapter.addFragment(new EasterFragment2(), "");
        pageTransformer.addViewToParallax(new PageTransformerAdapter.ParallaxTransformInformation(R.id.tv_intro_2_1_easter, 1.75f, 1.75f))
                .addViewToParallax(new PageTransformerAdapter.ParallaxTransformInformation(R.id.tv_intro_2_2_easter, 1.75f, 1.75f))
                .addViewToParallax(new PageTransformerAdapter.ParallaxTransformInformation(R.id.ll_e_easter_pic2, -0.5f, -0.5f))
                .addViewToParallax(new PageTransformerAdapter.ParallaxTransformInformation(R.id.ll_e_easter_pic3, 0.5f, 0.5f))
                .addViewToParallax(new PageTransformerAdapter.ParallaxTransformInformation(R.id.ll_e_easter_pic4, -0.5f, -0.5f));


        // Page 14
        adapter.addFragment(new TimeIsRunningFragment1(), getResources().getString(R.string.tab_name_tir));
        pageTransformer.addViewToParallax(new PageTransformerAdapter.ParallaxTransformInformation(R.id.tv_intro_tir, 1.75f, 1.75f))
                .addViewToParallax(new PageTransformerAdapter.ParallaxTransformInformation(R.id.tv_tir_text_1, -0.5f, -0.5f))
                .addViewToParallax(new PageTransformerAdapter.ParallaxTransformInformation(R.id.et_tir_inputText_1, -0.5f, -0.5f))
                .addViewToParallax(new PageTransformerAdapter.ParallaxTransformInformation(R.id.tv_tir_text_2, 0.5f, 0.5f))
                .addViewToParallax(new PageTransformerAdapter.ParallaxTransformInformation(R.id.et_tir_inputText_2, 0.5f, 0.5f))
                .addViewToParallax(new PageTransformerAdapter.ParallaxTransformInformation(R.id.tv_tir_text_3, -0.75f, -0.75f))
                .addViewToParallax(new PageTransformerAdapter.ParallaxTransformInformation(R.id.et_tir_inputText_3, -0.75f, -0.75f))
                .addViewToParallax(new PageTransformerAdapter.ParallaxTransformInformation(R.id.tv_tir_text_4, 0.5f, 0.5f))
                .addViewToParallax(new PageTransformerAdapter.ParallaxTransformInformation(R.id.et_tir_inputText_4, 0.5f, 0.5f))
                .addViewToParallax(new PageTransformerAdapter.ParallaxTransformInformation(R.id.ll_tir_pic_1, -0.75f, -0.75f));

        // Page 15
        adapter.addFragment(new TimeIsRunningFragment2(), "");
        pageTransformer.addViewToParallax(new PageTransformerAdapter.ParallaxTransformInformation(R.id.ll_tir_pic_2, 0.75f, 0.75f))
                .addViewToParallax(new PageTransformerAdapter.ParallaxTransformInformation(R.id.tv_tir_text_5, -0.5f, -0.5f))
                .addViewToParallax(new PageTransformerAdapter.ParallaxTransformInformation(R.id.et_tir_inputText_5, -0.5f, -0.5f))
                .addViewToParallax(new PageTransformerAdapter.ParallaxTransformInformation(R.id.tv_tir_text_6, 0.5f, 0.5f))
                .addViewToParallax(new PageTransformerAdapter.ParallaxTransformInformation(R.id.et_tir_inputText_6, 0.5f, 0.5f));

        // Page 16
        adapter.addFragment(new BaptismFragment1(), getResources().getString(R.string.tab_name_btm));
        pageTransformer.addViewToParallax(new PageTransformerAdapter.ParallaxTransformInformation(R.id.tv_intro_baptism, 0.75f, 0.75f))
                .addViewToParallax(new PageTransformerAdapter.ParallaxTransformInformation(R.id.tv_btm_text_1_0, -0.5f, -0.5f))
                .addViewToParallax(new PageTransformerAdapter.ParallaxTransformInformation(R.id.et_btm_inputText_1_0, -0.5f, -0.5f))
                .addViewToParallax(new PageTransformerAdapter.ParallaxTransformInformation(R.id.tv_btm_text_1_1, -0.5f, -0.5f))
                .addViewToParallax(new PageTransformerAdapter.ParallaxTransformInformation(R.id.et_btm_inputText_1_1, -0.5f, -0.5f))
                .addViewToParallax(new PageTransformerAdapter.ParallaxTransformInformation(R.id.tv_btm_text_1_2, -0.5f, -0.5f))
                .addViewToParallax(new PageTransformerAdapter.ParallaxTransformInformation(R.id.et_btm_inputText_1_2, -0.5f, -0.5f))
                .addViewToParallax(new PageTransformerAdapter.ParallaxTransformInformation(R.id.tv_btm_text_1_3, -0.5f, -0.5f))
                .addViewToParallax(new PageTransformerAdapter.ParallaxTransformInformation(R.id.tv_btm_text_2, 0.5f, 0.5f))
                .addViewToParallax(new PageTransformerAdapter.ParallaxTransformInformation(R.id.et_btm_inputText_2, 0.5f, 0.5f))
                .addViewToParallax(new PageTransformerAdapter.ParallaxTransformInformation(R.id.tv_btm_text_3, -0.75f, -0.75f))
                .addViewToParallax(new PageTransformerAdapter.ParallaxTransformInformation(R.id.et_btm_inputText_3, -0.75f, -0.75f))
                .addViewToParallax(new PageTransformerAdapter.ParallaxTransformInformation(R.id.tv_btm_text_4, 0.75f, 0.75f))
                .addViewToParallax(new PageTransformerAdapter.ParallaxTransformInformation(R.id.et_btm_inputText_4, 0.75f, 0.75f))
                .addViewToParallax(new PageTransformerAdapter.ParallaxTransformInformation(R.id.tv_btm_text_5, -0.5f, -0.5f))
                .addViewToParallax(new PageTransformerAdapter.ParallaxTransformInformation(R.id.et_btm_inputText_5, -0.5f, -0.5f))
                .addViewToParallax(new PageTransformerAdapter.ParallaxTransformInformation(R.id.tv_btm_text_6, 0.5f, 0.5f))
                .addViewToParallax(new PageTransformerAdapter.ParallaxTransformInformation(R.id.et_btm_inputText_6, 0.5f, 0.5f))
                .addViewToParallax(new PageTransformerAdapter.ParallaxTransformInformation(R.id.tv_btm_text_7, -0.75f, -0.75f))
                .addViewToParallax(new PageTransformerAdapter.ParallaxTransformInformation(R.id.et_btm_inputText_7, -0.75f, -0.75f))
                .addViewToParallax(new PageTransformerAdapter.ParallaxTransformInformation(R.id.tv_btm_text_8, 0.5f, 0.5f))
                .addViewToParallax(new PageTransformerAdapter.ParallaxTransformInformation(R.id.et_btm_inputText_8, 0.5f, 0.5f));

        // Page 17
        adapter.addFragment(new BaptismFragment2(), "");
        pageTransformer.addViewToParallax(new PageTransformerAdapter.ParallaxTransformInformation(R.id.tv_btm_text_9, 0.75f, 0.75f))
                .addViewToParallax(new PageTransformerAdapter.ParallaxTransformInformation(R.id.et_btm_inputText_9, 0.75f, 0.75f))
                .addViewToParallax(new PageTransformerAdapter.ParallaxTransformInformation(R.id.tv_btm_text_10, -0.5f, -0.5f))
                .addViewToParallax(new PageTransformerAdapter.ParallaxTransformInformation(R.id.et_btm_inputText_10, -0.5f, -0.5f))
                .addViewToParallax(new PageTransformerAdapter.ParallaxTransformInformation(R.id.ll_btm_pic1, -0.75f, -0.75f))
                .addViewToParallax(new PageTransformerAdapter.ParallaxTransformInformation(R.id.ll_btm_pic2, -0.75f, -0.75f))
                .addViewToParallax(new PageTransformerAdapter.ParallaxTransformInformation(R.id.ll_btm_pic3, 0.5f, 0.5f));

        // Page 18
        adapter.addFragment(new FirstStepsFragment1(), getResources().getString(R.string.tab_name_fst));
        pageTransformer.addViewToParallax(new PageTransformerAdapter.ParallaxTransformInformation(R.id.tv_intro_fst, 0.75f, 0.75f))
                .addViewToParallax(new PageTransformerAdapter.ParallaxTransformInformation(R.id.tv_fst_text_1, 0.75f, 0.75f))
                .addViewToParallax(new PageTransformerAdapter.ParallaxTransformInformation(R.id.et_fst_textInput_1, 0.75f, 0.75f))
                .addViewToParallax(new PageTransformerAdapter.ParallaxTransformInformation(R.id.tv_fst_text_2, -0.5f, -0.5f))
                .addViewToParallax(new PageTransformerAdapter.ParallaxTransformInformation(R.id.et_fst_textInput_2, -0.5f, -0.5f))
                .addViewToParallax(new PageTransformerAdapter.ParallaxTransformInformation(R.id.tv_fst_poem, 0.75f, 0.75f))
                .addViewToParallax(new PageTransformerAdapter.ParallaxTransformInformation(R.id.tv_fst_text_3, -0.75f, -0.75f))
                .addViewToParallax(new PageTransformerAdapter.ParallaxTransformInformation(R.id.et_fst_textInput_3, -0.75f, -0.75f))
                .addViewToParallax(new PageTransformerAdapter.ParallaxTransformInformation(R.id.tv_fst_text_4, 0.5f, 0.5f))
                .addViewToParallax(new PageTransformerAdapter.ParallaxTransformInformation(R.id.et_fst_textInput_4, 0.5f, 0.5f))
                .addViewToParallax(new PageTransformerAdapter.ParallaxTransformInformation(R.id.tv_fst_text_5, -0.5f, -0.5f))
                .addViewToParallax(new PageTransformerAdapter.ParallaxTransformInformation(R.id.et_fst_textInput_5, -0.5f, -0.5f));

        // Page 19
        adapter.addFragment(new FirstStepsFragment2(), "");
        pageTransformer.addViewToParallax(new PageTransformerAdapter.ParallaxTransformInformation(R.id.ll_fst_pic_1, 0.75f, 0.75f))
                .addViewToParallax(new PageTransformerAdapter.ParallaxTransformInformation(R.id.ll_fst_pic_2, -0.75f, -0.75f))
                .addViewToParallax(new PageTransformerAdapter.ParallaxTransformInformation(R.id.ll_fst_pic_3, -0.75f, -0.75f));

        // Store current Number of used Bookpages
        userLokalStore.setCurrentNumberPages(adapter.getCount());
        // Set Configurations to ViewPager-Adapter
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(getCurrentPage());

        // Set Page-Animation while swiping
        viewPager.setPageTransformer(true, pageTransformer);


    }

    // get Current Page for PageViewer
    private int getCurrentPage() {
        return userLokalStore.getCurrentPage();
    }
/*
private  final class longTermOperation extends AsyncTask<Context, Void, String> {
    @Override
    protected String doInBackground(Context... contexts) {

        PdfDocumentCreater pdfDocument = PdfDocumentCreater.getInstance();
        pdfDocument.createPDF(ChildDataActivity.this);

        return null;
    }
}*/

private void longTermOperation (Context context){
    PdfDocumentCreater pdfDocument = PdfDocumentCreater.getInstance();
    pdfDocument.createPDF(context);
}

}


