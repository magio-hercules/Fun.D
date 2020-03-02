var express = require("express");
var router = express.Router();
const db = require(`../config/db_connections`)();
const DB_TABLE_LISTLOCATION = `list_location`;
const DB_TABLE_LISTJOB = `list_job`;

function getListLocation() {
  console.log(`call getListLocation`);
  let queryString = `SELECT * FROM ${DB_TABLE_LISTLOCATION}`;

  return new Promise(function(resolve, reject) {
    db.query(queryString, [], function(err, result) {
      if (err) {
        reject(err);
      }
      resolve(result);
    });
  });
}

function getListJob() {
  console.log(`call getListJob`);
  let queryString = `SELECT * FROM ${DB_TABLE_LISTJOB}`;

  return new Promise(function(resolve, reject) {
    db.query(queryString, [], function(err, result) {
      if (err) {
        reject(err);
      }
      resolve(result);
    });
  });
}

/* GET users listing. */
router.get("/location", function(req, res, next) {
  console.log(`API = /location`);

  getListLocation()
    .then(result => {
      res.json(result);
    })
    .catch(function(err) {
      console.log(`[getListLocation] error : ${err}`);
      res.end(`NOK`);
    });
});

router.get("/job", function(req, res, next) {
  console.log(`API = /job`);

  getListJob()
    .then(result => {
      res.json(result);
    })
    .catch(function(err) {
      console.log(`[getListJob] error : ${err}`);
      res.end(`NOK`);
    });
});

module.exports = router;
