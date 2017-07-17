package org.cacophonie.cacompagnon.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.cacophonie.cacompagnon.R;
import org.cacophonie.cacompagnon.utils.VanillaAPI;

import java.text.DateFormat;

/**
 * Created by SMaiz on 12/07/17.
 */

public class DiscussionView extends LinearLayout {
    TextView title = null;
    TextView startedBy = null;
    TextView lastUser = null;
    TextView date = null;
    private DateFormat df;

    public DiscussionView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public DiscussionView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DiscussionView(Context context) {
        super(context);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.view_discussion, this);
        title = (TextView) findViewById(R.id.disc_title);
        startedBy = (TextView) findViewById(R.id.disc_startedBy);
        lastUser = (TextView) findViewById(R.id.disc_lastUser);
        date = (TextView) findViewById(R.id.disc_date);
        df = DateFormat.getDateInstance();

		setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        setOrientation(LinearLayout.VERTICAL);

        TypedValue outValue = new TypedValue();
        getContext().getTheme().resolveAttribute(android.R.attr.selectableItemBackground, outValue, true);
        setBackgroundResource(outValue.resourceId);
    }

    public void bind(VanillaAPI.Discussion discussion) {
        title.setText(discussion.Name);
        startedBy.setText(discussion.FirstName);
        lastUser.setText(discussion.LastName);
        date.setText(df.format(discussion.DateInserted));
    }
}
