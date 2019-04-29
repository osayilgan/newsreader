package ai.jobox.assignment.database;

import androidx.paging.DataSource;

public class ArticlesDbDataSourceFactory extends DataSource.Factory<String, Article> {

    private ArticlesDbPageKeyedDataSource dataSource;
    private ArticleDao articleDao;

    public ArticlesDbDataSourceFactory(ArticleDao dao) {
        articleDao = dao;
        init();
    }

    private void init() {
        dataSource = new ArticlesDbPageKeyedDataSource(articleDao);
    }

    @Override
    public DataSource<String, Article> create() {
        init();
        return dataSource;
    }

    public ArticlesDbPageKeyedDataSource getDataSource() {
        return dataSource;
    }
}
