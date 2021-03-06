var express = require("express");
var router = express.Router();
const db = require(`../config/db_connections`)();
const DB_TABLE_CHANNELINFO = `channel_info`;
const DB_TABLE_CHANNELUSER = `channel_user`;
const DB_TABLE_USERINFO = `user_info`;

/* -------------- ::START:: API Function Zone -------------- */

function postChanner(param) {
  console.log(`call postChannerInfo`);
  let queryString = `SELECT * FROM ${DB_TABLE_CHANNELINFO}`;

  return new Promise(function (resolve, reject) {
    db.query(queryString, param, function (err, result) {
      if (err) {
        reject(err);
      }
      resolve(result);
    });
  });
}

function postChannerInfo(param) {
  console.log(`call postChannerInfo`);
  let queryString = `SELECT * FROM ${DB_TABLE_CHANNELINFO} WHERE id = ?`;

  return new Promise(function (resolve, reject) {
    db.query(queryString, param, function (err, result) {
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

  return new Promise(function (resolve, reject) {
    db.query(queryString, param, function (err, result) {
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

  return new Promise(function (resolve, reject) {
    db.query(queryString, params, function (err, result) {
      if (err) {
        reject(err);
      }
      resolve(result);
    });
  });
}

function channerList(user_id) {
  console.log(`call channerList`);

  let queryString = `SELECT DISTINCT INFO.id, INFO.name, INFO.description, INFO.image_url 
                     FROM ${DB_TABLE_CHANNELUSER} JOIN ${DB_TABLE_CHANNELINFO} AS INFO 
                     ON ${DB_TABLE_CHANNELUSER}.channel_id = INFO.id
                     WHERE user_id = ?
                     ORDER BY id ASC`;

  return new Promise(function (resolve, reject) {
    db.query(queryString, user_id, function (err, result) {
      if (err) {
        reject(err);
      }
      resolve(result);
    });
  });
}

function postChannelUserInfo(param) {
  console.log(`call postChannelUserInfo`);

  let queryString = `SELECT A.id, A.channel_id, A.user_id, B.email, B.user_name, B.nick_name,
  B.sns_type, B.token, B.image_url, B.age, B.gender, B.phone, B.job_list, B.location_list, B.portfolio_list, 
  A.create_date, A.modify_date FROM ${DB_TABLE_CHANNELUSER} AS A JOIN ${DB_TABLE_USERINFO} AS B
  ON A.user_id = B.id
  WHERE A.channel_id = ?`;

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
  let queryString = `INSERT INTO ${DB_TABLE_CHANNELUSER} SET ? `;

  return new Promise(function (resolve, reject) {
    db.query(queryString, param, function (err, result) {
      if (err) {
        reject(err);
      }
      resolve(result);
    });
  });
}

function userInsertChannel(param) {
  console.log(`call userInsert`);
  let queryString = `UPDATE ${DB_TABLE_CHANNELINFO} SET now_user = now_user + 1 WHERE id = ?`;

  return new Promise(function (resolve, reject) {
    db.query(queryString, param, function (err, result) {
      if (err) {
        reject(err);
      }
      resolve(result);
    });
  });
}

function userDeleteChannel(param) {
  console.log(`call userDeleteChannel`);

  let params = [];
  params.push(param.channel_id);
  params.push(param.user_id);

  let queryString = `DELETE FROM ${DB_TABLE_CHANNELUSER} WHERE channel_id = ? AND user_id = ?`;

  return new Promise(function (resolve, reject) {
    db.query(queryString, params, function (err, result) {
      if (err) {
        reject(err);
      }
      resolve(result);
    });
  });
}

function DeleteNowUser(param) {
  console.log(`call userDeleteChannel`);

  let queryString = `UPDATE ${DB_TABLE_CHANNELINFO} SET now_user = now_user - 1 WHERE id = ?`;

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

  let queryString = `UPDATE ${DB_TABLE_CHANNELUSER} SET ? WHERE id = ?`;

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

router.post("/channer", function (req, res, next) {
  console.log(`API = /channer`);

  postChanner()
    .then((result) => {
      res.json(result);
    })
    .catch(function (err) {
      console.log(`[postChanner] error : ${err}`);
      res.end(`NOK`);
    });
});

router.post("/channerInfo", function (req, res, next) {
  console.log(`API = /channerInfo`);

  postChannerInfo(req.body.id)
    .then((result) => {
      res.json(result);
    })
    .catch(function (err) {
      console.log(`[postChannerInfo] error : ${err}`);
      res.end(`NOK`);
    });
});

router.post(`/channerInsert`, function (req, res, next) {
  console.log(`API = /channerInsert`);

  channerInsert(req.body)
    .then((result) => {
      return postChannerInfo(result.insertId);
    })
    .then((result) => {
      res.json(result);
    })
    .catch(function (err) {
      console.log(`[channerInsert] error : ${err}`);
      res.end(`NOK`);
    });
});

router.post(`/channerUpdate`, function (req, res, next) {
  console.log(`API = /channerUpdate`);

  // 에러로 보내야됨
  if (req.body.id == null) {
    return res.json({ msg: `id is NULL` });
  }

  let params = [];
  params.push(req.body);
  params.push(req.body.id);

  channerUpdate(params)
    .then((result) => {
      console.log(result);
      res.json(result);
    })
    .catch(function (err) {
      console.log(`[channerUpdate] error : ${err}`);
      res.end(`NOK`);
    });
});

router.post(`/channerList`, function (req, res, next) {
  console.log(`API = /channerList`);

  channerList(req.body.user_id)
    .then((result) => {
      console.log(result);
      res.json(result);
    })
    .catch(function (err) {
      console.log(`[channerUpdate] error : ${err}`);
      res.end(`NOK`);
    });
});

router.post("/user", function (req, res, next) {
  console.log(`API = /user`);

  postChannelUserInfo(req.body.channel_id)
    .then((result) => {
      res.json(result);
    })
    .catch(function (err) {
      console.log(`[postChannelUserInfo] error : ${err}`);
      res.end(`NOK`);
    });
});

router.post(`/userInsert`, function (req, res, next) {
  console.log(`API = /userInsert`);

  userInsert(req.body)
    .then((result) => {
      return userInsertChannel(req.body.channel_id);
    })
    .then((result) => {
      res.json(result);
    })
    .catch(function (err) {
      console.log(`[userInsert] error : ${err}`);
      res.end(`NOK`);
    });
});

router.post(`/userDeleteChannel`, function (req, res, next) {
  console.log(`API = /userDeleteChannel`);

  console.log(req.body);

  userDeleteChannel(req.body)
    .then((result) => {
      return DeleteNowUser(req.body.channel_id);
    })
    .then((result) => {
      res.json(result);
    })
    .catch(function (err) {
      console.log(`[userDeleteChannel] error : ${err}`);
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
