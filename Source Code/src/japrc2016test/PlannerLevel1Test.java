package japrc2016test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import japrc2016.StudyBlockInterface;
import japrc2016.StudyPlanner;
import japrc2016.StudyPlannerInterface;

public class PlannerLevel1Test {
	private StudyPlannerInterface planner;

	@Before
	public void setUp() throws Exception {
		planner = new StudyPlanner();
	}

	@Test
	public final void testAddTopic() {
		planner.addTopic("Java file handling", 480);
		assertTrue(planner.getTopics().get(0).getSubject().equals("Java file handling"));

		planner.addTopic("Data Structure", 120);
		assertEquals(2, planner.getTopics().size());

		try {
			planner.addTopic("Java file handling", 480);
			fail("Method didn't throw Any Exception");
		} catch (Exception expectedException) {
			assertEquals("This Topic Already Exist In The List!", expectedException.getMessage());
			assertEquals(2, planner.getTopics().size());
		}
	}

	@Test
	public void testDeleteTopic() {
		planner.addTopic("Java file handling", 480);
		planner.addTopic("Data Structure", 120);

		assertTrue(planner.getTopics().get(0).getSubject().equals("Java file handling"));

		planner.deleteTopic("Java file handling");

		assertEquals(1, planner.getTopics().size());
		assertTrue(planner.getTopics().get(0).getSubject().equals("Data Structure"));
		
		planner.deleteTopic("Object Orinted Programming");
		assertEquals(1, planner.getTopics().size());
	}

	@Test
	public final void testGenerateStudyPlan() {
		try {
			planner.generateStudyPlan();
			fail("Method didn't throw Any Exception");
		} catch (Exception expectedException) {
			assertEquals(expectedException.getMessage(), "Topics List Is Empty!");
		}

		planner.addTopic("Java file handling", 480);
		planner.addTopic("Data Structure", 120);
		planner.generateStudyPlan();
		List<StudyBlockInterface> events = planner.getStudyPlan();
		assertNotNull(events);
		assertEquals((480 + 120) / ((StudyPlanner) planner).getBlockSize(), events.size());
		assertEquals(events.get(0).getTopic(), "Java file handling");
		assertEquals(events.get(1).getTopic(), "Data Structure");
		assertEquals(Math.min(((StudyPlanner) planner).getBlockSize(), 480), events.get(0).getDuration());
		assertEquals(Math.min(((StudyPlanner) planner).getBlockSize(), 120), events.get(1).getDuration());
	}
}
