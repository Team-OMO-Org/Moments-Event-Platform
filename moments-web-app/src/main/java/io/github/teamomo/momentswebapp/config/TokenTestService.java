//package io.github.teamomo.momentswebapp.config;
//
//import com.auth0.jwt.JWT;
//import com.auth0.jwt.interfaces.DecodedJWT;
//import io.github.teamomo.momentswebapp.client.BackendClient;
//import org.springframework.stereotype.Service;
//
//@Service
//public class TokenTestService {
//
//  public void testTokenRefresh(String accessToken, BackendClient backendClient) {
//    // Decode the access token
//    DecodedJWT decodedJWT = JWT.decode(accessToken);
//    long expirationTime = decodedJWT.getExpiresAt().getTime();
//    System.out.println("Access Token Expiration Time: " + expirationTime);
//
//    // Simulate token expiry (wait or set short expiration in Keycloak)
//    try {
//      Thread.sleep(60000*5); // Wait for 5 minutes (adjust based on token lifespan)
//    } catch (InterruptedException e) {
//      Thread.currentThread().interrupt();
//    }
//
//    // Make a backend call to trigger token refresh
//    try {
//      backendClient.someAuthenticatedCall();
//      System.out.println("Token refresh successful, new access token used.");
//    } catch (Exception e) {
//      System.err.println("Token refresh failed: " + e.getMessage());
//    }
//  }
//}