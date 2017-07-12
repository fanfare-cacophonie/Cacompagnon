package org.cacophonie.cacompagnon.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.cacophonie.cacompagnon.R;
import org.cacophonie.cacompagnon.activity.MainActivity;
import org.cacophonie.cacompagnon.utils.VanillaAPI;
import org.cacophonie.cacompagnon.view.CategoryFullAdapter;
import org.cacophonie.cacompagnon.view.DiscussionFullAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class ThreadFragment extends ListFragment implements VanillaAPI.Callback {
    private DiscussionFullAdapter adapter = null;

    public ThreadFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Create a new adapter
        adapter = new DiscussionFullAdapter(getContext());
        // Use it
        setListAdapter(adapter);

        MainActivity activity = (MainActivity) getActivity();
        activity.getAPI().getDiscussion(getArguments().getInt("DiscussionID"), this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_thread, container, false);
    }

    @Override
    public void onFinished(Object result) {
        VanillaAPI.DiscussionFull disc = (VanillaAPI.DiscussionFull) result;
        adapter.bind(disc);
        adapter.notifyDataSetChanged();

    }
}
