package japrc2016test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;

import org.junit.Before;
import org.junit.Test;

import japrc2016.CalendarEvent;
import japrc2016.CalendarEventInterface;
import japrc2016.StudyBlockInterface;
import japrc2016.StudyPlanner;
import japrc2016.StudyPlannerInterface;
import japrc2016.StudyPlannerInterface.CalendarEventType;
import japrc2016.TopicInterface;

public class PlannerLevel2Test {

	private StudyPlannerInterface planner;

	@Before
	public void setUp() throws Exception {
		planner = new StudyPlanner();
	}

	@Test
	public void testAddCalendarEvent() {
		planner.addCalendarEvent("Football Time", Calendar.getInstance(), 10);
		assertEquals(1, planner.getCalendarEvents().size());
		assertTrue(!planner.getCalendarEvents().get(0).isValidTopicTarget());
		try {
			planner.addCalendarEvent("Music Time", Calendar.getInstance(), 5);
			fail("Method didn't throw Any Exception");
		} catch (Exception expectedException) {
			assertEquals("This Event Is Overlaping With Another Event!", expectedException.getMessage());
		}
		Calendar startTime = Calendar.getInstance();
		startTime.add(Calendar.MINUTE, 20);
		planner.addCalendarEvent("Exam", startTime, 5, CalendarEventType.EXAM);
		assertTrue(planner.getCalendarEvents().get(1).isValidTopicTarget());
	}

	@Test
	public void testSaveData() throws IOException {
		planner.addCalendarEvent("Football Time", Calendar.getInstance(), 10);
		Calendar startTime = Calendar.getInstance();
		startTime.add(Calendar.MINUTE, 20);
		planner.addCalendarEvent("Exam", startTime, 5, CalendarEventType.EXAM);
		planner.addTopic("Java file handling", 480);
		planner.addTopic("Data Structure", 120);
		planner.generateStudyPlan();
		File file = new File("temp.txt");
		if (file.exists()) {
			file.delete();
			file.createNewFile();
		}
		OutputStream out = new FileOutputStream(file);
		planner.saveData(out);
		InputStream input = new FileInputStream(file);
		DataInputStream dataInput = new DataInputStream(input);
		assertEquals(60, dataInput.readInt());
		assertEquals(0, dataInput.readInt());
		assertEquals(planner.getDailyStartStudyTime().getTimeInMillis(), dataInput.readLong());
		assertEquals(planner.getDailyEndStudyTime().getTimeInMillis(), dataInput.readLong());
		assertEquals(planner.getTopics().size(), dataInput.readInt());
		ArrayList<TopicInterface> topics = new ArrayList<>();
		for (int i = 0; i < planner.getTopics().size(); ++i) {
			assertEquals(planner.getTopics().get(i).getSubject(), dataInput.readUTF());
			assertEquals(planner.getTopics().get(i).getDuration(), dataInput.readInt());
			assertTrue(!dataInput.readBoolean());
		}
		ArrayList<CalendarEventInterface> calendarEvents = new ArrayList<>();
		assertEquals(planner.getCalendarEvents().size(), dataInput.readInt());
		for (int i = 0; i < planner.getCalendarEvents().size(); ++i) {
			assertEquals(planner.getCalendarEvents().get(i).getName(), dataInput.readUTF());
			assertEquals(planner.getCalendarEvents().get(i).getStartTime().getTimeInMillis(), dataInput.readLong());
			assertEquals(planner.getCalendarEvents().get(i).getDuration(), dataInput.readInt());
			assertEquals(((CalendarEvent) planner.getCalendarEvents().get(i)).getType(),
					CalendarEventType.valueOf(dataInput.readUTF()));
		}
		ArrayList<StudyBlockInterface> plan = new ArrayList<>();
		assertEquals(planner.getStudyPlan().size(), dataInput.readInt());
		for (int i = 0; i < planner.getStudyPlan().size(); ++i) {
			assertEquals(planner.getStudyPlan().get(i).getTopic(), dataInput.readUTF());
			assertEquals(planner.getStudyPlan().get(i).getStartTime().getTimeInMillis(), dataInput.readLong());
			assertEquals(planner.getStudyPlan().get(i).getDuration(), dataInput.readInt());
		}
		file.delete();
	}

	@Test
	public void testLoadData() throws IOException {
		planner.addCalendarEvent("Football Time", Calendar.getInstance(), 10);
		Calendar startTime = Calendar.getInstance();
		startTime.add(Calendar.MINUTE, 20);
		planner.addCalendarEvent("Exam", startTime, 5, CalendarEventType.EXAM);
		planner.addTopic("Java file handling", 480);
		planner.addTopic("Data Structure", 120);
		planner.generateStudyPlan();
		File file = new File("temp.txt");
		if (file.exists()) {
			file.delete();
			file.createNewFile();
		}
		OutputStream out = new FileOutputStream(file);
		Calendar start = planner.getDailyStartStudyTime(), end = planner.getDailyEndStudyTime();
		ArrayList<TopicInterface> topics = (ArrayList<TopicInterface>) planner.getTopics();
		ArrayList<CalendarEventInterface> calendarEvents = (ArrayList<CalendarEventInterface>) planner
				.getCalendarEvents();
		ArrayList<StudyBlockInterface> plan = (ArrayList<StudyBlockInterface>) planner.getStudyPlan();
		planner.saveData(out);
		InputStream input = new FileInputStream(file);
		planner.loadData(input);
		assertEquals(start.getTimeInMillis(), planner.getDailyStartStudyTime().getTimeInMillis());
		assertEquals(end.getTimeInMillis(), planner.getDailyEndStudyTime().getTimeInMillis());
		assertEquals(topics.size(), planner.getTopics().size());
		for (int i = 0; i < topics.size(); ++i) {
			assertEquals(topics.get(i).getSubject(), planner.getTopics().get(i).getSubject());
			assertEquals(topics.get(i).getDuration(), planner.getTopics().get(i).getDuration());
		}
		assertEquals(calendarEvents.size(), planner.getCalendarEvents().size());
		for (int i = 0; i < calendarEvents.size(); ++i) {
			assertEquals(calendarEvents.get(i).getName(), planner.getCalendarEvents().get(i).getName());
			assertEquals(calendarEvents.get(i).getDuration(), planner.getCalendarEvents().get(i).getDuration());
			assertEquals(calendarEvents.get(i).getStartTime().getTimeInMillis(),
					planner.getCalendarEvents().get(i).getStartTime().getTimeInMillis());
		}
		assertEquals(plan.size(), planner.getStudyPlan().size());
		for (int i = 0; i < plan.size(); ++i) {
			assertEquals(plan.get(i).getTopic(), planner.getStudyPlan().get(i).getTopic());
			assertEquals(plan.get(i).getDuration(), planner.getStudyPlan().get(i).getDuration());
			assertEquals(plan.get(i).getStartTime().getTimeInMillis(),
					planner.getStudyPlan().get(i).getStartTime().getTimeInMillis());
		}
		file.delete();
	}
}
