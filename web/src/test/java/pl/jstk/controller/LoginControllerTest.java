package pl.jstk.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import pl.jstk.constants.ModelConstants;
import pl.jstk.constants.ViewNames;

@RunWith(SpringJUnit4ClassRunner.class)
public class LoginControllerTest {

	private MockMvc mockMvc;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(new LoginController()).setViewResolvers(viewResolver()).build();
	}

	@Test
	public void testLoginMethod() throws Exception {
		// given when
		ResultActions resultActions = mockMvc.perform(get("/login"));
		// then
		resultActions.andExpect(status().isOk()).andExpect(view().name(ViewNames.LOGIN));
	}

	@Test
	public void shouldReturnLoginFailedCommunicateWhenCredentialsAreWrong() throws Exception {
		// given when
		ResultActions resultActions = mockMvc.perform(get("/loginfailed"));
		// then
		resultActions.andExpect(status().isOk()).andExpect(model().attribute(ModelConstants.ERROR, true));
	}

	private ViewResolver viewResolver() {
		InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
		viewResolver.setPrefix("classpath:templates/");
		viewResolver.setSuffix(".html");
		return viewResolver;
	}
}
