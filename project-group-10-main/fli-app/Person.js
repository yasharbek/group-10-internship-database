var mongoose = require('mongoose');

// DO NOT CHANGE THE URL FOR THE DATABASE!
// Please speak to the instructor if you need to do so or want to create your own instance
mongoose.connect('mongodb://mongo.cs.swarthmore.edu:27017/group10_jaguar');

var Schema = mongoose.Schema;

var personSchema = new Schema({
	name: {type: String, required: true, unique: true},
	age: Number
    });

// export personSchema as a class called Person
module.exports = mongoose.model('Person', personSchema);

// this is so that the names are case-insensitive
personSchema.methods.standardizeName = function() {
    this.name = this.name.toLowerCase();
    return this.name;
}