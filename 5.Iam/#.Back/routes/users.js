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

function TEST(param) {
  console.log(`call postUserInfo`);
  let queryString = `SELECT * FROM ${DB_TABLE_USERPORTFOLIO}`;

  return new Promise(function (resolve, reject) {
    db.query(queryString, param, function (err, result) {
      if (err) {
        reject(err);
      }
      resolve(result);
    });
  });
}

function postUserInfo(email, sns_type) {
  console.log(`call postUserInfo`);
  let queryString = `SELECT * FROM ${DB_TABLE_USERINFO} WHERE email = ? AND sns_type = ?`;

  let params = [];
  params.push(email);
  params.push(sns_type);

  return new Promise(function (resolve, reject) {
    db.query(queryString, params, function (err, result) {
      if (err) {
        reject(err);
      }
      resolve(result);
    });
  });
}

function userLogin(param) {
  console.log(`call userLogin`);
  let queryString = `INSERT INTO ${DB_TABLE_USERINFO} SET ?, 
  modify_date = now() ON DUPLICATE KEY UPDATE token=?, modify_date = now();`;

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

function portfolioInsert(body) {
  console.log(`call portfolioInsert`);

  let queryString = `INSERT INTO ${DB_TABLE_USERPORTFOLIO} SET ? `;
  console.log(body);
  let insertInfo;
  if (Number(body.type) === 1) {
    insertInfo = {
      user_id: body.user_id,
      type: body.type,
      text: body.text,
    };
  } else if (Number(body.type) === 2) {
    insertInfo = {
      user_id: body.user_id,
      type: body.type,
      image_url: body.text,
    };
  }
  console.log(queryString);
  console.log(insertInfo);

  return new Promise(function (resolve, reject) {
    db.query(queryString, insertInfo, function (err, result) {
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

  postUserInfo(req.body.email, req.body.sns_type)
    .then((result) => {
      res.json(result);
    })
    .catch(function (err) {
      console.log(`[postUserInfo] error : ${err}`);
      res.end(`NOK`);
    });
});

router.post(`/login`, function (req, res, next) {
  console.log(`API = /login`);

  let param = [];
  param.push(req.body);
  param.push(req.body.token);
  userLogin(param)
    .then((result) => {
      // console.result(result);
      return postUserInfo(req.body.email, req.body.sns_type);
    })
    .then((result) => {
      res.json(result);
    })
    .catch(function (err) {
      console.log(`[login] error : ${err}`);
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
      // console.result(result);
      return postUserInfo(req.body.email, req.body.sns_type);
    })
    .then((result) => {
      res.json(result);
    })
    .catch(function (err) {
      console.log(`[userUpdate] error : ${err}`);
      res.end(`NOK`);
    });
});

router.post("/test", function (req, res, next) {
  console.log(`API = /`);

  TEST()
    .then((result) => {
      res.json(result);
    })
    .catch(function (err) {
      console.log(`[postUser] error : ${err}`);
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
