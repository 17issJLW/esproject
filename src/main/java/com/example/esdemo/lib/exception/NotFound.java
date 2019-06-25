package com.example.esdemo.lib.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@ResponseStatus(value = HttpStatus.NOT_FOUND,reason = "Not found document")
public class NotFound extends RuntimeException {
}
