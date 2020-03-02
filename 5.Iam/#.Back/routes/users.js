var express = require("express");
var router = express.Router();
const db = require(`../config/db_connections`)();
const DB_TABLE_USERINFO = `user_info`;
const DB_TABLE_USERPORTFOLIO = `user_portfolio`;

function getAllUser() {
  console.log(`call getAllUser`);
  let queryString = `SELECT * FROM ${DB_TABLE_USERINFO}`;

  return new Promise(function(resolve, reject) {
    db.query(queryString, [], function(err, result) {
      if (err) {
        reject(err);
      }
      resolve(result);
    });
  });
}

function getAllPortfolio() {
  console.log(`call getAllUserPortfolio`);
  let queryString = `SELECT * FROM ${DB_TABLE_USERPORTFOLIO}`;

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
router.get("/", function(req, res, next) {
  console.log(`API = /`);

  getAllUser()
    .then(result => {
      res.json(result);
    })
    .catch(function(err) {
      console.log(`[getAllUser] error : ${err}`);
      res.end(`NOK`);
    });
});

router.get("/portfolio", function(req, res, next) {
  console.log(`API = /portfolio`);

  getAllPortfolio()
    .then(result => {
      res.json(result);
    })
    .catch(function(err) {
      console.log(`[getAllPortfolio] error : ${err}`);
      res.end(`NOK`);
    });
});

module.exports = router;
