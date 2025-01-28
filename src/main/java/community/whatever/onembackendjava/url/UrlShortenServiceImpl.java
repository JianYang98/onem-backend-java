package community.whatever.onembackendjava.url;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UrlShortenServiceImpl implements  UrlShortenService{

    private  UrlShortenRepository urlShortenRepository ;


    @Autowired
    public UrlShortenServiceImpl(UrlShortenRepository urlShortenRepository) {
        this.urlShortenRepository = urlShortenRepository ;
    }

    public Boolean existKey(String key) {

        return urlShortenRepository.existKey(key) ;
    }

    public Boolean existUrl(String url) {
        return urlShortenRepository.existUrl(url) ;
    }

    public String searchKey(String url) {
        return urlShortenRepository.searchKey(url);
    }

    public String createKey(String url) {
        String result = "";
        if(urlShortenRepository.existKey(url)){
            result = urlShortenRepository.searchKey(url);
        }else{
            result = urlShortenRepository.createKey(url);
        }
        System.out.println("result = " + result);
        return result;
    }

    public String searchUrl(String key) {
        if(urlShortenRepository.existKey(key)){
            return urlShortenRepository.searchUrl(key);
        }else{
            // 키가 존재 하지 않으면 Exception
            throw new IllegalArgumentException("Invalid key");
        }
    }
    public boolean deleteKey(String key) {
        if(urlShortenRepository.existKey(key)){
            return urlShortenRepository.deleteKey(key);
        }else{
            throw new IllegalArgumentException("Invalid key");
        }

    }
}
