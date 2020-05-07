package com.yevhenii.muzyka.web.rest.errors;

import java.net.URI;

public final class ErrorConstants {

    public static final String ERR_CONCURRENCY_FAILURE = "error.concurrencyFailure";
    public static final String ERR_VALIDATION = "error.validation";
    public static final String PROBLEM_BASE_URL = "https://www.jhipster.tech/problem";
    public static final URI DEFAULT_TYPE = URI.create(PROBLEM_BASE_URL + "/problem-with-message");
    public static final URI CONSTRAINT_VIOLATION_TYPE = URI.create(PROBLEM_BASE_URL + "/constraint-violation");
    public static final String ENGLISH_WORD_ALREADY_EXISTS = "english.word.exists";
    public static final String ENGLISH_WORD_NOT_FOUND = "english.word.not.found";
    public static final String RUSSIAN_WORD_NOT_FOUND = "russian.word.not.found";
    public static final String RUSSIAN_WORD_ALREADY_EXISTS = "russian.word.exists";

    private ErrorConstants() {
    }
}
