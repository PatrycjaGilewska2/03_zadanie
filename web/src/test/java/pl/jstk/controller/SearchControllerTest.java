package pl.jstk.controller;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import pl.jstk.constants.ModelConstants;
import pl.jstk.constants.ViewNames;
import pl.jstk.service.BookService;
import pl.jstk.service.validation.SearchValidation;
import pl.jstk.to.BookTo;

public class SearchControllerTest {

	@Mock
	private BookService bookServiceMock;

	@Mock
	private SearchValidation searchValidation;

	@InjectMocks
	private SearchController searchController;

	private MockMvc mockMvc;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(searchController).setViewResolvers(viewResolver()).build();
	}
	
	@Test
	public void shouldInitEmptyModel() throws Exception {
		// given
		// when
		ResultActions resultActions = mockMvc.perform(get("/books/search"));
		// then
		resultActions.andExpect(status().isOk())
			.andExpect(model().attribute(ModelConstants.SEARCH_BOOK, new BookTo()))
			.andExpect(model().attribute(ModelConstants.EMPTY_RESULT, false))
			.andExpect(view().name(ViewNames.SEARCHING));
	}

	@Test
	public void shouldDisplayErrorMessageWhenLessThan3SignsInQuery() throws Exception {
		// given
		BookTo book = new BookTo(null, "Pa", "To", null);

		ArgumentCaptor<String> titleCaptor = ArgumentCaptor.forClass(String.class);
		ArgumentCaptor<String> authorCaptor = ArgumentCaptor.forClass(String.class);

		when(searchValidation.validate("Pa", "To")).thenReturn(false);
		// when
		ResultActions resultActions = mockMvc.perform(post("/books/search").flashAttr("newBook", book));
		// then
		Mockito.verify(searchValidation).validate(titleCaptor.capture(), authorCaptor.capture());
		assertEquals("Pa", titleCaptor.getValue());
		assertEquals("To", authorCaptor.getValue());
		resultActions.andExpect(status().isOk()).andExpect(model().attribute(ModelConstants.ERROR, true));
	}

	@Test
	public void shouldFindBookAndShowListOfBooks() throws Exception {
		// given
		BookTo book = new BookTo(null, "TomcioPaluch", "Tomcio Paluch", null);
		List<BookTo> books = Arrays.asList(book);

		ArgumentCaptor<String> titleCaptorValidation = ArgumentCaptor.forClass(String.class);
		ArgumentCaptor<String> authorCaptorValidation = ArgumentCaptor.forClass(String.class);
		ArgumentCaptor<String> titleCaptorFind = ArgumentCaptor.forClass(String.class);
		ArgumentCaptor<String> authorCaptorFind = ArgumentCaptor.forClass(String.class);

		when(searchValidation.validate("TomcioPaluch", "Tomcio Paluch")).thenReturn(true);
		when(bookServiceMock.findBooksByTitleAndAuthor("TomcioPaluch", "Tomcio Paluch")).thenReturn(books);
		// when
		ResultActions resultActions = mockMvc.perform(post("/books/search").flashAttr("newBook", book));
		// then
		Mockito.verify(searchValidation).validate(titleCaptorValidation.capture(), authorCaptorValidation.capture());
		assertEquals("TomcioPaluch", titleCaptorValidation.getValue());
		assertEquals("Tomcio Paluch", authorCaptorValidation.getValue());
		Mockito.verify(bookServiceMock).findBooksByTitleAndAuthor(titleCaptorFind.capture(),
				authorCaptorFind.capture());
		assertEquals("TomcioPaluch", titleCaptorFind.getValue());
		assertEquals("Tomcio Paluch", authorCaptorFind.getValue());

		resultActions.andExpect(status().isOk()).andExpect(model().attribute(ModelConstants.ERROR, false))
				.andExpect(model().attribute(ModelConstants.BOOK_LIST, books))
				.andExpect(model().attribute(ModelConstants.EMPTY_RESULT, false));
	}

	@Test
	public void shouldTryToFindBookAndShowInfoAboutNoResult() throws Exception {
		// given
		BookTo book = new BookTo(null, "TomcioPaluch", "Tomcio Paluch", null);

		ArgumentCaptor<String> titleCaptorValidation = ArgumentCaptor.forClass(String.class);
		ArgumentCaptor<String> authorCaptorValidation = ArgumentCaptor.forClass(String.class);
		ArgumentCaptor<String> titleCaptorFind = ArgumentCaptor.forClass(String.class);
		ArgumentCaptor<String> authorCaptorFind = ArgumentCaptor.forClass(String.class);

		when(searchValidation.validate("TomcioPaluch", "Tomcio Paluch")).thenReturn(true);
		when(bookServiceMock.findBooksByTitleAndAuthor("TomcioPaluch", "Tomcio Paluch")).thenReturn(new ArrayList<>());
		// when
		ResultActions resultActions = mockMvc.perform(post("/books/search").flashAttr("newBook", book));
		// then
		Mockito.verify(searchValidation).validate(titleCaptorValidation.capture(), authorCaptorValidation.capture());
		assertEquals("TomcioPaluch", titleCaptorValidation.getValue());
		assertEquals("Tomcio Paluch", authorCaptorValidation.getValue());
		Mockito.verify(bookServiceMock).findBooksByTitleAndAuthor(titleCaptorFind.capture(),
				authorCaptorFind.capture());
		assertEquals("TomcioPaluch", titleCaptorFind.getValue());
		assertEquals("Tomcio Paluch", authorCaptorFind.getValue());

		resultActions.andExpect(status().isOk()).andExpect(model().attribute(ModelConstants.ERROR, false))
				.andExpect(model().attribute(ModelConstants.BOOK_LIST, new ArrayList<>()))
				.andExpect(model().attribute(ModelConstants.EMPTY_RESULT, true));
	}

	private ViewResolver viewResolver() {
		InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
		viewResolver.setPrefix("classpath:templates/");
		viewResolver.setSuffix(".html");
		return viewResolver;
	}
}
