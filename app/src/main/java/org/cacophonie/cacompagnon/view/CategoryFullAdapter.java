package org.cacophonie.cacompagnon.view;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import org.cacophonie.cacompagnon.utils.VanillaAPI;

/**
 * Created by sidou on 11/07/17.
 */

public class CategoryFullAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private VanillaAPI.CategoryFull mCategory = null;
    private View.OnClickListener mClickListener;

    public static final int NESTED_CATEGORY = 0;
    public static final int DISCUSSION = 1;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private View view;

        public ViewHolder(View v) {
            super(v);
            view = v;
        }

        public View getView() {
            return view;
        }
    }

    public void setClickListener(View.OnClickListener listener) {
        mClickListener = listener;
    }

    @Override
    public int getItemViewType(int i) {
        return i < mCategory.Categories.size() ? NESTED_CATEGORY : DISCUSSION;
    }

    @Override
    public int getItemCount() {
        if (mCategory == null)
            return 0;
        return mCategory.Categories.size() + mCategory.Discussions.size();
    }

    public Object getItem(int i) {
        if (mCategory.Categories.size() != 0) {
            if (i < mCategory.Categories.size())
                return mCategory.Categories.get(i);
            else
                return mCategory.Discussions.get(i - mCategory.Categories.size());
        }
        return mCategory.Discussions.get(i);
    }

    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {
        switch (viewHolder.getItemViewType()) {
            case NESTED_CATEGORY:
                ((CategoryView) ((CategoryFullAdapter.ViewHolder) viewHolder).getView()).bind((VanillaAPI.Category) getItem(position));
                break;
            default:
            case DISCUSSION:
                ((DiscussionView) ((CategoryFullAdapter.ViewHolder) viewHolder).getView()).bind((VanillaAPI.Discussion) getItem(position));
                break;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        switch (viewType) {
            case NESTED_CATEGORY:
                v = new CategoryView(parent.getContext());
                v.setOnClickListener(mClickListener);
                break;
            default:
            case DISCUSSION:
                v = new DiscussionView(parent.getContext());
                v.setOnClickListener(mClickListener);
                break;
        }
        return new CategoryFullAdapter.ViewHolder(v);
    }

    public void bind(VanillaAPI.CategoryFull category) {
        mCategory = category;
    }
}
