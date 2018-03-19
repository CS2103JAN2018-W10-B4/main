package seedu.address.model.person;

import static java.util.Objects.requireNonNull;

/**
 * Represents a Person's date added to the address book.
 * Guarantees: immutable; is valid
 */
public class DateAdded {

    public static final String MESSAGE_DATE_CONSTRAINTS =
            "Date input should be in the format: dd/MM/yyyy";
    /*
     * The first character of the address must not be a whitespace,
     * otherwise " " (a blank string) becomes a valid input.
     */
    public static final String DATE_VALIDATION_REGEX = "\\d{2}/\\d{2}/\\d{4}";

    public final String dateAdded;

    /**
     * Constructs a {@code DateAdded}.
     *
     * @param dateAdded A valid date.
     */
    public DateAdded(String dateAdded) {
        requireNonNull(dateAdded);
        assert isValidDate(dateAdded); //dateAdded generated by the program should be correct
        this.dateAdded = dateAdded;
    }

    /**
     * Returns true if a given string is a valid date.
     */
    public static boolean isValidDate(String test) {
        return test.matches(DATE_VALIDATION_REGEX);
    }

    @Override
    public String toString() {
        return dateAdded;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof DateAdded // instanceof handles nulls
                && this.dateAdded.equals(((DateAdded) other).dateAdded)); // state check
    }

    @Override
    public int hashCode() {
        return dateAdded.hashCode();
    }

}
