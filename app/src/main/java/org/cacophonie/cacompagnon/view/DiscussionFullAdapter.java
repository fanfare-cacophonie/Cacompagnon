package org.cacophonie.cacompagnon.view;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.cacophonie.cacompagnon.utils.VanillaAPI;

/**
 * Created by SMaiz on 12/07/17.
 */

public class DiscussionFullAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private VanillaAPI.DiscussionFull mDiscussion = null;

    public static final int TITLE = 0;
    public static final int FIRST = 1;
    public static final int MESSAGE = 2;

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
        return i == 0 ? TITLE : i == 1 ? FIRST : MESSAGE;
    }

    @Override
    public int getItemCount() {
        if (mDiscussion == null)
            return 0;
        return mDiscussion.Comments.size() + 2;
    }

    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {
        switch (viewHolder.getItemViewType()) {
            case TITLE:
                ((TextView) ((ViewHolder) viewHolder).getView()).setText(mDiscussion.Discussion.Name);
                break;
            case FIRST:
                ((MessageView) ((ViewHolder) viewHolder).getView()).bind(mDiscussion.Discussion);
                break;
            default:
            case MESSAGE:
                ((MessageView) ((ViewHolder) viewHolder).getView()).bind(mDiscussion.Comments.get(position - 2));
                break;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        switch (viewType) {
            case TITLE:
                v = new TextView(parent.getContext());
                break;
            default:
            case FIRST:
            case MESSAGE:
                v = new MessageView(parent.getContext());
                break;
        }
        return new ViewHolder(v);
    }

    public void bind(VanillaAPI.DiscussionFull discussion) {
        mDiscussion = discussion;
    }
}
