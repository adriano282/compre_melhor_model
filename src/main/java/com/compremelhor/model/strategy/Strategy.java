package com.compremelhor.model.strategy;

import com.compremelhor.model.entity.EntityModel;

public interface Strategy<T extends EntityModel> {
	Status process(T t);
}
