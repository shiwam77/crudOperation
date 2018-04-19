package com.aoezdemir.todoapp.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.aoezdemir.todoapp.R;
import com.aoezdemir.todoapp.activity.adapter.OverviewAdapter;
import com.aoezdemir.todoapp.model.Todo;
import com.aoezdemir.todoapp.remote.ServiceFactory;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OverviewActivity extends AppCompatActivity {

    private static final String TAG = OverviewActivity.class.getSimpleName();

    private RecyclerView rvOverview;
    private OverviewAdapter ovAdapter;
    private List<Todo> todos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rvOverview = findViewById(R.id.rvOverview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvOverview.setLayoutManager(linearLayoutManager);

        ServiceFactory.getServiceTodo().get().enqueue(new Callback<List<Todo>>() {
            @Override
            public void onResponse(Call<List<Todo>> call, Response<List<Todo>> response) {
                todos = response.body();
                ovAdapter = new OverviewAdapter(todos);
                rvOverview.setAdapter(ovAdapter);
            }

            @Override
            public void onFailure(Call<List<Todo>> call, Throwable t) {
                Log.e(TAG, t.getMessage());
            }
        });
    }
}