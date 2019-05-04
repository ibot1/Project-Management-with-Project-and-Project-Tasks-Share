import React, { Component } from "react";
import ProjectItem from "./Project/ProjectItem";
import CreateProjectButton from "./Project/CreateProjectButton";
import { connect } from "react-redux";
import { getProjects } from "../actions/projectActions";
import PropTypes from "prop-types";
import Axios from "axios";

const maxInactive = 100; //10 minutes
var sessionStart = new Date().getTime() / 1000; //session start-time in seconds
var sessionActive = sessionStart;
const checkTime = 1000; //1-second

function postToken() {
  var str = localStorage.getItem("jwtToken");
  str = str.substring(7, str.length);
  console.log("Token retrieved: " + str);

  Axios.get("/api/users/token/" + str);
  /*
  var xhttp = new XMLHttpRequest();
  xhttp.onreadystatechange = function() {
    if (this.readyState === 4 && this.status === 200) {
      document.getElementById("demo").innerHTML = this.responseText;
    }
  };
  xhttp.open("GET", "localhost:8090/api/users/token/" + str, true);
  xhttp.send();
  */

  console.log("API called");
}

function detectEvents() {
  var arr = [
    "drag",
    "wheel",
    "select",
    "click",
    "auxclick",
    "mousemove",
    "mousedown",
    "mouseup",
    "keydown",
    "keyup",
    "keypress",
    "cut",
    "copy",
    "paste",
    "scroll",
    "compositionupdate",
    "compositionend",
    "compositionstart",
    "submit"
  ];

  for (let i = 0; i < arr.length; i++) {
    document.addEventListener(arr[i], resetSessionStartAndActive);
    //postToken();
    deleteSessionKey();
  }
}

function resetSessionStartAndActive() {
  sessionStart = new Date().getTime() / 1000;
  sessionActive = sessionStart;
  console.log("refreshed");
}

function checkActivity() {
  if (sessionActive - sessionStart >= maxInactive) {
    return true;
  } else {
    return false;
  }
}

function deleteSessionKey() {
  var chk = checkActivity();
  if (chk === true) {
    localStorage.removeItem("jwtToken");
    console.log("Removed the Authorization-token");
    window.location.href = "localhost:8090/login";
    //redirect the page
  } else {
    sessionActive = new Date().getTime() / 1000;
  }
}

class Dashboard extends Component {
  componentDidMount() {
    this.props.getProjects();
  }

  render() {
    const { projects } = this.props.project;
    setInterval(detectEvents, checkTime);
    return (
      <div className="projects">
        <div className="container">
          <div className="row">
            <div className="col-md-12">
              <h1 className="display-4 text-center">Projects</h1>
              <br />
              <CreateProjectButton />

              <br />
              <hr />

              {projects.map(project => (
                <ProjectItem key={project.id} project={project} />
              ))}
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

export default connect(
  mapStateToProps,
  { getProjects }
)(Dashboard);
