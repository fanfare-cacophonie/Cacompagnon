package org.cacophonie.cacompagnon.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.cacophonie.cacompagnon.R;
import org.cacophonie.cacompagnon.utils.VanillaAPI;

/**
 * Created by SMaiz on 12/07/17.
 */

public class MessageView extends LinearLayout {
    TextView userName = null;
    TextView date = null;
    TextView body = null;
    ImageView photo = null;

    public MessageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public MessageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MessageView(Context context) {
        super(context);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.view_message, this);
        userName = (TextView) findViewById(R.id.msg_name);
        body = (TextView) findViewById(R.id.msg_body);
        date = (TextView) findViewById(R.id.msg_date);
        photo = (ImageView) findViewById(R.id.msg_photo);
    }

    public void bind(VanillaAPI.Comment comment) {
        userName.setText(comment.InsertName);
        body.setText(comment.Body);
        date.setText(comment.DateInserted);
        // TODO: get picture
    }

    public void bind(VanillaAPI.Discussion disc) {
        userName.setText(disc.InsertName);
        body.setText(disc.Body);
        date.setText(disc.DateInserted);
        // TODO: get picture
    }
}
