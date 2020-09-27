import React, { useState } from "react";
import GoogleLogin from "react-google-login";
import { BrowserRouter, Route, Switch } from "react-router-dom";
import "./App.css";
import logo from "./logo.svg";

const clientId = "your-client-id";

function App() {
  const [user, setUser] = useState(null);

  function onSuccess(res) {
    const { tokenId } = res;
    fetch("http://localhost:8080/auth/google", {
      method: "POST",
      credentials: "include",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({
        idToken: tokenId,
      }),
    })
      .then(() =>
        fetch("http://localhost:8080/user/me", {
          credentials: "include",
        }),
      )
      .then((res) => res.json())
      .then((user) => setUser(user))
      .catch((err) => console.error(err));
  }

  return (
    <BrowserRouter>
      <Switch>
        <Route exact path="/">
          <div className="App">
            <header className="App-header">
              <img src={logo} className="App-logo" alt="logo" />
              <p>Awesome React!</p>
              {user == null ? (
                <GoogleLogin onSuccess={onSuccess} onFailure={console.error} clientId={clientId}>
                  Login with Google
                </GoogleLogin>
              ) : (
                <>
                  <h2>Hello {user.firstName}</h2>
                  <img src={user.pictureUrl} alt="Profile" />
                </>
              )}
            </header>
          </div>
        </Route>
      </Switch>
    </BrowserRouter>
  );
}

export default App;
