const express = require("express");
const router = express.Router();

var AWS = require("aws-sdk");

//이미지 리사이즈 하는 모듈입니다.(사용)
// const sharp = require("sharp");
// const src = `..//Back/s3/Image_1.jpg`; //여기에 반드시 넣어줘야됩니다 서버에 해당하는 이미지 경로를 넣어줘야됩니다.ex) ./imageTest
// console.log("S3 start");

// /* -------------- ::START:: API Function Zone -------------- */

function deleteImage(fileName) {
  //아마존 S3에 저장하려면 먼저 설정을 업데이트합니다.
  AWS.config.region = "ap-northeast-2"; //Seoul
  AWS.config.update({
    accessKeyId: "AKIAU7ZDEDBRFCBKDSOH",
    secretAccessKey: "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",
  });

  var s3_params = {
    Bucket: "fund7bucket",
    Key: "iam_bucket/" + fileName, // 폴더/파일명
  };

  var s3obj = new AWS.S3({ params: s3_params });

  return new Promise(function (resolve, reject) {
    s3obj.deleteObject().send(function (err, data) {
      if (err) {
        reject(err);
      } else {
        resolve(data);
      }
    });
  });
}

function uploadImage(file, fileName) {
  //아마존 S3에 저장하려면 먼저 설정을 업데이트합니다.
  AWS.config.region = "ap-northeast-2"; //Seoul
  AWS.config.update({
    accessKeyId: "AKIAU7ZDEDBRFCBKDSOH",
    secretAccessKey: "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",
  });

  var s3_params = {
    Bucket: "fund7bucket",
    Key: "iam_bucket/" + fileName, // 폴더/파일명
    ACL: "public-read",
    ContentType: file.mimetype,
    Body: file.data,
  };

  var s3obj = new AWS.S3({ params: s3_params });

  return new Promise(function (resolve, reject) {
    s3obj
      .upload()
      .on("httpUploadProgress", function (evt) {})
      .send(function (err, data) {
        if (err) {
          reject(err);
        } else {
          resolve(data.Location);
        }
      });
  });
}

router.post(`/`, function (req, res, next) {
  console.log(`upload image : `);

  console.log(req.body);
  console.log(req.files);
  console.log(req.files.file);
  console.log(req.body.fileName);
  const file = req.files.file;
  const fileName = req.body.fileName;

  uploadImage(file, fileName)
    .then((result) => {
      res.json(result);
    })
    .catch(function (err) {
      console.log(`[uploadImage] error : ${err}`);
      res.end(`NOK`);
    });
});

router.post(`/delete`, function (req, res, next) {
  console.log(`delete image : `);

  console.log(req.body);
  console.log(req.body.fileName);

  const fileName = req.body.fileName;

  deleteImage(fileName)
    .then((result) => {
      res.json(result);
    })
    .catch(function (err) {
      console.log(`[deleteImage] error : ${err}`);
      res.end(`NOK`);
    });
});

module.exports = router;
