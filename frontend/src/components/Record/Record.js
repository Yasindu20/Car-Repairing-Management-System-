import React, { Component } from "react";
import { Link } from "react-router-dom";
import PropTypes from "prop-types";
import { connect } from "react-redux";
import { deleteProject } from "../../actions/dashboardActions";

class Record extends Component {
  onDeleteClick = id => {
    this.props.deleteProject(id);
  };

  render() {
    const { project } = this.props;
    
    
    return (
      <div className="container">
        <div className="card card-body bg-light mb-3">
          <div className="row">
            <div className="col-2">
              <span className="mx-auto">{project.projectIdentifier}</span>
            </div>
            <div className="col-lg-6 col-md-4 col-8">
              <h4>{project.deviceName}</h4>
              <p>Issue : {project.description}</p>

            </div>
            <div className="col-md-4 d-none d-lg-block">
              <ul className="list-group">
                <Link to={`/projectBoard/${project.projectIdentifier}`}>
                  <li className="list-group-item board" style={{ textAlign: 'center'    }}>
                    <i className="fa pr-1" style={{ fontFamily: 'Calibri', fontSize: 20 }}> Repairing tasks </i>
                  </li>
                </Link>
                <Link to={`/updateProject/${project.projectIdentifier}`}>
                  <li className="list-group-item update" style={{ textAlign: 'center'    }}>
                    <i className="fa pr-1" style={{ fontFamily: 'Calibri', fontSize: 20 }}> Update Device Details</i>
                  </li>
                </Link>

                <li
                  className="list-group-item delete " style={{  textAlign: 'center'    }}
                  onClick={this.onDeleteClick.bind(
                    this,
                    project.projectIdentifier
                  )}
                >
                  <i className="fa pr-1" style={{ fontFamily: 'Calibri', fontSize: 20}}> Delete the Record</i>
                </li>
              </ul>
            </div>
          </div>
        </div>
      </div>
    );
  }
}

Record.propTypes = {
  deleteProject: PropTypes.func.isRequired
};

export default connect(
  null,
  { deleteProject }
)(Record);
