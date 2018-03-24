var express = require('express');
var router = express.Router();

var request = require('request');
var url = 'http://admin:password@127.0.0.1:5984/'
var db = 'test2'
var id = 'document_id'
var nano = require('nano')('http://admin:password@127.0.0.1:5984/');
var test_db = nano.db.use(db);

const http = require('http');


/* GET users listing. */
router.get('/', function(req, res, next) {

    http.get('http://127.0.0.1:5984/test2/_all_docs?include_docs=true', (resp) => {
          let data = '';
        
          // A chunk of data has been recieved.
          resp.on('data', (chunk) => {
            data += chunk;
          });
        
          // The whole response has been received. Print out the result.
          resp.on('end', () => {
            console.log(JSON.parse(data).explanation);
            res.send(data);
          });
        
        }).on("error", (err) => {
          console.log("Error: " + err.message);
        });

});

/* POST users listing. */
router.post('/', function(req, res, next) {


    var data = { 
        message: 'pikachu2', 
        user: 'electric2' 
    };
    
    test_db.insert(data, function(err, body){
      if(!err){
        //awesome
      }
    });
    res.send('this is post realtime');

 /*    // Create a database/collection inside CouchDB
    request.put(url + db, function(err, resp, body) {
            // Add a document with an ID
            request.put({
            url: url + db + id,
            body: {message:'New Shiny Docu2ment', user: 'stefan'},
            json: true,
            }, function(err, resp, body) {
            // Read the document
            request(url + db + id, function(err, res, body) {
                console.log(body.user + ' : ' + body.message)
            })
            })
        }
    );



        // Save a document
    exports.save = function(db, doc, done) {
        request.put({
        url: url + '/' + db + '/' + doc._id,
        body: doc,
        json: true,
        }, function(err, resp, body) {
        if (err) return done('Unable to connect CouchDB')
        if (body.ok) {
            doc._rev = body.rev
            return done(null)
        }
    
        done('Unable to save the document')
        })
    }
    */
    
  });

module.exports = router;
