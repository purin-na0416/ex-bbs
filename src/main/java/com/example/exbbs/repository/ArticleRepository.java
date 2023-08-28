package com.example.exbbs.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
                  select a.id, a.name, a.content, c.id as com_id, 
                    c.name as com_name, c.content as com_content, 
                    c.article_id
                  from articles a
                  left outer join comments c
                  on a.id = c.article_id
                  order by a.id desc, c.id desc;
                 """;
    ArticleResultSetExtractor extractor = new ArticleResultSetExtractor();

    List<Article> articleList = template.query(sql, extractor);

    return articleList;
  }

  /**
   * 投稿IDを指定して記事を削除する
   * 
   * @param id
   */
  public void deleteById(int id) {
    String sql = "delete from articles where id =:id;";

    SqlParameterSource param = new MapSqlParameterSource().addValue("id", id);

    template.update(sql, param);
  }
}
