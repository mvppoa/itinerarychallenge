package com.mvppoa.adidas.web.rest.errors;

import org.zalando.problem.ThrowableProblem;

/**
 *
 * @author Guilherme.Salomone
 *
 */
public class ConnectionServerException extends ThrowableProblem {

	private static final long serialVersionUID = 1L;

	private String message;

	public ConnectionServerException() {
        super();
    }

	public ConnectionServerException(String message) {
		super();
		this.message = message;
	}

	@Override
	public String getMessage() {
		return message;
	}


}
