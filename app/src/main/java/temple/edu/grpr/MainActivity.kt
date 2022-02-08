package temple.edu.grpr

import android.Manifest
import android.content.*
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import java.io.File

class MainActivity : AppCompatActivity() {

    lateinit var name_te         : TextView
    lateinit var username_te     : TextView
    lateinit var password_te     : TextView
    lateinit var confirm_te      : TextView
    lateinit var submit          : Button

    var listOfUsers = arrayListOf<Account>()
    val retrievedAccount : Set<String> = setOf()
    private lateinit var preferences: SharedPreferences
    private val internalFilename = "user_storage"
    private lateinit var user_storage: File

    var isFirstTime = true

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        preferences = getPreferences(MODE_PRIVATE)
        user_storage = File(filesDir, internalFilename)

        initGUI()
        if (!permissionGranted())
            requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION), 123)
        if(savedInstanceState != null){
            isFirstTime = false }
        submit.setOnClickListener {
            if(password_te.text.toString() == confirm_te.text.toString()) {

                val editor = preferences.edit().putStringSet(username_te.text.toString(),
                    setOf(name_te.text.toString(), username_te.text.toString(), password_te.text.toString())).apply()

                val sharedPref = this.getPreferences(Context.MODE_PRIVATE)
                val highScore = sharedPref.getStringSet(username_te.text.toString(), retrievedAccount)
                Log.d("SSSSSSSSSSSSSSSSSS: ", highScore.toString())
                Log.d("XXXXXXXXXXXXXXXXX: ", password_te.text.toString() + " " + confirm_te.text.toString())
                listOfUsers.add(
                    Account(
                        name_te.text.toString(), username_te.text.toString(),
                        password_te.text.toString()
                    )
                )
            }else{
                Toast.makeText(this, "Please make sure you entered the same password twice!", Toast.LENGTH_LONG).show()
            }
        }
    }
    private fun permissionGranted () : Boolean {
        return checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }
    private fun initGUI(){
        name_te = findViewById(R.id.name_te)
        username_te = findViewById(R.id.username_te)
        password_te = findViewById(R.id.password_te)
        confirm_te = findViewById(R.id.confirm_te)
        submit = findViewById(R.id.submit)
    }
}