package com.codepath.instagram.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.codepath.instagram.R;
import com.codepath.instagram.activities.PhotoGridActivity;
import com.codepath.instagram.models.InstagramUser;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

/**
 * Created by yuanzhang on 10/30/15.
 */
public class SearchUserResultsAdapter extends RecyclerView.Adapter<SearchUserResultsAdapter.SearchUserViewHolder> {
    private List<InstagramUser> insUsers;
    private Context context;

    public SearchUserResultsAdapter(List<InstagramUser> insUsers, Context context) {
        this.insUsers = insUsers;
        this.context = context;
    }

    @Override
    public SearchUserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View usersView = layoutInflater.inflate(R.layout.layout_item_user, parent, false);
        SearchUserViewHolder searchUserViewHolder = new SearchUserViewHolder(usersView);

        return searchUserViewHolder;
    }

    @Override
    public void onBindViewHolder(SearchUserViewHolder holder, final int position) {
        final InstagramUser user = insUsers.get(position);

        holder.sdvUserImage.setImageURI(Uri.parse(user.profilePictureUrl));
        holder.tvUserName.setText(user.userName);
        holder.tvFullName.setText(user.fullName);

        holder.sdvUserImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, PhotoGridActivity.class);
                i.putExtra("position", position);
                i.putExtra("userId", user.userId);
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return insUsers.size();
    }

    public static class SearchUserViewHolder extends RecyclerView.ViewHolder {
        public SimpleDraweeView sdvUserImage;
        public TextView tvUserName;
        public TextView tvFullName;

        public SearchUserViewHolder(View itemView) {
            super(itemView);

            sdvUserImage = (SimpleDraweeView) itemView.findViewById(R.id.sdvUserImage);
            tvUserName = (TextView) itemView.findViewById(R.id.tvUserName);
            tvFullName = (TextView) itemView.findViewById(R.id.tvFullName);
        }
    }
}
