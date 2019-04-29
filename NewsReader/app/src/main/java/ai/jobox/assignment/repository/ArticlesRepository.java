package ai.jobox.assignment.repository;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.paging.PagedList;

import ai.jobox.assignment.database.Article;
import ai.jobox.assignment.database.ArticleDatabase;
import ai.jobox.assignment.model.NetworkState;
import rx.schedulers.Schedulers;

public class ArticlesRepository {

    private static final String TAG = ArticlesRepository.class.getSimpleName();

    private static ArticlesRepository instance;

    private final ArticlesNetwork network;
    private final ArticleDatabase database;

    private final MediatorLiveData liveDataMerger;

    private ArticlesRepository(Context context) {

        network = new ArticlesNetwork(new BoundaryCallback());
        database = ArticleDatabase.getArticleDatabase(context.getApplicationContext());

        /* Get new Articles from API && Set them into the database */
        liveDataMerger = new MediatorLiveData<>();
        liveDataMerger.addSource(network.getPagedArticles(), value -> {
            liveDataMerger.setValue(value);
            Log.d(TAG, value.toString());
        });

        /* Save Articles to Database */
        network.getDataSourceFactory().getArticles().observeOn(Schedulers.io()).subscribe(article -> database.articleDao().insertOrUpdate(article));
    }

    /**
     * Boundary Callback
     */
    private class BoundaryCallback extends PagedList.BoundaryCallback<Article> {

        @Override
        public void onZeroItemsLoaded() {
            super.onZeroItemsLoaded();
            liveDataMerger.addSource(database.getArticles(), value -> {
                liveDataMerger.setValue(value);
                liveDataMerger.removeSource(database.getArticles());
            });
        }
    }

    public static ArticlesRepository getInstance(Context context){
        if(instance == null) instance = new ArticlesRepository(context);
        return instance;
    }

    public LiveData<PagedList<Article>> getArticles(){
        return  liveDataMerger;
    }

    public LiveData<NetworkState> getNetworkState() {
        return network.getNetworkState();
    }

    /**
     * Invalidates Both Network and Database Data Sources.
     */
    public void invalidateDataSources() {
        network.invalidate();
        database.invalidate();
    }
}
