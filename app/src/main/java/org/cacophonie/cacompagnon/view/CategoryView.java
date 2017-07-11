package org.cacophonie.cacompagnon.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.cacophonie.cacompagnon.R;
import org.cacophonie.cacompagnon.utils.VanillaAPI;

/**
 * Created by sidou on 11/07/17.
 */

public class CategoryView extends LinearLayout {
    TextView title = null;
    TextView desc = null;

    public CategoryView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public CategoryView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CategoryView(Context context) {
        super(context);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.view_category, this);
        title = (TextView) findViewById(R.id.cat_title);
        desc = (TextView) findViewById(R.id.cat_desc);
    }

    public void bind(VanillaAPI.Category category) {
        title.setText(category.Name);
        desc.setText(category.Description);
    }
}
