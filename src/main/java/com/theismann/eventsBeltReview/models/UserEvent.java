package com.theismann.eventsBeltReview.models;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name="users_events")
public class UserEvent {
	
	 @Id
	 @GeneratedValue(strategy = GenerationType.IDENTITY)
	 private Long id;
	 
	 @Column(updatable=false)
	 @DateTimeFormat(pattern="yyyy-MM-dd")
	 private Date createdAt;
	 @DateTimeFormat(pattern="yyyy-MM-dd")
	 private Date updatedAt;
	 
	 @ManyToOne(fetch = FetchType.LAZY)
	 @JoinColumn(name="user_id")
	 private User attendee;
	    
	 @ManyToOne(fetch = FetchType.LAZY)
	 @JoinColumn(name="event_id")
	 private Event attending_event;	 
	
	 public UserEvent() {
		}

		public UserEvent(Long id, Date createdAt, Date updatedAt, User attendee, Event attending_event) {
			this.id = id;
			this.createdAt = createdAt;
			this.updatedAt = updatedAt;
			this.attendee = attendee;
			this.attending_event = attending_event;
		}

		public UserEvent(User attendee, Event attending_event) {
			this.attendee=attendee;
			this.attending_event=attending_event;
		}

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public Date getCreatedAt() {
			return createdAt;
		}

		public void setCreatedAt(Date createdAt) {
			this.createdAt = createdAt;
		}

		public Date getUpdatedAt() {
			return updatedAt;
		}

		public void setUpdatedAt(Date updatedAt) {
			this.updatedAt = updatedAt;
		}

		public User getAttendee() {
			return attendee;
		}

		public void setAttendee(User attendee) {
			this.attendee = attendee;
		}

		public Event getAttending_event() {
			return attending_event;
		}

		public void setAttending_event(Event attending_event) {
			this.attending_event = attending_event;
		}
		
	    
		@PrePersist
		protected void onCreate(){
			this.createdAt = new Date();
		}
		
		@PreUpdate
	    protected void onUpdate(){
	        this.updatedAt = new Date();
		}
}
