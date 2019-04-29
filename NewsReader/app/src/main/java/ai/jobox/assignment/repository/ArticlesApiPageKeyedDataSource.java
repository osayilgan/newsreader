package ai.jobox.assignment.repository;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.PageKeyedDataSource;

import java.util.ArrayList;

import ai.jobox.assignment.api.ArticlesApi;
import ai.jobox.assignment.database.Article;
import ai.jobox.assignment.model.ArticlesWrapper;
import ai.jobox.assignment.model.NetworkState;
import ai.jobox.assignment.util.DateTimeUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.subjects.ReplaySubject;

public class ArticlesApiPageKeyedDataSource extends PageKeyedDataSource<String, Article> {

    private static final String TAG = ArticlesApiPageKeyedDataSource.class.getSimpleName();

    private final ArticlesApi articlesApi;

    private final MutableLiveData<NetworkState> networkState;
    private final ReplaySubject<Article> articles;

    ArticlesApiPageKeyedDataSource() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ArticlesApi.BASE_API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        articlesApi = retrofit.create(ArticlesApi.class);

        networkState = new MutableLiveData<>();
        articles = ReplaySubject.create();
    }

    public MutableLiveData getNetworkState() {
        return networkState;
    }

    public ReplaySubject<Article> getArticles() {
        return articles;
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<String> params, @NonNull final LoadInitialCallback<String, Article> callback) {
        Log.i(TAG, "Loading Initial Data, Requested Load Size: " + params.requestedLoadSize);

        networkState.postValue(NetworkState.LOADING);

        final String currentDate = DateTimeUtils.getCurrentDate();
        Log.i(TAG, "Loading Articles from the date : " + currentDate);

        /* Retrieve the Articles based on Today's Date */
        Call<ArticlesWrapper> callBack = articlesApi.getArticles(currentDate, currentDate);
        callBack.enqueue(new Callback<ArticlesWrapper>() {

            @Override
            public void onResponse(Call<ArticlesWrapper> call, Response<ArticlesWrapper> response) {

                if (response.isSuccessful()) {

                    callback.onResult(response.body().getArticles(), null, DateTimeUtils.getPreviousDay(currentDate));
                    networkState.postValue(NetworkState.LOADED);
                    response.body().getArticles().forEach(articles::onNext);

                } else {

                    Log.e("onResponse: ", response.message());
                    networkState.postValue(new NetworkState(NetworkState.Status.FAILED, response.message()));
                }
            }

            @Override
            public void onFailure(Call<ArticlesWrapper> call, Throwable t) {

                String errorMessage;
                if (t.getMessage() == null) {
                    errorMessage = "unknown error";
                } else {
                    errorMessage = t.getMessage();
                }

                networkState.postValue(new NetworkState(NetworkState.Status.FAILED, errorMessage));
                callback.onResult(new ArrayList<>(), null, DateTimeUtils.getPreviousDay(currentDate));
            }
        });
    }

    @Override
    public void loadAfter(@NonNull LoadParams<String> params, final @NonNull LoadCallback<String, Article> callback) {
        Log.i(TAG, "Loading page " + params.key );

        networkState.postValue(NetworkState.LOADING);

        Call<ArticlesWrapper> callBack = articlesApi.getArticles(params.key, params.key);
        callBack.enqueue(new Callback<ArticlesWrapper>() {

            @Override
            public void onResponse(Call<ArticlesWrapper> call, Response<ArticlesWrapper> response) {

                if (response.isSuccessful()) {

                    callback.onResult(response.body().getArticles(), DateTimeUtils.getPreviousDay(params.key));
                    networkState.postValue(NetworkState.LOADED);
                    response.body().getArticles().forEach(articles::onNext);

                } else {

                    Log.e("onResponse: ", response.message());
                    networkState.postValue(new NetworkState(NetworkState.Status.FAILED, response.message()));
                }
            }

            @Override
            public void onFailure(Call<ArticlesWrapper> call, Throwable t) {

                String errorMessage;
                if (t.getMessage() == null) {
                    errorMessage = "unknown error";
                } else {
                    errorMessage = t.getMessage();
                }

                networkState.postValue(new NetworkState(NetworkState.Status.FAILED, errorMessage));
                callback.onResult(new ArrayList<>(), DateTimeUtils.getPreviousDay(params.key));
            }
        });
    }

    @Override
    public void loadBefore(@NonNull LoadParams<String> params, @NonNull LoadCallback<String, Article> callback) {}
}
