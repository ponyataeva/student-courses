package basic.service.impl;

import basic.dto.SimpleUser;
import basic.service.BasicService;
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
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.nio.charset.Charset;
import java.util.Base64;

@Service
public class BasicServiceImpl implements BasicService {

    private static final String USER_INFO = "/data/{user_id}";
    private static final String AUTH = "/auth";

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
    private RestTemplate restTemplate;

    @Autowired
    private HttpSessionCsrfTokenRepository csrfTokenRepository;

    @PostConstruct
    public void setEncodedAuthCredential() {
        authCredential = encodeCredential(authLogin, authPassword);
        dataCredential = encodeCredential(dataLogin, dataPassword);
    }

    /**
     * Encode credential to Base64 for Basic Authentication.
     *
     * @param login user login
     * @param password user password
     * @return encoded string.
     */
    private String encodeCredential(String login, String password) {
        String credential = login + ":" + password;
        String encodedCredential = new String(Base64.getEncoder().encode(credential.getBytes()), Charset.forName("UTF-8"));
        return "Basic " + encodedCredential;
    }

    @Value("${data.host}")
    public void setUserDataUrl(String hostAddress) {
        userDataUrl = "http://" + hostAddress + USER_INFO;
    }

    @Value("${auth.host}")
    public void setAuthUrl(String hostAddress) {
        authUrl = "http://" + hostAddress + AUTH;
    }

    public ResponseEntity getUserById(String userId) {
        HttpEntity entity = new HttpEntity<>(addAuthInfo(dataCredential));
        ResponseEntity result = restTemplate.exchange(userDataUrl, HttpMethod.GET, entity, String.class, userId);
        HttpStatus resultStatus = result.getStatusCode();
        if (!HttpStatus.OK.equals(resultStatus)) {
            return getErrorEntity(resultStatus);
        } else {
            return result;
        }
    }

    public ResponseEntity doAuth(SimpleUser user, HttpServletRequest request) {
        HttpEntity entity = new HttpEntity<>(user, addAuthInfo(authCredential));
        ResponseEntity result = restTemplate.exchange(authUrl, HttpMethod.POST, entity, String.class);
        HttpStatus resultStatus = result.getStatusCode();
        if (!HttpStatus.OK.equals(resultStatus)) {
            return getErrorEntity(resultStatus);
        } else {
            CsrfToken token = csrfTokenRepository.generateToken(request);
            csrfTokenRepository.saveToken(token, request, null);
            HttpHeaders headers = new HttpHeaders();
            headers.add(token.getHeaderName(), token.getToken());
            return new ResponseEntity(headers, resultStatus);
        }
    }

    /**
     * Handle expected error.
     *
     * @param errorStatus result status of invoke.
     * @return entity with designed error.
     */
    private ResponseEntity getErrorEntity(HttpStatus errorStatus) {
        if (HttpStatus.FORBIDDEN.equals(errorStatus)) {
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        } else {
            return new ResponseEntity(errorStatus);
        }
    }

    /**
     * Create headers class with Basic authentication info.
     *
     * @param credentials will encode and set to header.
     * @return prepared header.
     */
    private HttpHeaders addAuthInfo(String credentials) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, credentials);
        return headers;
    }
}
