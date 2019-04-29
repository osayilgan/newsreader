package ai.jobox.assignment.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import ai.jobox.assignment.database.Article;

public class ArticlesWrapper {

    @SerializedName("articles")
    @Expose private List<Article> articles;

    @SerializedName("status")
    @Expose private String status;

    @SerializedName("totalResults")
    @Expose private int totalResults;

    @SerializedName("message")
    @Expose private String errorMessage;

    public ArticlesWrapper() {}

    public List<Article> getArticles() {
        return articles;
    }

    public void setArticles(List<Article> articles) {
        this.articles = articles;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
