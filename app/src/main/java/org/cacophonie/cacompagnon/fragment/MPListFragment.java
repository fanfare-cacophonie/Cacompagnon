package org.cacophonie.cacompagnon.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.cacophonie.cacompagnon.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MPListFragment extends Fragment {

    public MPListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().setTitle(R.string.nav_MP);
        return inflater.inflate(R.layout.fragment_mplist, container, false);
    }
}
