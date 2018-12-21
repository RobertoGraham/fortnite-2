package io.github.robertograham.fortniteclienttwo.implementation;

import org.apache.http.client.ResponseHandler;

import java.util.Optional;

interface OptionalResponseHandlerProvider {

    ResponseHandler<Optional<String>> forString();

    <T> ResponseHandler<Optional<T>> forClass(Class<T> tClass);
}
