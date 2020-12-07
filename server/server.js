/*
# 3a. create server using ExpressJS

const express = require('express');
const colors = require('colors');
const morgan = require('morgan');

const app = express();

app.use((req, res, next)=>{
    console.log("middleware fan");
    req.title="web";
    next();
});

app.get('/todo', (req, res)=>{
    res.status(200).json({
        "name":"judy",
        "title" : req.title
    });
});

app.listen(3000, console.log("Server running on port: 3000".red.underline.bold))
*/

/*
require('./models/db');

const express = require('express')
const colors = require('colors')
const morgan = require('morgan')

const app = express();

app.use(morgan('dev'));

// https://localhost:3000/api/todo/auth/register

app.use('/api/todo/auth',require('./routes/user'));

app.listen(3000, ()=>{
    console.log('Express server started at port : 3000');
});
*/

const express = require('express')
const dotenv = require('dotenv')
const connectDB = require('./config/db')
const colors = require('colors')
const morgan = require('morgan')

const app = express();

app.use(morgan('dev'));

app.use(express.json({}));
app.use(express.json({
    express: true
}))


// use dotenv file for DB connection
dotenv.config({
    path:'./config/config.env'
});

connectDB();

app.use('/api/todo/auth',require('./routes/user'));
app.use('/api/todo',require('./routes/todo'));

const PORT = process.env.PORT || 3000;
app.listen(PORT,
    console.log(`Server running mode on port ${PORT}`.red.underline.bold)
);