package org.cacophonie.cacompagnon.view;

import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.cacophonie.cacompagnon.utils.VanillaAPI;

import java.util.List;

/**
 * Created by sidou on 11/07/17.
 */

public class CategoriesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<VanillaAPI.Category> mCategories = null;
    private View.OnClickListener mClickListener;

    public static final int HEADING = 0;
    public static final int DISCUSSIONS = 1;

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

    @Override
    public int getItemViewType(int i) {
        return getItem(i).DisplayAs.equals("Heading") ? HEADING : DISCUSSIONS;
    }

    public VanillaAPI.Category getItem(int i) {
        return mCategories.get(i);
    }

    public void setClickListener(View.OnClickListener listener) {
        mClickListener = listener;
    }

    @Override
    public int getItemCount() {
        if (mCategories == null)
            return 0;
        return mCategories.size();
    }

    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {
        switch (viewHolder.getItemViewType()) {
            case HEADING:
                ((TextView) ((CategoriesAdapter.ViewHolder) viewHolder).getView()).setText(getItem(position).Name);
                ((TextView) ((CategoriesAdapter.ViewHolder) viewHolder).getView()).setTextSize(TypedValue.COMPLEX_UNIT_SP, 22);
                break;
            default:
            case DISCUSSIONS:
                ((CategoryView) ((CategoriesAdapter.ViewHolder) viewHolder).getView()).bind(getItem(position));
                break;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        switch (viewType) {
            case HEADING:
                v = new TextView(parent.getContext());
                break;
            default:
            case DISCUSSIONS:
                v = new CategoryView(parent.getContext());
                v.setOnClickListener(mClickListener);
                break;
        }
        return new CategoriesAdapter.ViewHolder(v);
    }

    public void bind(List<VanillaAPI.Category> categories) {
        mCategories = categories;
    }
}
