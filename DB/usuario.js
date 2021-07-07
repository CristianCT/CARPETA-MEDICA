const mongoose = require('mongoose');
const bcrypt = require('bcrypt-nodejs');
const Schema = mongoose.Schema;

//Propiedad para definir como se van a ver los datos
//La estructura de los datos

const UsuarioSchema = new Schema({

    name:String,
    lastname:String,
    idtype: String,
    number_id: String,
    gender: String,
    date: Date,
    celular: String,
    direction: String, 
    email: String,
    password: String,
    historial: [],
    documents: [],
    
});

UsuarioSchema.methods.encryptPassword = (password) => { //Cifrar las contraseñas
    return bcrypt.hashSync(password, bcrypt.genSaltSync(10));
};

UsuarioSchema.methods.comparePassword= function (password) { // Comparar las contraseñas cifradas

    return bcrypt.compareSync(password, this.password);
};

UsuarioSchema.methods.newDocument= function newDocument(req , req1){
    this.historial.push(req);
    this.documents.push(req1);
};

//Usuario es el nombre de la collection
module.exports = mongoose.model('Usuarios',UsuarioSchema);