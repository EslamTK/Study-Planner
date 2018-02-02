package japrc2016;

import java.io.File;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import javax.swing.filechooser.FileFilter;

public class HelperUtility {
	/*
	 * A helper method to check if 2 calendars is overlapping
	 */
	public static boolean isCalendarsOverlap(Calendar firstStart, int firstDuration, Calendar secondStart,
			int secondDuration) {
		Calendar firstEnd = (Calendar) firstStart.clone();
		firstEnd.add(Calendar.MINUTE, firstDuration);
		Calendar secondEnd = (Calendar) secondStart.clone();
		secondEnd.add(Calendar.MINUTE, secondDuration);
		return firstStart.compareTo(secondEnd) < 1 && secondStart.compareTo(firstEnd) < 1;
	}

	/*
	 * A helper method to calculate and return the empty time between 2
	 * calendars
	 */
	public static int getDurationBetweenCalendars(Calendar start, Calendar end) {
		return Math.max(((int) (((long) (end.getTimeInMillis() - start.getTimeInMillis()) / (1000 * 60)))), 0);
	}

	/*
	 * A helper method to round the calendar to the nearest minutes
	 */
	//Level2.8
	public static Calendar roundCalendarToMinutes(Calendar time) {
		time.add(Calendar.MINUTE, ((Calendar.SECOND + (Calendar.MILLISECOND / 1000)) / 60));
		time.set(Calendar.SECOND, 0);
		time.set(Calendar.MILLISECOND, 0);
		return time;
	}

	/*
	 * A helper method to calculate and return the time after a period
	 */
	public static long getTimeInMillisAfterDuration(Calendar start, int duration) {
		return start.getTimeInMillis() + (duration * 60 * 1000);
	}

	/*
	 * A helper method to copy the durations of the topics and return a array of
	 * them
	 */
	public static int[] getTopicsDurations(ArrayList<TopicInterface> topics) {
		int[] Arr = new int[topics.size()];

		for (int i = 0; i < topics.size(); i++)
			Arr[i] = topics.get(i).getDuration();

		return Arr;
	}

	/*
	 * A helper method to create and return a file filter to filter files format
	 * to display txt files only in the gui
	 */
	public static FileFilter getFileFilter() {
		FileFilter fileFilter = new FileFilter() {

			@Override
			public String getDescription() {
				return "Text Files (*.txt)";
			}

			@Override
			public boolean accept(File file) {
				String fileName = file.getName();
				int exIndex = fileName.lastIndexOf('.');
				if (exIndex > -1) {
					if (fileName.substring(exIndex + 1).equals("txt"))
						return true;
				}
				return file.isDirectory();
			}
		};
		return fileFilter;
	}

	/*
	 * A helper method to format the Date and the Time and return a formated
	 * string of them
	 */
	public static String getFormatedTimeAndDate(int month, int day, int hour, int minute) {
		String[] months = new String[] { "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov",
				"Dec" };
		LocalTime localTime = LocalTime.of(hour, minute);
		String format = String.valueOf(day) + " " + months[month] + " " + localTime;
		return format;
	}
}
