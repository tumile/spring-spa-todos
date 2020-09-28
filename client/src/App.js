import React, { useEffect, useState } from "react";
import GoogleLogin from "react-google-login";
import { BrowserRouter, Redirect, Route, Switch } from "react-router-dom";
import "./App.css";
import logo from "./logo.svg";

const clientId = "your-client-id";

function App() {
  const [user, setUser] = useState(null);

  async function getUser() {
    const user = await fetch("http://localhost:8080/me", {
      credentials: "include",
    })
      .then((res) => {
        if (!res.ok) throw res;
        return res.json();
      })
      .catch(() => null);
    setUser(user);
  }

  useEffect(() => {
    getUser();
  }, []);

  return (
    <BrowserRouter>
      <div className="app">
        <Switch>
          <Route exact path="/" render={(props) => <Home {...props} user={user} getUser={getUser} />} />
          <Route path="/todos" render={(props) => (user ? <TodoList {...props} user={user} /> : <Redirect to="/" />)} />
        </Switch>
      </div>
    </BrowserRouter>
  );
}

function Home(props) {
  async function login(res) {
    const { tokenId } = res;
    await fetch("http://localhost:8080/auth/google", {
      method: "POST",
      credentials: "include",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({
        idToken: tokenId,
      }),
    }).then(props.getUser);
  }

  if (props.user) {
    return <Redirect to="/todos" />;
  }
  return (
    <div className="text-center">
      <img src={logo} className="app-logo" alt="logo" />
      <h1 className="mb-4">Simple todos</h1>
      <GoogleLogin onSuccess={login} clientId={clientId}>
        Login with Google
      </GoogleLogin>
    </div>
  );
}

function TodoList(props) {
  const { user } = props;
  const [todos, setTodos] = useState([]);
  const [todoText, setTodoText] = useState("");

  async function getTodos() {
    await fetch("http://localhost:8080/todos", {
      credentials: "include",
    })
      .then((res) => {
        if (!res.ok) throw res;
        return res.json();
      })
      .then(setTodos)
      .catch(console.error);
  }

  async function addTodo() {
    if (todoText.trim()) {
      await fetch("http://localhost:8080/todos", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          "X-XSRF-TOKEN": getXsrfToken(),
        },
        credentials: "include",
        body: JSON.stringify({ content: todoText }),
      }).then((res) => {
        if (res.ok) {
          return getTodos().then(() => setTodoText(""));
        }
      });
    }
  }

  async function toggleTodo(todo) {
    await fetch(`http://localhost:8080/todos/${todo.id}`, {
      method: "PUT",
      headers: {
        "Content-Type": "application/json",
        "X-XSRF-TOKEN": getXsrfToken(),
      },
      credentials: "include",
      body: JSON.stringify({ ...todo, completed: !todo.completed }),
    }).then((res) => {
      if (res.ok) {
        return getTodos();
      }
    });
  }

  async function deleteTodo(todo) {
    await fetch(`http://localhost:8080/todos/${todo.id}`, {
      method: "DELETE",
      headers: {
        "X-XSRF-TOKEN": getXsrfToken(),
      },
      credentials: "include",
    }).then((res) => {
      if (res.ok) {
        return getTodos();
      }
    });
  }

  useEffect(() => {
    getTodos();
  }, []);

  return (
    <div>
      <div className="d-flex align-items-center mb-4">
        <h2 className="mr-3">Hi {user.firstName}!</h2>
        <img width="50" src={user.pictureUrl} alt="Profile" />
      </div>
      <div className="input-group mb-4">
        <input
          type="text"
          className="form-control"
          placeholder="New todo"
          value={todoText}
          onChange={(e) => setTodoText(e.target.value)}
        />
        <div className="input-group-append">
          <button className="btn btn-outline-primary" type="button" onClick={addTodo}>
            Add
          </button>
        </div>
      </div>
      {todos.map((todo) => (
        <div className="form-check d-flex justify-content-between" key={todo.id}>
          <label className="form-check-label">
            <input
              className="form-check-input"
              type="checkbox"
              checked={todo.completed}
              onChange={() => toggleTodo(todo)}
            />
            {todo.content}
          </label>
          <button type="button" className="btn btn-outline-danger btn-sm" onClick={() => deleteTodo(todo)}>
            X
          </button>
        </div>
      ))}
    </div>
  );
}

function getXsrfToken() {
  const start = document.cookie.indexOf("XSRF-TOKEN");
  if (start === -1) {
    return "";
  }
  let end = document.cookie.indexOf(";", start);
  if (end === -1) {
    end = document.cookie.length;
  }
  return document.cookie.substring(start + 11, end);
}

export default App;
