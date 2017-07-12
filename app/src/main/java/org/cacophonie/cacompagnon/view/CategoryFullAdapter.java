package org.cacophonie.cacompagnon.view;

import android.content.Context;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.cacophonie.cacompagnon.utils.VanillaAPI;

import java.util.List;

/**
 * Created by sidou on 11/07/17.
 */

public class CategoryFullAdapter extends BaseAdapter {
    private Context mContext;
    private VanillaAPI.CategoryFull mCategory = null;

    public static final int NESTED_CATEGORY = 0;
    public static final int DISCUSSION = 1;

    public CategoryFullAdapter(Context context) {
        mContext = context;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int i) {
        return i < mCategory.Categories.size() ? NESTED_CATEGORY : DISCUSSION;
    }

    @Override
    public int getCount() {
        if (mCategory == null)
            return 0;
        return mCategory.Categories.size() + mCategory.Discussions.size();
    }

    @Override
    public Object getItem(int i) {
        if (mCategory.Categories.size() != 0) {
            if (i < mCategory.Categories.size())
                return mCategory.Categories.get(i);
            else
                return mCategory.Discussions.get(i - mCategory.Categories.size());
        }
        return mCategory.Discussions.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = null;
        int type = getItemViewType(i);

        // If no view is passed, create a new one
        if (view == null) {
            switch (type) {
                case NESTED_CATEGORY:
                    v = new CategoryView(mContext);
                    break;
                case DISCUSSION:
                    v = new DiscussionView(mContext);
                    break;
            }
        }
        else {
            v = view;
        }

        switch (type) {
            case NESTED_CATEGORY:
                ((CategoryView) v).bind((VanillaAPI.Category) getItem(i));
                break;
            case DISCUSSION:
                ((DiscussionView) v).bind((VanillaAPI.Discussion) getItem(i));
                break;
        }
        return v;
    }

    public void bind(VanillaAPI.CategoryFull category) {
        mCategory = category;
    }
}
