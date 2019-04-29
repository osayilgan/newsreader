package ai.jobox.assignment.view;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.snackbar.Snackbar;

import ai.jobox.assignment.R;
import ai.jobox.assignment.model.ErrorInterface;
import ai.jobox.assignment.viewmodel.ArticlesViewModel;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, ErrorInterface {

    private RecyclerView recyclerView;
    private ArticlesAdapter adapter;
    private SwipeRefreshLayout swipeToRefresh;
    private ArticlesViewModel articlesViewModel;
    private Snackbar errorSnackbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* Toolbar setup */
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /* Swipe To Refresh Layout */
        swipeToRefresh = findViewById(R.id.swipeToRefresh);
        swipeToRefresh.setOnRefreshListener(this);

        /* Setup Recycler View */
        recyclerView = findViewById(R.id.mArticleListView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ArticlesAdapter(this);

        /* Fetch and Show data in the adapter */
        articlesViewModel = ViewModelProviders.of(this).get(ArticlesViewModel.class);

        /* Observe Articles in the Adapter */
        articlesViewModel.getArticles().observe(this, articles -> {
            swipeToRefresh.setRefreshing(false);
            adapter.submitList(articles);
        });

        /* Observe Network Status in the Adapter */
        articlesViewModel.getNetworkState().observe(this, networkState -> {
            Log.e(getClass().getName(), "Network Status Received");
            adapter.setNetworkState(networkState);
        });

        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onRefresh() {
        articlesViewModel.invalidate();
    }

    @Override
    public void onError(String errorMessage) {

        /* Show SnackBar when Error Occurs */
        errorSnackbar = Snackbar.make(findViewById(android.R.id.content), errorMessage, Snackbar.LENGTH_INDEFINITE);
    }
}
