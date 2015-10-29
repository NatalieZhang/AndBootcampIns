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
import com.codepath.instagram.models.InstagramComment;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

/**
 * Created by yuanzhang on 10/29/15.
 */
public class InstagramCommentsAdapter extends RecyclerView.Adapter<InstagramCommentsAdapter.InsCommentsViewHolder>{

    private static List<InstagramComment> insComments;
    private static Context context;

    public InstagramCommentsAdapter(List<InstagramComment> insComments, Context context) {
        this.insComments = insComments;
        this.context = context;
    }

    @Override
    public InsCommentsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View insView = inflater.inflate(R.layout.layout_item_comment, parent, false);
        InsCommentsViewHolder insViewHolder = new InsCommentsViewHolder(insView);

        return insViewHolder;
    }

    @Override
    public void onBindViewHolder(InsCommentsViewHolder insCommentsViewHolder, int position) {
        ForegroundColorSpan blueForeGroundColorSpan =
                new ForegroundColorSpan(context.getResources().getColor(R.color.blue_text));
        ForegroundColorSpan grayForeGroundColorSpan =
                new ForegroundColorSpan(context.getResources().getColor(R.color.gray_text));
        TypefaceSpan sansMediumSpan = new TypefaceSpan("sans-serif-medium");
        TypefaceSpan sansSpan = new TypefaceSpan("sans-serif");


        InstagramComment insComment = insComments.get(position);

        Uri imageUri = Uri.parse(insComment.user.profilePictureUrl);
        insCommentsViewHolder.sdvCommentProfileImage.setImageURI(imageUri);
        insCommentsViewHolder.tvCommentAndUserName.setText(insComment.user.userName);


        String commentUserName = insComment.user.userName;
        SpannableStringBuilder ssbCommentUser = new SpannableStringBuilder(commentUserName);
        ssbCommentUser.setSpan(blueForeGroundColorSpan, 0, commentUserName.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ssbCommentUser.setSpan(sansMediumSpan, 0, commentUserName.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ssbCommentUser.append(" ");

        String commentText = insComment.text;
        SpannableStringBuilder ssbComment = new SpannableStringBuilder(commentText);
        ssbComment.setSpan(grayForeGroundColorSpan, 0, commentText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ssbComment.setSpan(sansSpan, 0, commentText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ssbComment.append(commentText);

        insCommentsViewHolder.tvCommentAndUserName.setVisibility(View.VISIBLE);
        insCommentsViewHolder.tvCommentAndUserName.setText(TextUtils.concat(ssbCommentUser, ssbComment), TextView.BufferType.SPANNABLE);

        CharSequence timeStamp = DateUtils.getRelativeTimeSpanString(insComment.createdTime * 1000);
        insCommentsViewHolder.tvCommentTimestamp.setText(timeStamp);
    }

    @Override
    public int getItemCount() {
        return insComments.size();
    }

    public static class InsCommentsViewHolder extends RecyclerView.ViewHolder {
        public SimpleDraweeView sdvCommentProfileImage;
        public TextView tvCommentAndUserName;
        public TextView tvCommentTimestamp;

        public InsCommentsViewHolder(View itemView) {
            super(itemView);

            sdvCommentProfileImage = (SimpleDraweeView) itemView.findViewById(R.id.sdvCommentProfileImage);
            tvCommentAndUserName = (TextView) itemView.findViewById(R.id.tvCommentAndUserName);
            tvCommentTimestamp = (TextView) itemView.findViewById(R.id.tvCommentTimeStamp);
        }
    }
}
