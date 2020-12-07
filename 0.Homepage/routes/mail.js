var express = require("express");
var router = express.Router();

const sendmail = require("sendmail")();

/* send of email */
router.post("/", function (req, res) {
  sendmail(
    {
      from: req.body.email,
      to: "kjmhercules@gmail.com",
      subject: req.body.subject,
      html: req.body.message,
    },
    function (err, reply) {
      if (err) {
        console.log(err);
        res.end("NOK");
      } else {
        console.log("Success to Send Mail - (From) " + req.body.email);
        res.end("OK");
      }
    }
  );
});

module.exports = router;
