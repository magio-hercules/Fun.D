var express = require("express");
var router = express.Router();
const db = require(`../config/db_connections`)();
const DB_TABLE_USERINFO = `user_info`;
const DB_TABLE_USERPORTFOLIO = `user_portfolio`;

/* -------------- ::START:: API Function Zone -------------- */

function postUser(param) {
  console.log(`call postUserInfo`);
  let queryString = `SELECT * FROM ${DB_TABLE_USERINFO}`;

  return new Promise(function (resolve, reject) {
    db.query(queryString, param, function (err, result) {
      if (err) {
        reject(err);
      }
      resolve(result);
    });
  });
}

function postUserInfo(param) {
  console.log(`call postUserInfo`);
  let queryString = `SELECT * FROM ${DB_TABLE_USERINFO} WHERE id = ?`;

  return new Promise(function (resolve, reject) {
    db.query(queryString, param, function (err, result) {
      if (err) {
        reject(err);
      }
      resolve(result);
    });
  });
}

function userInsert(param) {
  console.log(`call userInsert`);
  let queryString = `INSERT INTO ${DB_TABLE_USERINFO} SET ? `;

  return new Promise(function (resolve, reject) {
    db.query(queryString, param, function (err, result) {
      if (err) {
        reject(err);
      }
      resolve(result);
    });
  });
}

function userUpdate(params) {
  console.log(`call userUpdate`);

  let queryString = `UPDATE ${DB_TABLE_USERINFO} SET ? WHERE id = ?`;

  return new Promise(function (resolve, reject) {
    db.query(queryString, params, function (err, result) {
      if (err) {
        reject(err);
      }
      resolve(result);
    });
  });
}

function postPortfolioInfo(param) {
  console.log(`call postPortfolioInfo`);
  let queryString = `SELECT * FROM ${DB_TABLE_USERPORTFOLIO} WHERE user_id = ?`;

  return new Promise(function (resolve, reject) {
    db.query(queryString, param, function (err, result) {
      if (err) {
        reject(err);
      }
      resolve(result);
    });
  });
}

function portfolioInsert(param) {
  console.log(`call portfolioInsert`);
  let queryString = `INSERT INTO ${DB_TABLE_USERPORTFOLIO} SET ? `;

  return new Promise(function (resolve, reject) {
    db.query(queryString, param, function (err, result) {
      if (err) {
        reject(err);
      }
      resolve(result);
    });
  });
}

function portfolioUpdate(params) {
  console.log(`call portfolioUpdate`);

  let queryString = `UPDATE ${DB_TABLE_USERPORTFOLIO} SET ? WHERE id = ?`;

  return new Promise(function (resolve, reject) {
    db.query(queryString, params, function (err, result) {
      if (err) {
        reject(err);
      }
      resolve(result);
    });
  });
}

function portfolioDelete(params) {
  console.log(`call portfolioDelete`);

  let queryString = `DELETE FROM ${DB_TABLE_USERPORTFOLIO} WHERE id = ?`;

  return new Promise(function (resolve, reject) {
    db.query(queryString, params, function (err, result) {
      if (err) {
        reject(err);
      }
      resolve(result);
    });
  });
}

/* -------------- ::START:: Router Zone -------------- */

router.post("/", function (req, res, next) {
  console.log(`API = /`);

  postUser()
    .then((result) => {
      res.json(result);
    })
    .catch(function (err) {
      console.log(`[postUser] error : ${err}`);
      res.end(`NOK`);
    });
});

router.post("/info", function (req, res, next) {
  console.log(`API = /info`);

  postUserInfo(req.body.id)
    .then((result) => {
      res.json(result);
    })
    .catch(function (err) {
      console.log(`[postUserInfo] error : ${err}`);
      res.end(`NOK`);
    });
});

router.post(`/userInsert`, function (req, res, next) {
  console.log(`API = /userInsert`);

  userInsert(req.body)
    .then((result) => {
      res.json(result);
    })
    .catch(function (err) {
      console.log(`[userInsert] error : ${err}`);
      res.end(`NOK`);
    });
});

router.post(`/userUpdate`, function (req, res, next) {
  console.log(`API = /userUpdate`);

  // 에러로 보내야됨
  if (req.body.id == null) {
    return res.json({ msg: `id is NULL` });
  }

  let params = [];
  params.push(req.body);
  params.push(req.body.id);

  userUpdate(params)
    .then((result) => {
      console.log(result);
      res.json(result);
    })
    .catch(function (err) {
      console.log(`[userUpdate] error : ${err}`);
      res.end(`NOK`);
    });
});

router.post("/portfolio", function (req, res, next) {
  console.log(`API = /portfolio`);

  postPortfolioInfo(req.body.id)
    .then((result) => {
      res.json(result);
    })
    .catch(function (err) {
      console.log(`[postPortfolioInfo] error : ${err}`);
      res.end(`NOK`);
    });
});

router.post(`/portfolioInsert`, function (req, res, next) {
  console.log(`API = /portfolioInsert`);

  portfolioInsert(req.body)
    .then((result) => {
      res.json(result);
    })
    .catch(function (err) {
      console.log(`[portfolioInsert] error : ${err}`);
      res.end(`NOK`);
    });
});

router.post(`/portfolioUpdate`, function (req, res, next) {
  console.log(`API = /portfolioUpdate`);

  // 에러로 보내야됨
  if (req.body.id == null) {
    return res.json({ msg: `id is NULL` });
  }

  let params = [];
  params.push(req.body);
  params.push(req.body.id);

  portfolioUpdate(params)
    .then((result) => {
      console.log(result);
      res.json(result);
    })
    .catch(function (err) {
      console.log(`[portfolioUpdate] error : ${err}`);
      res.end(`NOK`);
    });
});

router.post(`/portfolioDelete`, function (req, res, next) {
  console.log(`API = /portfolioDelete`);

  portfolioDelete(req.body.id)
    .then((result) => {
      console.log(result);
      res.json(result);
    })
    .catch(function (err) {
      console.log(`[portfolioUpdate] error : ${err}`);
      res.end(`NOK`);
    });
});

module.exports = router;
