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

public class CategoriesAdapter extends BaseAdapter {
    private Context mContext;
    private List<VanillaAPI.Category> mCategories = null;

    public static final int HEADING = 0;
    public static final int DISCUSSIONS = 1;

    public CategoriesAdapter(Context context) {
        mContext = context;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int i) {
        return getItem(i).DisplayAs.equals("Heading") ? HEADING : DISCUSSIONS;
    }

    @Override
    public int getCount() {
        if (mCategories == null)
            return 0;
        return mCategories.size();
    }

    @Override
    public VanillaAPI.Category getItem(int i) {
        return mCategories.get(i);
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
                case HEADING:
                    v = new TextView(mContext);
                    break;
                case DISCUSSIONS:
                    v = new CategoryView(mContext);
                    break;
            }
        }
        else {
            v = view;
        }

        switch (type) {
            case HEADING:
                ((TextView) v).setText(getItem(i).Name);
                ((TextView) v).setTextSize(TypedValue.COMPLEX_UNIT_SP, 22);
                break;
            case DISCUSSIONS:
                ((CategoryView) v).bind(getItem(i));
                break;
        }
        return v;
    }

    public void bind(List<VanillaAPI.Category> categories) {
        mCategories = categories;
    }
}
