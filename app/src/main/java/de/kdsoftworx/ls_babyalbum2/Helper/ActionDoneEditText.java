package de.kdsoftworx.ls_babyalbum2.Helper;


import android.content.Context;
import android.util.AttributeSet;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import androidx.appcompat.widget.AppCompatEditText;

public class ActionDoneEditText extends AppCompatEditText
{
    public ActionDoneEditText(Context context)
    {
        super(context);
    }

    public ActionDoneEditText(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public ActionDoneEditText(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
    }

    @Override
    public InputConnection onCreateInputConnection(EditorInfo outAttrs)
    {
        InputConnection conn = super.onCreateInputConnection(outAttrs);
        outAttrs.imeOptions &= ~EditorInfo.IME_FLAG_NO_ENTER_ACTION;
        return conn;
    }
}