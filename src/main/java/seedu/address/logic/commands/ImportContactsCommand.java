package seedu.address.logic.commands;
import seedu.address.logic.commands.exceptions.CommandException;
import static java.util.Objects.requireNonNull;
import java.util.Set;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import seedu.address.model.person.*;
import seedu.address.model.person.exceptions.DuplicatePersonException;
import seedu.address.model.tag.Tag;
import seedu.address.model.tag.UniqueTagList;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

//This class borrows from https://www.callicoder.com/java-read-write-csv-file-apache-commons-csv/
// and https://github.com/callicoder/java-read-write-csv-file

public class ImportContactsCommand extends Command {

    public static final String COMMAND_WORD = "import_contacts"; //is there a list of added commands?
    public static final String COMMAND_ALIAS = "ic"; //is there a list of aliases??
    public static final String MESSAGE_SUCCESS = "Contacts successfully imported.\n"; //is there a list of aliases??
    public static final String MESSAGE_FILE_SUCCESS_OPEN = "File was successfully opened.\n";
    public static final String MESSAGE_FILE_FAILED_OPEN = "File failed to open. Please try a different address " +
            "or check if file may be corrupt.\n";
    public static final String MESSAGE_FILE_NOT_FOUND = "No file was found at the address provided. " +
            "Please provide anotehr address.\n";
    public static final String MESSAGE_NO_ADDRESS = "No address was provided, please provide an address to a csv, "
            + "from which to import the file\n";


    private String fileAddress;
    private CSVParser csvParser;


    public ImportContactsCommand(String _fileAddress) throws IOException {
        requireNonNull(_fileAddress); //This throws IOException if _fileAddress is null
        fileAddress = _fileAddress;
    }

    public CommandResult openFile() throws IOException, CommandException { //go ahead... see what happens
        try {
            Reader reader = Files.newBufferedReader(Paths.get(fileAddress));
            csvParser = new CSVParser(reader, CSVFormat.DEFAULT
                    .withFirstRecordAsHeader()
                    .withIgnoreHeaderCase()
                    .withTrim());
            return new CommandResult(MESSAGE_FILE_SUCCESS_OPEN + "from : " + fileAddress);
        }
        catch (NullPointerException npe) { //file won't open, null ptr
            throw new CommandException(MESSAGE_FILE_FAILED_OPEN);
        }
        catch (FileNotFoundException fnf) {
            throw new CommandException(MESSAGE_FILE_NOT_FOUND);
        }
        catch(IOException ioe) {
            throw new CommandException("IOException thrown in ImportContactsCommand.");
        }
    }


    public void printResult(String n, String e, String p, String a) {
        System.out.println("---------------");
        System.out.println("Name : " + n);
        System.out.println("Email : " + e);
        System.out.println("Phone : " + p);
        System.out.println("Address : " + a);
        System.out.println("---------------\n\n");
    }

    @Override
    public CommandResult execute() throws CommandException {
        Person personToAdd;
        String name;
        String email;
        String phone;
        String address;

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        DateAdded addDate;

        try {
            Iterable<CSVRecord> csvRecords = csvParser.getRecords(); //get iterator to go through records in csv

            for (CSVRecord csvRecord : csvRecords) { //iterate through the
                // Accessing values by Header names
                 name = csvRecord.get("Name");
                 email = csvRecord.get("Email");
                 phone = csvRecord.get("Phone");
                 address = csvRecord.get("Address");
                 addDate = new DateAdded(formatter.format(date));

                printResult(name, email, phone, address); //mainly for debugging

                Set<Tag> tagSet = (Set<Tag>) new Tag("friend"); //temporary tag, fix later

                personToAdd = new Person(new Name(name), new Phone(phone), new Email(email),
                        new Address(address), addDate, tagSet);

                AddCommand addMe = new AddCommand(personToAdd); //not the most efficient...
                addMe.executeUndoableCommand();
            }

            return new CommandResult(MESSAGE_SUCCESS);
        }
        catch (IOException ioe) {
            throw new CommandException("Aw Fuck."); //Obviously need to change this
        }
    }

}

