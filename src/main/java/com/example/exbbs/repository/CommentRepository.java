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

import com.example.exbbs.domain.Comment;

@Repository
@Transactional
public class CommentRepository {
  @Autowired
  private NamedParameterJdbcTemplate template;

  private static final RowMapper<Comment> COMMENT_ROW_MAPPER = (rs, i) -> {
    Comment comment = new Comment();

    comment.setId(rs.getInt("id"));
    comment.setName(rs.getString("name"));
    comment.setContent(rs.getString("content"));
    comment.setArticleId(rs.getInt("article_id"));

    return comment;
  };

  /**
   * 投稿IDで検索し、その記事についたコメントを取得する
   * 
   * @param id 投稿ID
   * @return コメントのリスト
   */
  public List<Comment> findByArticleId(int id) {
    String sql = """
                  select id, name, content, article_id 
                  from comments 
                  where article_id =:articleId
                  order by id desc;
                  """;
                      
    SqlParameterSource param = new MapSqlParameterSource().addValue("articleId", id);

    List<Comment> commentList = template.query(sql, param, COMMENT_ROW_MAPPER);

    return commentList;
  }

  /**
   * コメントをデータベースに登録する
   * 
   * @param comment
   */
  public void insert(Comment comment) {
    String sql = """
                  insert into comments (name, content, article_id) values(:name, :content, :articleId);
                 """;

    SqlParameterSource param = new BeanPropertySqlParameterSource(comment);
    
    template.update(sql, param);
  }

  /**
   * 投稿IDを指定してそのIDに結びつけられたコメントを削除する
   * 
   * @param articleId
   */
  public void deleteByArticleId(int articleId) {
    String sql = "delete from comments where article_id =:articleId;";

    SqlParameterSource param = new MapSqlParameterSource().addValue("articleId", articleId);

    template.update(sql, param);
  }
}
