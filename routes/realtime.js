var express = require('express');
var router = express.Router();

const nodemailer = require('nodemailer');

var request = require('request');
//var url = 'http://admin:password@127.0.0.1:5984/'
var db = 'realtime'
//var id = 'document_id'
//var nano = require('nano')('http://admin:password@127.0.0.1:5984/');
//var test_db = nano.db.use(db);

const http = require('http');


/* GET users listing. */
router.get('/', function(req, res, next) {

    var transporter = nodemailer.createTransport({
        service: 'gmail',
        auth: {
          user: 'fallxalert@gmail.com',
          pass: 'aliali12345'
        }
      });
      
      //var bod= req.body;
      var bod= "testeststestest";

      var data = {
        "id": "000001",
        "type": "REAL_TIME",
        "date": "2018-03-24 EDT",
        "time": "11:42:03",
        "gps": {
                "longitude": "-102.89477928",
                "latitude": "34.34844343"
            },
        "acceleration": {
                "x": "3.12",
                "y": "0.1",
                "z": "10"
            },
        "heart_rate": "78"
        } 
        ;

        var textMessage = "Dear Family Member, \n" +
                        'Qi has just fallen. His location is at Longitude -102.89 and Latitude 34.34 \n Click here for Google Maps Location.'+
                        'His Heart Rate is 78 bpm \n' +
                        'Click here to call him or here to call 911';
        

       var mailOptions = {
        from: 'fallxalert@gmail.com',
        to: 'bq13nju@outlook.com',
        subject: 'Alert: Your Family Member has fallen',
        text: textMessage
      };
      
      transporter.sendMail(mailOptions, function(error, info){
        if (error) {
          console.log(error);
        } else {
          console.log('Email sent: ' + info.response);
        }

        res.send(textMessage);
      }); 
/* 
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
        }); */

});

/* POST users listing. */
router.post('/', function(req, res, next) {


    var data = {
        "id": "000001",
        "type": "REAL_TIME",
        "date": "2018-03-24 EDT",
        "time": "11:42:03",
        "gps": {
                "longitude": "-102.89477928",
                "latitude": "34.34844343"
            },
        "acceleration": {
                "x": "3.12",
                "y": "0.1",
                "z": "10"
            },
        "heart_rate": "78"
        } 
        ;
    //data = req.body;
    
    var transporter = nodemailer.createTransport({
        service: 'gmail',
        auth: {
          user: 'fallxalert@gmail.com',
          pass: 'aliali12345'
        }
      });
      
      //var bod= req.body;
      var bod= JSON.stringify(req.body);

      
      console.log(req.gps.longitude); 


      //send email

      var textMessage = 'Dear Family Member, \n' +
      'Qi has just fallen. His location is at Longitude -102.89 and Latitude 34.34 \n Click here for Google Maps Location.'+
      'His Heart Rate is 78 bpm \n' +
      'Click here to call him or here to call 911';


        var mailOptions = {
        from: 'fallxalert@gmail.com',
        to: 'bq13nju@outlook.com',
        subject: 'Alert: Your Family Member has fallen',
        text: textMessage
        };

        transporter.sendMail(mailOptions, function(error, info){
        if (error) {
        console.log(error);
        } else {
        console.log('Email sent: ' + info.response);
        }
      }); 




/*     test_db.insert(data, function(err, body){
      if(!err){
        //awesome
      }
    }); */
    res.send(textMessage);

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
