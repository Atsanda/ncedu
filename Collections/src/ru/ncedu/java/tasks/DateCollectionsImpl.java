package ru.ncedu.java.tasks;

import java.text.ParseException;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import ru.ncedu.java.tasks.DateCollections.Element;

public class DateCollectionsImpl implements DateCollections {
	private int dateFormatStyle = DateFormat.MEDIUM;
	SortedMap<String, Element> map = null;
	List<Element> list = null;
	@Override
	public void setDateStyle(int dateStyle) {
		dateFormatStyle = dateStyle;
	}

	@Override
	public Calendar toCalendar(String dateString) throws ParseException {
		DateFormat dateFormat = DateFormat.getDateInstance(dateFormatStyle);
		Date date = dateFormat.parse(dateString);
		Calendar calndr = Calendar.getInstance();
		calndr.setTime(date);
		return calndr;
	}

	@Override
	public String toString(Calendar date) {
		DateFormat dateFormat = DateFormat.getDateInstance(dateFormatStyle);
		return dateFormat.format(date.getTime());
	}

	@Override
	public void initMainMap(int elementsNumber, Calendar firstDate) {
		
		map = new TreeMap<String, Element>(new Comparator<String>(){
			
			private DateFormat dateFormat = DateFormat.getDateInstance(dateFormatStyle);
			@Override
			public int compare(String o1, String o2) {
				try {
					Calendar c1 = toCalendar(o1);
					Calendar c2 = toCalendar(o2);
					return c1.compareTo(c2);
				} catch (ParseException e) {
					return 0;
				}
			}			
		});
		Random rand = new Random();
		for(int i=0;i<elementsNumber; i++){
			Calendar dateToAdd = Calendar.getInstance();
			dateToAdd.setTime(firstDate.getTime());
			dateToAdd.add(Calendar.DAY_OF_MONTH, 110*i);
			map.put(toString(dateToAdd), new Element(dateToAdd, rand.nextInt(2000)));
		}
	}

	@Override
	public void setMainMap(Map<String, Element> map) {
		map = new TreeMap<String, Element>(new Comparator<String>(){
			
			private DateFormat dateFormat = DateFormat.getDateInstance(dateFormatStyle);
			@Override
			public int compare(String o1, String o2) {
				try {
					Calendar c1 = toCalendar(o1);
					Calendar c2 = toCalendar(o1);
					return c1.compareTo(c2);
				} catch (ParseException e) {}
				return 0;
			}			
		});
		
		map.putAll(map);
		
	}

	@Override
	public Map<String, Element> getMainMap() {
		return map;
	}

	@Override
	public SortedMap<String, Element> getSortedSubMap() {
		DateFormat dateFormat = DateFormat.getDateInstance(dateFormatStyle);
		Date cur = Calendar.getInstance().getTime();
		
		SortedMap<String, Element> ret = new TreeMap<String, Element>(new Comparator<String>(){
			
			@Override
			public int compare(String o1, String o2) {
				try {
					Calendar c1 = toCalendar(o1);
					Calendar c2 = toCalendar(o2);
					return c1.compareTo(c2);
				} catch (ParseException e) {
					return 0;
				}
			}			
		});	
		
		Set<String> keys = map.keySet();
		Element tmp;
		for(String key: keys){
			tmp = map.get(key);
			if(tmp.getBirthDate().getTime().compareTo(cur) > 0)
				ret.put(key, tmp);
		}
		return ret;
	}

	@Override
	public List<Element> getMainList() {
		List<Element> ret = new ArrayList<Element>(map.values());
		Collections.sort(ret, new Comparator<Element>(){
			
			@Override
			public int compare(Element o1, Element o2) {
				Calendar c1 = o1.getBirthDate();
				Calendar c2 = o2.getBirthDate();
				return c1.compareTo(c2);
				
			}			
		});
		return ret;
	}

	@Override
	public void sortList(List<Element> list) {
		Collections.sort(list, new Comparator<Element>(){
			
			private DateFormat dateFormat = DateFormat.getDateInstance(dateFormatStyle);
			@Override
			public int compare(Element o1, Element o2) {
				Calendar c1 = o1.getDeathDate();
				Calendar c2 = o2.getDeathDate();
				return c1.compareTo(c2);
				
			}			
		});
	}

	@Override
	public void removeFromList(List<Element> list) {
		Iterator<Element> listiterator = list.iterator();
		List<Element> toDel = new ArrayList<Element>();
		Element tmp;
		while(listiterator.hasNext()){
			tmp = listiterator.next();
			int month = tmp.getBirthDate().get(Calendar.MONTH);
			if( month == Calendar.DECEMBER ||
					month == Calendar.JANUARY ||
					month == Calendar.FEBRUARY){
				toDel.add(tmp);
			}
		}
		for(Element elem: toDel)
			list.remove(elem);
		
	}

}
