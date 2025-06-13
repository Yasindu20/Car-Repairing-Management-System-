import React from "react";
import { Link } from "react-router-dom";

const AddRecordButton = () => {
  return (
    <React.Fragment>
      <Link to="/addProject" className="btn btn-lg btn-info">
        Add New Device
      </Link>
    </React.Fragment>
  );
};

export default AddRecordButton;
