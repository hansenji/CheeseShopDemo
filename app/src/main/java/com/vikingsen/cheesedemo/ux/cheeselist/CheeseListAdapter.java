package com.vikingsen.cheesedemo.ux.cheeselist;


import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.RequestManager;
import com.devbrackets.android.recyclerext.adapter.viewholder.ClickableViewHolder;
import com.vikingsen.cheesedemo.BuildConfig;
import com.vikingsen.cheesedemo.R;
import com.vikingsen.cheesedemo.model.database.cheese.Cheese;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

class CheeseListAdapter extends RecyclerView.Adapter<CheeseListAdapter.CheeseViewHolder> {

    @NonNull
    private final RequestManager glideRequestManager;

    @Nullable
    private OnClickListener onClickListener = null;
    @NonNull
    private List<Cheese> cheeseList = new ArrayList<>();

    CheeseListAdapter(@NonNull RequestManager glideRequestManager) {
        this.glideRequestManager = glideRequestManager;
    }

    @Override
    public CheeseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CheeseViewHolder cheeseViewHolder = new CheeseViewHolder(parent);
        cheeseViewHolder.setOnClickListener(viewHolder -> {
            if (onClickListener != null) {
                onClickListener.onClick(cheeseList.get(viewHolder.getAdapterPosition()));
            }
        });
        return cheeseViewHolder;
    }

    @Override
    public void onBindViewHolder(CheeseViewHolder holder, int position) {
        Cheese cheese = cheeseList.get(position);
        holder.icTextView.setText(cheese.getName());
        holder.icImageView.setContentDescription(cheese.getName());

        glideRequestManager.load(BuildConfig.IMAGE_BASE_URL + cheese.getImageUrl())
                .centerCrop()
                .error(R.drawable.ic_broken_image_white_48dp)
                .into(holder.icImageView);
    }

    @Override
    public int getItemCount() {
        return cheeseList.size();
    }

    void setCheeseList(@NonNull List<Cheese> cheeseList) {
        this.cheeseList = cheeseList;
        notifyDataSetChanged();
    }

    void setOnClickListener(@Nullable OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    interface OnClickListener {
        void onClick(Cheese cheese);
    }

    static class CheeseViewHolder extends ClickableViewHolder {
        @BindView(R.id.icImageView)
        ImageView icImageView;
        @BindView(R.id.icTextView)
        TextView icTextView;

        CheeseViewHolder(@NonNull ViewGroup parent) {
            super(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cheese, parent, false));
            ButterKnife.bind(this, itemView);
        }
    }
}
