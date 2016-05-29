/*
 * LCT
 * Written by Sylvain Gourio
 * sylvain.gourio@gmail.com
 */

package org.lct.game.ws.controllers;

import org.lct.game.ws.beans.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.io.File;

/**
 * Created by sgourio on 29/05/16.
 */
@RestController
@RequestMapping(value="/picture")
public class PictureController {
    @Value("${image.profil.folder}")
    private String uploadPath;

    @RequestMapping(value="/{userId}/{pictureName}.png", method= RequestMethod.GET, produces = MediaType.IMAGE_PNG_VALUE)
    @ResponseStatus(value= HttpStatus.OK)
    @ResponseBody
    public File uploadPicture(@ModelAttribute User user, @PathVariable("userId") String userId, @PathVariable("pictureName") String pictureName) throws Exception{
        return new File(uploadPath+"/"+ userId + "/" + pictureName +".png");
    }
}
