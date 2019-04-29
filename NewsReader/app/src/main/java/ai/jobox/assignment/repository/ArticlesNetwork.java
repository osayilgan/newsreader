package ai.jobox.assignment.repository;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import ai.jobox.assignment.database.Article;
import ai.jobox.assignment.model.NetworkState;

public class ArticlesNetwork {

    private int NUMBERS_OF_THREADS = 3;
    private int LOADING_PAGE_SIZE = 20;

    final private LiveData<PagedList<Article>> articlesPaged;
    final private LiveData<NetworkState> networkState;

    private ArticlesApiDataSourceFactory dataSourceFactory;

    public ArticlesNetwork(PagedList.BoundaryCallback<Article> boundaryCallback) {

        dataSourceFactory = new ArticlesApiDataSourceFactory();

        PagedList.Config pagedListConfig = (new PagedList.Config.Builder()).setEnablePlaceholders(false)
                .setInitialLoadSizeHint(LOADING_PAGE_SIZE)
                .setPageSize(LOADING_PAGE_SIZE)
                .build();

        networkState = Transformations.switchMap(dataSourceFactory.getNetworkStatus(),
                (Function<ArticlesApiPageKeyedDataSource, LiveData<NetworkState>>) ArticlesApiPageKeyedDataSource::getNetworkState);

        Executor executor = Executors.newFixedThreadPool(NUMBERS_OF_THREADS);

        articlesPaged = new LivePagedListBuilder<>(dataSourceFactory, pagedListConfig)
                .setFetchExecutor(executor)
                .setBoundaryCallback(boundaryCallback).build();
    }

    public LiveData<PagedList<Article>> getPagedArticles(){
        return articlesPaged;
    }

    public LiveData<NetworkState> getNetworkState() {
        return networkState;
    }

    public ArticlesApiDataSourceFactory getDataSourceFactory() {
        return dataSourceFactory;
    }

    /**
     * Invalidates Data Source.
     */
    public void invalidate() {
        dataSourceFactory.getDataSource().invalidate();
    }
}
