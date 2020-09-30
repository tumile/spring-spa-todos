# Simple TODOs 🧾

<img src="https://img.shields.io/badge/awesome%20%F0%9F%98%84-100%25-brightgreen">

This project is a full-stack demo of the good old todo list application. Powered by Spring 🍃 and React ⚛️.

## Authentication

Since we want to authenticate our users with a third-party provider like Google, we'll use the OpenID Connect flow, which is an extension of OAuth2 for authentication (i.e with `response-type='id_token'`). I find [this](https://auth0.com/docs/authorization/which-oauth-2-0-flow-should-i-use) and [this](https://medium.com/@darutk/diagrams-of-all-the-openid-connect-flows-6968e3990660) article explaining extremely well the difference between all OAuth2 flows. Using [react-google-login](https://www.npmjs.com/package/react-google-login), which internally uses [Google's sign-in library](https://developers.google.com/identity/sign-in/web), we have this flow implemented out of the box.

At the end of the OAuth flow, our application receives an ID Token from Google, specified by the [OpenID Connect](https://developers.google.com/identity/protocols/oauth2/openid-connect) specs, then POSTs it to our [backend to verify](https://developers.google.com/identity/sign-in/web/backend-auth#verify-the-integrity-of-the-id-token). The ID Token is essentially a JWT, but signed asymmetrically and verified with the provider's public JWK. Assuming the verification succeeds, we extract the user profile from the ID Token and update our database. Lastly, we issue our JWT wrapped in a secured cookie to be set in the user's browser.

Every incoming request goes through the [JWTRequestFilter](server/src/main/java/com/example/server/config/JWTRequestFilter.java), which extracts the auth cookie, verifies the JWT, and authenticates the current user.
