package com.city.guide.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.city.guide.R;
import com.city.guide.api.adapters.CityGuideAdapter;
import com.city.guide.listeners.DataListener;
import com.city.guide.utils.CityGuideUtils;

import retrofit.RetrofitError;

public class ItemFragment extends Fragment {
    private final int[] mIconIds = {R.drawable.ic_bar, R.drawable.ic_bistro, R.drawable.ic_cafe};
    private static final String ITEM_TYPE = "item_type";
    private static final String POSITION = "position";
    private ListView mListView;
    private CityGuideAdapter mAdapter;

    public static ItemFragment newInstance(int position, String itemType) {
        ItemFragment fragment = new ItemFragment();
        Bundle b = new Bundle();
        b.putInt(POSITION, position);
        b.putString(ITEM_TYPE, itemType);
        fragment.setArguments(b);
        return fragment;
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ItemFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAdapter = new CityGuideAdapter(getActivity(), mIconIds[getArguments().getInt(POSITION)],
                getArguments().getString(ITEM_TYPE));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item, container, false);

        // Set the adapter
        mListView = (ListView) view.findViewById(android.R.id.list);
        mListView.setAdapter(mAdapter);

        final SwipeRefreshLayout swipeLayout =
                (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);

        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                CityGuideUtils.getInstance().getResults(getActivity(), new DataListener() {
                    @Override
                    public void onDataReceived() {
                        swipeLayout.setRefreshing(false);
                        setAdapterData();
                    }

                    @Override
                    public void onDataFailed(RetrofitError error) {

                    }
                });
            }
        });

        swipeLayout.setColorScheme(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mAdapter.setData(CityGuideUtils.getInstance().getResults(getActivity()));
    }

    public void setAdapterData() {
        if (getActivity() != null) {
            mAdapter.setData(CityGuideUtils.getInstance().getResults(getActivity()));
        }
    }
}
