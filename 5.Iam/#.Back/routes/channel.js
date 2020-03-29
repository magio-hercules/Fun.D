var express = require("express");
var router = express.Router();
const db = require(`../config/db_connections`)();
const DB_TABLE_CHANNELINFO = `channel_info`;
const DB_TABLE_CHANNELUSER = `channel_user`;

/* -------------- ::START:: API Function Zone -------------- */

function postChannerInfo(param) {
  console.log(`call postChannerInfo`);
  let queryString = `SELECT * FROM ${DB_TABLE_CHANNELINFO} WHERE id = ?`;

  return new Promise(function(resolve, reject) {
    db.query(queryString, param, function(err, result) {
      if (err) {
        reject(err);
      }
      resolve(result);
    });
  });
}

function channerInsert(param) {
  console.log(`call channerInsert`);
  let queryString = `INSERT INTO ${DB_TABLE_CHANNELINFO} SET ? `;

  return new Promise(function(resolve, reject) {
    db.query(queryString, param, function(err, result) {
      if (err) {
        reject(err);
      }
      resolve(result);
    });
  });
}

function channerUpdate(params) {
  console.log(`call channerUpdate`);

  let queryString = `UPDATE ${DB_TABLE_CHANNELINFO} SET ? WHERE id = ?`;

  return new Promise(function(resolve, reject) {
    db.query(queryString, params, function(err, result) {
      if (err) {
        reject(err);
      }
      resolve(result);
    });
  });
}

function postChannelUserInfo(param) {
  console.log(`call postChannelUserInfo`);
  let queryString = `SELECT * FROM ${DB_TABLE_CHANNELUSER} WHERE id = ?`;

  return new Promise(function(resolve, reject) {
    db.query(queryString, param, function(err, result) {
      if (err) {
        reject(err);
      }
      resolve(result);
    });
  });
}

function userInsert(param) {
  console.log(`call userInsert`);
  let queryString = `INSERT INTO ${DB_TABLE_CHANNELUSER} SET ? `;

  return new Promise(function(resolve, reject) {
    db.query(queryString, param, function(err, result) {
      if (err) {
        reject(err);
      }
      resolve(result);
    });
  });
}

function userUpdate(params) {
  console.log(`call userUpdate`);

  let queryString = `UPDATE ${DB_TABLE_CHANNELUSER} SET ? WHERE id = ?`;

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

router.post("/channerInfo", function(req, res, next) {
  console.log(`API = /channerInfo`);

  postChannerInfo(req.body.id)
    .then(result => {
      res.json(result);
    })
    .catch(function(err) {
      console.log(`[postChannerInfo] error : ${err}`);
      res.end(`NOK`);
    });
});

router.post(`/channerInsert`, function(req, res, next) {
  console.log(`API = /channerInsert`);

  channerInsert(req.body)
    .then(result => {
      res.json(result);
    })
    .catch(function(err) {
      console.log(`[channerInsert] error : ${err}`);
      res.end(`NOK`);
    });
});

router.post(`/channerUpdate`, function(req, res, next) {
  console.log(`API = /channerUpdate`);

  // 에러로 보내야됨
  if (req.body.id == null) {
    return res.json({ msg: `id is NULL` });
  }

  let params = [];
  params.push(req.body);
  params.push(req.body.id);

  channerUpdate(params)
    .then(result => {
      console.log(result);
      res.json(result);
    })
    .catch(function(err) {
      console.log(`[channerUpdate] error : ${err}`);
      res.end(`NOK`);
    });
});

router.post("/user", function(req, res, next) {
  console.log(`API = /user`);

  postChannelUserInfo(req.body.id)
    .then(result => {
      res.json(result);
    })
    .catch(function(err) {
      console.log(`[postChannelUserInfo] error : ${err}`);
      res.end(`NOK`);
    });
});

router.post(`/userInsert`, function(req, res, next) {
  console.log(`API = /userInsert`);

  userInsert(req.body)
    .then(result => {
      res.json(result);
    })
    .catch(function(err) {
      console.log(`[userInsert] error : ${err}`);
      res.end(`NOK`);
    });
});

router.post(`/userUpdate`, function(req, res, next) {
  console.log(`API = /userUpdate`);

  // 에러로 보내야됨
  if (req.body.id == null) {
    return res.json({ msg: `id is NULL` });
  }

  let params = [];
  params.push(req.body);
  params.push(req.body.id);

  userUpdate(params)
    .then(result => {
      console.log(result);
      res.json(result);
    })
    .catch(function(err) {
      console.log(`[userUpdate] error : ${err}`);
      res.end(`NOK`);
    });
});

module.exports = router;
