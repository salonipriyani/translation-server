package ds;
/**
 Represents a language with a code and name.
 */

//Name: Saloni Priyani
// Andrew ID: spriyani
public class Language {

    // The language code (e.g. "en" for English)
    private String code;

    // The language name (e.g. "English")
    private String name;

    /**

     Returns the language code.
     @return the language code
     */
    public String getCode() {
        return code;
    }
    /**

     Sets the language code.
     @param code the language code
     */
    public void setCode(String code) {
        this.code = code;
    }
    /**

     Returns the language name.
     @return the language name
     */
    public String getName() {
        return name;
    }
    /**

     Sets the language name.
     @param name the language name
     */
    public void setName(String name) {
        this.name = name;
    }
}
