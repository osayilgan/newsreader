package ai.jobox.assignment.api;

import ai.jobox.assignment.model.ArticlesWrapper;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ArticlesApi {

    String BASE_API_URL = "https://newsapi.org/";

    @GET("/v2/everything?q=\"ai\"&sortBy=publishedAt&apiKey=d1d760ed1c1e4b5189e8b810108ac762&language=en&pageSize=25")
    Call<ArticlesWrapper> getArticles(@Query("from") String from, @Query("to") String to);
}
