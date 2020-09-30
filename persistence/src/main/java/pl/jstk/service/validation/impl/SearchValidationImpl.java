package pl.jstk.service.validation.impl;

import org.springframework.stereotype.Service;

import pl.jstk.service.validation.SearchValidation;

@Service
public class SearchValidationImpl implements SearchValidation {
	
	private int MIN_LENGHT = 3;

	@Override
	public boolean validate(String title, String author) {
		return title.length()>MIN_LENGHT || author.length()>MIN_LENGHT;
	}

}
