const express = require("express");
const router = express.Router();
const jwt = require("jsonwebtoken");
const rateLimit = require('express-rate-limit');

// Rate limiter: max 100 requests per 15 minutes per IP
const verifyLimiter = rateLimit({
    windowMs: 15 * 60 * 1000, // 15 minutes
    max: 100, // Limit each IP to 100 requests per windowMs
    message: {
        message: "Too many requests, please try again later."
    }
});

router.get("/verify", verifyLimiter, function (req, res, next) {
    const token = req.cookies.token;
    if (!token) {
      return res.status(401).json({
        message: "Token Not Found",
      });
    }
    console.log("here");
    jwt.verify(token, process.env.JWT_KEY, (err, decoded) => {
      if (err) {
        res.clearCookie("token");
        return res.status(401).json({
          message: err,
        });
      }

      return res.status(200).json({
        message: "Auth successful",
      });
    });
  });
  
module.exports = router;