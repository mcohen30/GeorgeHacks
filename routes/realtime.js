var express = require('express');
var router = express.Router();

/* GET users listing. */
router.get('/', function(req, res, next) {
  res.send('this is get realtime');
});

/* POST users listing. */
router.post('/', function(req, res, next) {
    res.send('this is post realtime');
  });

module.exports = router;
