package basic;

import basic.dto.SimpleUser;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.Filter;

import static org.junit.Assert.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = BasicApplication.class
        , webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class BasicControllerTest {

    private static final String CSRF_HEADER = "X-CSRF-TOKEN";
    @Value("${auth.login}")
    private String authLogin;

    @Value("${auth.password}")
    private String authPassword;

    @Value("${data.login}")
    private String dataLogin;

    @Value("${data.password}")
    private String dataPassword;

    private MockMvc mvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private Filter springSecurityFilterChain;

    @Before
    public void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .addFilters(springSecurityFilterChain)
                .build();
    }

    @Test
    public void login_success_addCsrfToken() throws Exception {
        SimpleUser user = new SimpleUser();
        user.setUser("user-has-a-grade");
        user.setPassword("aGrade");

        ResultActions result = performLogin(user);
        int status = result.andReturn().getResponse().getStatus();
        assertEquals(HttpStatus.OK.value(), status);
        String header = result.andReturn().getResponse().getHeader(CSRF_HEADER);
        System.out.println("Header : " + header);
        assertNotNull(header);
    }

    @Test
    public void logout_withHeader_removeToken() throws Exception {
        SimpleUser user = new SimpleUser();
        user.setUser("user-has-a-grade");
        user.setPassword("aGrade");

        ResultActions result = performLogout();
        int status = result.andReturn().getResponse().getStatus();
        assertEquals(HttpStatus.OK.value(), status);
        String header = result.andReturn().getResponse().getHeader(CSRF_HEADER);
        assertNull(header);
    }

    private ResultActions performLogin(SimpleUser user) throws Exception {
        return mvc.perform(post(BasicController.LOGIN)
                .with(httpBasic(authLogin, authPassword))
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(user)));
    }

    private ResultActions performLogout() throws Exception {
        return mvc.perform(post(BasicController.LOGOUT)
                .with(httpBasic(authLogin, authPassword))
                .header(CSRF_HEADER, "123"));
    }
}
