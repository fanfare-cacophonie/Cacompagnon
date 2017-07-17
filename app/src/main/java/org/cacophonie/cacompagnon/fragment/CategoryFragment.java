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
import org.cacophonie.cacompagnon.view.CategoryFullAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class CategoryFragment extends Fragment implements VanillaAPI.Callback {
    private CategoryFullAdapter adapter;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;

    public class clickListener implements View.OnClickListener {
        public void onClick(View v) {
            int i = recyclerView.getChildAdapterPosition(v);
            if (adapter.getItemViewType(i) == CategoryFullAdapter.NESTED_CATEGORY) {
                VanillaAPI.Category cat = (VanillaAPI.Category) adapter.getItem(i);
                Bundle args = new Bundle();
                args.putInt("CategoryID", cat.CategoryID);
                CategoryFragment fragment = new CategoryFragment();
                fragment.setArguments(args);
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame, fragment).addToBackStack(null).commit();
            }
            else {
                VanillaAPI.Discussion disc = (VanillaAPI.Discussion) adapter.getItem(i);
                Bundle args = new Bundle();
                args.putInt("DiscussionID", disc.DiscussionID);
                ThreadFragment fragment = new ThreadFragment();
                fragment.setArguments(args);
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame, fragment).addToBackStack(null).commit();
            }
        }
    }

    public CategoryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Create a new adapter
        adapter = new CategoryFullAdapter();
        adapter.setClickListener(new clickListener());
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((MainActivity) getActivity()).getAPI().getCategory(getArguments().getInt("CategoryID"), this);
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

    @Override
    public void onFinished(Object result) {
        VanillaAPI.CategoryFull cat = (VanillaAPI.CategoryFull) result;
        adapter.bind(cat);
        adapter.notifyDataSetChanged();
    }
}
