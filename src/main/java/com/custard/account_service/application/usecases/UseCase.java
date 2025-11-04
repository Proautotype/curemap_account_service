package com.custard.account_service.application.usecases;

public interface UseCase<T, R> {
    R execute(T command);
}
