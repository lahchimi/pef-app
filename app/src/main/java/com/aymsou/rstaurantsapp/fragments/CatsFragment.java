package com.aymsou.rstaurantsapp.fragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.aymsou.rstaurantsapp.R;
import com.aymsou.rstaurantsapp.adapters.CategoryAdapter;
import com.aymsou.rstaurantsapp.model.Category;
import com.aymsou.rstaurantsapp.utils.ApiService;
import com.aymsou.rstaurantsapp.utils.RetroClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;



public class CatsFragment extends Fragment {

    public CatsFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_cats, container, false);
        final RecyclerView catsRecyclerView = (RecyclerView) rootView.findViewById(R.id.catsRecyclerView);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        catsRecyclerView.setLayoutManager(mLayoutManager);
        catsRecyclerView.setItemAnimator(new DefaultItemAnimator());
        //ApiService api = RetroClient.getApiService("");
        ApiService api = RetroClient.getApiService(RetroClient.WEBSITE_ROOT_URL);
        Call<List<Category>> call = api.getCategories();
        final ProgressBar progressLoad = (ProgressBar)rootView.findViewById(R.id.progressLoad);
        call.enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                if(response.isSuccessful()){
                    progressLoad.setVisibility(View.GONE);
                    List<Category> cats = response.body();
                    CategoryAdapter cAdapter = new CategoryAdapter(cats, getActivity());
                    catsRecyclerView.setAdapter(cAdapter);
                }

                Log.d("CatsFragment response", response.toString());
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
                Log.d("CatsFragment cats_is", t.getMessage());
            }
        });
        return rootView;
    }

}