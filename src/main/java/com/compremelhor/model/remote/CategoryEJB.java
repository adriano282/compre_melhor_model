package com.compremelhor.model.remote;

import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.inject.Inject;

import com.compremelhor.model.entity.Category;
import com.compremelhor.model.service.CategoryService;

@Stateless
@Remote(EJBRemote.class)
public class CategoryEJB extends AbstractRemote<Category>{

	@Inject private CategoryService categoryService;
	
	@Override
	void setService() { super.service = this.categoryService;}

}
