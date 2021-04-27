package com.theismann.eventsBeltReview.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.theismann.eventsBeltReview.models.UserEvent;

@Repository
public interface UserEventRepository extends CrudRepository<UserEvent, Long>{
	
	
}
