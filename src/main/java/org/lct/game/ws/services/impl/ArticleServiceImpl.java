package org.lct.game.ws.services.impl;

import org.joda.time.DateTime;
import org.lct.game.ws.beans.model.Article;
import org.lct.game.ws.dao.ArticleRepository;
import org.lct.game.ws.services.ArticleService;
import org.springframework.data.domain.PageRequest;

import java.util.List;

/**
 * Created by sgourio on 27/12/2015.
 */
public class ArticleServiceImpl implements ArticleService {

    private final ArticleRepository articleRepository;

    public ArticleServiceImpl(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    @Override
    public Article save(String id, String title, String content, DateTime creationDate, boolean published) {
        Article article = new Article(id, creationDate.toDate(), title, content, published);
        article = articleRepository.save(article);
        return article;
    }

    @Override
    public void publish(String id) {
        publish(id, true);
    }

    @Override
    public void unpublish(String id) {
        publish(id, false);
    }

    public void publish(String id, boolean publish) {
        Article article = articleRepository.findOne(id);
        Article newOne = new Article(article.getId(), article.getCreationDate(), article.getTitle(), article.getContent(), publish);
        articleRepository.save(newOne);
    }

    @Override
    public List<Article> findArticles(Boolean published) {
        if( published != null ){
            return articleRepository.findByPublishedOrderByCreationDateDesc(published, new PageRequest(0, 12));
        }
        return (List<Article>) articleRepository.findTop20ByOrderByCreationDateDesc();
    }

    @Override
    public void delete(String id) {
        articleRepository.delete(id);
    }
}
