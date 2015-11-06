package com.codepath.instagram.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.codepath.instagram.R;
import com.codepath.instagram.activities.PhotoGridActivity;
import com.codepath.instagram.helpers.Utils;
import com.codepath.instagram.models.InstagramSearchTag;

import java.util.List;

/**
 * Created by yuanzhang on 11/1/15.
 */
public class SearchTagResultsAdapter extends RecyclerView.Adapter<SearchTagResultsAdapter.SearchTagViewHolder> {
    private List<InstagramSearchTag> insTags;
    private Context context;

    public SearchTagResultsAdapter(List<InstagramSearchTag> insTags, Context context) {
        this.insTags = insTags;
        this.context = context;
    }

    @Override
    public SearchTagViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View tagsView = layoutInflater.inflate(R.layout.layout_item_tag, parent, false);
        SearchTagViewHolder searchTagViewHolder = new SearchTagViewHolder(tagsView);

        return searchTagViewHolder;
    }

    @Override
    public void onBindViewHolder(SearchTagViewHolder holder, int position) {
        final InstagramSearchTag tag = insTags.get(position);

        holder.tvSearchTag.setText("#" + tag.tag);
        holder.tvTagCount.setText(Utils.formatNumberForDisplay(tag.count) + " posts");

        holder.tvSearchTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, PhotoGridActivity.class);
                i.putExtra("searchTag", tag.tag);
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return insTags.size();
    }

    public static class SearchTagViewHolder extends RecyclerView.ViewHolder {
        public TextView tvSearchTag;
        public TextView tvTagCount;

        public SearchTagViewHolder(View itemView) {
            super(itemView);

            tvSearchTag = (TextView) itemView.findViewById(R.id.tvSearchTag);
            tvTagCount = (TextView) itemView.findViewById(R.id.tvTagCount);
        }
    }
}
