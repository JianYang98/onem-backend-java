package community.whatever.onembackendjava.url;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UrlShortenRestAssuredControllerTest {

    @LocalServerPort
    private int port; // 현재 실행 중인 포트 저장

    @BeforeEach
    void setUp() {
        io.restassured.RestAssured.port = port; // RestAssured가 사용할 포트 설정
    }


    @Test
    @DisplayName("키생성테스트")
    void shortenUrlCreateTest(){
        Map<String, Object> params = new HashMap<>();
        params.put("originUrl", "https://docs.oracle.com");

        ExtractableResponse<Response> createResponse =
                RestAssured
                        .given().log().all()
                        .body(params)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .when()
                        .post("/shorten-url")
                        .then().log().all()
                        .extract();

        assertThat(createResponse.statusCode()).isEqualTo(200);
        assertThat(createResponse.body().asString()).isNotEmpty() ;

    }

    @Test
    @DisplayName("키 조회")
    void getUrlByKeyTest(){
        Map<String, Object> params = new HashMap<>();
        String url = "https://docs.oracle.com";
        params.put("originUrl", url );

        ExtractableResponse<Response> createResponse1 =
                RestAssured
                        .given().log().all()
                        .body(params)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .when()
                        .post("/shorten-url")
                        .then().log().all()
                        .extract();


        assertThat(createResponse1.body().asString()).isNotEmpty();
        String key = createResponse1.body().asString();
        ExtractableResponse<Response> createResponse2 =
                RestAssured
                        .given().log().all()
                        .when()
                        .get("/shorten-url/" + key)
                        .then().log().all()
                        .extract();
        assertThat(createResponse2.statusCode()).isEqualTo(200);
        assertThat(createResponse2.body().asString()).isEqualTo(url);

    }
    @Test
    @DisplayName("키 조회 X ")
    void getUrlByKeyXTest(){

        ExtractableResponse<Response> createResponse2 =
                RestAssured
                        .given().log().all()
                        .when()
                        .get("/shorten-url/" + 1234)
                        .then().log().all()
                        .extract();
        assertThat(createResponse2.statusCode()).isEqualTo(400);

    }


    @Test
    @DisplayName("차단도메인테스트")
    void shortenBlocekdDomainsTest(){
        Map<String, Object> params = new HashMap<>();
        params.put("originUrl", "https://papago.naver.com");

        ExtractableResponse<Response> createResponse =
                RestAssured
                        .given().log().all()
                        .body(params)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .when()
                        .post("/shorten-url")
                        .then().log().all()
                        .extract();

        assertThat(createResponse.statusCode()).isEqualTo(400);
        assertThat(createResponse.body().asString()).isNotEmpty() ;

    }


    @Test
    @DisplayName("유지시간 테스트 X")
    void shortenExpiredTest() throws InterruptedException {
        Map<String, Object> params = new HashMap<>();
        String url = "https://docs.oracle.com";
        params.put("originUrl", url );

        ExtractableResponse<Response> createResponse1 =
                RestAssured
                        .given().log().all()
                        .body(params)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .when()
                        .post("/shorten-url")
                        .then().log().all()
                        .extract();
        String key = createResponse1.body().asString();

        Thread.sleep(60000); // 1 분정 stop

        ExtractableResponse<Response> createResponse2 =
                RestAssured
                        .given().log().all()
                        .when()
                        .get("/shorten-url/" + key)
                        .then().log().all()
                        .extract();
        assertThat(createResponse2.statusCode()).isEqualTo(400);

    }
}
