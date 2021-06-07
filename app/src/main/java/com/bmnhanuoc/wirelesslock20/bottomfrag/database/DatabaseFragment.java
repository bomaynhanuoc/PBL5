package com.bmnhanuoc.wirelesslock20.bottomfrag.database;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bmnhanuoc.wirelesslock20.R;
import com.bmnhanuoc.wirelesslock20.service.ApiService;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DatabaseFragment extends Fragment {

    RecyclerView recyclerView;
    ArrayList<DataBase> dataBaseList = new ArrayList<>();
    private DataBaseAdapter dataBaseAdapter;


    ProgressBar progressBar;
    TextView textView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_database,container,false);
        textView = root.findViewById(R.id.textViewTestData);
        progressBar = root.findViewById(R.id.progressbar);
        progressBar.setIndeterminate(true);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);

        ApiService.apiService.getList().enqueue(new Callback<List<DataBase>>() {
            @Override
            public void onResponse(Call<List<DataBase>> call, Response<List<DataBase>> response) {
                if (response.isSuccessful()) {
                    progressBar.setVisibility(View.GONE);
                    dataBaseList = new ArrayList<>(response.body());
                    dataBaseAdapter = new DataBaseAdapter(dataBaseList, getActivity());
                    recyclerView.setAdapter(dataBaseAdapter);
                }
            }

            @Override
            public void onFailure(Call<List<DataBase>> call, Throwable t) {

            }
        });
    }
}
