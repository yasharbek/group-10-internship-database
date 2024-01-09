var mongoose = require('mongoose');


// DO NOT CHANGE THE URL FOR THE DATABASE!
// Please speak to the instructor if you need to do so or want to create your own instance
mongoose.connect('mongodb://mongo.cs.swarthmore.edu:27017/group10_jaguar');


var Schema = mongoose.Schema;


var SpotlightProgramSchema = new Schema({
    name: {type: String, required: true, unique: true},
    link: String,
    category: String,
    deadline: String,
    applicableYear: String,
    programDates: String,
    gpaReq: Number,
    alumniEmail: String
});


// export SpotlightedProgramSchema as a class called SpotlightedProgram
module.exports = mongoose.model('SpotlightedProgram', SpotlightProgramSchema);
