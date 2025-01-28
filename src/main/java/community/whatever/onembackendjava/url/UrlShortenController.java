package community.whatever.onembackendjava.url;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class UrlShortenController {


    private  final UrlShortenService urlShortenService ;

    @Autowired
    private UrlShortenController(UrlShortenService urlShortenService){
        this.urlShortenService = urlShortenService ;
    }

    @PostMapping("/shorten-url/search")
    public String shortenUrlSearch(@RequestBody RequestVO requestVO ) {
        return urlShortenService.searchUrl(requestVO.getKey());
    }
    @PostMapping("/shorten-url/create")
    public String shortenUrlCreate(@RequestBody RequestVO requestVO ) {
        return  urlShortenService.createKey(requestVO.getUrl()) ;
    }

    @GetMapping("/shorten-url/delete/{key}")
    public String shortenUrlDelete(@PathVariable String key){
    //  delete에 대하여 return 메세지 고민 즁
        String result ="False" ;
        if(urlShortenService.deleteKey(key)){
            result = "True";
        }
        return result;

    }
}
