package com.theismann.eventsBeltReview.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.theismann.eventsBeltReview.models.Message;

@Repository
public interface MessageRepository extends CrudRepository<Message, Long>{

}
