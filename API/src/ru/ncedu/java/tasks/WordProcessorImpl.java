package ru.ncedu.java.tasks;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by artyom on 16.10.15.
 */
public class WordProcessorImpl implements WordProcessor {
    private  String text;

    @Override
    public String getText() {
        return text;
    }

    @Override
    public void setSource(String src) {
        if(src == null)
            throw new IllegalArgumentException();
        text = src;
    }

    @Override
    public void setSourceFile(String srcFile) throws IOException {
        if(srcFile == null)
            throw new IllegalArgumentException();
        byte[] encoded = Files.readAllBytes(Paths.get(srcFile));
        text = new String(encoded);
    }

    @Override
    public void setSource(FileInputStream fis) throws IOException {
        if(fis == null)
            throw new IllegalArgumentException();
        StringBuilder builder = new StringBuilder();
        int ch;
        while((ch = fis.read()) != -1){
            builder.append((char)ch);
        }
        text = builder.toString();
    }

    @Override
    public Set<String> wordsStartWith(String begin) {
        if(begin == null)
            begin = "";

        text = text.toLowerCase();
        begin = begin.toLowerCase();
        Set<String> ret = new HashSet<String>();

        Pattern pattern;
        pattern = Pattern.compile("[ \\f\\n\\r\\t\\v]"+begin+"[^ \\f\\n\\r\\t\\v]*");
        Matcher matcher = pattern.matcher(text);
        while (matcher.find())
        {
            ret.add(matcher.group(0).trim());
        }
        ret.remove("");
        pattern = Pattern.compile("^"+begin+"[^ \\f\\n\\r\\t\\v]*");
        matcher = pattern.matcher(text);
        while (matcher.find())
        {
            ret.add(matcher.group(0).trim());
        }

        return ret;
    }

    public static void main(String[] args) throws IOException{
        WordProcessor tst = new WordProcessorImpl();
        tst.setSourceFile("tst.txt");
        System.out.println(tst.wordsStartWith(null));
    }
}
