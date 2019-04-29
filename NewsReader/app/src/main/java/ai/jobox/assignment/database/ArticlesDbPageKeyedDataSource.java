package ai.jobox.assignment.database;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.paging.PageKeyedDataSource;

import java.util.List;

public class ArticlesDbPageKeyedDataSource extends PageKeyedDataSource<String, Article> {

    static final String TAG = ArticlesDbPageKeyedDataSource.class.getName();
    private final ArticleDao articleDao;

    public ArticlesDbPageKeyedDataSource(ArticleDao dao) {
        articleDao = dao;
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<String> params, @NonNull final LoadInitialCallback<String, Article> callback) {
        Log.i(TAG, "Loading Initial Rang, Count " + params.requestedLoadSize);

        List<Article> articles = articleDao.getArticlesByDate();
        if(articles.size() != 0) callback.onResult(articles, null, null);
    }

    @Override
    public void loadAfter(@NonNull LoadParams<String> params, final @NonNull LoadCallback<String, Article> callback) {}

    @Override
    public void loadBefore(@NonNull LoadParams<String> params, @NonNull LoadCallback<String, Article> callback) {}
}
