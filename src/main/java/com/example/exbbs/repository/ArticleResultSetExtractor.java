package com.example.exbbs.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import com.example.exbbs.domain.Article;
import com.example.exbbs.domain.Comment;

public class ArticleResultSetExtractor implements ResultSetExtractor<List<Article>> {
  @Override
  public List<Article> extractData(ResultSet rs) throws SQLException, DataAccessException {
    List<Article> articleList = new ArrayList<>();
    List<Comment> commentList = null;

    //1列前の記事ID
    int beforeId = 0;

    while(rs.next()) {
      //今検索している記事のIDを変数に入れる
      int currentId = rs.getInt("id");

      //今検索している記事のIDが1列前の記事のIDと違うときに、Articleオブジェクトを生成する
      if(beforeId != currentId) {
        Article article = new Article();

        article.setId(rs.getInt("id"));
        article.setName(rs.getString("name"));
        article.setContent(rs.getString("content"));

        commentList = new ArrayList<>();

        article.setCommentList(commentList);
        articleList.add(article);
      }

      //コメントのIDが0でないとき(コメントが存在しているとき)に、Commentオブジェクトを生成する
      if(rs.getInt("com_id") != 0) {
        Comment comment = new Comment();

        comment.setId(rs.getInt("com_id"));
        comment.setName(rs.getString("com_name"));
        comment.setContent(rs.getString("com_content"));
        comment.setArticleId(rs.getInt("article_id"));

        commentList.add(comment);
      }
      
      //beforeIDに今検索している記事のIDを代入する
      beforeId = currentId; 
    }
    
    return articleList;
  }
}
