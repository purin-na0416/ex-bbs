package com.example.exbbs.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.exbbs.domain.Article;
import com.example.exbbs.domain.Comment;
import com.example.exbbs.repository.ArticleRepository;
import com.example.exbbs.repository.CommentRepository;

import jakarta.servlet.ServletContext;

@Controller
@RequestMapping("/top")
public class ArticleController {
  @Autowired
  private ArticleRepository articleRepository;

  @Autowired
  private CommentRepository commentRepository;

  @Autowired
  private ServletContext application;

  @GetMapping("")

  /**
   * 記事一覧とコメント一覧をスコープに入れ、掲示板画面を表示する
   * 
   * @return bbs.htmlに遷移する
   */
  public String index() {
    List<Article> articleList = articleRepository.findAll();
    application.setAttribute("articleList", articleList);

    List<Comment> commentList = new ArrayList<>();
    for(Article article : articleList) {
      commentList = commentRepository.findByArticleId(article.getId());
    }

    application.setAttribute("commentList", commentList);

    return "bbs";
  }

  @PostMapping("/article")

  /**
   * 記事を投稿する
   * 
   * @param article
   * @return bbs.htmlにリダイレクト
   */
  public String insertArticle(Article article) {
    articleRepository.insert(article);
    return "redirect:/top";
  }

  @PostMapping("/comment")

  /**
   * 
   * @param comment
   * @return
   */
  public String insertComment(Comment comment) {
    commentRepository.insert(comment);
    return "redirect:/top";
  }

  @GetMapping("/delete")
  public String deleteArticle() {
    return "redirect:/top";
  }
}
