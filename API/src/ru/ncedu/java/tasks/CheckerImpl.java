package ru.ncedu.java.tasks;

import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



public class CheckerImpl implements Checker {

	@Override
	public Pattern getPLSQLNamesPattern() {
		String pattern = "\\p{Alpha}([\\p{Alpha}\\d_$]){0,29}";
		return Pattern.compile(pattern);
	}

	@Override
	/**
	 * Содержащиеся на web-странице URLы описываются тегом &lt;a href = ...&gt; (или &lt;a href=.../&gt;).<br/>
	 *  Ремарка для начинающих: в HTML &gt; - это > (больше), &lt; - это < (меньше), а комментарии пишутся в таком
	 *  "странном" виде, чтобы они корректно отображались в HTML, который из них генерируется через javadoc).<br/>
	 *  То есть, следует читать: URLы описываются тегом <a href = ...> (или <a href=.../>).<br/>
	 * Будем условно называть URLом закрытый или незакрытый тег a с обязательным атрибутом href,
	 *  значение которого не должно содержать пробельных символов (см.ниже). <br/>
	 * Заключать значение атрибута href в кавычки необязательно, но если использованы двойные кавычки,
	 *  то в значении МОГУТ быть пробельные символы.<br/>
	 * Имена тега A и атрибута HREF (как и другие имена в HTML) не чувствительны к регистру.<br/> 
	 * Между символом меньше, именем тега, названием атрибута, знаком равно и символом больше 
	 * могут быть следующие пробельные символы: 
	 * табуляция, перевод строки, возврат каретки, перевод формата, пробел.<br/> 
	 * @return шаблон для выделения содержащихся на web-странице URL-ов.
	 * */
	public Pattern getHrefURLPattern() {//
		String pattern = "<([ \t\n\r\f])*[Aa][ ([ \t\n\r\f])*]*[hH][Rr][Ee][Ff]([ \t\n\r\f])*=([ \t\n\r\f])*(([^\\s>\"]+)|(\"([^\"])*\"))([ \t\n\r\f])*/?>";//  spase bet a and href!!
		return Pattern.compile(pattern);
	}

	@Override
	/**
	 * e-mail имеет формат: <аккаунт>@<домен>.<домен_первого_уровня><br/>
	 * <Аккаунт> должен быть длиной не более 22 символов и состоять из символов:
	 *  латинские буквы, цифры, знак подчеркивания ("_"), точка ("."), дефис ("-").<br/> 
	 * Аккаунт не может начинаться с символов дефис ("-"), точка (".") или знак подчеркивания ("_").<br/>
	 * Аккаунт не может заканчиваться символом дефис ("-"), точка (".") или знак подчеркивания ("_"). <br/>
	 * <Домен> может быть доменом любого уровня, каждый уровень отделяется от другого символом точка (".").
	 * Название домена каждого уровня должно состоять более чем из одного символа, 
	 * начинаться и заканчиваться буквой латинского алфавита или цифрой. <br/>
	 * Промежуточными символами могут быть буквы латинского алфавита, цифры или дефис.<br/> 
	 * <Домен первого уровня> - допустим один из следующих: .ru, .com, .net, .org.
	 * @return шаблон для e-mail адресов.
	 * */
	public Pattern getEMailPattern() {
		String pattern = "\\p{Alnum}([\\p{Alnum}-._]{0,20})\\p{Alnum}@(\\p{Alnum}([\\p{Alnum}-]*)\\p{Alnum}.)+(ru|com|net|org)";
		return Pattern.compile(pattern);
	}

	@Override
	public boolean checkAccordance(String inputString, Pattern pattern)
			throws IllegalArgumentException {
		if(inputString == null && pattern == null)
			return true;
		if(pattern == null)
			throw new IllegalArgumentException();
		if(inputString == null)
			throw new IllegalArgumentException();
		
		
		Matcher m = pattern.matcher(inputString);
		return m.matches();
	}

	@Override
	public List<String> fetchAllTemplates(StringBuffer inputString,
			Pattern pattern) throws IllegalArgumentException {
		if(pattern == null || inputString == null)
			throw new IllegalArgumentException();
		
		List<String> l = new LinkedList<String>();
		Matcher m = pattern.matcher(inputString);
		
		while(m.find())
			l.add(m.group());
		
		return l;
	}
	/*
	private static boolean applyPattern(Pattern pattern) {
        try {
            String sourceString = new BufferedReader(new InputStreamReader(System.in)).readLine();
            Matcher m = pattern.matcher(sourceString);
            return m.matches();
        } catch (IOException e) {
            return false;
        }
    }

	
	public static void main(String[] args) {
		Checker t = new CheckerImpl();
		t.checkAccordance(null, null);
	}*/

}
