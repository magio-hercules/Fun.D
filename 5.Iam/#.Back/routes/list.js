var express = require("express");
var router = express.Router();
const db = require(`../config/db_connections`)();
const DB_TABLE_LISTLOCATION = `list_location`;
const DB_TABLE_LISTJOB = `list_job`;

/* -------------- ::START:: API Function Zone -------------- */

function postLocationInfo(param) {
  console.log(`call postListLocationInfo`);
  let queryString = `SELECT * FROM ${DB_TABLE_LISTLOCATION} WHERE id = ?`;

  return new Promise(function(resolve, reject) {
    db.query(queryString, param, function(err, result) {
      if (err) {
        reject(err);
      }
      resolve(result);
    });
  });
}

function locationInsert(param) {
  console.log(`call locationInsert`);
  let queryString = `INSERT INTO ${DB_TABLE_LISTLOCATION} SET ? `;

  return new Promise(function(resolve, reject) {
    db.query(queryString, param, function(err, result) {
      if (err) {
        reject(err);
      }
      resolve(result);
    });
  });
}

function locationUpdate(params) {
  console.log(`call locationUpdate`);

  let queryString = `UPDATE ${DB_TABLE_LISTLOCATION} SET ? WHERE id = ?`;

  return new Promise(function(resolve, reject) {
    db.query(queryString, params, function(err, result) {
      if (err) {
        reject(err);
      }
      resolve(result);
    });
  });
}

function postJobInfo(param) {
  console.log(`call postListJobInfo`);
  let queryString = `SELECT * FROM ${DB_TABLE_LISTJOB} WHERE id = ?`;

  return new Promise(function(resolve, reject) {
    db.query(queryString, param, function(err, result) {
      if (err) {
        reject(err);
      }
      resolve(result);
    });
  });
}

function jobInsert(param) {
  console.log(`call jobInsert`);
  let queryString = `INSERT INTO ${DB_TABLE_LISTJOB} SET ? `;

  return new Promise(function(resolve, reject) {
    db.query(queryString, param, function(err, result) {
      if (err) {
        reject(err);
      }
      resolve(result);
    });
  });
}

function jobUpdate(params) {
  console.log(`call jobUpdate`);

  let queryString = `UPDATE ${DB_TABLE_LISTJOB} SET ? WHERE id = ?`;

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

router.post("/locationInfo", function(req, res, next) {
  console.log(`API = /locationInfo`);

  postLocationInfo(req.body.id)
    .then(result => {
      res.json(result);
    })
    .catch(function(err) {
      console.log(`[getListLocation] error : ${err}`);
      res.end(`NOK`);
    });
});

router.post(`/locationInsert`, function(req, res, next) {
  console.log(`API = /locationInsert`);

  locationInsert(req.body)
    .then(result => {
      res.json(result);
    })
    .catch(function(err) {
      console.log(`[locationInsert] error : ${err}`);
      res.end(`NOK`);
    });
});

router.post(`/locationUpdate`, function(req, res, next) {
  console.log(`API = /locationUpdate`);

  // 에러로 보내야됨
  if (req.body.id == null) {
    return res.json({ msg: `id is NULL` });
  }

  let params = [];
  params.push(req.body);
  params.push(req.body.id);

  locationUpdate(params)
    .then(result => {
      console.log(result);
      res.json(result);
    })
    .catch(function(err) {
      console.log(`[locationUpdate] error : ${err}`);
      res.end(`NOK`);
    });
});

router.post("/jobInfo", function(req, res, next) {
  console.log(`API = /jobInfo`);

  postJobInfo(req.body.id)
    .then(result => {
      res.json(result);
    })
    .catch(function(err) {
      console.log(`[getListJob] error : ${err}`);
      res.end(`NOK`);
    });
});

router.post(`/jobInsert`, function(req, res, next) {
  console.log(`API = /jobInsert`);

  jobInsert(req.body)
    .then(result => {
      res.json(result);
    })
    .catch(function(err) {
      console.log(`[jobInsert] error : ${err}`);
      res.end(`NOK`);
    });
});

router.post(`/jobUpdate`, function(req, res, next) {
  console.log(`API = /jobUpdate`);

  // 에러로 보내야됨
  if (req.body.id == null) {
    return res.json({ msg: `id is NULL` });
  }

  let params = [];
  params.push(req.body);
  params.push(req.body.id);

  jobUpdate(params)
    .then(result => {
      console.log(result);
      res.json(result);
    })
    .catch(function(err) {
      console.log(`[jobUpdate] error : ${err}`);
      res.end(`NOK`);
    });
});

module.exports = router;
