package org.cacophonie.cacompagnon.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.cacophonie.cacompagnon.R;
import org.cacophonie.cacompagnon.utils.VanillaAPI;

import java.text.DateFormat;

/**
 * Created by SMaiz on 12/07/17.
 */

public class MessageView extends LinearLayout {
    TextView userName = null;
    TextView date = null;
    TextView body = null;
    ImageView photo = null;
    private RequestOptions options = new RequestOptions().placeholder(R.drawable.ic_account_circle);
    private DateFormat df;

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
        df = DateFormat.getDateInstance();

        setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        setOrientation(LinearLayout.HORIZONTAL);
    }

    public void bind(VanillaAPI.Comment comment) {
        userName.setText(comment.InsertName);
        body.setText(comment.Body);
        date.setText(df.format(comment.DateInserted));
        Glide.with(this).load(comment.InsertPhoto).apply(options).apply(RequestOptions.circleCropTransform()).into(photo);
    }

    public void bind(VanillaAPI.Discussion disc) {
        userName.setText(disc.InsertName);
        body.setText(disc.Body);
        date.setText(df.format(disc.DateInserted));
        Glide.with(this).load(disc.InsertPhoto).apply(options).apply(RequestOptions.circleCropTransform()).into(photo);
    }
}
