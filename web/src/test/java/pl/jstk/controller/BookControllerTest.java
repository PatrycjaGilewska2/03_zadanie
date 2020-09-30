package pl.jstk.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
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
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import pl.jstk.config.SecurityConfig;
import pl.jstk.constants.ModelConstants;
import pl.jstk.constants.ViewNames;
import pl.jstk.enumerations.BookStatus;
import pl.jstk.service.BookService;
import pl.jstk.to.BookTo;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = SecurityConfig.class) 
public class BookControllerTest {
	
	@Mock
	private BookService bookServiceMock;

	@InjectMocks
	private BookController bookController;

	private MockMvc mockMvc;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(bookController).setViewResolvers(viewResolver()).build();
	}

	@Test
	public void shouldGetAllBooksAndReturnAList() throws Exception {
		// given
		BookTo first = new BookTo();
		first.setId(1L);
		first.setTitle("First book");
		first.setAuthors("Jan Kowalski");
		first.setStatus(BookStatus.FREE);
		BookTo second = new BookTo();
		second.setId(2L);
		second.setTitle("Second book");
		second.setAuthors("Zbigniew Nowak");
		second.setStatus(BookStatus.FREE);
		BookTo third = new BookTo();
		third.setId(3L);
		third.setTitle("Third book");
		third.setAuthors("Janusz Jankowski");
		third.setStatus(BookStatus.FREE);
		BookTo fourth = new BookTo();
		fourth.setId(4L);
		fourth.setTitle("Starter kit book");
		fourth.setAuthors("Kacper Ossoliński");
		fourth.setStatus(BookStatus.FREE);
		BookTo fifth = new BookTo();
		fifth.setId(5L);
		fifth.setTitle("Z kamerą wśród programistów");
		fifth.setAuthors("Krystyna Czubówna");
		fifth.setStatus(BookStatus.MISSING);
		List<BookTo> list = Arrays.asList(first, second, third, fourth, fifth);
		// when
		when(bookServiceMock.findAllBooks()).thenReturn(list);
		ResultActions resultActions = mockMvc.perform(get("/books"));
		// then
		resultActions.andExpect(status().isOk()).andExpect(view().name(ViewNames.BOOKS))
				.andExpect(model().attribute(ModelConstants.BOOK_LIST, list))
				.andExpect(model().attribute(ModelConstants.BOOK_LIST, hasSize(5)));
		verify(bookServiceMock, times(1)).findAllBooks();
	}

	@Test
	public void shouldFindBookById() throws Exception {
		// given
		BookTo first = new BookTo();
		first.setId(1L);
		first.setTitle("First book");
		first.setAuthors("Jan Kowalski");
		first.setStatus(BookStatus.FREE);
		// when
		when(bookServiceMock.findBookById(1L)).thenReturn(first);
		ResultActions resultActions = mockMvc.perform(get("/books/book?id=1"));
		// then
		resultActions.andExpect(view().name(ViewNames.BOOK)).andExpect(model().attribute(ModelConstants.BOOK, first));
		verify(bookServiceMock, times(1)).findBookById(1L);
	}

	@Test
	public void shouldDeleteExsistingBook() throws Exception {
		// given
		BookTo book = new BookTo();
		List<BookTo> books = new ArrayList<>();
		when(bookServiceMock.findAllBooks()).thenReturn(books);
		when(bookServiceMock.findBookById(Mockito.anyLong())).thenReturn(book);
		// when
		ResultActions resultActions = mockMvc
				.perform(get("/books/delete/1").with(user("admin").password("admin").roles("ADMIN")));
		// then
		resultActions.andExpect(status().isOk()).andExpect(view().name(ViewNames.DELETE));
		verify(bookServiceMock, times(1)).deleteBook(1L);
	}
	
	@Test
	public void shouldRedirectTo403ErrorPage() throws Exception {
		// given
		BookTo book = new BookTo();
		List<BookTo> books = new ArrayList<>();
		when(bookServiceMock.findAllBooks()).thenReturn(books);
		when(bookServiceMock.findBookById(Mockito.anyLong())).thenReturn(book);
		// when
		ResultActions resultActions = mockMvc
				.perform(get("/books/delete/1").with(user("user").password("user").roles("USER")));
		// then
		resultActions.andExpect(status().isOk()).andExpect(view().name(ViewNames.DELETE)); 	
		verify(bookServiceMock, times(1)).deleteBook(1L);
	}
	
	@Test
	public void shouldAddBook() throws Exception {
		//given
		BookTo book = new BookTo(null, "TomcioPaluch", "Tomcio Paluch", BookStatus.FREE);
		ArgumentCaptor<BookTo> captor = ArgumentCaptor.forClass(BookTo.class);
		//when
		ResultActions resultActions = mockMvc.perform(post("/books/add").flashAttr("newBook", book)
				.with(user("admin").password("admin").roles("ADMIN")));
		//then
		resultActions.andExpect(status().isOk());
		Mockito.verify(bookServiceMock).saveBook(captor.capture());
		assertEquals(book, captor.getValue());
	}

	private ViewResolver viewResolver() {
		InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
		viewResolver.setPrefix("classpath:templates/");
		viewResolver.setSuffix(".html");
		return viewResolver;
	}

}