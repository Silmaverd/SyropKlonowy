package com.blinenterprise.SyropKlonowy.service;

public interface SearchCriterion<T> {
    boolean fulfilledBy(T testedObject);
}
