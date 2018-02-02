package japrc2016test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import japrc2016.StudyBlockInterface;
import japrc2016.StudyPlanner;
import japrc2016.StudyPlannerException;
import japrc2016.StudyPlannerGUIInterface;
import japrc2016.StudyPlannerInterface;
import japrc2016.TopicInterface;

public class StudyPlannerBasicTest
{
    private static final class MockPlannerGUI implements StudyPlannerGUIInterface
    {
        public int changeCount;
        
        @Override
        public void notifyModelHasChanged()
        {
            changeCount++;
        }
    }
    
    private StudyPlannerInterface planner;
    
    @Before
    public void setUp() throws Exception
    {
        planner = new StudyPlanner();
    }

    @Test
    public final void testAddTopic()
    {
        planner.addTopic("Java file handling", 480);
    }
    
    @Test
    public final void getTopics()
    {
        planner.addTopic("Java file handling", 480);
        Collection<TopicInterface> topics = planner.getTopics();
        assertEquals(1, topics.size());
    }
    
    @Test
    public final void testGetStudyPlan()
    {
        //minimal test to check this functionality exists
        //behaviour of this method will change a lot as levels progress, but this minimal test should still pass
        //on your final version
        
        planner.addTopic("Java file handling", 480);
        planner.generateStudyPlan();
        List<StudyBlockInterface> events = planner.getStudyPlan();
        assertNotNull(events);
        assertTrue(events.size() != 0);
        assertEquals("Java file handling", events.get(0).getTopic());
        assertTrue(events.get(0).getDuration() > 0);
    }    
    
    @Test
    public void testNotifiesGUIOnNewPlan() {
        planner.addTopic("Java file handling", 480);
        
        MockPlannerGUI gui = new MockPlannerGUI();        
        planner.setGUI(gui);      
        assertEquals(0, gui.changeCount);
        
        planner.generateStudyPlan();
        assertEquals(1, gui.changeCount);
        
        planner.generateStudyPlan();
        assertEquals(2, gui.changeCount);        
    }

}
