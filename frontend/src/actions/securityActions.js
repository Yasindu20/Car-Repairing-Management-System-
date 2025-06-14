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
    const res = await axios.post("/api/users/register", newUser);
    
    console.log("Registration API response:", res);
    
    // If we get here, registration was successful
    history.push("/login");
    
  } catch (err) {
    console.error("Registration error:", err);
    
    if (err.response) {
      console.error("Error response data:", err.response.data);
      console.error("Error response status:", err.response.status);
      
      dispatch({
        type: GET_ERRORS,
        payload: err.response.data
      });
    } else if (err.request) {
      // The request was made but no response was received
      console.error("No response received:", err.request);
      
      dispatch({
        type: GET_ERRORS,
        payload: { error: "Server did not respond. Please try again later." }
      });
    } else {
      // Something happened in setting up the request
      console.error("Request setup error:", err.message);
      
      dispatch({
        type: GET_ERRORS,
        payload: { error: "Could not send request: " + err.message }
      });
    }
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
    
    if (err.response) {
      dispatch({
        type: GET_ERRORS,
        payload: err.response.data
      });
    } else {
      dispatch({
        type: GET_ERRORS,
        payload: { error: "Could not connect to server. Please try again." }
      });
    }
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