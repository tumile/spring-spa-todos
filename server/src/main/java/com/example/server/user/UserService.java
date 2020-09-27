package com.example.server.user;

import com.example.server.config.JWTUtils;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final JWTUtils jwtUtils;
    private final GoogleIdTokenVerifier verifier;

    public UserService(@Value("${app.googleClientId}") String clientId, UserRepository userRepository,
                       JWTUtils jwtUtils) {
        this.userRepository = userRepository;
        this.jwtUtils = jwtUtils;
        NetHttpTransport transport = new NetHttpTransport();
        JsonFactory jsonFactory = new JacksonFactory();
        verifier = new GoogleIdTokenVerifier.Builder(transport, jsonFactory)
                .setAudience(Collections.singletonList(clientId))
                .build();
    }

    public User getUser(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public String loginOAuthGoogle(IDTokenRequest requestBody) {
        User user = verifyIDToken(requestBody.getIdToken());
        if (user == null) {
            return null;
        }
        user = createOrUpdateUser(user);
        return jwtUtils.createToken(user, false);
    }

    @Transactional
    public User createOrUpdateUser(User user) {
        User existingUser = userRepository.findByEmail(user.getEmail()).orElse(null);
        if (existingUser == null) {
            user.setRoles("ROLE_USER");
            userRepository.save(user);
            return user;
        }
        existingUser.setFirstName(user.getFirstName());
        existingUser.setLastName(user.getLastName());
        existingUser.setPictureUrl(user.getPictureUrl());
        userRepository.save(existingUser);
        return existingUser;
    }

    private User verifyIDToken(String idToken) {
        try {
            GoogleIdToken idTokenObj = verifier.verify(idToken);
            if (idTokenObj == null) {
                return null;
            }
            GoogleIdToken.Payload payload = idTokenObj.getPayload();
            String firstName = (String) payload.get("given_name");
            String lastName = (String) payload.get("family_name");
            String email = payload.getEmail();
            String pictureUrl = (String) payload.get("picture");
            return new User(firstName, lastName, email, pictureUrl);
        } catch (GeneralSecurityException | IOException e) {
            return null;
        }
    }
}
