package com.example.exbbs.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.exbbs.domain.Article;
import com.example.exbbs.domain.Comment;
import com.example.exbbs.repository.ArticleRepository;
import com.example.exbbs.repository.CommentRepository;


@Controller
@RequestMapping("/top")
public class ArticleController {
  @Autowired
  private ArticleRepository articleRepository;

  @Autowired
  private CommentRepository commentRepository;

  @GetMapping("")

  /**
   * 記事一覧とコメント一覧をスコープに入れ、掲示板画面を表示する
   * 
   * @return bbs.htmlに遷移する
   */
  public String index(Model model) {
    List<Article> articleList = articleRepository.findAll();
    
    //各記事のコメントリストに関連したコメントを追加する
    /*
    for(Article article : articleList) {
      article.setCommentList(commentRepository.findByArticleId(article.getId()));
    }
    */

    model.addAttribute("articleList", articleList);
    
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
   * コメントを投稿する
   * 
   * @param comment
   * @return bbs.htmlにリダイレクト
   */
  public String insertComment(Comment comment) {
    commentRepository.insert(comment);
    return "redirect:/top";
  }

  @GetMapping("/delete")

  /**
   * 記事とそれに関連したコメントを削除する
   * 
   * @param id 記事の投稿ID
   * @return bbs.htmlにリダイレクト
   */
  public String deleteArticle(int id) {
    articleRepository.deleteById(id);
    commentRepository.deleteByArticleId(id);

    return "redirect:/top";
  }
}
