package org.cacophonie.cacompagnon.view;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.cacophonie.cacompagnon.utils.VanillaAPI;

/**
 * Created by SMaiz on 12/07/17.
 */

public class DiscussionFullAdapter extends BaseAdapter {
    private Context mContext;
    private VanillaAPI.DiscussionFull mDiscussion = null;

    public static final int TITLE = 0;
    public static final int PREMIER = 1;
    public static final int MESSAGE = 2;

    public DiscussionFullAdapter(Context context) {
        mContext = context;
    }

    @Override
    public int getViewTypeCount() {
        return 3;
    }

    @Override
    public int getItemViewType(int i) {
        return i == 0 ? TITLE : i == 1 ? PREMIER : MESSAGE;
    }

    @Override
    public int getCount() {
        if (mDiscussion == null)
            return 0;
        return mDiscussion.Comments.size() + 2;
    }

    @Override
    public Object getItem(int i) {
        if (i == 0)
            return mDiscussion.Discussion.Name;
        if (i == 1)
            return mDiscussion.Discussion;
        return mDiscussion.Comments.get(i - 2);
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
                case TITLE:
                    v = new TextView(mContext);
                    break;
                case PREMIER:
                case MESSAGE:
                    v = new MessageView(mContext);
                    break;
            }
        }
        else {
            v = view;
        }

        switch (type) {
            case TITLE:
                ((TextView) v).setText((String) getItem(i));
                break;
            case PREMIER:
                ((MessageView) v).bind((VanillaAPI.Discussion) getItem(i));
                break;
            case MESSAGE:
                ((MessageView) v).bind((VanillaAPI.Comment) getItem(i));
                break;
        }
        return v;
    }

    public void bind(VanillaAPI.DiscussionFull discussion) {
        mDiscussion = discussion;
    }
}
