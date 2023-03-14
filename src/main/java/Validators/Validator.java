package Validators;

import Exceptions.ValidationException;

public abstract class Validator<T> {
    public abstract void validate(T entity) throws ValidationException;
}
