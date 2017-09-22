package data;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.Filter;

import static org.junit.Assert.assertEquals;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = DataApplication.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class DataControllerTest {

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
    public void getData_notExistingUser_return404() throws Exception {
        ResultActions result = mvc.perform(get(DataController.USER_INFO, "-1").with(httpBasic(login, password)));
        assertEquals("", result.andReturn().getResponse().getContentAsString());
        int status = result.andReturn().getResponse().getStatus();
        assertEquals(HttpStatus.NOT_FOUND.value(), status);
    }

    @Test
    public void getData_authorized_returnUser() throws Exception {
        ResultActions result = mvc.perform(get(DataController.USER_INFO, "1").with(httpBasic(login, password)));
        JSONObject jsonObj = new JSONObject(result.andReturn().getResponse().getContentAsString());
        assertEquals("1", jsonObj.getString("userId"));
        int status = result.andReturn().getResponse().getStatus();
        assertEquals(HttpStatus.OK.value(), status);
    }

    @Test
    public void getData_notAuthorized_return403() throws Exception {
        ResultActions result = mvc.perform(get(DataController.USER_INFO, "1").with(httpBasic(login, password + "_error")));
        int status = result.andReturn().getResponse().getStatus();
        assertEquals(HttpStatus.FORBIDDEN.value(), status);
    }

}