package org.lct.game.ws.dao;

import org.lct.game.ws.beans.model.Article;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * Created by sgourio on 27/12/2015.
 */
public interface ArticleRepository extends MongoRepository<Article, String> {

    public List<Article> findByPublishedOrderByCreationDateDesc(boolean published, Pageable pageable);
    public List<Article> findTop20ByOrderByCreationDateDesc();
}
