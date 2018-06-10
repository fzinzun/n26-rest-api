package com.github.zinzun.n26.statistics.utils;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/***
 * This class is used to send the message 204 then the time stam is out of the range. 
 * @author Francisco Zinzun
 *
 */
@ResponseStatus(value=HttpStatus.NO_CONTENT, reason="No such Order")  // 204
public class OutOfRangeException extends RuntimeException {

}
