import java.text.*;


/* 
	* Date validator to handle correctness of date at front end 
	* Thanks https://www.baeldung.com/java-string-valid-date for this :-
*/

public class DateValidatorUsingDateFormat{
    private String dateFormat;

    public DateValidatorUsingDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }

    public boolean isValid(String dateStr) {
        DateFormat sdf = new SimpleDateFormat(this.dateFormat);
        sdf.setLenient(false);
        try {
            sdf.parse(dateStr);
        } catch (ParseException e) {
        	e.printStackTrace();
            return false;
        }
        return true;
    }
}