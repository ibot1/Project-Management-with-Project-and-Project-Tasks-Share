import axios from "axios";
import {
  GET_ERRORS,
  GET_BACKLOG,
  GET_PROJECT_TASK,
  DELETE_PROJECT_TASK
} from "./types";

export const addProjectTask = (
  backlog_id,
  project_task,
  history
) => async dispatch => {
  try {
    await axios.post(`/api/backlog/${backlog_id}`, project_task);
    history.push(`/projectBoard/${backlog_id}`);
    dispatch({
      type: GET_ERRORS,
      payload: {}
    });
  } catch (err) {
    dispatch({
      type: GET_ERRORS,
      payload: err.response.data
    });
  }
};

export const updateSort = (backlog_id, sort) => async dispatch => {
  const res = await axios.get(`/api/project/${backlog_id}`);
  res.data.sort = sort;
  await axios.post("/api/project", res.data);
};

export const getBacklog = backlog_id => async dispatch => {
  try {
    const projectRes = await axios.get(`/api/project/${backlog_id}`);
    const res = await axios.get(`/api/backlog/${backlog_id}`, {
      params: {
        sort: projectRes.data.sort
      }
    });
    dispatch({
      type: GET_BACKLOG,
      payload: res.data
    });
  } catch (err) {
    dispatch({
      type: GET_ERRORS,
      payload: err.response.data
    });
  }
};

export const getProjectTask = (
  backlog_id,
  pt_id,
  history
) => async dispatch => {
  try {
    const res = await axios.get(`/api/backlog/${backlog_id}/${pt_id}`);
    dispatch({
      type: GET_PROJECT_TASK,
      payload: res.data
    });
  } catch (err) {
    history.push("/dashboard");
  }
};

export const updateProjectTask = (
  backlog_id,
  pt_id,
  project_task,
  history
) => async dispatch => {
  try {
    await axios.patch(`/api/backlog/${backlog_id}/${pt_id}`, project_task);
    history.push(`/projectBoard/${backlog_id}`);
    dispatch({
      type: GET_ERRORS,
      payload: {}
    });
  } catch (error) {
    dispatch({
      type: GET_ERRORS,
      payload: error.response.data
    });
  }
};

export const updateProjectTaskCategory = (
  backlog_id,
  pt_id,
  newstatus
) => async dispatch => {
  const res = await axios.get(`/api/backlog/${backlog_id}/${pt_id}`);
  console.log("res.data.status ", res.data.status);
  res.data.status = newstatus;
  await axios.patch(`/api/backlog/${backlog_id}/${pt_id}`, res.data);
};

export const deleteProjectTask = (backlog_id, pt_id) => async dispatch => {
  if (
    window.confirm(
      `You are deleting project task ${pt_id}, this action can't be undone`
    )
  ) {
    await axios.delete(`/api/backlog/${backlog_id}/${pt_id}`);
    dispatch({
      type: DELETE_PROJECT_TASK,
      payload: pt_id
    });
  }
};
