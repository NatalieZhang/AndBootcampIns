package com.codepath.instagram.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.instagram.R;
import com.codepath.instagram.models.InstagramPost;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

/**
 * Created by yuanzhang on 11/5/15.
 */
public class PhotoGridAdapter extends RecyclerView.Adapter<PhotoGridAdapter.PhotoGridViewHolder> {
    private List<InstagramPost> insPosts;
    private Context context;

    public PhotoGridAdapter(List<InstagramPost> posts, Context context) {
        insPosts = posts;
        this.context = context;
    }

    @Override
    public PhotoGridViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View photosView = layoutInflater.inflate(R.layout.layout_item_photo, parent, false);

        PhotoGridViewHolder photoGridViewHolder = new PhotoGridViewHolder(photosView);

        return photoGridViewHolder;
    }

    @Override
    public void onBindViewHolder(PhotoGridViewHolder holder, final int position) {
        final InstagramPost post = insPosts.get(position);

        holder.sdvOnePhoto.setImageURI(Uri.parse(post.image.imageUrl));
    }

    @Override
    public int getItemCount() {
        return insPosts.size();
    }

    public static class PhotoGridViewHolder extends RecyclerView.ViewHolder {
        public SimpleDraweeView sdvOnePhoto;

        public PhotoGridViewHolder(View itemView) {
            super(itemView);

            sdvOnePhoto = (SimpleDraweeView) itemView.findViewById(R.id.sdvOnePhoto);
        }
    }
}
