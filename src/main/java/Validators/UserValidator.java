package Validators;

import Domain.User;
import Exceptions.ValidationException;

public class UserValidator extends Validator<User> {
    @Override
    public void validate(User entity) throws ValidationException {
        String message = "";
        if (entity.getID() < 1) {
            message = message + "Invalid ID ";
        }
        if (entity.getUsername().equals("")) {
            message = message + "Invalid username";
        }
        if (!message.equals("")) {
            throw new ValidationException(message);
        }
    }
}
