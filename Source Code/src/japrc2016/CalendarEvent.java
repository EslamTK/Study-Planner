package japrc2016;

import java.util.Calendar;

import japrc2016.StudyPlannerInterface.CalendarEventType;

public class CalendarEvent implements CalendarEventInterface {

	private String name;
	private Calendar startTime;
	private int duration;
	CalendarEventType type;

	public CalendarEvent(String name, Calendar startTime, int duration, CalendarEventType type) {
		this.name = name;
		this.startTime = startTime;
		this.duration = duration;
		this.type = type;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public Calendar getStartTime() {
		return startTime;
	}

	@Override
	public int getDuration() {
		return duration;
	}

	public CalendarEventType getType() {
		return type;
	}

	/*
	 * Method for check if the event is valid as a target for a topic or not the
	 * event can be target if it's type is Exam Or Essay
	 */
	@Override
	public boolean isValidTopicTarget() {
		if (type.equals(CalendarEventType.ESSAY) || type.equals(CalendarEventType.EXAM))
			return true;
		return false;
	}

}
