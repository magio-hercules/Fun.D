let mysql = require('mysql');
let config = require('../config/db_info').real;

let pool = mysql.createPool({
  host: config.host,
  port: config.port,
  user: config.user,
  password: config.password,
  database: config.database,
  connectionLimit: 200,
  waitForConnections: false
});

module.exports = function () {
  return {
    query: function (query, params, callback) {
      pool.getConnection(function (err, connection) {
        if (err) {
          console.error('mysql connection error :' + err);
        } else {
          let exec = connection.query(query, params, function (err, results) {
            console.log("excute sql = " + exec.sql);
            if (err) {
              callback(err, null);
            } else {
              callback(null, results);
            }
            connection.release();
          });
        }
      });
    }
  }
};
