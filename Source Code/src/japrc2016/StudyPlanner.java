package japrc2016;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class StudyPlanner implements StudyPlannerInterface {
	private ArrayList<TopicInterface> topics = new ArrayList<TopicInterface>();
	private ArrayList<StudyBlockInterface> plan = new ArrayList<StudyBlockInterface>();
	private ArrayList<CalendarEventInterface> calendarEvents = new ArrayList<CalendarEventInterface>();;

	private StudyPlannerGUIInterface gui;
	//Level1.6.a
	private int blockLength = 60, breakLength = 0;
	//Level2.3
	private int minBlockLength = 10;
	private Calendar startTime;
	private Calendar endTime;
	private Calendar tempStartStudy;

	/*
	 * A constructor for set the start time of the study to 9 am and the end
	 * time to 5 pm
	 */
	public StudyPlanner() {
		//Level2.1.a
		startTime = Calendar.getInstance();
		endTime = Calendar.getInstance();

		startTime.set(startTime.get(Calendar.YEAR), startTime.get(Calendar.MONTH), startTime.get(Calendar.DAY_OF_MONTH),
				9, 0, 0);
		startTime.set(Calendar.MILLISECOND, 0);

		endTime.set(endTime.get(Calendar.YEAR), endTime.get(Calendar.MONTH), endTime.get(Calendar.DAY_OF_MONTH), 17, 0,
				0);
		endTime.set(Calendar.MILLISECOND, 0);
	}

	/*
	 * Method to add a new topic to the Topics list by Looping through the
	 * topics list if there a topic with the same name throw an exception else
	 * add the new topic to the list
	 */
	@Override
	public void addTopic(String name, int duration) {
		//
		for (TopicInterface t : topics) {
			if (t.getSubject().equals(name)) {
				//Level1.2
				throw new StudyPlannerException("This Topic Already Exist In The List!");
			}
		}
		topics.add(new Topic(name, duration));
		if (!plan.isEmpty())
			generateStudyPlan(tempStartStudy);
		else {
			//Level3.1.c
			if (gui != null)
				gui.notifyModelHasChanged();
		}
	}

	@Override
	public List<TopicInterface> getTopics() {
		return topics;
	}

	@Override
	public List<StudyBlockInterface> getStudyPlan() {
		return plan;
	}

	public void setGUI(StudyPlannerGUIInterface gui) {
		this.gui = gui;
	}

	/*
	 * Method to delete a topic from the topics list by Looping through the
	 * topics list until find the require topic then remove it from the list and
	 * stop looping
	 */
	//Level1.3
	@Override
	public void deleteTopic(String topic) {
		for (TopicInterface t : topics) {
			if (t.getSubject().equals(topic)) {
				topics.remove(t);
				break;
			}
		}
		if (!plan.isEmpty() && !topics.isEmpty())
			generateStudyPlan(tempStartStudy);
		else 
		{
			if(!plan.isEmpty())
			{
				plan = new ArrayList<>();
			}
			
			if (gui != null)
			{
				//Level3.1.c
				gui.notifyModelHasChanged();
			}
		}
	}

	/*
	 * Method to generate The Study plan if no starting time is provided start
	 * the plan from the current start day time
	 */
	@Override
	public void generateStudyPlan() {
		generateStudyPlan(startTime);
	}

	/*
	 * Method to generate The Study plan if the topics list is empty throw an
	 * exp else generate the plan by study one block of each topic in the order
	 * it inserted to topics list and repeat until finish it and make sure the
	 * time to study is not overlapping with the end of the day or eny event
	 * otherwise change the time to study or the length of studying if the time
	 * between the study time and the event or the end of the day is above or
	 * equal the minimal block study length
	 */
	//Level1.4
	@SuppressWarnings("static-access")
	@Override
	public void generateStudyPlan(Calendar startStudy) {
		if (topics.isEmpty()) {
			//Leve1.1
			throw new StudyPlannerException("Topics List Is Empty!");
		}
		startStudy = (Calendar) HelperUtility.roundCalendarToMinutes(startStudy).clone();
		tempStartStudy = (Calendar) startStudy.clone();
		Calendar startDayOfStudy = (Calendar) startStudy.clone();
		Calendar endDayOfStudy = (Calendar) endTime.clone();
		int[] topicsDurations = HelperUtility.getTopicsDurations(topics);
		plan = new ArrayList<StudyBlockInterface>();
		boolean noMoreTopics = false;

		while (!noMoreTopics) {
			noMoreTopics = true;
			for (int j = 0; j < topics.size(); j++) {
				if (topicsDurations[j] > 0) {
					//Level1.7
					int studyTimeLength = Math.min(blockLength, topicsDurations[j]);
					int breakTimeLength = breakLength;
                    
					//Level1.5
					studyTimeLength = getTimeToStudyAndLength(startStudy, studyTimeLength, startDayOfStudy,
							endDayOfStudy);

					topicsDurations[j] -= studyTimeLength;
					plan.add(
							new StudyBlock(topics.get(j).getSubject(), (Calendar) startStudy.clone(), studyTimeLength));
					startStudy.add(startStudy.MINUTE, studyTimeLength);
                    //Level1.6.b && Level1.6.c
					breakTimeLength = getTimeToBreakAndLength(startStudy, breakTimeLength, endDayOfStudy);

					if (breakTimeLength > 0) {
						plan.add(new StudyBlock("Break", (Calendar) startStudy.clone(), breakTimeLength));
						startStudy.add(startStudy.MINUTE, breakTimeLength);
					}

					noMoreTopics = false;
				}
			}
		}

		if (gui != null) {
			gui.notifyModelHasChanged();
		}
	}

	/*
	 * Method to get the best time to study and length that not overlapping with
	 * the end of the day or any event
	 */
	private int getTimeToStudyAndLength(Calendar startStudy, int studyTimeLength, Calendar startDayOfStudy,
			Calendar endDayOfStudy) {
		//Level2.3.c
		studyTimeLength = Math.max(studyTimeLength, minBlockLength);

		studyTimeLength = getTimeNotOverLapWithDayEndAndLength(startStudy, studyTimeLength, startDayOfStudy,
				endDayOfStudy);
        //Level2.4.b
		for (CalendarEventInterface calendarEvent : calendarEvents) {
			if (HelperUtility.isCalendarsOverlap(startStudy, studyTimeLength, calendarEvent.getStartTime(),
					calendarEvent.getDuration())) {
				if (calendarEvent.getStartTime().before(startStudy)) {
					startStudy.setTimeInMillis(HelperUtility.getTimeInMillisAfterDuration(calendarEvent.getStartTime(),
							calendarEvent.getDuration()));
				} else {
					int availableTime = HelperUtility.getDurationBetweenCalendars(startStudy,
							calendarEvent.getStartTime());
					if (availableTime < minBlockLength) {
						startStudy.setTimeInMillis(HelperUtility.getTimeInMillisAfterDuration(
								calendarEvent.getStartTime(), calendarEvent.getDuration()));
					} else
						studyTimeLength = availableTime;
				}
				studyTimeLength = getTimeNotOverLapWithDayEndAndLength(startStudy, studyTimeLength, startDayOfStudy,
						endDayOfStudy);
			} else if (calendarEvent.getStartTime().after(startStudy))
				break;
		}
		return studyTimeLength;
	}

	/*
	 * Method to check if the study time is overlapping with the end of the day
	 * and change the study time to the next day if the available time is below
	 * the minimal
	 */
	//Level2.2
	private int getTimeNotOverLapWithDayEndAndLength(Calendar startStudy, int studyTimeLength, Calendar startDayOfStudy,
			Calendar endDayOfStudy) {
		if (HelperUtility.isCalendarsOverlap(startStudy, studyTimeLength, endDayOfStudy, 0)
				|| startStudy.after(endDayOfStudy)) {
			int availableTime = HelperUtility.getDurationBetweenCalendars(startStudy, endDayOfStudy);
			//Level2.3.b
			if (availableTime < minBlockLength) {
				startDayOfStudy.add(Calendar.DAY_OF_MONTH, 1);
				endDayOfStudy.add(Calendar.DAY_OF_MONTH, 1);
				startStudy.setTime(startDayOfStudy.getTime());
			} else
				studyTimeLength = availableTime;
		}
		return studyTimeLength;
	}

	/*
	 * Method to check if the break time is overlapping with the end of the day
	 * or any event and calculate the available time between them
	 */
	private int getTimeToBreakAndLength(Calendar startStudy, int breakTimeLength, Calendar endDayOfStudy) {
		//Level2.5.a
		if (HelperUtility.isCalendarsOverlap(startStudy, breakTimeLength, endDayOfStudy, 0)) {
			breakTimeLength = HelperUtility.getDurationBetweenCalendars(startStudy, endDayOfStudy);
		}
        
		//Level2.5.b
		for (CalendarEventInterface calendarEvent : calendarEvents) {
			if (HelperUtility.isCalendarsOverlap(startStudy, breakTimeLength, calendarEvent.getStartTime(),
					calendarEvent.getDuration())) {
				int availableTime = HelperUtility.getDurationBetweenCalendars(startStudy, calendarEvent.getStartTime());
				breakTimeLength = Math.min(breakTimeLength, availableTime);
			} else if (calendarEvent.getStartTime().after(startStudy))
				break;
		}
		return breakTimeLength;
	}

	/*
	 * Method to set the block size of time to study if below the minimal throw
	 * an exp
	 */
	@Override
	public void setBlockSize(int size) {
		if (size < minBlockLength) {
			//Level2.3.a
			throw new StudyPlannerException("Study Time Can't Be Less Than " + minBlockLength + " Min!");
		}
		this.blockLength = size;
	}

	public int getBlockSize() {
		return blockLength;
	}

	@Override
	public void setBreakLength(int i) {
		this.breakLength = i;
	}

	/*
	 * Method to set the start of the day time to study
	 */
	//Level2.1.b
	@Override
	public void setDailyStartStudyTime(Calendar startTime) {
		startTime = HelperUtility.roundCalendarToMinutes(startTime);
		
		if (isCorrectTime(startTime, this.endTime)) this.startTime = startTime;
	}

	/*
	 * Method to set the end of the day time to study
	 */
	//Level2.1.b
	@Override
	public void setDailyEndStudyTime(Calendar endTime) {
		endTime = HelperUtility.roundCalendarToMinutes(endTime);
		
		if (isCorrectTime(this.startTime, endTime)) this.endTime = endTime;
	}

	/*
	 * Method to check if the start time to study is before the end time and the
	 * time between the start and the end is >= minimal time to study
	 */
	//Level2.1.c
	private boolean isCorrectTime(Calendar start, Calendar end) {
		if (start.compareTo(end) == 1) {
			throw new StudyPlannerException("Start Study Time Can't Be After The End Time!");
		} else if (HelperUtility.getDurationBetweenCalendars(start, end) < blockLength) {
			throw new StudyPlannerException(
					"The Study Time Must Be More Than " + "Or At Least Equal One Block Of Study");
		}
		return true;
	}
    
	public void setDailyStartAndEndStudyTime(Calendar startTime,Calendar endTime) {
		if (isCorrectTime(startTime, endTime))
		{
			this.startTime = startTime;
			this.endTime = endTime;
		}
	}
	
	@Override
	public Calendar getDailyStartStudyTime() {
		return startTime;
	}

	@Override
	public Calendar getDailyEndStudyTime() {
		return endTime;
	}

	/*
	 * Method to add a calendar event to the list if no type is provided the
	 * default is "Other"
	 */
	@Override
	public void addCalendarEvent(String eventName, Calendar startTime, int duration) {
		addCalendarEvent(eventName, startTime, duration, CalendarEventType.OTHER);
	}

	/*
	 * Method for add a new event to the events list by check if no event is
	 * overlapping with the new one by looping through the list and check for
	 * every event is not overlapping or throw an exp and stop if found
	 * overlapping
	 */
	//Level2.4.a
	@Override
	public void addCalendarEvent(String eventName, Calendar startTime, int duration, CalendarEventType type) {
		startTime = HelperUtility.roundCalendarToMinutes(startTime);
		for (int i = 0; i <= calendarEvents.size(); ++i) {
			if (i < calendarEvents.size()) {
				if (HelperUtility.isCalendarsOverlap(calendarEvents.get(i).getStartTime(),
						calendarEvents.get(i).getDuration(), startTime, duration)) {
					//Level2.4.c
					throw new StudyPlannerException("This Event Is Overlaping With Another Event!");
				} else if (startTime.after(calendarEvents.get(i))) {
					calendarEvents.add(i, new CalendarEvent(eventName, startTime, duration, type));
					break;
				}
			} else if (i == calendarEvents.size()) {
				calendarEvents.add(new CalendarEvent(eventName, startTime, duration, type));
				break;
			}
		}
		if (!plan.isEmpty())
			generateStudyPlan(tempStartStudy);
		else {
			if (gui != null)
				//Level3.3
				gui.notifyModelHasChanged();
		}
	}

	@Override
	public List<CalendarEventInterface> getCalendarEvents() {
		return calendarEvents;
	}

	/*
	 * Method for saving the planner data into a OutputStream object as a
	 * specific format
	 */
	//Level2.7
	@Override
	public void saveData(OutputStream saveStream) {
		DataOutputStream out = new DataOutputStream(saveStream);
		try {
			out.writeInt(blockLength);
			out.writeInt(breakLength);
			out.writeLong(startTime.getTimeInMillis());
			out.writeLong(endTime.getTimeInMillis());
			out.writeInt(topics.size());
			for (TopicInterface topic : topics) {
				out.writeUTF(topic.getSubject());
				out.writeInt(topic.getDuration());
				if (topic.getTargetEvent() != null) {
					out.writeBoolean(true);
					saveEventToStream(topic.getTargetEvent(), out);
				} else {
					out.writeBoolean(false);
				}
			}
			out.writeInt(calendarEvents.size());
			for (CalendarEventInterface calendarEvent : calendarEvents) {
				saveEventToStream(calendarEvent, out);
			}
			out.writeInt(plan.size());
			for (StudyBlockInterface block : plan) {
				out.writeUTF(block.getTopic());
				out.writeLong(block.getStartTime().getTimeInMillis());
				out.writeInt(block.getDuration());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void saveEventToStream(CalendarEventInterface event, DataOutputStream out) {
		try {
			out.writeUTF(event.getName());
			out.writeLong(event.getStartTime().getTimeInMillis());
			out.writeInt(event.getDuration());
			out.writeUTF(((CalendarEvent) event).getType().toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/*
	 * Method for loading the planner data from a InputStream object as a
	 * specific format
	 */
	//Level2.7
	@Override
	public void loadData(InputStream loadStream) {
		DataInputStream input = new DataInputStream(loadStream);
		try {
			blockLength = input.readInt();
			breakLength = input.readInt();
			startTime.setTimeInMillis(input.readLong());
			endTime.setTimeInMillis(input.readLong());
			topics = new ArrayList<>();
			int size = input.readInt();
			while (size > 0) {
				String name = input.readUTF();
				int duration = input.readInt();
				Topic topic = new Topic(name, duration);
				boolean hasTarget = input.readBoolean();
				if (hasTarget) {
					topic.setTargetEvent(readEventFromStream(input));
				}
				topics.add(topic);
				--size;
			}
			calendarEvents = new ArrayList<>();
			size = input.readInt();
			while (size > 0) {
				calendarEvents.add(readEventFromStream(input));
				--size;
			}
			plan = new ArrayList<>();
			size = input.readInt();
			while (size > 0) {
				String subject = input.readUTF();
				Calendar startTime = Calendar.getInstance();
				startTime.setTimeInMillis(input.readLong());
				int duration = input.readInt();
				plan.add(new StudyBlock(subject, startTime, duration));
				--size;
			}
		} catch (IOException e) {
			throw new StudyPlannerException("Invalid Input Stream!");
		}

	}

	private CalendarEventInterface readEventFromStream(DataInputStream input) {
		CalendarEventInterface event = null;
		try {
			String name = input.readUTF();
			Calendar startTime = Calendar.getInstance();
			startTime.setTimeInMillis(input.readLong());
			int duration = input.readInt();
			CalendarEventType type = CalendarEventType.valueOf(input.readUTF());
			event = new CalendarEvent(name, startTime, duration, type);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return event;
	}

}
