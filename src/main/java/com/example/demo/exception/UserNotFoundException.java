package com.example.demo.exception;

/*
 *  02/07/2018 at 23:28
 *     by Guillaume M.
 */
public class UserNotFoundException extends NotFoundException {
    public UserNotFoundException(String message) {
        super(message);
    }
}
