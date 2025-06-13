import React, { Component } from "react";
import Record from "./Record/Record";
import AddRecordButton from "./Record/AddRecordButton";
import { connect } from "react-redux";
import { getProjects } from "../actions/dashboardActions";
import PropTypes from "prop-types";
import html2canvas from 'html2canvas';
import jsPDF from 'jspdf';



class Dashboard extends Component {
  componentDidMount() {
    this.props.getProjects();
  }






  render() {
    const { projects } = this.props.project;

    return (
      <div className="projects">
        <div className="container">
          <div className="row">
            <div className="col-md-12">
              <h1 className="display-4 text-center">Repairing List</h1>
              <br />
              
          
          

              <br />


              <div style={{ display: 'flex', justifyContent: 'space-between' }}>

              <AddRecordButton />  <button className="btn btn-lg btn-info" onClick={handlePrint}>Print List</button>
            </div>

              

              <hr />

              <div id="print-container">
  {projects.map(project => (
    <Record key={project.id} project={project} />
  ))}
</div>

            </div>
          </div>
        </div>
      </div>




    );
  }
}

Dashboard.propTypes = {
  project: PropTypes.object.isRequired,
  getProjects: PropTypes.func.isRequired
};

const mapStateToProps = state => ({
  project: state.project
});


const handlePrint = () => {
  const elementToPrint = document.getElementById('print-container'); // Replace with your element ID
  html2canvas(elementToPrint).then(canvas => {
    const imgData = canvas.toDataURL('image/png');
    const pdf = new jsPDF('p', 'mm', 'a4');
    pdf.addImage(imgData, 'PNG', 10, 10, 190, 270);
    pdf.save('Repairing_Records.pdf');
  });
};


export default connect(
  mapStateToProps,
  { getProjects }
)(Dashboard);
