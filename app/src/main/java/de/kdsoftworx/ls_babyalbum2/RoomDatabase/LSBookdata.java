package de.kdsoftworx.ls_babyalbum2.RoomDatabase;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

@Entity(tableName = "LSBookdata")
public class LSBookdata {


    // declare all Columns of the Table LSBookdata
    @PrimaryKey(autoGenerate = true)
    @NotNull
    @ColumnInfo(name = "id")
    public int id;

    @ColumnInfo(name = "customer_id")
    public int customer_id;
    public String child_name;

    public String HT_inputText1;
    public String HT_inputText2;

    public String P_inputText1;
    public String P_inputText2;
    public String P_inputText3;
    public String P_inputText4;
    public String P_inputText5;

    public String P_image1;
    public String P_image2;
    public String P_image3;

    public String CBD_inputText1;
    public String CBD_inputText2;
    public String CBD_inputText3;
    public String CBD_inputText4;
    public String CBD_inputText5;
    public String CBD_inputText6;
    public String CBD_inputText7;
    public String CBD_inputText8;

    public String B_inputText1;
    public String B_inputText2;
    public String B_inputText3;
    public String B_inputText4;
    public String B_inputText5;
    public String B_inputText6;
    public String B_inputText7;
    public String B_inputText8;
    public String B_inputText9;
    public String B_inputText10;

    public String FI_image1;
    public String FI_image2;

    public String N_inputText1;
    public String N_inputText2;
    public String N_inputText3;
    public String N_inputText4;
    public String N_inputText5;
    public String N_inputText6;

    public String FB_inputText1;
    public String FB_inputText2;

    public String FB_image1;
    public String FB_image2;

    public String FB_inputText3;
    public String FB_inputText4;
    public String FB_inputText5;

    public String FS_inputText1;
    public String FS_inputText2;
    public String FS_inputText3;
    public String FS_inputText4;

    public String FS_image1;
    public String FS_image2;

    public String E_inputText1;
    public String E_inputText2;

    public String E_image1;
    public String E_image2;
    public String E_image3;
    public String E_image4;

    public String TIR_inputText1;
    public String TIR_inputText2;
    public String TIR_inputText3;
    public String TIR_inputText4;

    public String TIR_image1;
    public String TIR_image2;

    public String TIR_inputText5;
    public String TIR_inputText6;

    public String BTM_inputText1;
    public String BTM_inputText2;
    public String BTM_inputText3;
    public String BTM_inputText4;
    public String BTM_inputText5;
    public String BTM_inputText6;
    public String BTM_inputText7;
    public String BTM_inputText8;
    public String BTM_inputText9;
    public String BTM_inputText10;
    public String BTM_inputText11;
    public String BTM_inputText12;

    public String BTM_image1;
    public String BTM_image2;
    public String BTM_image3;

    public String FST_inputText1;
    public String FST_inputText2;
    public String FST_inputText3;
    public String FST_inputText4;
    public String FST_inputText5;

    public String FST_image1;
    public String FST_image2;
    public String FST_image3;

}
