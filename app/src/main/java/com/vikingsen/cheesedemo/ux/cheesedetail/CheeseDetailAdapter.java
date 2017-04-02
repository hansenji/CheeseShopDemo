package com.vikingsen.cheesedemo.ux.cheesedetail;


import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.devbrackets.android.recyclerext.adapter.RecyclerHeaderAdapter;
import com.vikingsen.cheesedemo.R;
import com.vikingsen.cheesedemo.model.database.cheese.Cheese;
import com.vikingsen.cheesedemo.model.database.comment.Comment;

import org.threeten.bp.LocalDate;
import org.threeten.bp.format.DateTimeFormatter;
import org.threeten.bp.format.FormatStyle;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

class CheeseDetailAdapter extends RecyclerHeaderAdapter<CheeseDetailAdapter.HeaderViewHolder, RecyclerView.ViewHolder> {

    private static final long COMMENT_HEADER_ID = 1;

    private static final int POSITION_PRICE = 0;
    private static final int POSITION_DESCRIPTION = 1;
    private static final int POSITION_FIRST_COMMENT = 2;

    private static final int TYPE_PRICE = 1;
    private static final int TYPE_DESCRIPTION = 2;
    private static final int TYPE_COMMENT = 3;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT);


    @Nullable
    private Cheese cheese = null;
    @NonNull
    private List<Comment> comments = new ArrayList<>();

    @NonNull
    @Override
    public HeaderViewHolder onCreateHeaderViewHolder(@NonNull ViewGroup parent, int viewType) {
        Timber.d("Creating Header View HOlder");
        return new HeaderViewHolder(parent);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateChildViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_PRICE:
                return createPriceViewHolder(parent);
            case TYPE_DESCRIPTION:
                return new DescriptionViewHolder(parent);
            case TYPE_COMMENT:
                return new CommentViewHolder(parent);
            default:
                throw new IllegalArgumentException("Invalid type " + viewType);
        }
    }

    @Override
    public void onBindHeaderViewHolder(@NonNull HeaderViewHolder holder, int firstChildPosition) {
        holder.headerTextView.setText(R.string.comments);
    }

    @Override
    public void onBindChildViewHolder(@NonNull RecyclerView.ViewHolder holder, int childPosition) {
        if (holder instanceof PriceViewHolder) {
            bindPriceViewHolder((PriceViewHolder) holder);
        } else if (holder instanceof DescriptionViewHolder) {
            bindDescriptionViewHolder((DescriptionViewHolder) holder);
        } else if (holder instanceof CommentViewHolder) {
            bindCommentViewHolder((CommentViewHolder) holder, childPosition - POSITION_FIRST_COMMENT);
        }
    }

    @Override
    public int getChildCount() {
        if (cheese == null) {
            return 0;
        }
        int count = 2;
        if (comments.isEmpty()) {
            count++;
        }
        return count + comments.size();
    }

    @Override
    public long getHeaderId(int childPosition) {
        if (childPosition >= POSITION_FIRST_COMMENT) {
            return COMMENT_HEADER_ID;
        }
        return RecyclerView.NO_ID;
    }

    @Override
    public int getChildViewType(int childPosition) {
        switch (childPosition) {
            case POSITION_PRICE:
                return TYPE_PRICE;
            case POSITION_DESCRIPTION:
                return TYPE_DESCRIPTION;
            default:
                return TYPE_COMMENT;
        }
    }

    public void setCheese(Cheese cheese) {
        this.cheese = cheese;
        notifyDataSetChanged();
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
        notifyDataSetChanged();
    }

    @NonNull
    private PriceViewHolder createPriceViewHolder(@NonNull ViewGroup parent) {
        PriceViewHolder viewHolder = new PriceViewHolder(parent);
        return viewHolder;
    }

    private void bindPriceViewHolder(PriceViewHolder holder) {
        holder.priceTextView.setText(R.string.price_unavailable);
    }

    private void bindDescriptionViewHolder(DescriptionViewHolder holder) {
        if (cheese != null) {
            holder.descriptionTextView.setText(cheese.getDescription());
        }
    }

    private void bindCommentViewHolder(CommentViewHolder holder, int position) {
        if (position < 0 || position >= comments.size()) {
            holder.itemView.setVisibility(View.INVISIBLE);
            return;
        }
        Comment comment = comments.get(position);
        holder.itemView.setVisibility(View.VISIBLE);
        holder.commentTextView.setText(comment.getComment());
        holder.commentTextView.setTextColor(getTextColor(comment.getUpdated() != null));
        holder.userTextView.setText(comment.getUser());
        holder.dateTextView.setText(getDateText(comment));
    }

    @ColorInt
    private int getTextColor(boolean syncedWithServer) {
        return syncedWithServer ? 0xde000000 : 0x8a000000;
    }

    private String getDateText(Comment comment) {
        LocalDate date;
        date = comment.getUpdated() != null ? comment.getUpdated() : comment.getCreated();
        return date.format(FORMATTER);
    }

    static class PriceViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.priceTextView)
        TextView priceTextView;

        PriceViewHolder(ViewGroup parent) {
            super(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_price, parent, false));
            ButterKnife.bind(this, itemView);
        }
    }

    static class DescriptionViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.descriptionTextView)
        TextView descriptionTextView;

        DescriptionViewHolder(ViewGroup parent) {
            super(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_description, parent, false));
            ButterKnife.bind(this, itemView);
        }
    }

    static class CommentViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.commentTextView)
        TextView commentTextView;
        @BindView(R.id.userTextView)
        TextView userTextView;
        @BindView(R.id.dateTextView)
        TextView dateTextView;

        CommentViewHolder(ViewGroup parent) {
            super(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment, parent, false));
            ButterKnife.bind(this, itemView);
        }
    }

    static class HeaderViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.headerTextView)
        TextView headerTextView;

        HeaderViewHolder(ViewGroup parent) {
            super(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_header, parent, false));
            ButterKnife.bind(this, itemView);
        }
    }
}
