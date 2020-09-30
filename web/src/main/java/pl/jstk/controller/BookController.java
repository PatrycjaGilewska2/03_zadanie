package pl.jstk.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import pl.jstk.constants.ModelConstants;
import pl.jstk.constants.ViewNames;
import pl.jstk.enumerations.BookStatus;
import pl.jstk.service.BookService;
import pl.jstk.to.BookTo;

@Controller
public class BookController {

	private static final String NOT_ADDED_BOOK = "Book is not added to the list";
	private static final String ADDED_BOOK = "Book is added to the list";

	@Autowired
	private BookService bookService;

	@GetMapping(value = "/books")
	public ModelAndView getAllBooks() {
		ModelAndView model = new ModelAndView();
		model.addObject(ModelConstants.BOOK_LIST, bookService.findAllBooks());
		model.setViewName(ViewNames.BOOKS);

		return model;
	}

	@GetMapping(value = "/books/book")
	public ModelAndView getBookDetails(@RequestParam("id") Long id) {
		ModelAndView model = new ModelAndView();
		model.addObject(ModelConstants.BOOK, bookService.findBookById(id));
		model.setViewName(ViewNames.BOOK);
		return model;
	}

	@GetMapping(value = "/books/delete/{id}")
	public String deleteBook(@PathVariable("id") Long id) {
		bookService.deleteBook(id);
		return ViewNames.DELETE;
	}

	@GetMapping(value = "/books/add")
	public String book(Model model) {
		model.addAttribute(ModelConstants.NEW_BOOK, new BookTo());
		return ViewNames.ADD_BOOK;
	}

	@PostMapping(value = "/books/add")
	public ModelAndView saveBook(@ModelAttribute("newBook") BookTo newBook) {
		String author = newBook.getAuthors();
		String title = newBook.getTitle();
		BookStatus bookStatus = newBook.getStatus();
		if (author.isEmpty() || title.isEmpty() || bookStatus == null) {
			return getModelView(NOT_ADDED_BOOK);
		}
		bookService.saveBook(newBook);
		return getModelView(ADDED_BOOK);
	}

	private ModelAndView getModelView(String msg) {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("message", msg);
		modelAndView.setViewName(ViewNames.ADD_BOOK_MSG);
		return modelAndView;
	}
}
