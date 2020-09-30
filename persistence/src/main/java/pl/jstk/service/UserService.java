package pl.jstk.service;

import java.util.List;

import pl.jstk.to.UserTo;

public interface UserService {

	List<UserTo> findUserByName(String name);
}
