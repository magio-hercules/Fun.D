var express = require("express");
var router = express.Router();
const db = require(`../config/db_connections`)();
const DB_TABLE_LETTERBOXMESSAGE = `letterbox_message`;
const DB_TABLE_LETTERBOXINFO = `letterbox_info`;

function getAllLetterBox() {
  console.log(`call getAllLetterBox`);
  let queryString = `SELECT * FROM ${DB_TABLE_LETTERBOXMESSAGE}`;

  return new Promise(function(resolve, reject) {
    db.query(queryString, [], function(err, result) {
      if (err) {
        reject(err);
      }
      resolve(result);
    });
  });
}

function getLetterBoxMessage() {
  console.log(`call getLetterBoxMessage`);
  let queryString = `SELECT * FROM ${DB_TABLE_LETTERBOXINFO}`;

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

  getAllLetterBox()
    .then(result => {
      res.json(result);
    })
    .catch(function(err) {
      console.log(`[getAllLetterBox] error : ${err}`);
      res.end(`NOK`);
    });
});

router.get("/message", function(req, res, next) {
  console.log(`API = /message`);

  getLetterBoxMessage()
    .then(result => {
      res.json(result);
    })
    .catch(function(err) {
      console.log(`[getLetterBoxMessage] error : ${err}`);
      res.end(`NOK`);
    });
});

module.exports = router;
