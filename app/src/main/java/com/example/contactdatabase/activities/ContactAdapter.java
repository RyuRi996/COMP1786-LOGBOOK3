package com.example.contactdatabase.activities;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.contactdatabase.R;
import com.example.contactdatabase.models.Detail;

import java.util.List;

import pl.droidsonroids.gif.GifImageView;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder> {
    private List<Detail> detailList;
    private OnClickListener onClickListener;

    public ContactAdapter(List<Detail> detailList, OnClickListener onClickListener) {
        this.detailList = detailList;
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view_card, parent, false);
        return new ContactViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
        Detail detail = detailList.get(position);
        holder.avatar.setImageResource(detail.avatar);
        holder.name.setText(detail.name);
        holder.dob.setText(detail.dob);
        holder.email.setText(detail.email);
        holder.phone.setText(detail.phone);

        holder.delete.setOnClickListener(v -> {
            if (onClickListener !=null){
                onClickListener.onDeleteClick(detailList.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return detailList.size();
    }

    public interface OnClickListener {
        void onDeleteClick(Detail detail);
        void deleteAll();
    }

    public static class ContactViewHolder extends RecyclerView.ViewHolder {
        TextView name, dob, email, phone;
        GifImageView avatar;
        ImageView delete;
        public ContactViewHolder(@NonNull View itemView) {
            super(itemView);
            avatar = itemView.findViewById(R.id.cardAvatar);
            name = itemView.findViewById(R.id.cardName);
            dob = itemView.findViewById(R.id.cartDob);
            email = itemView.findViewById(R.id.cardEmail);
            delete = itemView.findViewById(R.id.deleteButton);
            phone = itemView.findViewById(R.id.cardPhone);
        }
    }
}
