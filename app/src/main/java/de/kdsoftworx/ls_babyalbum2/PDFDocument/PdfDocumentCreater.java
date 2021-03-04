package de.kdsoftworx.ls_babyalbum2.PDFDocument;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.hendrix.pdfmyxml.PdfDocument;
import com.hendrix.pdfmyxml.viewRenderer.AbstractViewRenderer;

import java.io.File;
import java.util.List;

import de.kdsoftworx.ls_babyalbum2.Data.UserLokalStore;
import de.kdsoftworx.ls_babyalbum2.R;
import de.kdsoftworx.ls_babyalbum2.RoomDatabase.LSBookdata;
import de.kdsoftworx.ls_babyalbum2.ViewModel.BookdataViewModel;

public class PdfDocumentCreater {

    private BookdataViewModel bookdataViewModel;
    private UserLokalStore userLokalStore;
    public static PdfDocumentCreater document;

    public static PdfDocumentCreater getInstance() {
        if (document == null) { //if there is no instance available... create new one

            // synchronize "getInstance-Method" that a second thread will have to wait until the "getInstance-Method" is completed for the first thread.
            synchronized (PdfDocumentCreater.class) {

                if (document == null) // double-check if there is a Instance available
                {
                    document = new PdfDocumentCreater();
                }
            }
        }
        return document;
    }


    public void createPDF(final Context context) {

        // get Instance of User-Object
        userLokalStore = UserLokalStore.getInstance(context);

        bookdataViewModel = ViewModelProviders.of ((FragmentActivity) context).get(BookdataViewModel.class);
        //ViewModelProvider.Factory factory = new ViewModelProvider.NewInstanceFactory();
        //bookdataViewModel = new ViewModelProvider ((FragmentActivity)context, factory).get(BookdataViewModel.class);
        bookdataViewModel.getAllBookdataEntries().observe(((FragmentActivity) context), new Observer<List<LSBookdata>>() {
            @Override
            public void onChanged(List<LSBookdata> lsBookdata) {

                // get all Data from Database
                final LSBookdata currentBookData = lsBookdata.get(0);


                // Create all selected Pages (set Texts an Image to Page)
                // ------------------------ HALF-TITLE ---------------------------------------------
                final AbstractViewRenderer page1 = new AbstractViewRenderer(context, R.layout.fragment_half_title) {

                    @Override
                    protected void initView(View view) {

                        // get all EditText on Layout
                       EditText HT_inputText1 = view.findViewById(R.id.etChildName);
                       EditText HT_inputText2 = view.findViewById(R.id.etCreator);

                        // set Data from DB to Layout
                        HT_inputText1.setText(currentBookData.HT_inputText1);
                        HT_inputText2.setText(currentBookData.HT_inputText2);
                    }
                };
                // Set Bitmap Reuse...
                page1.setReuseBitmap(true);

                // ------------------------ PREGNANCY ----------------------------------------------
                final AbstractViewRenderer page2 = new AbstractViewRenderer(context, R.layout.fragment_pregnancy_1) {

                    @Override
                    protected void initView(View view) {

                        // get all EditText on Layout
                        EditText P_inputText1 = (EditText) view.findViewById(R.id.et_p_preg_knowing);
                        EditText P_inputText2 = (EditText) view.findViewById(R.id.et_p_preg_feeling);
                        EditText P_inputText3 = (EditText) view.findViewById(R.id.et_p_preg_well_being);
                        EditText P_inputText4 = (EditText) view.findViewById(R.id.et_p_preg_sex);
                        EditText P_inputText5 = (EditText) view.findViewById(R.id.et_p_preg_movements);

                        // set Data from DB to Layout
                        P_inputText1.setText(currentBookData.P_inputText1);
                        P_inputText2.setText(currentBookData.P_inputText2);
                        P_inputText3.setText(currentBookData.P_inputText3);
                        P_inputText4.setText(currentBookData.P_inputText4);
                        P_inputText5.setText(currentBookData.P_inputText5);
                    }
                };
                // Set Bitmap Reuse...
                page2.setReuseBitmap(true);


                final AbstractViewRenderer page3 = new AbstractViewRenderer(context, R.layout.fragment_pregnancy_2) {

                    @Override
                    protected void initView(View view) {

                        // get all Elements on Layout
                        ImageView P_image1 = (ImageView) view.findViewById(R.id.iv_p_mom_1);
                        ImageView P_image2 = (ImageView) view.findViewById(R.id.iv_p_mom_2);
                        ImageView P_image3 = (ImageView) view.findViewById(R.id.iv_p_ultrasound_pic);

                        // get Path of Image from Database
                        String pathImage1 = currentBookData.P_image1;
                        String pathImage2 = currentBookData.P_image2;
                        String pathImage3 = currentBookData.P_image3;

                        // Load File
                        Bitmap bmp1 = BitmapFactory.decodeFile(pathImage1);
                        Bitmap bmp2 = BitmapFactory.decodeFile(pathImage2);
                        Bitmap bmp3 = BitmapFactory.decodeFile(pathImage3);

                        // Set Image to ImageView
                        P_image1.setImageBitmap(bmp1);
                        P_image2.setImageBitmap(bmp2);
                        P_image3.setImageBitmap(bmp3);
                    }
                };
                // Set Bitmap Reuse...
                page3.setReuseBitmap(true);
                page3.render(1120, 1800);

                // ------------------------ BIRTH DATES --------------------------------------------
                final AbstractViewRenderer page4 = new AbstractViewRenderer(context, R.layout.fragment_child_birth_dates){

                    @Override
                    protected void initView(View view) {

                        // get all EditText on Layout
                        EditText CBD_inputText1 = view.findViewById(R.id.et_childname_cbd);
                        EditText CBD_inputText2 = view.findViewById(R.id.et_birthday_cbd);
                        EditText CBD_inputText3 = view.findViewById(R.id.et_birth_weight_cbd);
                        EditText CBD_inputText4 = view.findViewById(R.id.et_birth_size_cbd);
                        EditText CBD_inputText5 = view.findViewById(R.id.et_head_circumference_cbd);
                        EditText CBD_inputText6 = view.findViewById(R.id.et_eye_color_cbd);
                        EditText CBD_inputText7 = view.findViewById(R.id.et_hair_color_cbd);
                        EditText CBD_inputText8 = view.findViewById(R.id.et_star_sign_cbd);

                        // set Data from DB to Layout
                        CBD_inputText1.setText(currentBookData.CBD_inputText1);
                        CBD_inputText2.setText(currentBookData.CBD_inputText2);
                        CBD_inputText3.setText(currentBookData.CBD_inputText3);
                        CBD_inputText4.setText(currentBookData.CBD_inputText4);
                        CBD_inputText5.setText(currentBookData.CBD_inputText5);
                        CBD_inputText6.setText(currentBookData.CBD_inputText6);
                        CBD_inputText7.setText(currentBookData.CBD_inputText7);
                        CBD_inputText8.setText(currentBookData.CBD_inputText8);

                    }
                };
                // Set Bitmap Reuse...
                page4.setReuseBitmap(true);
                //page4.render(1240, 1754);


                final AbstractViewRenderer page5 = new AbstractViewRenderer(context, R.layout.fragment_birth){

                    @Override
                    protected void initView(View view) {

                        // get all EditText on Layout
                        EditText B_inputText1 = view.findViewById(R.id.et_b_contraction_start);
                        EditText B_inputText2 = view.findViewById(R.id.et_b_contr_start_time);
                        EditText B_inputText3 = view.findViewById(R.id.et_b_contr_start_date);
                        EditText B_inputText4 = view.findViewById(R.id.et_b_weather);
                        EditText B_inputText5 = view.findViewById(R.id.et_b_delivery_room_time);
                        EditText B_inputText6 = view.findViewById(R.id.et_b_delivery_room_duration);
                        EditText B_inputText7 = view.findViewById(R.id.et_b_location_of_birth);
                        EditText B_inputText8 = view.findViewById(R.id.et_b_name_midwife);
                        EditText B_inputText9 = view.findViewById(R.id.et_b_Hospital_Docs_midwife);
                        EditText B_inputText10 = view.findViewById(R.id.et_b_accompaniment);

                        // set Data from DB to Layout
                        B_inputText1.setText(currentBookData.B_inputText1);
                        B_inputText2.setText(currentBookData.B_inputText2);
                        B_inputText3.setText(currentBookData.B_inputText3);
                        B_inputText4.setText(currentBookData.B_inputText4);
                        B_inputText5.setText(currentBookData.B_inputText5);
                        B_inputText6.setText(currentBookData.B_inputText6);
                        B_inputText7.setText(currentBookData.B_inputText7);
                        B_inputText8.setText(currentBookData.B_inputText8);
                        B_inputText9.setText(currentBookData.B_inputText9);
                        B_inputText10.setText(currentBookData.B_inputText10);
                    }
                };
                // Set Bitmap Reuse...
                page5.setReuseBitmap(true);
                page5.render(1240, 1754);

                // ------------------------ FIRST PICS ---------------------------------------------
                final AbstractViewRenderer page6 = new AbstractViewRenderer(context, R.layout.fragment_cbd_pics) {

                    @Override
                    protected void initView(View view) {

                        // get all Elements on Layout
                        ImageView FI_image1 = (ImageView) view.findViewById(R.id.iv_cbd_fi_baby);
                        ImageView FI_image2 = (ImageView) view.findViewById(R.id.iv_cbd_fi_with_mum);

                        // get Path of Image from Database
                        String pathImage1 = currentBookData.FI_image1;
                        String pathImage2 = currentBookData.FI_image2;

                        // Load File
                        Bitmap bmp1 = BitmapFactory.decodeFile(pathImage1);
                        Bitmap bmp2 = BitmapFactory.decodeFile(pathImage2);

                        // Set Image to ImageView
                        FI_image1.setImageBitmap(bmp1);
                        FI_image2.setImageBitmap(bmp2);
                    }
                };
                // Set Bitmap Reuse...
                page6.setReuseBitmap(true);
                // ------------------------ NIGHTS --------------------------------------------------
                final AbstractViewRenderer page7 = new AbstractViewRenderer(context, R.layout.fragment_nights){

                    @Override
                    protected void initView(View view) {

                        // get all EditText on Layout
                        EditText N_inputText1 = view.findViewById(R.id.et_n_inputText_1);
                        EditText N_inputText2 = view.findViewById(R.id.et_n_inputText_2);
                        EditText N_inputText3 = view.findViewById(R.id.et_n_inputText_3);
                        EditText N_inputText4 = view.findViewById(R.id.et_n_inputText_4);
                        EditText N_inputText5 = view.findViewById(R.id.et_n_inputText_5);
                        EditText N_inputText6 = view.findViewById(R.id.et_n_inputText_6);

                        // set Data from DB to Layout
                        N_inputText1.setText(currentBookData.N_inputText1);
                        N_inputText2.setText(currentBookData.N_inputText2);
                        N_inputText3.setText(currentBookData.N_inputText3);
                        N_inputText4.setText(currentBookData.N_inputText4);
                        N_inputText5.setText(currentBookData.N_inputText5);
                        N_inputText6.setText(currentBookData.N_inputText6);
                    }
                };
                // Set Bitmap Reuse...
                page7.setReuseBitmap(true);

                // ------------------------ FIRST BATH ---------------------------------------------
                final AbstractViewRenderer page8 = new AbstractViewRenderer(context, R.layout.fragment_bath_1) {

                    @Override
                    protected void initView(View view) {

                        // get all Elements on Layout
                        ImageView FB_image1 = (ImageView) view.findViewById(R.id.iv_fb_baby1);
                        EditText FB_inputText1 = view.findViewById(R.id.et_fb_textinput_1);
                        EditText FB_inputText2 = view.findViewById(R.id.et_fb_textinput_2);

                        // get Path of Image from Database
                        String pathImage1 = currentBookData.FB_image1;

                        // Load File
                        Bitmap bmp1 = BitmapFactory.decodeFile(pathImage1);

                        // Set Image to ImageView
                        FB_image1.setImageBitmap(bmp1);
                        // set Data from DB to Layout
                        FB_inputText1.setText(currentBookData.FB_inputText1);
                        FB_inputText2.setText(currentBookData.FB_inputText2);
                    }
                };
                // Set Bitmap Reuse...
                page8.setReuseBitmap(true);

                final AbstractViewRenderer page9 = new AbstractViewRenderer(context, R.layout.fragment_bath_2) {

                    @Override
                    protected void initView(View view) {

                        // get all Elements on Layout
                        ImageView FB_image2 = (ImageView) view.findViewById(R.id.iv_fb_baby2);
                        EditText FB_inputText3 = view.findViewById(R.id.et_fb_textinput_3);
                        EditText FB_inputText4 = view.findViewById(R.id.et_fb_textinput_4);
                        EditText FB_inputText5 = view.findViewById(R.id.et_fb_textinput_5);

                        // get Path of Image from Database
                        String pathImage1 = currentBookData.FB_image2;

                        // Load File
                        Bitmap bmp1 = BitmapFactory.decodeFile(pathImage1);

                        // Set Image to ImageView
                        FB_image2.setImageBitmap(bmp1);
                        // set Data from DB to Layout
                        FB_inputText3.setText(currentBookData.FB_inputText3);
                        FB_inputText4.setText(currentBookData.FB_inputText4);
                        FB_inputText5.setText(currentBookData.FB_inputText5);
                    }
                };
                // Set Bitmap Reuse...
                page9.setReuseBitmap(true);

                // ------------------------ FIRST SNOW ---------------------------------------------
                final AbstractViewRenderer page10 = new AbstractViewRenderer(context, R.layout.fragment_first_snow_1){

                    @Override
                    protected void initView(View view) {

                        // get all EditText on Layout
                        EditText FS_inputText1 = view.findViewById(R.id.et_fs_textinput_1);
                        EditText FS_inputText2 = view.findViewById(R.id.et_fs_textinput_2);
                        EditText FS_inputText3 = view.findViewById(R.id.et_fs_textinput_3);
                        EditText FS_inputText4 = view.findViewById(R.id.et_fs_textinput_4);

                        // set Data from DB to Layout
                        FS_inputText1.setText(currentBookData.FS_inputText1);
                        FS_inputText2.setText(currentBookData.FS_inputText2);
                        FS_inputText3.setText(currentBookData.FS_inputText3);
                        FS_inputText4.setText(currentBookData.FS_inputText4);
                    }
                };
                // Set Bitmap Reuse...
                page10.setReuseBitmap(true);

                final AbstractViewRenderer page11 = new AbstractViewRenderer(context, R.layout.fragment_first_snow_2) {

                    @Override
                    protected void initView(View view) {

                        // get all Elements on Layout
                        ImageView FS_image1 = (ImageView) view.findViewById(R.id.iv_fs_baby1);
                        ImageView FS_image2 = (ImageView) view.findViewById(R.id.iv_fs_baby2);

                        // get Path of Image from Database
                        String pathImage1 = currentBookData.FS_image1;
                        String pathImage2 = currentBookData.FS_image2;

                        // Load File
                        Bitmap bmp1 = BitmapFactory.decodeFile(pathImage1);
                        Bitmap bmp2 = BitmapFactory.decodeFile(pathImage2);

                        // Set Image to ImageView
                        FS_image1.setImageBitmap(bmp1);
                        FS_image2.setImageBitmap(bmp2);
                    }
                };
                // Set Bitmap Reuse...
                page11.setReuseBitmap(true);

                // -------------------------- EASTER -----------------------------------------------
                final AbstractViewRenderer page12 = new AbstractViewRenderer(context, R.layout.fragment_easter_1) {

                    @Override
                    protected void initView(View view) {

                        // get all Elements on Layout
                        ImageView E_image1 = (ImageView) view.findViewById(R.id.iv_e_easter_gift_pic);
                        EditText E_inputText1 = view.findViewById(R.id.et_e_easter_location);
                        EditText E_inputText2 = view.findViewById(R.id.et_e_easter_gift);

                        // get Path of Image from Database
                        String pathImage1 = currentBookData.E_image1;

                        // Load File
                        Bitmap bmp1 = BitmapFactory.decodeFile(pathImage1);

                        // Set Image to ImageView
                        E_image1.setImageBitmap(bmp1);
                        // set Data from DB to Layout
                        E_inputText1.setText(currentBookData.E_inputText1);
                        E_inputText2.setText(currentBookData.E_inputText2);
                    }
                };
                // Set Bitmap Reuse...
                page12.setReuseBitmap(true);

                final AbstractViewRenderer page13 = new AbstractViewRenderer(context, R.layout.fragment_easter_2) {

                    @Override
                    protected void initView(View view) {

                        // get all Elements on Layout
                        ImageView E_image2 = (ImageView) view.findViewById(R.id.iv_e_easter_pic2);
                        ImageView E_image3 = (ImageView) view.findViewById(R.id.iv_e_easter_pic3);
                        ImageView E_image4 = (ImageView) view.findViewById(R.id.iv_e_easter_pic4);

                        // get Path of Image from Database
                        String pathImage1 = currentBookData.E_image2;
                        String pathImage2 = currentBookData.E_image3;
                        String pathImage3 = currentBookData.E_image4;

                        // Load File
                        Bitmap bmp1 = BitmapFactory.decodeFile(pathImage1);
                        Bitmap bmp2 = BitmapFactory.decodeFile(pathImage2);
                        Bitmap bmp3 = BitmapFactory.decodeFile(pathImage2);

                        // Set Image to ImageView
                        E_image2.setImageBitmap(bmp1);
                        E_image3.setImageBitmap(bmp2);
                        E_image4.setImageBitmap(bmp2);
                    }
                };
                // Set Bitmap Reuse...
                page13.setReuseBitmap(true);

                // -------------------------- TIME IS RUNNING --------------------------------------
                final AbstractViewRenderer page14 = new AbstractViewRenderer(context, R.layout.fragment_times_running_1) {

                    @Override
                    protected void initView(View view) {

                        // get all Elements on Layout
                        EditText TIR_inputText1 = view.findViewById(R.id.et_tir_inputText_1);
                        EditText TIR_inputText2 = view.findViewById(R.id.et_tir_inputText_2);
                        EditText TIR_inputText3 = view.findViewById(R.id.et_tir_inputText_3);
                        EditText TIR_inputText4 = view.findViewById(R.id.et_tir_inputText_4);
                        ImageView TIR_image1 = (ImageView) view.findViewById(R.id.iv_tir_pic_1);


                        // get Path of Image from Database
                        String pathImage1 = currentBookData.TIR_image1;

                        // Load File
                        Bitmap bmp1 = BitmapFactory.decodeFile(pathImage1);

                        // Set Image to ImageView
                        TIR_image1.setImageBitmap(bmp1);
                        // set Data from DB to Layout
                        TIR_inputText1.setText(currentBookData.TIR_inputText1);
                        TIR_inputText2.setText(currentBookData.TIR_inputText2);
                        TIR_inputText3.setText(currentBookData.TIR_inputText3);
                        TIR_inputText4.setText(currentBookData.TIR_inputText4);
                    }
                };
                // Set Bitmap Reuse...
                page14.setReuseBitmap(true);

                final AbstractViewRenderer page15 = new AbstractViewRenderer(context, R.layout.fragment_times_running_2_oou) {

                    @Override
                    protected void initView(View view) {

                        // get all Elements on Layout
                        EditText TIR_inputText5 = view.findViewById(R.id.et_tir_inputText_5);
                        EditText TIR_inputText6 = view.findViewById(R.id.et_tir_inputText_6);
                        ImageView TIR_image2 = (ImageView) view.findViewById(R.id.iv_tir_pic_2);


                        // get Path of Image from Database
                        String pathImage1 = currentBookData.TIR_image2;

                        // Load File
                        Bitmap bmp1 = BitmapFactory.decodeFile(pathImage1);

                        // Set Image to ImageView
                        TIR_image2.setImageBitmap(bmp1);
                        // set Data from DB to Layout
                        TIR_inputText5.setText(currentBookData.TIR_inputText5);
                        TIR_inputText6.setText(currentBookData.TIR_inputText6);
                    }
                };
                // Set Bitmap Reuse...
                page15.setReuseBitmap(true);

                // ------------------------ BAPTISM ---------------------------------------------
                final AbstractViewRenderer page16 = new AbstractViewRenderer(context, R.layout.fragment_baptism_1){

                    @Override
                    protected void initView(View view) {

                        // get all EditText on Layout
                        EditText BTM_inputText1 = view.findViewById(R.id.et_btm_inputText_1_0);
                        EditText BTM_inputText2 = view.findViewById(R.id.et_btm_inputText_1_1);
                        EditText BTM_inputText3 = view.findViewById(R.id.et_btm_inputText_1_2);
                        EditText BTM_inputText4 = view.findViewById(R.id.et_btm_inputText_2);
                        EditText BTM_inputText5 = view.findViewById(R.id.et_btm_inputText_3);
                        EditText BTM_inputText6 = view.findViewById(R.id.et_btm_inputText_4);
                        EditText BTM_inputText7 = view.findViewById(R.id.et_btm_inputText_5);
                        EditText BTM_inputText8 = view.findViewById(R.id.et_btm_inputText_6);
                        EditText BTM_inputText9 = view.findViewById(R.id.et_btm_inputText_7);
                        EditText BTM_inputText10 = view.findViewById(R.id.et_btm_inputText_8);

                        // set Data from DB to Layout
                        BTM_inputText1.setText(currentBookData.BTM_inputText1);
                        BTM_inputText2.setText(currentBookData.BTM_inputText2);
                        BTM_inputText3.setText(currentBookData.BTM_inputText3);
                        BTM_inputText4.setText(currentBookData.BTM_inputText4);
                        BTM_inputText5.setText(currentBookData.BTM_inputText5);
                        BTM_inputText6.setText(currentBookData.BTM_inputText6);
                        BTM_inputText7.setText(currentBookData.BTM_inputText7);
                        BTM_inputText8.setText(currentBookData.BTM_inputText8);
                        BTM_inputText9.setText(currentBookData.BTM_inputText9);
                        BTM_inputText10.setText(currentBookData.BTM_inputText10);
                    }
                };
                // Set Bitmap Reuse...
                page16.setReuseBitmap(true);

                final AbstractViewRenderer page17 = new AbstractViewRenderer(context, R.layout.fragment_baptism_2) {

                    @Override
                    protected void initView(View view) {

                        // get all Elements on Layout
                        EditText BTM_inputText9 = view.findViewById(R.id.et_btm_inputText_9);
                        EditText BTM_inputText10 = view.findViewById(R.id.et_btm_inputText_10);
                        ImageView BTM_image1 = (ImageView) view.findViewById(R.id.iv_btm_pic1);
                        ImageView BTM_image2 = (ImageView) view.findViewById(R.id.iv_btm_pic2);
                        ImageView BTM_image3 = (ImageView) view.findViewById(R.id.iv_btm_pic3);


                        // get Path of Image from Database
                        String pathImage1 = currentBookData.BTM_image1;
                        String pathImage2 = currentBookData.BTM_image2;
                        String pathImage3 = currentBookData.BTM_image3;

                        // Load File
                        Bitmap bmp1 = BitmapFactory.decodeFile(pathImage1);
                        Bitmap bmp2 = BitmapFactory.decodeFile(pathImage2);
                        Bitmap bmp3 = BitmapFactory.decodeFile(pathImage3);

                        // Set Image to ImageView
                        BTM_image1.setImageBitmap(bmp1);
                        BTM_image2.setImageBitmap(bmp2);
                        BTM_image3.setImageBitmap(bmp3);
                        // set Data from DB to Layout
                        BTM_inputText9.setText(currentBookData.BTM_inputText9);
                        BTM_inputText10.setText(currentBookData.BTM_inputText10);
                    }
                };
                // Set Bitmap Reuse...
                page17.setReuseBitmap(true);

                // ------------------------ FIRST STEPS ---------------------------------------------
                final AbstractViewRenderer page18 = new AbstractViewRenderer(context, R.layout.fragment_first_steps_1){

                    @Override
                    protected void initView(View view) {

                        // get all EditText on Layout
                        EditText FST_inputText1 = view.findViewById(R.id.et_fst_textInput_1);
                        EditText FST_inputText2 = view.findViewById(R.id.et_fst_textInput_2);
                        EditText FST_inputText3 = view.findViewById(R.id.et_fst_textInput_3);
                        EditText FST_inputText4 = view.findViewById(R.id.et_fst_textInput_4);
                        EditText FST_inputText5 = view.findViewById(R.id.et_fst_textInput_5);

                        // set Data from DB to Layout
                        FST_inputText1.setText(currentBookData.FST_inputText1);
                        FST_inputText2.setText(currentBookData.FST_inputText2);
                        FST_inputText3.setText(currentBookData.FST_inputText3);
                        FST_inputText4.setText(currentBookData.FST_inputText4);
                        FST_inputText5.setText(currentBookData.FST_inputText5);
                    }
                };
                // Set Bitmap Reuse...
                page18.setReuseBitmap(true);

                final AbstractViewRenderer page19 = new AbstractViewRenderer(context, R.layout.fragment_first_steps_2) {

                    @Override
                    protected void initView(View view) {

                        // get all Elements on Layout
                        ImageView FST_image1 = (ImageView) view.findViewById(R.id.iv_fst_pic_1);
                        ImageView FST_image2 = (ImageView) view.findViewById(R.id.iv_fst_pic_2);
                        ImageView FST_image3 = (ImageView) view.findViewById(R.id.iv_fst_pic_3);


                        // get Path of Image from Database
                        String pathImage1 = currentBookData.FST_image1;
                        String pathImage2 = currentBookData.FST_image2;
                        String pathImage3 = currentBookData.FST_image3;

                        // Load File
                        Bitmap bmp1 = BitmapFactory.decodeFile(pathImage1);
                        Bitmap bmp2 = BitmapFactory.decodeFile(pathImage2);
                        Bitmap bmp3 = BitmapFactory.decodeFile(pathImage3);

                        // Set Image to ImageView
                        FST_image1.setImageBitmap(bmp1);
                        FST_image2.setImageBitmap(bmp2);
                        FST_image3.setImageBitmap(bmp3);
                    }
                };
                // Set Bitmap Reuse...
                page19.setReuseBitmap(true);


                // Create Path for Storing PDF
                File direct = new File(Environment.getExternalStorageDirectory().getPath() + "/de.kdsofworx.ls_babyalbum2/pdfForOrder/");
                // Create a FilePath and Imagename for Image
                //File file = new File(direct, "MyBabybook_UID" + userLokalStore.getLoggedInUser().getUserId() + "_RID" + userLokalStore.getCurrentRecordId());
                String file = "MyBabybook_UID" + userLokalStore.getLoggedInUser().getUserId() + "_RID" + userLokalStore.getCurrentRecordId();

                // Check if Directory exist - if not -> Create Folder
                if (!direct.exists()) {
                    File wallpaperDirectory = new File(Environment.getExternalStorageDirectory().getPath() + "/de.kdsofworx.ls_babyalbum2/pdfForOrder/");
                    wallpaperDirectory.mkdirs();
                }


                PdfDocument pdfDoc = new PdfDocument(context);

                // add as many pages as you have
                if (true) { // Check if user has selected Page
                    pdfDoc.addPage(page1);
                }
                if (true) { // Check if user has selected Page
                    pdfDoc.addPage(page2);
                }
                if (true) { // Check if user has selected Page
                    pdfDoc.addPage(page3);
                }
                if (true) { // Check if user has selected Page
                    pdfDoc.addPage(page4);
                }
                if (true) { // Check if user has selected Page

                    page5.render(page5.getWidth(), page5.getHeight());

                    int width = page5.getWidth()/2;
                    int height = page5.getHeight()/2;
                    page5.render(width, height);
                    pdfDoc.addPage(page5);
                }
                if (true) { // Check if user has selected Page
                    pdfDoc.addPage(page6);
                }
                if (true) { // Check if user has selected Page
                    pdfDoc.addPage(page7);
                }
                if (true) { // Check if user has selected Page
                    pdfDoc.addPage(page8);
                }
                if (true) { // Check if user has selected Page
                    pdfDoc.addPage(page9);
                }
                if (true) { // Check if user has selected Page
                    pdfDoc.addPage(page10);
                }
                if (true) { // Check if user has selected Page
                    pdfDoc.addPage(page11);
                }
                if (true) { // Check if user has selected Page
                    pdfDoc.addPage(page12);
                }
                if (true) { // Check if user has selected Page
                    pdfDoc.addPage(page13);
                }
                if (true) { // Check if user has selected Page
                    pdfDoc.addPage(page14);
                }
                if (true) { // Check if user has selected Page
                    pdfDoc.addPage(page15);
                }
                if (true) { // Check if user has selected Page
                    pdfDoc.addPage(page16);
                }
                if (true) { // Check if user has selected Page
                    pdfDoc.addPage(page17);
                }
                if (true) { // Check if user has selected Page
                    pdfDoc.addPage(page18);
                }
                if (true) { // Check if user has selected Page
                    pdfDoc.addPage(page19);
                }

               // pdfDoc.setRenderWidth(1240);
                pdfDoc.setRenderWidth(2480);
               // pdfDoc.setRenderHeight(1754);
                pdfDoc.setRenderHeight(3508);
                pdfDoc.setOrientation(PdfDocument.A4_MODE.PORTRAIT);
                pdfDoc.setProgressTitle(R.string.m_CreatePDFMessageTitle);
                pdfDoc.setProgressMessage(R.string.m_CreatePDFMessageText);
                pdfDoc.setFileName(file);
                pdfDoc.setSaveDirectory(direct);
                pdfDoc.setInflateOnMainThread(false);
                pdfDoc.setListener(new PdfDocument.Callback() {
                    @Override
                    public void onComplete(File file) {
                        Log.i(PdfDocument.TAG_PDF_MY_XML, "Complete");
                    }

                    @Override
                    public void onError(Exception e) {
                        Log.i(PdfDocument.TAG_PDF_MY_XML, "Error");
                    }
                });

                pdfDoc.createPdf(context);
            }
        });
    }

}
