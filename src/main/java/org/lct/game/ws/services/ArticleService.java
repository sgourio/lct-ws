package org.lct.game.ws.services;

import org.joda.time.DateTime;
import org.lct.game.ws.beans.model.Article;

import java.util.List;

/**
 * Created by sgourio on 27/12/2015.
 */
public interface ArticleService {

    public Article save(String id, String title, String content, DateTime creationDate, boolean published);

    public void publish(String id);

    public void unpublish(String id);

    public void delete(String id);

    public List<Article> findArticles(Boolean published);
}
