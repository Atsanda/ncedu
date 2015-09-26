package ru.ncedu.java.tasks;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class StringFilterImpl implements StringFilter {
	Set<String> container = new HashSet<String>();
	
	@Override
	public void add(String s) {
		if(s != null){
			String tobeAdded = s.toLowerCase();
			container.add(tobeAdded);
		}else
			container.add(null);
	}

	@Override
	public boolean remove(String s) {
		if(s != null){
		String tobeRemoved = s.toLowerCase();
		return container.remove(tobeRemoved);
		}else
			return container.remove(null);
	}

	@Override
	public void removeAll() {
		container.removeAll(container);
	}

	@Override
	public Collection<String> getCollection() {
		return container;
	}
	
	private Iterator<String> getStringsContainingRgx(String rgx){
		if(rgx == null || rgx == "")
			return container.iterator();
		Iterator<String> iterator = container.iterator();
		Set<String> retcontainer = new HashSet<String>();
		String tmp;
		while(iterator.hasNext()){
			tmp = iterator.next();
			if(tmp != null && tmp.matches(rgx))
				retcontainer.add(tmp);
		}
		return retcontainer.iterator();
	}

	@Override
	public Iterator<String> getStringsContaining(String chars) {
		if(chars == null || chars == "")
			return container.iterator();
		
		return this.getStringsContainingRgx(".*?" + chars + ".*?");
	}

	@Override
	public Iterator<String> getStringsStartingWith(String begin) {
		if(begin == null || begin == "")
			return container.iterator();
		String str = begin.toLowerCase();
		return this.getStringsContainingRgx("^" + str + ".*?");
	}

	@Override
	public Iterator<String> getStringsByNumberFormat(String format) {
		if(format == null || format.length() == 0)
			return container.iterator();
		Iterator<String> iterator = container.iterator();
		Set<String> retcontainer = new HashSet<String>();
		String tmp;
		while(iterator.hasNext()){
			tmp = iterator.next();
			if(tmp == null)
				continue;
			if(tmp.length() != format.length())
				continue;
			
			int i;
			for(i=0; i < format.length(); i++){
				if(format.charAt(i) != '#' && format.charAt(i) == tmp.charAt(i))
					continue;
				if(format.charAt(i) == '#' && Character.isDigit(tmp.charAt(i)))
					continue;
				break;
			}
			if(i == format.length()){
				retcontainer.add(tmp);
			}
		}
	return retcontainer.iterator();
	}

	@Override
	public Iterator<String> getStringsByPattern(String pattern) {
		if(pattern == null || pattern == "")
			return container.iterator();
		String str = pattern.toLowerCase();
		return this.getStringsContainingRgx(str.replace("?", ".?")
				.replace("*", ".*?"));
	}

}
