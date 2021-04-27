package com.theismann.eventsBeltReview.services;

import java.util.List;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;

import com.theismann.eventsBeltReview.models.Event;
import com.theismann.eventsBeltReview.models.User;
import com.theismann.eventsBeltReview.models.UserEvent;
import com.theismann.eventsBeltReview.repositories.EventRepository;
import com.theismann.eventsBeltReview.repositories.MessageRepository;
import com.theismann.eventsBeltReview.repositories.UserEventRepository;
import com.theismann.eventsBeltReview.repositories.UserRepository;

@Service
public class AppService {
	
	private final UserRepository userRepository;
	private final EventRepository eventRepository;
//	private final MessageRepository messageRepository;
	private final UserEventRepository userEventRepository;
    
    // dependency injection
    public AppService(UserRepository userRepository, MessageRepository messageRepository, EventRepository eventRepository, UserEventRepository userEventRepository) {
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
//        this.messageRepository = messageRepository;
        this.userEventRepository = userEventRepository;
    }
    
    
    
    // register user and hash their password
    public User registerUser(User user) {
        String hashed = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
        user.setPassword(hashed);
        user.setEmail(user.getEmail().toLowerCase());
        return userRepository.save(user);
    }
    
    // authenticate user
    public boolean authenticateUser(String email, String password) {
        // first find the user by email
        User user = userRepository.findByEmail(email);
        // if we can't find it by email, return false
        if(user == null) {
            return false;
        } else {
            // if the passwords match, return true, else, return false
            if(BCrypt.checkpw(password, user.getPassword())) {
                return true;
            } else {
                return false;
            }
        }
    }
    //************************
    // Find
    //************************
    
    public List<User> findAllUsers(){
    	return (List<User>)this.userRepository.findAll();
    }
    
    public User findUserById(Long id) {
    	
    	return userRepository.findById(id).orElse(null);
    }
    
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    
    public Event findEventById(Long event_id) {
    	return this.eventRepository.findEventById(event_id);
    }
    
    public List<Event> eventsInState(String state) {
    	return this.eventRepository.findByState(state);
    }
    
    public List<Event> eventsOutOfState(String state) {
    	return this.eventRepository.findByStateIsNot(state);
    }
    
  //************************
    // create, create, and delete
    //************************
    
    public Event createEvent(Event event) {
    	return eventRepository.save(event);
    }
    
    public UserEvent createRelationship(UserEvent userEvent) {
    	return this.userEventRepository.save(userEvent);
    }
    
    public void deleteEvent(Long event_id) {
    	this.eventRepository.deleteById(event_id);
    }
    
    public Event updateEvent(Event event) {
    	return this.eventRepository.save(event);
    }
    
    public void updateUser(User attendee) {
    	this.userRepository.save(attendee);
    }
    
 }
	
