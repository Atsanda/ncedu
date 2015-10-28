package isx;

/**
 * Created by artyom on 27.10.15.
 */
public class Attribute {
    private String name;
    private int maxLenth = -1;
    private String checkRgx;

    public String getCheckRgx() {
        return checkRgx;
    }

    public void setCheckRgx(String checkRgx) {
        this.checkRgx = checkRgx;
    }

    public int getMaxLenth() {
        return maxLenth;
    }

    public void setMaxLenth(int maxLenth) {
        this.maxLenth = maxLenth;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
