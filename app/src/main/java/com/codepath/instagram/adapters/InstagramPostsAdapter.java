package com.codepath.instagram.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.TypefaceSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.codepath.instagram.R;
import com.codepath.instagram.helpers.Utils;
import com.codepath.instagram.models.InstagramPost;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

/**
 * Created by yuanzhang on 10/26/15.
 */
public class InstagramPostsAdapter extends RecyclerView.Adapter<InstagramPostsAdapter.InstagramViewHolder>{

    private List<InstagramPost> insPosts;
    private Context context;

    public InstagramPostsAdapter(List<InstagramPost> insPosts, Context context) {
        this.insPosts = insPosts;
        this.context = context;
    }

    @Override
    public InstagramViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View insView = inflater.inflate(R.layout.layout_item_post, viewGroup, false);
        InstagramViewHolder insViewHolder = new InstagramViewHolder(insView);

        return insViewHolder;
    }

    @Override
    public void onBindViewHolder(InstagramViewHolder instagramViewHolder, int position) {
        InstagramPost post = insPosts.get(position);
        Uri profileImageUri = Uri.parse(post.user.profilePictureUrl);
        CharSequence timeStamp = DateUtils.getRelativeTimeSpanString(post.createdTime * 1000);
        Uri imageUri = Uri.parse(post.image.imageUrl);
        ForegroundColorSpan blueForeGroundColorSpan =
                new ForegroundColorSpan(context.getResources().getColor(R.color.blue_text));
        ForegroundColorSpan grayForeGroundColorSpan =
                new ForegroundColorSpan(context.getResources().getColor(R.color.gray_text));
        TypefaceSpan sansMediumSpan = new TypefaceSpan("sans-serif-medium");
        TypefaceSpan sansSpan = new TypefaceSpan("sans-serif");
        String userName = post.user.userName;
        SpannableStringBuilder ssbName = new SpannableStringBuilder(userName);

        instagramViewHolder.sdvProfileImage.setImageURI(profileImageUri);
        instagramViewHolder.tvUserName.setText(userName);
        instagramViewHolder.tvTimeStamp.setText(timeStamp);
        instagramViewHolder.sdvImage.setImageURI(imageUri);
        instagramViewHolder.tvLike.setText(Utils.formatNumberForDisplay(post.likesCount) + " likes");

        ssbName.setSpan(blueForeGroundColorSpan, 0, userName.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ssbName.setSpan(sansMediumSpan, 0, userName.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ssbName.append(" ");

        String caption = post.caption;

        if (caption != null) {
            SpannableStringBuilder ssbCaption = new SpannableStringBuilder(caption);
            ssbCaption.setSpan(grayForeGroundColorSpan, 0, caption.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            ssbCaption.setSpan(sansSpan, 0, caption.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            ssbCaption.append(caption);
            instagramViewHolder.tvCaption.setVisibility(View.VISIBLE);
            instagramViewHolder.tvCaption.setText(TextUtils.concat(ssbName, ssbCaption), TextView.BufferType.SPANNABLE);
        } else {
            instagramViewHolder.tvCaption.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return insPosts.size();
    }

    public static class InstagramViewHolder extends RecyclerView.ViewHolder{
        public SimpleDraweeView sdvProfileImage;
        public TextView tvUserName;
        public TextView tvTimeStamp;
        public SimpleDraweeView sdvImage;
        public TextView tvLike;
        public TextView tvCaption;

        public InstagramViewHolder(View itemView) {
            super(itemView);

            sdvProfileImage = (SimpleDraweeView) itemView.findViewById(R.id.sdvProfileImage);
            tvUserName = (TextView) itemView.findViewById(R.id.tvUserName);
            tvTimeStamp = (TextView) itemView.findViewById(R.id.tvTimeStamp);
            sdvImage = (SimpleDraweeView) itemView.findViewById(R.id.sdvImage);
            tvLike = (TextView) itemView.findViewById(R.id.tvLike);
            tvCaption = (TextView) itemView.findViewById(R.id.tvNameAndCaption);
        }
    }
}
