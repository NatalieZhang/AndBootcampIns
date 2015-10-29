package com.codepath.instagram.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.codepath.instagram.R;
import com.codepath.instagram.activities.CommentsActivity;
import com.codepath.instagram.helpers.Constants;
import com.codepath.instagram.helpers.Utils;
import com.codepath.instagram.models.InstagramComment;
import com.codepath.instagram.models.InstagramPost;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

/**
 * Created by yuanzhang on 10/26/15.
 */
public class InstagramPostsAdapter extends RecyclerView.Adapter<InstagramPostsAdapter.InsPostsViewHolder>{
    private static List<InstagramPost> insPosts;
    private static Context context;
    private ForegroundColorSpan blueForeGroundColorSpan;
    private ForegroundColorSpan grayForeGroundColorSpan;

    public InstagramPostsAdapter(List<InstagramPost> insPosts, Context context) {
        this.insPosts = insPosts;
        this.context = context;
    }

    @Override
    public InsPostsViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View insView = inflater.inflate(R.layout.layout_item_post, viewGroup, false);
        InsPostsViewHolder insViewHolder = new InsPostsViewHolder(insView);

        return insViewHolder;
    }

    @Override
    public void onBindViewHolder(InsPostsViewHolder instagramViewHolder, final int position) {
        final InstagramPost post = insPosts.get(position);
        Uri profileImageUri = Uri.parse(post.user.profilePictureUrl);
        CharSequence timeStamp = DateUtils.getRelativeTimeSpanString(post.createdTime * 1000);
        final Uri imageUri = Uri.parse(post.image.imageUrl);

        blueForeGroundColorSpan =
                new ForegroundColorSpan(context.getResources().getColor(R.color.blue_text));
        grayForeGroundColorSpan =
                new ForegroundColorSpan(context.getResources().getColor(R.color.gray_text));

        String userName = post.user.userName;
        SpannableStringBuilder ssbName = new SpannableStringBuilder(userName);

        instagramViewHolder.sdvProfileImage.setImageURI(profileImageUri);

        instagramViewHolder.ibShareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.getBitmapAndShare(imageUri, context);
            }
        });

        instagramViewHolder.tvUserName.setText(userName);
        instagramViewHolder.tvTimeStamp.setText(timeStamp);
        instagramViewHolder.sdvImage.setImageURI(imageUri);
        instagramViewHolder.tvLike.setText(Utils.formatNumberForDisplay(post.likesCount) + " likes");

        ssbName.setSpan(blueForeGroundColorSpan, 0, userName.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ssbName.setSpan(Constants.SANS_MEDIUM_SPAN, 0, userName.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ssbName.append(" ");

        String caption = post.caption;

        if (caption != null) {
            SpannableStringBuilder ssbCaption = new SpannableStringBuilder(caption);
            ssbCaption.setSpan(grayForeGroundColorSpan, 0, caption.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            ssbCaption.setSpan(Constants.SANS_SPAN, 0, caption.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            ssbCaption.append(caption);
            instagramViewHolder.tvCaption.setVisibility(View.VISIBLE);
            instagramViewHolder.tvCaption.setText(TextUtils.concat(ssbName, ssbCaption), TextView.BufferType.SPANNABLE);
        } else {
            instagramViewHolder.tvCaption.setVisibility(View.GONE);
        }

        instagramViewHolder.separatorLine.setBackgroundColor(context.getResources().
                getColor(R.color.light_gray_separator_line));
        instagramViewHolder.llComments.removeAllViews();

        if (post.comments != null) {
            int commentsCount = post.commentsCount;

            // view all comments button
            instagramViewHolder.tvViewAllComments.setText("View all " + commentsCount + " comments");

            instagramViewHolder.tvViewAllComments.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(context, CommentsActivity.class);
                    i.putExtra("mediaId", post.mediaId);
                    context.startActivity(i);
                }
            });

            if (commentsCount == 0) {
                instagramViewHolder.llComments.setVisibility(View.GONE);
            } else if (commentsCount < 3) {
                for (InstagramComment oneComment : post.comments) {
                    setUserNameAndCommentSpan(oneComment, instagramViewHolder, blueForeGroundColorSpan,
                            grayForeGroundColorSpan);
                }
            } else {
                for (int i = post.comments.size()-2; i <= post.comments.size()-1; i++) {
                    setUserNameAndCommentSpan(post.comments.get(i), instagramViewHolder,
                            blueForeGroundColorSpan, grayForeGroundColorSpan);
                }
            }
        } else {
            instagramViewHolder.llComments.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return insPosts.size();
    }

    private void setUserNameAndCommentSpan(InstagramComment oneComment, InsPostsViewHolder instagramViewHolder,
                                           ForegroundColorSpan blueForeGroundColorSpan, ForegroundColorSpan grayForeGroundColorSpan) {
        TextView commentView = (TextView) LayoutInflater.from(context).inflate(R.layout.layout_item_text_comment, instagramViewHolder.llComments, false);
        String commentUser = oneComment.user.userName;
        String commentText = oneComment.text;

        SpannableStringBuilder ssbCommentUser = new SpannableStringBuilder(commentUser);
        ssbCommentUser.setSpan(blueForeGroundColorSpan, 0, commentUser.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ssbCommentUser.setSpan(Constants.SANS_MEDIUM_SPAN, 0, commentUser.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ssbCommentUser.append(" ");

        SpannableStringBuilder ssbComment = new SpannableStringBuilder(commentText);
        ssbComment.setSpan(grayForeGroundColorSpan, 0, commentText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ssbComment.setSpan(Constants.SANS_SPAN, 0, commentText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ssbComment.append(commentText);

        commentView.setVisibility(View.VISIBLE);
        commentView.setText(TextUtils.concat(ssbCommentUser, ssbComment), TextView.BufferType.SPANNABLE);
        instagramViewHolder.llComments.addView(commentView);
    }

    public static class InsPostsViewHolder extends RecyclerView.ViewHolder{
        public SimpleDraweeView sdvProfileImage;
        public TextView tvUserName;
        public TextView tvTimeStamp;
        public SimpleDraweeView sdvImage;
        public ImageButton ibLikeButton;
        public ImageButton ibCommentButton;
        public ImageButton ibShareButton;
        public View separatorLine;
        public TextView tvLike;
        public TextView tvCaption;
        public TextView tvViewAllComments;
        public TextView tvComment;
        public LinearLayout llComments;

        public InsPostsViewHolder(View itemView) {
            super(itemView);

            sdvProfileImage = (SimpleDraweeView) itemView.findViewById(R.id.sdvProfileImage);
            tvUserName = (TextView) itemView.findViewById(R.id.tvUserName);
            tvTimeStamp = (TextView) itemView.findViewById(R.id.tvTimeStamp);
            sdvImage = (SimpleDraweeView) itemView.findViewById(R.id.sdvImage);
            ibLikeButton = (ImageButton) itemView.findViewById(R.id.ibLikeBtn);
            ibCommentButton = (ImageButton) itemView.findViewById(R.id.ibComment);
            ibShareButton = (ImageButton) itemView.findViewById(R.id.ibShare);
            separatorLine = itemView.findViewById(R.id.separatorLine);
            tvLike = (TextView) itemView.findViewById(R.id.tvLikeIcon);
            tvCaption = (TextView) itemView.findViewById(R.id.tvNameAndCaption);
            tvViewAllComments = (TextView) itemView.findViewById(R.id.tvViewAllComments);
            tvComment = (TextView) itemView.findViewById(R.id.tvComment);
            llComments = (LinearLayout) itemView.findViewById(R.id.llComments);
        }
    }
}
