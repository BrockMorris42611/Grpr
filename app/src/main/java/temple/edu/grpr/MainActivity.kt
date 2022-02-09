package temple.edu.grpr

import android.Manifest
import android.content.*
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject
import java.io.File

class MainActivity : AppCompatActivity(), CreateAccFrag.CreateAccInterface, ChooseFrag.ChooseInterface, SignInFrag.SignInInterface {

    private var account = Account("", "", "", "")
    private lateinit var preferences: SharedPreferences

    private var isFirstTime = true

    private val createaccfragID = "CREATEACCFRAG"
    private val signinfragID = "SIGNINFRAG"
    private val retreiveIsFirstTime = "isFirstTime"

    private lateinit var createAccFrag : CreateAccFrag
    private lateinit var signInFrag: SignInFrag

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        preferences = getPreferences(MODE_PRIVATE)
        account = Account("", "", "", "")
        createAccFrag = CreateAccFrag.newInstance()
        signInFrag = SignInFrag.newInstance()

        if (!permissionGranted())
            requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION), 123)

        if(!preferences.contains(retreiveIsFirstTime)){
            isFirstTime = false
            preferences.edit().putBoolean(retreiveIsFirstTime, isFirstTime).apply()
            supportFragmentManager.beginTransaction().setReorderingAllowed(true).
                add(R.id.containter, createAccFrag).addToBackStack(createaccfragID).commit()
        }else{
            supportFragmentManager.beginTransaction().setReorderingAllowed(true).
                add(R.id.containter, signInFrag).addToBackStack(signinfragID).commit()
        }
    }
    private fun permissionGranted () : Boolean {
        return checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }
    private fun registerAPI(){
        val queue = Volley.newRequestQueue(this)
        val url = "https://kamorris.com/lab/grpr/account.php"
        var key : String
        val jsonObjectRequest =  JsonObjectRequest(Request.Method.POST, url, null, { response ->
            //key = response["session_key"] as String
            Log.d(":::::::::::::::::::::::::::::::::", account.username)
            Log.d("{}{}}{}{}{}{}{}{}{}{}{}{}: ", response.toString())
        }, Response.ErrorListener { error ->
            Log.i("response error:", "$error")
            error.printStackTrace()
        })
        @Throws(AuthFailureError::class)
        fun getParams(): Map<String, String> {
            val params: MutableMap<String, String> = HashMap()
            params["action"]    = "REGISTER";   params["username"]  = account.username; params["firstname"] = account.name;
            params["lastname"]  = account.name; params["password"]  = account.password
            return params }
        queue.add(jsonObjectRequest)
    }
    override fun saveAccount(s: Set<String>) {
        val name = s.elementAt(0); val username = s.elementAt(1); val password = s.elementAt(2)
        Log.d("GL JKDSFHGSDFKJSDFLG: ", s.elementAt(0) + s.elementAt(1) + s.elementAt(2))
        if(preferences.contains(username)) {
            Log.d("THIS ALREADY EXISTS ", "XXXXXXXXXXXXXXXXXXXXX")
        }else {
            val hi = s
            Log.d(" HERE IT IS SHSDFLGKJ;HDFSGBKL: ", hi.toString())
            preferences.edit().putStringSet(username, setOf(name, username, password)).apply()
            Log.d(" HERE IT IS SHSDFLGKJ;HDFSGBKL: ", preferences.getStringSet(username, null)!!.toString())
            supportFragmentManager.beginTransaction().setReorderingAllowed(true).
            replace(R.id.containter, signInFrag).addToBackStack(signinfragID).commit()
            //registerAPI()
        }
    }
    override fun pick(i: Int) {
        if(i == 0){ // if we click the create account button
            supportFragmentManager.beginTransaction().setReorderingAllowed(true).
            add(R.id.containter, CreateAccFrag.newInstance()).addToBackStack(createaccfragID).commit()
        }else{ // if we click the sign in button
            supportFragmentManager.beginTransaction().setReorderingAllowed(true).
            add(R.id.containter, SignInFrag.newInstance()).addToBackStack(signinfragID).commit()
        }
    }
    override fun signIntoAcc(username: String, password : String) {
        var name = ""; val usrnme = username; val psswrd = password
        if(preferences.contains(username) && preferences.getStringSet(username, null)!!.contains(password)){
            val set = preferences.getStringSet(username, null)!! as Set<String>
            for (i in set)
                if( i != username && i != password)
                    name = i
            account = Account(name, username, password, "")
            Log.d("WE HAVE THAT ACCOUNT: ", account.name)
        }
    }
}