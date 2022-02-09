package temple.edu.grpr

class Account(var name: String, var username: String, var password: String, var session_key : String) {
    var obj = object {
        var _name = name
        var _username = username
        var _password = password
        var _session_key = session_key
    }
}