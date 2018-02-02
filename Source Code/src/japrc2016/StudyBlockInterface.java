package japrc2016;

import java.util.Calendar;

public interface StudyBlockInterface
{

    /** Returns the name of the topic that the study block is for
     * 
     * @return
     */
    String getTopic();

    /** Returns the start time for the study block
     * 
     * @return
     */
    Calendar getStartTime();
    
    /** Returns the duration of the study block, in minutes
     * 
     * @return
     */
    int getDuration();


}
