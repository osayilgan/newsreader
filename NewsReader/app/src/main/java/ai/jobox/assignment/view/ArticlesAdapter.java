package ai.jobox.assignment.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import ai.jobox.assignment.R;
import ai.jobox.assignment.database.Article;
import ai.jobox.assignment.model.ErrorInterface;
import ai.jobox.assignment.model.NetworkState;

public class ArticlesAdapter extends PagedListAdapter<Article, ArticlesAdapter.ArticlesViewHolder> {

    private NetworkState networkState;
    private ErrorInterface errorHandlerInterface;

    ArticlesAdapter(ErrorInterface errorHandlerInterface) {
        super(DIFF_CALLBACK);
        this.errorHandlerInterface = errorHandlerInterface;
    }

    @NonNull
    @Override
    public ArticlesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.articles_adapter_item, parent, false);
        return new ArticlesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ArticlesViewHolder holder, int position) {

        Article article = getItem(position);
        if (article != null) holder.bindData(article);
        else holder.clear();
    }

    /**
     * ViewHolder for Article Adapter.
     */
    class ArticlesViewHolder extends RecyclerView.ViewHolder {

        ImageView articleImage;
        ImageView articleErrorImage;
        TextView articleTitle;
        TextView articleDescription;

        public ArticlesViewHolder(View itemView) {
            super(itemView);
            articleImage = itemView.findViewById(R.id.article_image);
            articleErrorImage = itemView.findViewById(R.id.article_broken_image);
            articleTitle = itemView.findViewById(R.id.article_title);
            articleDescription = itemView.findViewById(R.id.article_description);
        }

        void clear() {
            itemView.invalidate();
            articleImage.invalidate();
            articleErrorImage.invalidate();
            articleTitle.invalidate();
            articleDescription.invalidate();
        }

        public void bindData(Article article) {

            /* Load Image with Picasso. Check if the URL of the Article not null or empty */
            if (article.getUrlToImage() != null && !article.getUrlToImage().isEmpty()) {

                articleErrorImage.setVisibility(View.INVISIBLE);
                articleImage.setVisibility(View.VISIBLE);

                Picasso.get().load(article.getUrlToImage()).fit().into(articleImage);

            } else {
                articleErrorImage.setVisibility(View.VISIBLE);
                articleImage.setVisibility(View.INVISIBLE);
            }

            /* Set Title */
            articleTitle.setText(article.getTitle());

            /* Set Description */
            articleDescription.setText(article.getDescription());
        }
    }

    /**
     * Sets Network State and Handles Error Messages.
     *
     * @param networkState
     */
    public void setNetworkState(NetworkState networkState) {
        this.networkState = networkState;
        if (networkState.getStatus() == NetworkState.Status.FAILED && errorHandlerInterface != null) errorHandlerInterface.onError(networkState.getMsg());
    }

    private static DiffUtil.ItemCallback<Article> DIFF_CALLBACK = new DiffUtil.ItemCallback<Article>() {

        @Override
        public boolean areItemsTheSame(Article oldArticle, Article newArticle) {
            return oldArticle.getUrl().equals(newArticle.getUrl());
        }

        @Override
        public boolean areContentsTheSame(Article oldArticle, Article newArticle) {
            return oldArticle.getUrl().equals(newArticle.getUrl());
        }
    };
}
