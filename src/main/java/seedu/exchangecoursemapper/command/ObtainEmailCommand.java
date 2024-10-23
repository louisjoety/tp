package seedu.exchangecoursemapper.command;

import seedu.exchangecoursemapper.exception.Exception;

import java.io.IOException;
import javax.json.JsonObject;

public class ObtainEmailCommand extends Command {
    @Override
    public void execute(String userInput) {
        try  {
            JsonObject jsonObject = super.createJsonObject();
            String schoolName = getSchoolName(userInput).toLowerCase();
            String matchingSchool = findMatchingSchool(jsonObject, schoolName);

            if (matchingSchool != null) {
                JsonObject schoolInfo = jsonObject.getJsonObject(matchingSchool);
                handleEmail(schoolInfo, matchingSchool);
            } else {
                System.out.println("Error: Unknown university - " + schoolName);
            }
        } catch (IOException e) {
            System.err.println(Exception.fileReadError());
        } catch (IllegalArgumentException e) {
            System.err.println(e.getMessage());
        }
    }

    private String getSchoolName(String userInput) {
        String inputWithoutCommand = userInput.substring(userInput.indexOf(" ") + 1).trim();
        String[] inputParts = inputWithoutCommand.split(" /");

        if (inputParts.length != 2 || !inputParts[1].equalsIgnoreCase("email")) {
            throw new IllegalArgumentException("Invalid input format.");
        }

        return inputParts[0].trim();
    }

    private void handleEmail(JsonObject schoolInfo, String schoolName) {
        String email = schoolInfo.getString("email", null);
        if (email != null) {
            System.out.println("Email for " + schoolName + ": " + email);
        } else {
            System.out.println("Email not available for " + schoolName);
        }
    }

    private String findMatchingSchool(JsonObject jsonObject, String schoolName) {
        for (String key : jsonObject.keySet()) {
            if (key.toLowerCase().equals(schoolName)) {
                return key;
            }
        }
        return null;
    }
}