# Simple TODOs 🧾

<img src="https://img.shields.io/badge/awesome%20%F0%9F%98%84-100%25-brightgreen">

This project is a full-stack demo of the good old todo list application. Powered by Spring 🍃 and React ⚛️.

## Authentication

Since we are building a SPA or a public client, we should use the [Authorization Code flow with PKCE](https://auth0.com/docs/flows/authorization-code-flow-with-proof-key-for-code-exchange-pkce) to authorize with third-party providers, in this case, Google. Using [react-google-login](https://www.npmjs.com/package/react-google-login), which internally uses [Google's sign-in library](https://developers.google.com/identity/sign-in/web), we have this flow implemented out of the box.

At the end of the OAuth flow, our application receives an ID Token from Google, specified by the [OpenID Connect](https://developers.google.com/identity/protocols/oauth2/openid-connect) specs, then POSTs it to our [backend to verify](https://developers.google.com/identity/sign-in/android/backend-auth#verify-the-integrity-of-the-id-token). The ID Token is essentially a JWT, but signed asymmetrically and verified with the provider's public JWK. Assuming the verification succeeds, we extract the user profile from the ID Token and update our database. Lastly, we issue our JWT wrapped in a secured cookie to be set in the user's browser.

Every incoming request goes through the [JWTRequestFilter](server/src/main/java/com/example/server/config/JWTRequestFilter.java), which extracts the auth cookie, verifies the JWT, and authenticates the current user.
