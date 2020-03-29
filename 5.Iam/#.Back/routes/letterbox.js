var express = require("express");
var router = express.Router();
const db = require(`../config/db_connections`)();
const DB_TABLE_LETTERBOXMESSAGE = `letterbox_message`;
const DB_TABLE_LETTERBOXINFO = `letterbox_info`;

/* -------------- ::START:: API Function Zone -------------- */

function postLetterBoxInfo(param) {
  console.log(`call postLetterBoxInfo`);
  let queryString = `SELECT * FROM ${DB_TABLE_LETTERBOXINFO} WHERE id = ?`;

  return new Promise(function(resolve, reject) {
    db.query(queryString, param, function(err, result) {
      if (err) {
        reject(err);
      }
      resolve(result);
    });
  });
}

function letterboxInsert(param) {
  console.log(`call letterboxInsert`);
  let queryString = `INSERT INTO ${DB_TABLE_LETTERBOXINFO} SET ? `;

  return new Promise(function(resolve, reject) {
    db.query(queryString, param, function(err, result) {
      if (err) {
        reject(err);
      }
      resolve(result);
    });
  });
}

function letterboxUpdate(params) {
  console.log(`call letterboxUpdate`);

  let queryString = `UPDATE ${DB_TABLE_LETTERBOXINFO} SET ? WHERE id = ?`;

  return new Promise(function(resolve, reject) {
    db.query(queryString, params, function(err, result) {
      if (err) {
        reject(err);
      }
      resolve(result);
    });
  });
}

function postLetterBoxMessageInfo(param) {
  console.log(`call postLetterBoxMessageInfo`);
  let queryString = `SELECT * FROM ${DB_TABLE_LETTERBOXMESSAGE} WHERE id = ?`;

  return new Promise(function(resolve, reject) {
    db.query(queryString, param, function(err, result) {
      if (err) {
        reject(err);
      }
      resolve(result);
    });
  });
}

function messageInsert(param) {
  console.log(`call messageInsert`);
  let queryString = `INSERT INTO ${DB_TABLE_LETTERBOXMESSAGE} SET ? `;

  return new Promise(function(resolve, reject) {
    db.query(queryString, param, function(err, result) {
      if (err) {
        reject(err);
      }
      resolve(result);
    });
  });
}

function messageUpdate(params) {
  console.log(`call messageUpdate`);

  let queryString = `UPDATE ${DB_TABLE_LETTERBOXMESSAGE} SET ? WHERE id = ?`;

  return new Promise(function(resolve, reject) {
    db.query(queryString, params, function(err, result) {
      if (err) {
        reject(err);
      }
      resolve(result);
    });
  });
}

/* -------------- ::START:: Router Zone -------------- */

router.post("/letterboxInfo", function(req, res, next) {
  console.log(`API = /letterboxInfo`);

  postLetterBoxInfo(req.body.id)
    .then(result => {
      res.json(result);
    })
    .catch(function(err) {
      console.log(`[postLetterBoxInfo] error : ${err}`);
      res.end(`NOK`);
    });
});

router.post(`/letterboxInsert`, function(req, res, next) {
  console.log(`API = /letterboxInsert`);

  letterboxInsert(req.body)
    .then(result => {
      res.json(result);
    })
    .catch(function(err) {
      console.log(`[letterboxInsert] error : ${err}`);
      res.end(`NOK`);
    });
});

router.post(`/letterboxUpdate`, function(req, res, next) {
  console.log(`API = /letterboxUpdate`);

  // 에러로 보내야됨
  if (req.body.id == null) {
    return res.json({ msg: `id is NULL` });
  }

  let params = [];
  params.push(req.body);
  params.push(req.body.id);

  letterboxUpdate(params)
    .then(result => {
      console.log(result);
      res.json(result);
    })
    .catch(function(err) {
      console.log(`[letterboxUpdate] error : ${err}`);
      res.end(`NOK`);
    });
});

router.post("/message", function(req, res, next) {
  console.log(`API = /message`);

  postLetterBoxMessageInfo(req.body.id)
    .then(result => {
      res.json(result);
    })
    .catch(function(err) {
      console.log(`[postLetterBoxMessageInfo] error : ${err}`);
      res.end(`NOK`);
    });
});

router.post(`/messageInsert`, function(req, res, next) {
  console.log(`API = /messageInsert`);

  messageInsert(req.body)
    .then(result => {
      res.json(result);
    })
    .catch(function(err) {
      console.log(`[messageInsert] error : ${err}`);
      res.end(`NOK`);
    });
});

router.post(`/messageUpdate`, function(req, res, next) {
  console.log(`API = /messageUpdate`);

  // 에러로 보내야됨
  if (req.body.id == null) {
    return res.json({ msg: `id is NULL` });
  }

  let params = [];
  params.push(req.body);
  params.push(req.body.id);

  messageUpdate(params)
    .then(result => {
      console.log(result);
      res.json(result);
    })
    .catch(function(err) {
      console.log(`[messageUpdate] error : ${err}`);
      res.end(`NOK`);
    });
});

module.exports = router;
