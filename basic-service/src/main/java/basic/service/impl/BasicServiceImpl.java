package basic.service.impl;

import basic.dto.SimpleUser;
import basic.service.BasicService;
import basic.service.RestTemplateFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.nio.charset.Charset;
import java.util.Base64;

@Service
public class BasicServiceImpl implements BasicService {

    private static final String USER_DATA_PATH = "/data/{user_id}";
    private static final String AUTH_PATH = "/auth";

    @Value("${auth.login}")
    private String authLogin;

    @Value("${auth.password}")
    private String authPassword;

    @Value("${data.login}")
    private String dataLogin;

    @Value("${data.password}")
    private String dataPassword;

    private String authCredential;
    private String dataCredential;

    private String userDataUrl;
    private String authUrl;

    @Autowired
    private RestTemplateFactory restTemplateFactory;
    private HttpSessionCsrfTokenRepository csrfTokenRepository = new HttpSessionCsrfTokenRepository();


    @PostConstruct
    public void setEncodedAuthCredential() {
        String credential = authLogin + ":" + authPassword;
        String encodedCredential = new String(Base64.getEncoder().encode(credential.getBytes()), Charset.forName("UTF-8"));
        authCredential = "Basic " + encodedCredential;
    }

    @PostConstruct
    public void setEncodedDataCredential() {
        String credential = dataLogin + ":" + dataPassword;
        String encodedCredential = new String(Base64.getEncoder().encode(credential.getBytes()), Charset.forName("UTF-8"));
        dataCredential = "Basic " + encodedCredential;
    }

    @Value("${data.host}")
    public void setUserDataUrl(String hostAddress) {
        userDataUrl = "http://" + hostAddress + USER_DATA_PATH;
    }

    @Value("${auth.host}")
    public void setAuthUrl(String hostAddress) {
        authUrl = "http://" + hostAddress + AUTH_PATH;
    }

    public RestTemplate getRestTemplate() {
        return restTemplateFactory.getObject();
    }

    public ResponseEntity<String> getUserById(String userId) {
        HttpEntity entity = new HttpEntity(addAuthInfo(dataCredential));
        ResponseEntity result;
        try {
            result = getRestTemplate().exchange(userDataUrl, HttpMethod.GET, entity, String.class, userId);
        } catch (ResourceAccessException e) {
            return new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE);
        }
        HttpStatus status = result.getStatusCode();
        if (HttpStatus.FORBIDDEN.equals(status)) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } else {
            return result;
        }
    }

    public ResponseEntity doAuth(SimpleUser user, HttpServletRequest request) {
        HttpEntity entity = new HttpEntity<>(user, addAuthInfo(authCredential));
        ResponseEntity result = doAuth(entity);
        if (HttpStatus.OK.equals(result.getStatusCode())) {
            CsrfToken token = csrfTokenRepository.generateToken(request);
            csrfTokenRepository.saveToken(token, request, null);
            HttpHeaders headers = new HttpHeaders();
            headers.add(token.getHeaderName(), token.getToken());
            return new ResponseEntity(headers, HttpStatus.OK);
        } else {
            return result;
        }
    }

    private ResponseEntity doAuth(HttpEntity entity) {
        try {
            return getRestTemplate().exchange(authUrl, HttpMethod.POST, entity, ResponseEntity.class);
        } catch (HttpClientErrorException e) {
            int responseCode = e.getRawStatusCode();
            if (Integer.compare(HttpStatus.UNAUTHORIZED.value(), responseCode) == 0) {
                return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
            } else if (Integer.compare(HttpStatus.FORBIDDEN.value(), responseCode) == 0) {
                return new ResponseEntity(HttpStatus.UNAUTHORIZED);
            } else {
                throw e;
            }
        } catch (ResourceAccessException e) {
            return new ResponseEntity(HttpStatus.SERVICE_UNAVAILABLE);
        }
    }

    private HttpHeaders addAuthInfo(String credentials) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, credentials);
        return headers;
    }
}
