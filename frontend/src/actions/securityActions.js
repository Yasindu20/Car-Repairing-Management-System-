import axios from "axios";
import { GET_ERRORS, SET_CURRENT_USER } from "./types";
import setJWTToken from "../securityUtils/setJWTToken";
import jwt_decode from "jwt-decode";

export const createNewUser = (newUser, history) => async dispatch => {
  try {
    console.log("Attempting to register user:", newUser);
    
    // Clear previous errors
    dispatch({
      type: GET_ERRORS,
      payload: {}
    });
    
    // Make the API call
    await axios.post("/api/users/register", newUser);
    
    console.log("Registration successful, redirecting to login");
    history.push("/login");
    
  } catch (err) {
    console.error("Registration error:", err);
    
    let errorData = { error: "Server error occurred. Please try again later." };
    
    if (err.response) {
      if (err.response.data) {
        errorData = err.response.data;
      }
      console.error("Error response status:", err.response.status);
    }
    
    dispatch({
      type: GET_ERRORS,
      payload: errorData
    });
  }
};

export const login = LoginRequest => async dispatch => {
  try {
    console.log("Attempting to login with:", LoginRequest.username);
    
    const res = await axios.post("/api/users/login", LoginRequest);
    
    console.log("Login successful, received token");
    
    // extract token from res.data
    const { token } = res.data;
    
    // store the token in the localStorage
    localStorage.setItem("jwtToken", token);
    
    // set our token in header
    setJWTToken(token);
    
    // decode token on React
    const decoded = jwt_decode(token);
    
    // dispatch to our securityReducer
    dispatch({
      type: SET_CURRENT_USER,
      payload: decoded
    });
  } catch (err) {
    console.error("Login error:", err);
    
    let errorData = { error: "Server error occurred. Please try again later." };
    
    if (err.response && err.response.data) {
      errorData = err.response.data;
    }
    
    dispatch({
      type: GET_ERRORS,
      payload: errorData
    });
  }
};

export const logout = () => dispatch => {
  localStorage.removeItem("jwtToken");
  setJWTToken(false);
  dispatch({
    type: SET_CURRENT_USER,
    payload: {}
  });
};