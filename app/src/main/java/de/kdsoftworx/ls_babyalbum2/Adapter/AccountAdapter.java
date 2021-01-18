package de.kdsoftworx.ls_babyalbum2.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import de.kdsoftworx.ls_babyalbum2.R;
import de.kdsoftworx.ls_babyalbum2.RoomDatabase.LSBookdata;

public class AccountAdapter extends RecyclerView.Adapter<AccountAdapter.AccountHolder> {

    List<LSBookdata> accounts = new ArrayList<>();

    @NonNull
    @Override
    public AccountHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.account_item, parent, false);

        return new AccountHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AccountHolder holder, int position) {
        LSBookdata lsBookdata = accounts.get(position);

            holder.tv_accountNumber.setText(String.valueOf(position+1) + ".");
            holder.tv_accountName.setText(lsBookdata.child_name);
    }

    @Override
    public int getItemCount() {
        return accounts.size();
    }

    public void setAccounts(List<LSBookdata> lsBookdata)
    {
            this.accounts = lsBookdata;
            notifyDataSetChanged();
    }

    public LSBookdata getAccoutAtPosX (int position)
    {
        return accounts.get(position);
    }

    class AccountHolder extends RecyclerView.ViewHolder{
        private TextView tv_accountNumber;
        private TextView tv_accountName;


        public AccountHolder(@NonNull View itemView) {
            super(itemView);

            tv_accountNumber = itemView.findViewById(R.id.tv_accountNumber);
            tv_accountName = itemView.findViewById(R.id.tv_accountName);
        }
    }
}
