package com.mentors.utility;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import com.mentors.exception.MentorException;


@Aspect
@Component
public class LogginAspect {
	
	private static final Log LOGGER = LogFactory.getLog(LogginAspect.class);
	
	@AfterThrowing(pointcut= "execution(* com.sinclair.digital.interns.service.*Impl.*(..))", throwing="exception")
	public void logServiceException(MentorException exception){
		LOGGER.info(exception.getMessage(), exception);
	}

}
