package com.github.zinzun.n26.statistics.utils;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.NO_CONTENT, reason="No such Order")  // 204
public class OutOfRangeException extends RuntimeException {

}
