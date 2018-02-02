package japrc2016test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Calendar;

import org.junit.Before;
import org.junit.Test;

import japrc2016.StudyPlanner;
import japrc2016.StudyPlannerInterface;
import japrc2016.StudyPlannerInterface.CalendarEventType;

public class PlannerLevel3Test {
	private StudyPlannerInterface planner;

	@Before
	public void setUp() throws Exception {
		planner = new StudyPlanner();
	}

	@Test
	public void testGenerateStudyPlanWithBreaks() {
		Calendar startTime = Calendar.getInstance();
		startTime.set(startTime.get(Calendar.YEAR), startTime.get(Calendar.MONTH), startTime.get(Calendar.DAY_OF_MONTH),
				9, 0, 0);
		startTime.set(Calendar.MILLISECOND, 0);

		planner.addTopic("Java file handling", 480);
		planner.addTopic("Data Structure", 120);

		planner.setBreakLength(10);

		planner.generateStudyPlan();

		assertTrue(planner.getStudyPlan().get(0).getTopic().equals("Java file handling"));
		assertEquals(60, planner.getStudyPlan().get(0).getDuration());
		assertEquals(startTime.getTimeInMillis(), planner.getStudyPlan().get(0).getStartTime().getTimeInMillis());

		startTime.add(Calendar.MINUTE, 60);
		assertTrue(planner.getStudyPlan().get(1).getTopic().equals("Break"));
		assertEquals(10, planner.getStudyPlan().get(1).getDuration());
		assertEquals(startTime.getTimeInMillis(), planner.getStudyPlan().get(1).getStartTime().getTimeInMillis());

		startTime.add(Calendar.MINUTE, 10);
		assertTrue(planner.getStudyPlan().get(2).getTopic().equals("Data Structure"));
		assertEquals(60, planner.getStudyPlan().get(2).getDuration());
		assertEquals(startTime.getTimeInMillis(), planner.getStudyPlan().get(2).getStartTime().getTimeInMillis());
	}

	@Test
	public void testGenerateStudyPlanWithEvents() {
		Calendar startTime = Calendar.getInstance();
		startTime.set(startTime.get(Calendar.YEAR), startTime.get(Calendar.MONTH), startTime.get(Calendar.DAY_OF_MONTH),
				9, 0, 0);
		startTime.set(Calendar.MILLISECOND, 0);

		planner.addTopic("Java file handling", 480);
		planner.addTopic("Data Structure", 120);

		planner.addCalendarEvent("Football Time", (Calendar) startTime.clone(), 10);

		startTime.add(Calendar.MINUTE, 30);
		planner.addCalendarEvent("Exam", (Calendar) startTime.clone(), 5, CalendarEventType.EXAM);

		planner.generateStudyPlan();

		startTime.add(Calendar.MINUTE, -20);
		assertTrue(planner.getStudyPlan().get(0).getTopic().equals("Java file handling"));
		assertEquals(20, planner.getStudyPlan().get(0).getDuration());
		assertEquals(startTime.getTimeInMillis(), planner.getStudyPlan().get(0).getStartTime().getTimeInMillis());

		startTime.add(Calendar.MINUTE, 25);
		assertTrue(planner.getStudyPlan().get(1).getTopic().equals("Data Structure"));
		assertEquals(60, planner.getStudyPlan().get(1).getDuration());
		assertEquals(startTime.getTimeInMillis(), planner.getStudyPlan().get(1).getStartTime().getTimeInMillis());
	}

	@Test
	public void testSetTargetEventForTopic() {
		planner.addTopic("Java file handling", 480);

		planner.addCalendarEvent("Football Time", Calendar.getInstance(), 10);
		Calendar startTime = Calendar.getInstance();
		startTime.add(Calendar.MINUTE, 30);
		planner.addCalendarEvent("Exam", startTime, 5, CalendarEventType.EXAM);

		try {
			planner.getTopics().get(0).setTargetEvent(planner.getCalendarEvents().get(0));
			fail("Method didn't throw Any Exception");
		} catch (Exception expectedException) {
			assertEquals("This Event Can't Set As A Target!", expectedException.getMessage());
		}
		planner.getTopics().get(0).setTargetEvent(planner.getCalendarEvents().get(1));
		assertEquals(planner.getCalendarEvents().get(1), planner.getTopics().get(0).getTargetEvent());
	}
}
