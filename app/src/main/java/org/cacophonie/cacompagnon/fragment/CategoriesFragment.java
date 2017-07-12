package org.cacophonie.cacompagnon.fragment;


import android.content.Context;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListAdapter;

import org.cacophonie.cacompagnon.R;
import org.cacophonie.cacompagnon.activity.MainActivity;
import org.cacophonie.cacompagnon.utils.VanillaAPI;
import org.cacophonie.cacompagnon.view.CategoryView;
import org.cacophonie.cacompagnon.view.CategoryViewAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class CategoriesFragment extends ListFragment implements VanillaAPI.Callback {
    private CategoryViewAdapter adapter;

    public CategoriesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Create a new adapter
        adapter = new CategoryViewAdapter(getContext());
        // Use it
        setListAdapter(adapter);

        MainActivity activity = (MainActivity) getActivity();
        activity.getAPI().getCategories(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().setTitle(R.string.nav_categories);
        return inflater.inflate(R.layout.fragment_categories, container, false);
    }

    // This function is called when the category list is available
    @Override
    public void onFinished(Object result) {
        // Get the list from the result
        List<VanillaAPI.Category> categories = (List<VanillaAPI.Category>) result;
        // Pass it to the adapter
        adapter.bind(categories);
        // And ask for an update
        adapter.notifyDataSetChanged();
    }
}
