module.exports = (function () {
    return {
      local: { // localhost
        host: 'localhost',
        port: '3307',
        user: '',
        password: '',
        database: ''
      },
      real: { // real server db info
        host: 'db-fun-d.cfaesel9mubh.ap-northeast-2.rds.amazonaws.com',
        port: '3306',
        user: 'admin',
        password: 'fund1234',
        database: 'iam'
      }
    }
  })();
