package org.lct.game.ws.controllers;

import org.joda.time.DateTime;
import org.lct.game.ws.beans.model.Article;
import org.lct.game.ws.beans.model.User;
import org.lct.game.ws.beans.view.ArticleBean;
import org.lct.game.ws.services.ArticleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sgourio on 29/12/2015.
 */
@RestController
@RequestMapping(value="/article")
public class ArticleController {

    private final Logger logger = LoggerFactory.getLogger(ArticleController.class);

    @Autowired
    private ArticleService articleService;

    @RequestMapping(value={"","/{id}"}, method= RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value= HttpStatus.OK)
    @ResponseBody
    public ArticleBean save(@RequestBody ArticleBean articleBean, @ModelAttribute User user) {
        logger.info("save article " + articleBean.getTitle() + "...");
        Article article = articleService.save(articleBean.getId(), articleBean.getTitle(), articleBean.getContent(), articleBean.getCreationDate() == null ? DateTime.now() : new DateTime(articleBean.getCreationDate()), articleBean.isPublished());
        return articleBeanMapper(article);
    }

    @RequestMapping(value="/{id}", method= RequestMethod.DELETE, produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseStatus(value= HttpStatus.OK)
    @ResponseBody
    public void delete(@PathVariable("id") String id, @ModelAttribute User user) {
        articleService.delete(id);
    }

    @RequestMapping(value="", method= RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value= HttpStatus.OK)
    @ResponseBody
    public List<ArticleBean> find(@RequestParam(required = false) Boolean all){
        if( all != null && all ){
            return articleBeanListMapper(articleService.findArticles(null));
        }
        return articleBeanListMapper(articleService.findArticles(true));
    }

    private ArticleBean articleBeanMapper(Article article){
        return new ArticleBean(article.getId(), article.getCreationDate(), article.getTitle(), article.getContent(), article.isPublished());
    }

    private List<ArticleBean> articleBeanListMapper(List<Article> articleList){
        List<ArticleBean> result = new ArrayList<>();
        for( Article article : articleList ){
            result.add(articleBeanMapper(article));
        }
        return result;
    }
}
