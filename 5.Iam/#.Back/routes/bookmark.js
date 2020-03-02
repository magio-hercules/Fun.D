var express = require("express");
var router = express.Router();
const db = require(`../config/db_connections`)();
const DB_TABLE_BOOKMARKCHANNEL = `bookmark_channel`;
const DB_TABLE_BOOKMARKUSER = `bookmark_user`;

function getBookMarkChannel() {
  console.log(`call getBookMarkChannel`);
  let queryString = `SELECT * FROM ${DB_TABLE_BOOKMARKCHANNEL}`;

  return new Promise(function(resolve, reject) {
    db.query(queryString, [], function(err, result) {
      if (err) {
        reject(err);
      }
      resolve(result);
    });
  });
}

function getBookMarkUser() {
  console.log(`call getBookMarkUser`);
  let queryString = `SELECT * FROM ${DB_TABLE_BOOKMARKUSER}`;

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
router.get("/channel", function(req, res, next) {
  console.log(`API = /channel`);

  getBookMarkChannel()
    .then(result => {
      res.json(result);
    })
    .catch(function(err) {
      console.log(`[getBookMarkChannel] error : ${err}`);
      res.end(`NOK`);
    });
});

router.get("/user", function(req, res, next) {
  console.log(`API = /user`);

  getBookMarkUser()
    .then(result => {
      res.json(result);
    })
    .catch(function(err) {
      console.log(`[getBookMarkUser] error : ${err}`);
      res.end(`NOK`);
    });
});

module.exports = router;
