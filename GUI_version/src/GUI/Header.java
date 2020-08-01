package GUI;

import java.io.Serializable;

/**
 * this is a class to hold needed information for each header
 * @author Armin Goodarzi
 */
public class Header implements Serializable {
    private boolean enable;
    private String name;
    private String value;
    /**
     *
     * @param enable a boolean to check if header is enable or not
     * @param name name of header
     * @param value value of header
     */
    public Header(boolean enable, String name, String value) {
        this.enable = enable;
        this.name = name;
        this.value = value;
    }

    /**
     *
     * @return if header is enable or not
     */
    public boolean isEnable() {
        return enable;
    }

    /**
     *
     * @param enable putted value
     */
    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    /**
     *
     * @return value
     */
    public String getValue() {
        return value;
    }

    /**
     *
     * @param value putted value
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     *
     * @return name of header
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @param name putted name for header
     */
    public void setName(String name) {
        this.name = name;
    }
}
