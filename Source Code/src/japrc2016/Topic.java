package japrc2016;

public class Topic implements TopicInterface {

	private String subject;

	/**
	 * The duration of a topic is the cumulative amount of time that should be
	 * spent studying a topic. For example, a topic of duration 240 minutes
	 * would be completed by four study sessions of 60 minutes, eight of 30
	 * minutes, etc. These sessions could take place in a single day, or be
	 * completed over multiple days.
	 */
	private int duration;

	private CalendarEventInterface target;

	public Topic(String name, int duration) {
		this.subject = name;
		this.duration = duration;
	}

	@Override
	public String getSubject() {
		return subject;
	}

	@Override
	public int getDuration() {
		return duration;
	}

	/*
	 * A method to set the target event of studying this topic if the topic is
	 * valid to be a target set it if not throw an exp
	 */
	//Level2.6.a
	@Override
	public void setTargetEvent(CalendarEventInterface target) {
		if (!target.isValidTopicTarget()) {
			throw new StudyPlannerException("This Event Can't Set As A Target!");
		}
		this.target = target;
	}
    //Level2.6.b
	@Override
	public CalendarEventInterface getTargetEvent() {
		return target;
	}
}
