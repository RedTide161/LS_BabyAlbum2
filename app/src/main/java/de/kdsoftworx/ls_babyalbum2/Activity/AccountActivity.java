package de.kdsoftworx.ls_babyalbum2.Activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.sqlite.db.SimpleSQLiteQuery;
import androidx.sqlite.db.SupportSQLiteQuery;

import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import de.kdsoftworx.ls_babyalbum2.Adapter.AccountAdapter;
import de.kdsoftworx.ls_babyalbum2.Data.UserLokalStore;
import de.kdsoftworx.ls_babyalbum2.Helper.RecyclerItemClickListener;
import de.kdsoftworx.ls_babyalbum2.Login.LoginActivity;
import de.kdsoftworx.ls_babyalbum2.R;
import de.kdsoftworx.ls_babyalbum2.RoomDatabase.LSBookdata;
import de.kdsoftworx.ls_babyalbum2.ViewModel.BookdataViewModel;

public class AccountActivity extends AppCompatActivity {

    private BookdataViewModel bookdataViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.tv_ao_headline);
        setContentView(R.layout.activity_child_accounts);

        // get floating ActionButton
        FloatingActionButton bAddAccount = findViewById(R.id.fabAddAccount);
        final TextView tvEmptyAccountList = findViewById(R.id.tv_empty_account_list);


        // create AccountList and add AccountAdapter
        LinearLayoutManager layoutmanager = new LinearLayoutManager(this);
        layoutmanager.setOrientation(LinearLayoutManager.VERTICAL);

        final RecyclerView recyclerView = findViewById(R.id.rv_account_list);
        recyclerView.setLayoutManager(layoutmanager);
        recyclerView.setHasFixedSize(true);

        final AccountAdapter adapter = new AccountAdapter();
        recyclerView.setAdapter(adapter);

        // get newest Data from Database
        bookdataViewModel = ViewModelProviders.of(this).get(BookdataViewModel.class);
        bookdataViewModel.getAllEntries().observe(this, new Observer<List<LSBookdata>>() {
            @Override
            public void onChanged(List<LSBookdata> lsBookdata) {

                if (lsBookdata.isEmpty()) {
                    recyclerView.setVisibility(View.GONE);
                    tvEmptyAccountList.setVisibility(View.VISIBLE);

                } else {
                    recyclerView.setVisibility(View.VISIBLE);
                    tvEmptyAccountList.setVisibility(View.GONE);
                    adapter.setAccounts(lsBookdata);
                }
            }
        });

        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {

                        // Get an Instance of User-Object
                        UserLokalStore userLokalStore = UserLokalStore.getInstance(getApplicationContext());
                        // Get selected Data from RecyclerView
                        final LSBookdata lsBookdata = adapter.getAccoutAtPosX(position);
                        // Store current Book-Record-ID
                        userLokalStore.setCurrentRecordId(lsBookdata.id);

                        // Go to Book-Area
                        Intent intent = new Intent(AccountActivity.this, ChildDataActivity.class);
                        AccountActivity.this.startActivity(intent);
                    }
                })
        );

        // react of actions on RecyclerView
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            private Drawable icon, iconDelete, iconEdit;
            private ColorDrawable background, backgroundDelete, backgroundEdit;

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull final RecyclerView.ViewHolder viewHolder, int direction) {
                // get context
                Context context = viewHolder.itemView.getContext();

                if (direction == ItemTouchHelper.LEFT) {
                    // Show AlertDialog -> Data will be deleted
                    showAlertDialog(viewHolder, adapter);

                }

                if (direction == ItemTouchHelper.RIGHT) {
                    // Show EditDialog -> Data will be edited
                    showEditDialog(viewHolder, adapter);

                }


            }

            @Override
            public void onSelectedChanged(@Nullable RecyclerView.ViewHolder viewHolder, int actionState) {
                super.onSelectedChanged(viewHolder, actionState);
            }

            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull final RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                // Add Red Background and Recycle-Bin in event of delete --------------------------------------------------
                // preset Variables with Icon and Color for Background (swipe action - delete data)
                iconDelete = ContextCompat.getDrawable(viewHolder.itemView.getContext(),
                        R.drawable.ic_delete_white_24dp);
                iconEdit = ContextCompat.getDrawable(viewHolder.itemView.getContext(),
                        R.drawable.ic_mode_edit_white_24dp);
                icon = iconEdit;
                backgroundDelete = new ColorDrawable(Color.RED);
                backgroundEdit = new ColorDrawable(Color.LTGRAY);

                View itemView = viewHolder.itemView;
                int backgroundCornerOffset = 20; //so background is behind the rounded corners of itemView

                int iconMargin = (itemView.getHeight() - iconDelete.getIntrinsicHeight()) / 2;
                int iconTop = itemView.getTop() + (itemView.getHeight() - iconDelete.getIntrinsicHeight()) / 2;
                int iconBottom = iconTop + iconDelete.getIntrinsicHeight();

                if (dX > 0) { // Swiping to the right
                    int iconLeft = itemView.getLeft() + iconMargin + iconEdit.getIntrinsicWidth();
                    int iconRight = itemView.getLeft() + iconMargin;
                    iconEdit.setBounds(iconRight, iconTop, iconLeft, iconBottom);

                    backgroundEdit.setBounds(itemView.getLeft(), itemView.getTop(),
                            itemView.getLeft() + ((int) dX) + backgroundCornerOffset, itemView.getBottom());

                    icon = iconEdit;
                    background = backgroundEdit;
                } else if (dX < 0) { // Swiping to the left
                    int iconLeft = itemView.getRight() - iconMargin - iconDelete.getIntrinsicWidth();
                    int iconRight = itemView.getRight() - iconMargin;
                    iconDelete.setBounds(iconLeft, iconTop, iconRight, iconBottom);

                    backgroundDelete.setBounds(itemView.getRight() + ((int) dX) - backgroundCornerOffset,
                            itemView.getTop(), itemView.getRight(), itemView.getBottom());

                    icon = iconDelete;
                    background = backgroundDelete;
                } else { // view is unSwiped
                    backgroundDelete.setBounds(0, 0, 0, 0);
                    background = backgroundDelete;
                }
                background.draw(c);
                icon.draw(c);
                //-----------------------------------------------------------------------------------------------------------
            }
        }).attachToRecyclerView(recyclerView);

        // Floating Action Button for adding new Babybooks
        bAddAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // show Dialog for adding an Account
                showAddDialog(view);
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();

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
        UserLokalStore userLokalStore = UserLokalStore.getInstance(getApplicationContext());

        if (id == R.id.action_logout) {
            userLokalStore.setUserLoggedIn(false);
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

            startActivity(new Intent(this, LoginActivity.class));

            // destroy the current Activity from BackStack
            finish();

        }

        return true;
    }
    //-----------------------------------------------------------------------------------------------------------------------------------


    @Override
    public void onBackPressed() {
        super.onBackPressed();

        moveTaskToBack(true);
    }

    public void showAddDialog(View view) {
        final AlertDialog dialogBuilder = new AlertDialog.Builder(this).create();
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.custom_dialog, null);

        final EditText et_accountChildName = (EditText) dialogView.findViewById(R.id.et_cd_childname);
        Button bCreate = (Button) dialogView.findViewById(R.id.b_create);
        Button bCancel = (Button) dialogView.findViewById(R.id.b_cancel);

        bCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogBuilder.dismiss();
            }
        });

        bCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                UserLokalStore userLokalStore = UserLokalStore.getInstance(getApplicationContext());

                try {
                    // Check if Data already exist in Database - if not a new record is generated in Database
                    bookdataViewModel.checkForDataAvailable(userLokalStore.getLoggedInUser().getUserId(), et_accountChildName.getText().toString());


                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Fehler: " + e, Toast.LENGTH_LONG).show();
                }

                dialogBuilder.dismiss();
            }

        });
        dialogBuilder.setView(dialogView);
        dialogBuilder.show();
    }

    public void showEditDialog(final RecyclerView.ViewHolder viewHolder, final AccountAdapter adapter) {
        final AlertDialog dialogBuilder = new AlertDialog.Builder(this).create();
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.custom_dialog, null);

        // Get Data from RecyclerView to edit
        final LSBookdata lsBookdata = adapter.getAccoutAtPosX(viewHolder.getAdapterPosition());

        final EditText et_accountChildName = (EditText) dialogView.findViewById(R.id.et_cd_childname);
        et_accountChildName.setText(lsBookdata.child_name);

        final TextView tv_editDialogHeadline = (TextView) dialogView.findViewById(R.id.tv_dialog_headline);
        tv_editDialogHeadline.setText(R.string.tv_editdialog_headline);

        Button bCreate = (Button) dialogView.findViewById(R.id.b_create);
        bCreate.setText(R.string.bao_edit_text); // Change Button Text


        Button bCancel = (Button) dialogView.findViewById(R.id.b_cancel);

        bCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogBuilder.dismiss();
                // refresh Account Overview
                adapter.notifyItemChanged(viewHolder.getAdapterPosition());
            }
        });

        bCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                UserLokalStore userLokalStore = UserLokalStore.getInstance(getApplicationContext());

                // Create query for updating Child_name in Database
                SupportSQLiteQuery query = new SimpleSQLiteQuery("UPDATE " + getString(R.string.tableName) + " " +
                        "SET child_name = '" + et_accountChildName.getText().toString() + "' WHERE customer_id = " + userLokalStore.getLoggedInUser().getUserId() + " AND id = " + lsBookdata.id);

                try {
                    // update Child_Name in Database
                    bookdataViewModel.updateChildName(et_accountChildName.getText().toString(), userLokalStore.getLoggedInUser().getUserId(), lsBookdata.id);

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Fehler: " + e, Toast.LENGTH_LONG).show();
                }

                dialogBuilder.dismiss();
                // refresh Account Overview
                adapter.notifyItemChanged(viewHolder.getAdapterPosition());

            }
        });
        dialogBuilder.setView(dialogView);
        dialogBuilder.show();
    }

    public void showAlertDialog(final RecyclerView.ViewHolder viewHolder, final AccountAdapter adapter){

        // Show alertDialog if Data where deleted
        final AlertDialog dialogBuilder = new AlertDialog.Builder(this).create();
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.custom_alertdialog, null);

        final TextView tv_headline = (TextView) dialogView.findViewById(R.id.tv_alertdialog_headline);
        Button bOk = (Button) dialogView.findViewById(R.id.b_ad_ok);
        Button bCancel = (Button) dialogView.findViewById(R.id.b_ad_cancel);

        tv_headline.setText(R.string.m_RequestDeleteAccout);

        // customize button an add ClickListener
        bOk.setText(R.string.bao_ok_text);
        bOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bookdataViewModel.delete(adapter.getAccoutAtPosX(viewHolder.getAdapterPosition()));
                dialogBuilder.dismiss();
                Toast.makeText(getApplicationContext(), R.string.m_AccountDeleted, Toast.LENGTH_LONG).show();
            }
        });

        // customize button an add ClickListener
        bCancel.setText(R.string.bao_cancel_text);
        bCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialogBuilder.dismiss();
                // refresh Account Overview
                adapter.notifyItemChanged(viewHolder.getAdapterPosition());
            }
        });
        dialogBuilder.setView(dialogView);
        dialogBuilder.show();
    }
}
