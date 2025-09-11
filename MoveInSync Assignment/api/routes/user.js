const express = require("express");
const router = express.Router();
const mongoose = require("mongoose");
const bcrypt = require("bcrypt");
const jwt = require("jsonwebtoken");
const rateLimit = require("express-rate-limit");

const User = require("../models/user");

// Rate limiter for login route (e.g. max 5 requests per 10 minutes per IP)
const loginLimiter = rateLimit({
  windowMs: 10 * 60 * 1000, // 10 minutes
  max: 5, // Limit each IP to 5 requests per windowMs
  message: {
    message: "Too many login attempts from this IP, please try again later."
  }
});
// Rate limiter for signup route (e.g. max 3 requests per hour per IP)
const signupLimiter = rateLimit({
  windowMs: 60 * 60 * 1000, // 1 hour
  max: 3, // Limit each IP to 3 signups per hour
  message: {
    message: "Too many signup attempts from this IP, please try again later."
  }
});
router.post("/signup", signupLimiter, (req, res, next) => {
  User.find({ username : req.body.username })
    .exec()
    .then((user) => {
      if (user.length >= 1) {
        console.log(user);
        return res.status(409).json({
          message: "User exists",
        });
      } else {
        bcrypt.hash(req.body.password, 10, (err, hash) => {
          // if (err) {
          //   return res.status(500).json({
          //     message: "here",
          //     error: err
          //   });
          // } else {
            const user = new User({
              _id: mongoose.Types.ObjectId(),
              password: hash,
              email: req.body.email,
              phoneNumber: req.body.number,
              username: req.body.username,    
              type: req.body.type        
            });
            user
              .save()
              .then((result) => {
                if (err) {
                  return res.status(500).json({ msg: err.message });
                }
                return res.status(201).json({
                  message: "User created",
                });
              })
              // .catch((err) => {
              //   res.status(500).json({
              //     message: "here2",
              //     error: err
              //   });
              // });
          }
        );
      }
    })
    .catch((err) => {
      res.status(500).json({
        error: err
      });
    });
});


router.post("/login", loginLimiter, (req, res, next) => {
  User.find({ username : req.body.username }) 
    .exec()
    .then((user) => {
      if (user.length < 1) {
        return res.status(401).json({
          message: "Auth failed",
        });
      }
      bcrypt.compare(req.body.password, user[0].password, (err, result) => {
        if (err) {
          return res.status(401).json({
            message: "Auth failed",
          });
        }
        if (result) {
          const token = jwt.sign(
            {
              username: user[0].username, 
              userId: user[0]._id,
              roles: user[0].roles,
            },
            process.env.JWT_KEY,
            {
              expiresIn: "1h",
            }
          );
          res.cookie("token", token, { maxAge: 3600 });
          return res.status(200).json({
            message: "Auth successful"
          });
        }
        return res.status(401).json({
          message: "Invalid Username/Password",
        });
      });
    })
    .catch((err) => {
      console.log(err);
      res.status(500).json({
        error: err,
      });
    });
});

router.get("/logout",(req,res,next) => {
    res.clearCookie("token")
    .then(
      res.status(200).json({
        message: "Logged out"
      })
    )
    .catch((err) => {
      console.log(err);
      res.status(500).json({
        error: err,
      });
    }
    )
});
  

module.exports = router;
