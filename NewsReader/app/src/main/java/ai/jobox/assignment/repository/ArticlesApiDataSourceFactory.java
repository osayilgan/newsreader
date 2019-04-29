package ai.jobox.assignment.repository;

import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;

import ai.jobox.assignment.database.Article;
import rx.subjects.ReplaySubject;

/**
 * Creates the DataSource to be used in PagedList.
 */
public class ArticlesApiDataSourceFactory extends DataSource.Factory<String, Article> {

    private MutableLiveData<ArticlesApiPageKeyedDataSource> networkStatus;
    private ArticlesApiPageKeyedDataSource articlesApiPageKeyedDataSource;

    public ArticlesApiDataSourceFactory() {
        init();
    }

    private void init() {
        networkStatus = new MutableLiveData<>();
        articlesApiPageKeyedDataSource = new ArticlesApiPageKeyedDataSource();
    }

    @Override
    public DataSource<String, Article> create() {

        init();

        networkStatus.postValue(articlesApiPageKeyedDataSource);
        return articlesApiPageKeyedDataSource;
    }

    public DataSource getDataSource() {
        return articlesApiPageKeyedDataSource;
    }

    public MutableLiveData<ArticlesApiPageKeyedDataSource> getNetworkStatus() {
        return networkStatus;
    }

    public ReplaySubject<Article> getArticles() {
        return articlesApiPageKeyedDataSource.getArticles();
    }
}
