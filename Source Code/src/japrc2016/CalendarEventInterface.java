package japrc2016;

import java.util.Calendar;

public interface CalendarEventInterface
{
    /**
     *  Returns the name of the events
     * 
     */
    String getName();

    /**
     *  Returns the start time for the event
     * 
     */
    Calendar getStartTime();
    
    /**
     *  Returns the duration of the event
     * 
     */
    int getDuration();
    
    /**
     * Returns true if this event is a valid target for a study e.g. it represents an exam or an essay hand-in time.
     * 
     */
    boolean isValidTopicTarget();
}


