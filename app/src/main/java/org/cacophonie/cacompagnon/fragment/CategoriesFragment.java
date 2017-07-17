package org.cacophonie.cacompagnon.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.cacophonie.cacompagnon.R;
import org.cacophonie.cacompagnon.activity.MainActivity;
import org.cacophonie.cacompagnon.utils.VanillaAPI;
import org.cacophonie.cacompagnon.view.CategoriesAdapter;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class CategoriesFragment extends Fragment implements VanillaAPI.Callback {
    private CategoriesAdapter adapter;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;

    public class clickListener implements View.OnClickListener {

        public void onClick(View v) {
            int i = recyclerView.getChildAdapterPosition(v);
            VanillaAPI.Category cat = adapter.getItem(i);
            Bundle args = new Bundle();
            args.putInt("CategoryID", cat.CategoryID);
            CategoryFragment fragment = new CategoryFragment();
            fragment.setArguments(args);
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frame, fragment).addToBackStack(null).commit();
        }
    }

    public CategoriesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Create a new adapter
        adapter = new CategoriesAdapter();
        adapter.setClickListener(new clickListener());

        MainActivity activity = (MainActivity) getActivity();
        activity.getAPI().getCategories(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().setTitle(R.string.nav_categories);
        View rootView =  inflater.inflate(R.layout.fragment_list, container, false);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerview);

        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        return rootView;
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
