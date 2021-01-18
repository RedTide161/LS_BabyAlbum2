package de.kdsoftworx.ls_babyalbum2.Helper;

import android.content.Context;
import android.util.AttributeSet;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
//import android.widget.EditText;
import androidx.appcompat.widget.AppCompatEditText;

// An EditText that lets you use actions ("Done", "Go", etc.) on multi-line edits.
public class ActionNextEditText extends AppCompatEditText
{
    public ActionNextEditText(Context context)
    {
        super(context);
    }

    public ActionNextEditText(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public ActionNextEditText(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
    }

    @Override
    public InputConnection onCreateInputConnection(EditorInfo outAttrs)
    {
        InputConnection conn = super.onCreateInputConnection(outAttrs);
        outAttrs.imeOptions &= ~EditorInfo.IME_FLAG_NO_ENTER_ACTION;
        outAttrs.imeOptions &= EditorInfo.IME_ACTION_NEXT;
        return conn;

    }
}
