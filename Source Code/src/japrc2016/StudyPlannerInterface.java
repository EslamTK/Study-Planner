package japrc2016;


import java.io.InputStream;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.List;

public interface StudyPlannerInterface
{
    public enum CalendarEventType {  
        EXAM,  
        ESSAY,  
        OTHER  
    }  
    
    /**
     * Adds the specified topic to the set of topics that need to be studied 
     * 
     * @param name the name of the topic
     * @param duration the amount of study needed for this topic, in minutes
     */
    void addTopic(String name, int duration);

    /**
     * Deletes the named topic from the set of topics to be studied 
     */
    void deleteTopic(String topic);
    
    /**
     * Returns a list of all the topics that need to be studied
     * 
     */
    List<TopicInterface> getTopics();

    /**
     * Returns the current study plan, as a list of StudyBlockInterface objects, in ascending order of start time
     * (so e.g. the earliest study event in time is first in the list)
     *  
     * @return the current study plan as a list of StudyEventInterface objects
     */
    List<StudyBlockInterface> getStudyPlan();

    /**
     * Updates the internal study plan held by the planner, starting study from the current system time
     */
    void generateStudyPlan();

    /**
     * Updates the internal study plan held by the planner, starting study from the time specified by the
     * {@link Calendar} parameter
     */
    void generateStudyPlan(Calendar startStudy);
    
    /** 
     * Sets the GUI object that is to be notified when any part of the planner objects externally visible state changes
     * 
     */
    void setGUI(StudyPlannerGUIInterface gui);

    /** 
     * Sets the standard duration of a study block
     * 
     * @param size the standard duration in minutes
     */
    void setBlockSize(int size);

    /** 
     * Set the length of the standard break period that the planner should assign between each block of study
     * 
     * @param the break length in minutes
     */
    void setBreakLength(int i);

    /**
     * Sets the standard time to start studying each day
     * 
     * @param startTime Calendar object specifying the time of day to start studying. The date parts of the Calendar are ignored
     */
    void setDailyStartStudyTime(Calendar startTime);

    /**
     * Sets the standard time to stop studying each day
     * 
     * @param startTime Calendar object specifying the time of day to stop studying. The date parts of the Calendar are ignored
     */
    void setDailyEndStudyTime(Calendar endTime);
    
    /**
     * Gets the standard time of day to start studying
     * 
     * @return a Calendar object with the hours and minutes set to the current start time (other values in the Calendar
     * are undefined)
     */
    Calendar getDailyStartStudyTime();

    /**
     * Gets the standard time of day to stop studying
     * 
     * @return a Calendar object with the hours and minutes set to the current start time (other values in the Calendar
     * are undefined)
     */
    Calendar getDailyEndStudyTime();
        
    /**
     * Adds an event of type "other" to the calendar at a specific date and time
     * 
     * @param eventName the name of the event by which it can be referred to later
     * @param startTime the date and time at which the event starts
     * @param duration the duration of the event in minutes
     */
    void addCalendarEvent(String eventName, Calendar startTime, int duration);
    
    /**
     * Adds an event of a specified type to the calendar at a specific date and time 
     * 
     * @param eventName the name of the event by which it can be referred to later
     * @param startTime the date and time at which the event starts
     * @param duration the duration of the event in minutes
     * @param type the type of the event
     */    
    void addCalendarEvent(String eventName, Calendar startTime, int duration, StudyPlannerInterface.CalendarEventType type);
    
    /**
     * Returns a list of all the current events in the planner's calendar (excluding study blocks)
     * 
     * @return
     */
    List<CalendarEventInterface> getCalendarEvents();
    
    /** 
     * Tells the planner to write a representation of its state to the supplied stream
     * 
     * @param saveStream The stream to write the save data to
     */
    void saveData(OutputStream saveStream);
    
    /** 
     * Tells the simulation to restore its state to the one stored in the supplied stream, which will have been
     * previous written by saveData()
     * 
     * @param loadStream The stream to load the data from
     */
    void loadData(InputStream loadStream);
}
