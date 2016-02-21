package com.compremelhor.model.service;

import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;

import com.compremelhor.model.dao.UserDao;
import com.compremelhor.model.entity.User;

@Stateless
public class UserService {
	@Inject
	private Logger logger;
	
	@Inject
	private UserDao userDao;
	
	public void createUser(User user) {
		userDao.persist(user);
		logger.info("User persisted: " + user);
	}
	
	public User editUser(User user) {
		User u = userDao.edit(user);
		logger.info("User altered: " + u);
		return u;
	}
	
	public void removeUser(User user) {
		userDao.remove(user);
		logger.info("User removed: " + user);
	}
	
	public User findUser(int id) {
		return userDao.find(id);
	}
	
	public User findUserByDocument(String document) {
		return userDao.findUserByDocument(document);
	}
}
