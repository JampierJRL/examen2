package com.codigo.examen.service;

import com.codigo.examen.request.SignInRequest;
import com.codigo.examen.response.AuthenticationResponse;

public interface AuthenticationService {

    AuthenticationResponse signin(SignInRequest signInRequest);
}
