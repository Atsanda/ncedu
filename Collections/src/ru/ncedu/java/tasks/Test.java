package ru.ncedu.java.tasks;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import ru.ncedu.java.tasks.DateCollections.Element;

public class Test {
	public static void main(String[] args) {
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.add(Calendar.DATE, 1);
		c.set(Calendar.HOUR, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		System.out.println(c.getTime());
		SortedMap<Integer, String> map = new TreeMap<Integer, String>();
		DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM);
		System.out.println(dateFormat.format(c.getTime()));
		map.put (1, "Mark");
		map.put (3, "Mark");
		map.put (2, "Tarryn");
		List<String> list = new ArrayList<String>(map.values());
		for (String s : list) {
		    //System.out.println(s);
		}
	}
}
