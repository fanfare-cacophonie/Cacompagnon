package org.cacophonie.cacompagnon.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.cacophonie.cacompagnon.R;
import org.cacophonie.cacompagnon.activity.MainActivity;
import org.cacophonie.cacompagnon.utils.VanillaAPI;
import org.cacophonie.cacompagnon.view.DiscussionFullAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class ThreadFragment extends Fragment implements VanillaAPI.Callback {
    private DiscussionFullAdapter adapter = null;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;

    public ThreadFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Create a new adapter
        adapter = new DiscussionFullAdapter();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((MainActivity) getActivity()).getAPI().getDiscussion(getArguments().getInt("DiscussionID"), this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_list, container, false);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerview);

        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        return rootView;
    }

    @Override
    public void onFinished(Object result) {
        VanillaAPI.DiscussionFull disc = (VanillaAPI.DiscussionFull) result;
        adapter.bind(disc);
        adapter.notifyDataSetChanged();

    }
}
