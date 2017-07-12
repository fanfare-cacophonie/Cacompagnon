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
import org.cacophonie.cacompagnon.view.CategoriesAdapter;
import org.cacophonie.cacompagnon.view.CategoryFullAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class CategoryFragment extends ListFragment implements VanillaAPI.Callback {
    private CategoryFullAdapter adapter;

    public CategoryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Create a new adapter
        adapter = new CategoryFullAdapter(getContext());
        // Use it
        setListAdapter(adapter);

        MainActivity activity = (MainActivity) getActivity();
        activity.getAPI().getCategory(getArguments().getInt("CategoryID"), this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_category, container, false);
    }

    @Override
    public void onFinished(Object result) {
        VanillaAPI.CategoryFull cat = (VanillaAPI.CategoryFull) result;
        adapter.bind(cat);
        adapter.notifyDataSetChanged();
    }
}
