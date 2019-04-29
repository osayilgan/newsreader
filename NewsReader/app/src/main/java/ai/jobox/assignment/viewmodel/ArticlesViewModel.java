package ai.jobox.assignment.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.paging.PagedList;

import ai.jobox.assignment.database.Article;
import ai.jobox.assignment.model.NetworkState;
import ai.jobox.assignment.repository.ArticlesRepository;

public class ArticlesViewModel extends AndroidViewModel {

    private ArticlesRepository repository;

    public ArticlesViewModel(@NonNull Application application) {
        super(application);
        repository = ArticlesRepository.getInstance(application);
    }

    public LiveData<PagedList<Article>> getArticles() {
        return repository.getArticles();
    }

    public LiveData<NetworkState> getNetworkState() {
        return repository.getNetworkState();
    }

    /**
     * Invalidates Both Network and DB Data Sources.
     */
    public void invalidate() {
        repository.invalidateDataSources();
    }
}
