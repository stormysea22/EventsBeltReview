package com.theismann.eventsBeltReview.controllers;

import java.util.List;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.theismann.eventsBeltReview.models.Event;
import com.theismann.eventsBeltReview.models.User;
import com.theismann.eventsBeltReview.services.AppService;
import com.theismann.eventsBeltReview.validator.UserValidator;

@Controller
public class HomeController {
	
	private final UserValidator userValidator;
	private final AppService appService;
	
	private final String[] states = { "AK", "AL", "AR", "AZ", "CA", "CO", "CT", "DE", "FL", "GA", "HI", "IA", "ID",
	        "IL", "IN", "KS", "KY", "LA", "MA", "MD", "ME", "MI", "MN", "MO", "MS", "MT", "NC", "ND", "NE", "NH", "NJ",
	        "NM", "NV", "NY", "OH", "OK", "OR", "PA", "RI", "SC", "SD", "TN", "TX", "UT", "VA", "VT", "WA", "WI", "WV",
	        "WY" };
	
	public HomeController(AppService appService, UserValidator userValidator) {
		this.appService = appService;
		this.userValidator = userValidator;
	}
	
	//**********************
	//  Events dashboard
	//**********************
	
	@RequestMapping("/events")
	public String dashboard(Model model, HttpSession session, @ModelAttribute("event") Event event) {
		Long id = (Long)session.getAttribute("userid");
		User loggedinuser = this.appService.findUserById(id);
		
		
		model.addAttribute("loggedinuser", loggedinuser);
		
		String state = loggedinuser.getState();
		List<Event> eventsIn = appService.eventsInState(state);
		model.addAttribute("eventsIn", eventsIn);
		List<Event> eventsOut = appService.eventsOutOfState(state);
		model.addAttribute("eventsOut", eventsOut);
		model.addAttribute("states", states);
		
		return "dashboard.jsp";
	}
	
	//**********************
	//  Events create--post
	//**********************
	
	@PostMapping(value="/events/create")
	public String createEvent(@Valid @ModelAttribute("event") Event event, BindingResult result, HttpSession session, Model model) {
		if(result.hasErrors()) {
			
			return "dashboard.jsp";
		}
		
		Long id = (Long) session.getAttribute("userid");
		User loggedinuser = this.appService.findUserById(id);
		
		model.addAttribute("loggedinuser", loggedinuser);
		
		String state = loggedinuser.getState();
		List<Event> eventsIn = appService.eventsInState(state);
		model.addAttribute("eventsIn", eventsIn);
		List<Event> eventsOut = appService.eventsOutOfState(state);
		model.addAttribute("eventsOut", eventsOut);
		model.addAttribute("states", states);
		
		this.appService.createEvent(event);
		return "redirect:/events";
	}
	
	//**********************************
	// adding and removing attendees to/from events
	//**********************************

	@RequestMapping(value="/events/{event_id}/join")
	public String addAttendee(@PathVariable("event_id") Long event_id, HttpSession session) {
		User attendee = appService.findUserById((Long)session.getAttribute("userid"));
		Event attending_event = appService.findEventById(event_id);
		List<User> attendees = attending_event.getAttendees();
		attendees.add(attendee);
		attending_event.setAttendees(attendees);
		appService.updateUser(attendee);
		return "redirect:/events";
	}
	
	@RequestMapping(value="/events/{event_id}/remove")
	public String removeAttendee(@PathVariable("event_id") Long event_id, HttpSession session) {
		
		User attendee = appService.findUserById((Long)session.getAttribute("userid"));
    	Event attending_event = appService.findEventById(event_id);
		List<User> attendees = attending_event.getAttendees();
		attendees.remove(attendee);
		attending_event.setAttendees(attendees);
		appService.updateUser(attendee);
		return "redirect:/events";
	}
	
	//**********************************
	// Delete and update Event
	//**********************************
	
	@RequestMapping(value="/events/{event_id}/delete")
	public String deleteEvent(@PathVariable("event_id")Long event_id) {
		this.appService.findEventById(event_id);
		this.appService.deleteEvent(event_id);
		return "redirect:/events";
	}
	
	@RequestMapping(value="/events/{event_id}/edit")
	public String editEvent(@PathVariable("event_id") Long event_id, Model model) {
		Event event = appService.findEventById(event_id);
		model.addAttribute("event", event);
		model.addAttribute("states", states);
		return"edit.jsp";
	}
	
	@PostMapping(value="/events/{id}/update")
	public String updateEvent(@Valid @ModelAttribute("event") Event event, BindingResult result, @PathVariable("id") Long event_id, Model model) {
		if(result.hasErrors()) {
			model.addAttribute("states", states);
			return "edit.jsp";
		} else {
			this.appService.updateEvent(event);
			return "redirect:/events/" +event.getId();
		}
	}
	
	//**********************************
	// Show Event
	//**********************************
	
	@RequestMapping(value="/events/{event_id}")
	public String showEvent(@PathVariable("event_id") Long event_id, Model model) {
		Event event = appService.findEventById(event_id);
		model.addAttribute("event", event);
		return "show.jsp";
	}
	
	
	//**********************************
	// Login and Reg
	//**********************************
		
	@RequestMapping("/")
	public String index(@ModelAttribute("user") User user) {
		return "login.jsp";
	}
	
	@PostMapping("/registration")
	public String register(@Valid @ModelAttribute("user") User user, BindingResult result, HttpSession session) {
		
		userValidator.validate(user, result);
		if(result.hasErrors() ) {
			return "login.jsp";
		}
		
		//add validation for duplicate emails
		
		User u = this.appService.registerUser(user);
		session.setAttribute("userid", u.getId());
		
		return "redirect:/events";
	}
	
	
	@RequestMapping("/logout")
	public String logout(HttpSession session) {
		session.invalidate();
		return "redirect:/";
	}
	
	@RequestMapping("/login")
	public String login(@RequestParam("email") String email, @RequestParam("password") String password, HttpSession session, RedirectAttributes redirectAttributes) {
		Boolean isLegit = this.appService.authenticateUser(email, password);
		
		if(isLegit) {
			
			User user = this.appService.findByEmail(email);
			session.setAttribute("userid", user.getId());
			return "redirect:/events";
		}
		redirectAttributes.addFlashAttribute("error", "Invalid login Attempt");
		return "redirect:/";
	}
	
	
}
