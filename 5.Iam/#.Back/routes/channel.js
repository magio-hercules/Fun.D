var express = require("express");
var router = express.Router();
const db = require(`../config/db_connections`)();
const DB_TABLE_CHANNELINFO = `channer_info`;
const DB_TABLE_CHANNELUSER = `channer_user`;

function getAllChanner() {
  console.log(`call getAllChanner`);
  let queryString = `SELECT * FROM ${DB_TABLE_CHANNELINFO}`;

  return new Promise(function(resolve, reject) {
    db.query(queryString, [], function(err, result) {
      if (err) {
        reject(err);
      }
      resolve(result);
    });
  });
}

function getChannelUser() {
  console.log(`call getChannelUser`);
  let queryString = `SELECT * FROM ${DB_TABLE_CHANNELUSER}`;

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
router.get("/info", function(req, res, next) {
  console.log(`API = /info`);

  getAllChanner()
    .then(result => {
      res.json(result);
    })
    .catch(function(err) {
      console.log(`[getAllChanner] error : ${err}`);
      res.end(`NOK`);
    });
});

router.get("/user", function(req, res, next) {
  console.log(`API = /user`);

  getChannelUser()
    .then(result => {
      res.json(result);
    })
    .catch(function(err) {
      console.log(`[getChannelUser] error : ${err}`);
      res.end(`NOK`);
    });
});

module.exports = router;
