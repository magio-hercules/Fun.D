var express = require("express");
var router = express.Router();
const db = require(`../config/db_connections`)();
const DB_TABLE_BOOKMARKCHANNEL = `bookmark_channel`;
const DB_TABLE_BOOKMARKUSER = `bookmark_user`;
const DB_TABLE_USERINFO = `user_info`;

/* -------------- ::START:: API Function Zone -------------- */

function postBookMarkChannelInfo(param) {
  console.log(`call postBookMarkChannelInfo`);
  let queryString = `SELECT * FROM ${DB_TABLE_BOOKMARKCHANNEL} WHERE id = ?`;

  return new Promise(function (resolve, reject) {
    db.query(queryString, param, function (err, result) {
      if (err) {
        reject(err);
      }
      resolve(result);
    });
  });
}

function channelInsert(param) {
  console.log(`call channelInsert`);
  let queryString = `INSERT INTO ${DB_TABLE_BOOKMARKCHANNEL} SET ? `;

  return new Promise(function (resolve, reject) {
    db.query(queryString, param, function (err, result) {
      if (err) {
        reject(err);
      }
      resolve(result);
    });
  });
}

function channelUpdate(params) {
  console.log(`call channelUpdate`);

  let queryString = `UPDATE ${DB_TABLE_BOOKMARKCHANNEL} SET ? WHERE id = ?`;

  return new Promise(function (resolve, reject) {
    db.query(queryString, params, function (err, result) {
      if (err) {
        reject(err);
      }
      resolve(result);
    });
  });
}

function postBookMarkUserInfo(param) {
  console.log(`call postBookMarkUserInfo`);

  let queryString = `SELECT B.id, B.email, B.user_name, B.nick_name,
                    B.sns_type, B.token, B.image_url, B.age, B.gender, B.phone, B.job_list, B.location_list, B.portfolio_list, 
                    A.create_date, A.modify_date 
                    FROM ${DB_TABLE_BOOKMARKUSER} AS A JOIN ${DB_TABLE_USERINFO} AS B
                    ON A.friend_id = B.id
                    WHERE A.user_id = ?
                    ORDER BY id ASC`;
  console.log(queryString);

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
  let queryString = `INSERT INTO ${DB_TABLE_BOOKMARKUSER} SET ? `;

  return new Promise(function (resolve, reject) {
    db.query(queryString, param, function (err, result) {
      if (err) {
        reject(err);
      }
      resolve(result);
    });
  });
}

function userDelete(param) {
  console.log(`call userDelete`);

  let params = [];
  params.push(param.user_id);
  params.push(param.friend_id);

  let queryString = `DELETE FROM ${DB_TABLE_BOOKMARKUSER} WHERE user_id = ? AND friend_id = ?`;

  return new Promise(function (resolve, reject) {
    db.query(queryString, params, function (err, result) {
      if (err) {
        reject(err);
      }
      resolve(result);
    });
  });
}

function userUpdate(params) {
  console.log(`call userUpdate`);

  let queryString = `UPDATE ${DB_TABLE_BOOKMARKUSER} SET ? WHERE id = ?`;

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

router.post("/channel", function (req, res, next) {
  console.log(`API = /channel`);

  postBookMarkChannelInfo(req.body.id)
    .then((result) => {
      res.json(result);
    })
    .catch(function (err) {
      console.log(`[postBookMarkChannelInfo] error : ${err}`);
      res.end(`NOK`);
    });
});

router.post(`/channelInsert`, function (req, res, next) {
  console.log(`API = /channelInsert`);

  channelInsert(req.body)
    .then((result) => {
      res.json(result);
    })
    .catch(function (err) {
      console.log(`[channelInsert] error : ${err}`);
      res.end(`NOK`);
    });
});

router.post(`/channelUpdate`, function (req, res, next) {
  console.log(`API = /channelUpdate`);

  // 에러로 보내야됨
  if (req.body.id == null) {
    return res.json({ msg: `id is NULL` });
  }

  let params = [];
  params.push(req.body);
  params.push(req.body.id);

  channelUpdate(params)
    .then((result) => {
      console.log(result);
      res.json(result);
    })
    .catch(function (err) {
      console.log(`[channelUpdate] error : ${err}`);
      res.end(`NOK`);
    });
});

router.post("/user", function (req, res, next) {
  console.log(`API = /user`);

  postBookMarkUserInfo(req.body.user_id)
    .then((result) => {
      res.json(result);
    })
    .catch(function (err) {
      console.log(`[postBookMarkUserInfo] error : ${err}`);
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

router.post(`/userDelete`, function (req, res, next) {
  console.log(`API = /userDelete`);

  userDelete(req.body)
    .then((result) => {
      res.json(result);
    })
    .catch(function (err) {
      console.log(`[userDelete] error : ${err}`);
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

module.exports = router;
