package back.api.web;


import back.api.business.ImageService;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.awt.*;
import java.io.File;
import java.util.logging.Logger;

@RestController
@RequestMapping("/resources")
public class ResourceController {

    private final Logger logger = Logger.getLogger(ResourceController.class.getName());
    private final ImageService imageService;

    public ResourceController(ImageService imageService){
        this.imageService = imageService;
    }

    @GetMapping(path = "/images/{product}", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<Resource> productImage(@PathVariable() String product) {

//      HttpHeaders headers = new HttpHeaders();
//      Resource resource = new FileSystemResource(new File("video.mp3"));
//      headers.setContentDisposition(ContentDisposition.attachment().filename("video.mp3").build());
//      HttpCookie cookie = ResponseCookie.from("heroku-nav-data", "caca").build();

        return ResponseEntity.ok(imageService.getImage(product));
    }
}
