package japrc2016;

import java.util.Calendar;

public interface TopicInterface {

	/**
	 * Returns a string describing the topic of study
	 */
	String getSubject();

	/**
	 * Returns the required study duration for this topic, in minutes. The
	 * duration of a topic is the cumulative amount of time that should be spent
	 * studying a topic. For example, a topic of duration 240 minutes would be
	 * completed by four study sessions of 60 minutes, eight of 30 minutes, etc.
	 * These sessions could take place in a single day, or be completed over
	 * multiple days.
	 * 
	 */
	int getDuration();

	/**
	 * Assigns a given event to be the target for which a particular topic is
	 * being studied (e.g. the exam). If another event was previously set to be
	 * the target for this topic, the specified one replaces it.
	 * 
	 */
	void setTargetEvent(CalendarEventInterface target);

	/**
	 * Returns the calendar event that this topic targets
	 * 
	 */
	CalendarEventInterface getTargetEvent();

}
