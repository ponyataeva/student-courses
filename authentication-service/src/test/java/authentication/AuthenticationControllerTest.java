package authentication;

import authentication.dto.SimpleUser;
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

import static org.junit.Assert.assertEquals;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = AuthenticationApplication.class
        , webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class AuthenticationControllerTest {

    @Value("${login}")
    private String login;

    @Value("${password}")
    private String password;

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
    public void auth_existingUser_returnOK() throws Exception {
        SimpleUser user = new SimpleUser();
        user.setUser("user-has-a-grade");
        user.setPassword("aGrade");

        ResultActions result = performAuth(user);
        int status = result.andReturn().getResponse().getStatus();
        assertEquals(HttpStatus.OK.value(), status);
    }

    @Test
    public void auth_notExistingUser_return401() throws Exception {
        SimpleUser user = new SimpleUser();
        user.setUser("notExists");
        user.setPassword("aGrade");

        ResultActions result = performAuth(user);
        int status = result.andReturn().getResponse().getStatus();
        assertEquals(HttpStatus.UNAUTHORIZED.value(), status);
    }

    @Test
    public void auth_notAuthorized_return403() throws Exception {
        SimpleUser user = new SimpleUser();
        user.setUser("user-has-a-grade");
        user.setPassword("aGrade");

        ResultActions result = mvc.perform(post(AuthenticationController.AUTH)
                .with(httpBasic(login, password + "_error"))
                .content(new ObjectMapper().writeValueAsString(user)));
        int status = result.andReturn().getResponse().getStatus();
        assertEquals(HttpStatus.FORBIDDEN.value(), status);
    }

    private ResultActions performAuth(SimpleUser user) throws Exception {
        return mvc.perform(post(AuthenticationController.AUTH)
                .with(httpBasic(login, password))
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(user)));
    }
}
