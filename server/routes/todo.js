const express = require('express');
const auth = require('../middleware/user_jwt');

const Todo = require('../models/Todo');

const router = express.Router();

// desc Create new todo task
// method POST
router.post('/', auth, async(req, res, next)=>{
    try{
        const toDo = await Todo.create({title: req.body.title, description: req.body.description, user: req.user.id});
        if(!toDo){
            return res.status(400).json({
                success: false, 
                msg: 'Something went wrong'
            });
        }

        res.status(200).json({
            success: true, 
            todo: toDo,
            msg: 'Successfully created.'
        });
    }catch(error){
        next(error);
    }
})

// desc Fech all todos
// method GET
router.get('/', auth, async(req, res, next)=>{
    try{
        const todo = await Todo.find({user: req.user.id, finished: false})

        if(!todo){
            return res.status(400).json({success: false, msg: 'Something error happened'});
        }

        res.status(200).json({success: true, count: todo.length, todos: todo, msg: 'Successfylly fetched'})
    }catch(error){
        next(error);
    }
})
module.exports = router;