package ai.jobox.assignment.database;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Database(entities = {Article.class}, version = 1, exportSchema = false)
public abstract class ArticleDatabase extends RoomDatabase {

    private int NUMBERS_OF_THREADS = 3;
    private int LOADING_PAGE_SIZE = 20;

    private static ArticleDatabase INSTANCE;

    public abstract ArticleDao articleDao();

    private LiveData<PagedList<Article>> articles;
    private ArticlesDbDataSourceFactory dataSourceFactory;

    public static ArticleDatabase getArticleDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), ArticleDatabase.class, "articles-database").build();
            INSTANCE.init();
        }
        return INSTANCE;
    }

    private void init() {

        dataSourceFactory = new ArticlesDbDataSourceFactory(articleDao());

        PagedList.Config pagedListConfig = (new PagedList.Config.Builder()).setEnablePlaceholders(false)
                .setInitialLoadSizeHint(LOADING_PAGE_SIZE)
                .setPageSize(LOADING_PAGE_SIZE).build();

        Executor executor = Executors.newFixedThreadPool(NUMBERS_OF_THREADS);

        LivePagedListBuilder livePagedListBuilder = new LivePagedListBuilder<>(dataSourceFactory, pagedListConfig);
        articles = livePagedListBuilder.setFetchExecutor(executor).build();
    }

    public LiveData<PagedList<Article>> getArticles() {
        return articles;
    }

    public void invalidate() {
        dataSourceFactory.getDataSource().invalidate();
    }
}
