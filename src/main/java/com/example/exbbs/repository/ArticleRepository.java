package com.example.exbbs.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.exbbs.domain.Article;

@Repository
@Transactional
public class ArticleRepository {
  @Autowired
  private NamedParameterJdbcTemplate template;

  private static final RowMapper<Article> ARTICLE_ROW_MAPPER = (rs, i) -> {
    Article article = new Article();

    article.setId(rs.getInt("id"));
    article.setName(rs.getString("name"));
    article.setContent(rs.getString("content"));
    
    return article;
  };

  /**
   * 記事をデータベースに挿入する
   * 
   * @param article
   */
  public void insert(Article article) {
    String sql = "insert into articles(name, content) values(:name, :content);";

    SqlParameterSource param = new BeanPropertySqlParameterSource(article);

    template.update(sql, param);
  }

  /**
   * 記事一覧を取得する
   * 
   * @return 取得した記事リスト
   */
  public List<Article> findAll() {
    String sql = """
                  select id, name, content
                  from articles order by id desc;
                 """;

    List<Article> articleList = template.query(sql, ARTICLE_ROW_MAPPER);

    return articleList;
  }

  public void deleteById(int id) {
    String sql = "delete from articles where id =:id;";

    SqlParameterSource param = new MapSqlParameterSource().addValue("id", id);

    template.update(sql, param);
  }
}
