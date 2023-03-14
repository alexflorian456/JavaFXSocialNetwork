package Validators;

import Domain.Friendship;
import Exceptions.ValidationException;

public class FriendshipValidator extends Validator<Friendship>{

    /*UserValidator userValidator;

    public FriendshipValidator() {
        this.userValidator = new UserValidator();
    }*/

    @Override
    public void validate(Friendship entity) throws ValidationException {
        if(entity.getUser2()==entity.getUser1())
            throw new ValidationException("User can't be his own friend!");
    }
}
