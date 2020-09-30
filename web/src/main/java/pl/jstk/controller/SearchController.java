package pl.jstk.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import pl.jstk.constants.ModelConstants;
import pl.jstk.constants.ViewNames;
import pl.jstk.service.BookService;
import pl.jstk.service.validation.SearchValidation;
import pl.jstk.to.BookTo;

@Controller
public class SearchController {

	@Autowired
	private BookService bookService;

	@Autowired
	private SearchValidation searchValidation;

	@GetMapping(value = "/books/search")
	public ModelAndView showSearch() {
		ModelAndView model = new ModelAndView();
		model.addObject(ModelConstants.SEARCH_BOOK, new BookTo());
		model.addObject(ModelConstants.EMPTY_RESULT, false);
		model.setViewName(ViewNames.SEARCHING);
		return model;
	}

	@PostMapping(value = "/books/search")
	public ModelAndView findBookByTitleAndAuthor(@ModelAttribute("newBook") BookTo newBook) {
		String author = newBook.getAuthors();
		String title = newBook.getTitle();
		ModelAndView model = new ModelAndView();
		model.addObject(ModelConstants.SEARCH_BOOK, newBook);
		if (searchValidation.validate(title, author)) {
			List<BookTo> result = bookService.findBooksByTitleAndAuthor(title, author);
			model.addObject(ModelConstants.ERROR, false);
			model.addObject(ModelConstants.BOOK_LIST, result);
			model.addObject(ModelConstants.EMPTY_RESULT, result.isEmpty());
		} else {
			model.addObject(ModelConstants.ERROR, true);
		}
		model.setViewName(ViewNames.SEARCHING);
		return model;
	}
}
