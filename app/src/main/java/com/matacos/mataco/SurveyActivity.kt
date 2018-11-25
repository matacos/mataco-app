package com.matacos.mataco

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.util.Log
import android.widget.*
import com.matacos.mataco.apiController.APIController
import com.matacos.mataco.apiController.ServiceVolley
import kotlinx.android.synthetic.main.activity_survey.*
import org.json.JSONObject

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class SurveyActivity : AppCompatActivity() {

    private val TAG: String = SurveyActivity::class.java.simpleName

    var token = ""
    var student = ""
    var course = 0
    var generalOpinionText = 0
    var theoricalClassLevelText = 0
    var practicalClassLevelText = 0
    var courseDificultyText = 0
    var tpDificultyText = 0
    var interestingToppicsText = 0
    var updatedToppicsText = 0
    var comentariesText = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate")

        val preferences: SharedPreferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE)

        val department: String = preferences.getString("subject_department", "")
        val code: String = preferences.getString("subject_code", "")
        val name: String = preferences.getString("subject_name", "")

        supportActionBar?.title = "$department.$code $name"

        setContentView(R.layout.activity_survey)

        send_survey_button.setOnClickListener {
            Log.d(TAG, "clicked send_survey_button")
            attemptSendingSurvey()
        }

        general_opinion_l1.clearCheck()
        theorical_class_level_l1.clearCheck()
        practical_class_level_l1.clearCheck()
        course_dificulty_l1.clearCheck()
        tp_dificulty_l1.clearCheck()
        interesting_toppics_l1.clearCheck()
        updated_toppics_l1.clearCheck()

        general_opinion_l2.clearCheck()
        theorical_class_level_l2.clearCheck()
        practical_class_level_l2.clearCheck()
        course_dificulty_l2.clearCheck()
        tp_dificulty_l2.clearCheck()
        interesting_toppics_l2.clearCheck()
        updated_toppics_l2.clearCheck()

        var general_opinion_isChecking = true
        var theorical_class_level_isChecking = true
        var practical_class_level_isChecking = true
        var course_dificulty_isChecking = true
        var tp_dificulty_isChecking = true
        var interesting_toppics_isChecking = true
        var updated_toppics_isChecking = true

        general_opinion_l1.setOnCheckedChangeListener { group, checkedId ->
            if (checkedId != -1 && general_opinion_isChecking) {
                general_opinion_isChecking = false
                general_opinion_l2.clearCheck()
            }
            general_opinion_isChecking = true
        }

        theorical_class_level_l1.setOnCheckedChangeListener{ group, checkedId ->
            if (checkedId != -1 && theorical_class_level_isChecking) {
                theorical_class_level_isChecking = false
                theorical_class_level_l2.clearCheck()
            }
            theorical_class_level_isChecking = true
        }
        practical_class_level_l1.setOnCheckedChangeListener{ group, checkedId ->
            if (checkedId != -1 && practical_class_level_isChecking) {
                practical_class_level_isChecking = false
                practical_class_level_l2.clearCheck()
            }
            practical_class_level_isChecking = true
        }
        course_dificulty_l1.setOnCheckedChangeListener{ group, checkedId ->
            if (checkedId != -1 && course_dificulty_isChecking) {
                course_dificulty_isChecking = false
                course_dificulty_l2.clearCheck()
            }
            course_dificulty_isChecking = true
        }
        tp_dificulty_l1.setOnCheckedChangeListener{ group, checkedId ->
            if (checkedId != -1 && tp_dificulty_isChecking) {
                tp_dificulty_isChecking = false
                tp_dificulty_l2.clearCheck()
            }
            tp_dificulty_isChecking = true
        }
        interesting_toppics_l1.setOnCheckedChangeListener{ group, checkedId ->
            if (checkedId != -1 && interesting_toppics_isChecking) {
                interesting_toppics_isChecking = false
                interesting_toppics_l2.clearCheck()
            }
            interesting_toppics_isChecking = true
        }
        updated_toppics_l1.setOnCheckedChangeListener{ group, checkedId ->
            if (checkedId != -1 && updated_toppics_isChecking) {
                updated_toppics_isChecking = false
                updated_toppics_l2.clearCheck()
            }
            updated_toppics_isChecking = true
        }

        general_opinion_l2.setOnCheckedChangeListener{ group, checkedId ->
            if (checkedId != -1 && general_opinion_isChecking) {
                general_opinion_isChecking = false
                general_opinion_l1.clearCheck()
            }
            general_opinion_isChecking = true
        }
        theorical_class_level_l2.setOnCheckedChangeListener{ group, checkedId ->
            if (checkedId != -1 && theorical_class_level_isChecking) {
                theorical_class_level_isChecking = false
                theorical_class_level_l1.clearCheck()
            }
            theorical_class_level_isChecking = true
        }
        practical_class_level_l2.setOnCheckedChangeListener{ group, checkedId ->
            if (checkedId != -1 && practical_class_level_isChecking) {
                practical_class_level_isChecking = false
                practical_class_level_l1.clearCheck()
            }
            practical_class_level_isChecking = true
        }
        course_dificulty_l2.setOnCheckedChangeListener{ group, checkedId ->
            if (checkedId != -1 && course_dificulty_isChecking) {
                course_dificulty_isChecking = false
                course_dificulty_l1.clearCheck()
            }
            course_dificulty_isChecking = true
        }
        tp_dificulty_l2.setOnCheckedChangeListener{ group, checkedId ->
            if (checkedId != -1 && tp_dificulty_isChecking) {
                tp_dificulty_isChecking = false
                tp_dificulty_l1.clearCheck()
            }
            tp_dificulty_isChecking = true
        }
        interesting_toppics_l2.setOnCheckedChangeListener{ group, checkedId ->
            if (checkedId != -1 && interesting_toppics_isChecking) {
                interesting_toppics_isChecking = false
                interesting_toppics_l1.clearCheck()
            }
            interesting_toppics_isChecking = true
        }
        updated_toppics_l2.setOnCheckedChangeListener{ group, checkedId ->
            if (checkedId != -1 && updated_toppics_isChecking) {
                updated_toppics_isChecking = false
                updated_toppics_l1.clearCheck()
            }
            updated_toppics_isChecking = true
        }




    }


/*    private val general_opinion_listener_l1 = object : RadioGroup.OnCheckedChangeListener() {
        override fun onCheckedChanged(group: RadioGroup, checkedId: Int) {
            if (checkedId != -1) {
                general_opinion_l2.setOnCheckedChangeListener(null)
                general_opinion_l2.clearCheck()
                general_opinion_l2.setOnCheckedChangeListener(general_opinion_listener_l2)
                Log.e("XXX2", "do the work")
            }
        }
        
    }

    private val general_opinion_listener_l2 = object : RadioGroup.OnCheckedChangeListener() {
        override fun onCheckedChanged(group: RadioGroup, checkedId: Int) {
            if (checkedId != -1) {
                general_opinion_l1.setOnCheckedChangeListener(null)
                general_opinion_l1.clearCheck()
                general_opinion_l1.setOnCheckedChangeListener(general_opinion_listener_l1)
                Log.e("XXX2", "do the work")
            }
        }

    }

    private val  theorical_class_level_listener_l1 = object : RadioGroup.OnCheckedChangeListener() {
        override fun onCheckedChanged(group: RadioGroup, checkedId: Int) {
            if (checkedId != -1) {
                general_opinion_l2.setOnCheckedChangeListener(null)
                general_opinion_l2.clearCheck()
                general_opinion_l2.setOnCheckedChangeListener( theorical_class_level_listener_l2)
                Log.e("XXX2", "do the work")
            }
        }

    }

    private val  theorical_class_level_listener_l2 = object : RadioGroup.OnCheckedChangeListener() {
        override fun onCheckedChanged(group: RadioGroup, checkedId: Int) {
            if (checkedId != -1) {
                general_opinion_l1.setOnCheckedChangeListener(null)
                general_opinion_l1.clearCheck()
                general_opinion_l1.setOnCheckedChangeListener( theorical_class_level_listener_l1)
                Log.e("XXX2", "do the work")
            }
        }

    }

    private val  practical_class_level_listener_l1 = object : RadioGroup.OnCheckedChangeListener() {
        override fun onCheckedChanged(group: RadioGroup, checkedId: Int) {
            if (checkedId != -1) {
                general_opinion_l2.setOnCheckedChangeListener(null)
                general_opinion_l2.clearCheck()
                general_opinion_l2.setOnCheckedChangeListener( practical_class_level_listener_l2)
                Log.e("XXX2", "do the work")
            }
        }

    }

    private val  practical_class_level_listener_l2 = object : RadioGroup.OnCheckedChangeListener() {
        override fun onCheckedChanged(group: RadioGroup, checkedId: Int) {
            if (checkedId != -1) {
                general_opinion_l1.setOnCheckedChangeListener(null)
                general_opinion_l1.clearCheck()
                general_opinion_l1.setOnCheckedChangeListener( practical_class_level_listener_l1)
                Log.e("XXX2", "do the work")
            }
        }

    }

    private val  course_dificulty_listener_l1 = object : RadioGroup.OnCheckedChangeListener() {
        override fun onCheckedChanged(group: RadioGroup, checkedId: Int) {
            if (checkedId != -1) {
                general_opinion_l2.setOnCheckedChangeListener(null)
                general_opinion_l2.clearCheck()
                general_opinion_l2.setOnCheckedChangeListener( course_dificulty_listener_l2)
                Log.e("XXX2", "do the work")
            }
        }

    }

    private val  course_dificulty_listener_l2 = object : RadioGroup.OnCheckedChangeListener() {
        override fun onCheckedChanged(group: RadioGroup, checkedId: Int) {
            if (checkedId != -1) {
                general_opinion_l1.setOnCheckedChangeListener(null)
                general_opinion_l1.clearCheck()
                general_opinion_l1.setOnCheckedChangeListener( course_dificulty_listener_l1)
                Log.e("XXX2", "do the work")
            }
        }

    }

    private val  tp_dificulty_listener_l1 = object : RadioGroup.OnCheckedChangeListener() {
        override fun onCheckedChanged(group: RadioGroup, checkedId: Int) {
            if (checkedId != -1) {
                general_opinion_l2.setOnCheckedChangeListener(null)
                general_opinion_l2.clearCheck()
                general_opinion_l2.setOnCheckedChangeListener( tp_dificulty_listener_l2)
                Log.e("XXX2", "do the work")
            }
        }

    }

    private val  tp_dificulty_listener_l2 = object : RadioGroup.OnCheckedChangeListener() {
        override fun onCheckedChanged(group: RadioGroup, checkedId: Int) {
            if (checkedId != -1) {
                general_opinion_l1.setOnCheckedChangeListener(null)
                general_opinion_l1.clearCheck()
                general_opinion_l1.setOnCheckedChangeListener( tp_dificulty_listener_l1)
                Log.e("XXX2", "do the work")
            }
        }

    }

    private val  interesting_toppics_listener_l1 = object : RadioGroup.OnCheckedChangeListener() {
        override fun onCheckedChanged(group: RadioGroup, checkedId: Int) {
            if (checkedId != -1) {
                general_opinion_l2.setOnCheckedChangeListener(null)
                general_opinion_l2.clearCheck()
                general_opinion_l2.setOnCheckedChangeListener(interesting_toppics_listener_l2)
                Log.e("XXX2", "do the work")
            }
        }

    }

    private val  interesting_toppics_listener_l2 = object : RadioGroup.OnCheckedChangeListener() {
        override fun onCheckedChanged(group: RadioGroup, checkedId: Int) {
            if (checkedId != -1) {
                general_opinion_l1.setOnCheckedChangeListener(null)
                general_opinion_l1.clearCheck()
                general_opinion_l1.setOnCheckedChangeListener( interesting_toppics_listener_l1)
                Log.e("XXX2", "do the work")
            }
        }

    }

    private val  updated_toppics_listener_l1 = object : RadioGroup.OnCheckedChangeListener() {
        override fun onCheckedChanged(group: RadioGroup, checkedId: Int) {
            if (checkedId != -1) {
                general_opinion_l2.setOnCheckedChangeListener(null)
                general_opinion_l2.clearCheck()
                general_opinion_l2.setOnCheckedChangeListener(updated_toppics_listener_l2)
                Log.e("XXX2", "do the work")
            }
        }

    }

    private val  updated_toppics_listener_l2 = object : RadioGroup.OnCheckedChangeListener() {
        override fun onCheckedChanged(group: RadioGroup, checkedId: Int) {
            if (checkedId != -1) {
                general_opinion_l1.setOnCheckedChangeListener(null)
                general_opinion_l1.clearCheck()
                general_opinion_l1.setOnCheckedChangeListener( updated_toppics_listener_l1)
                Log.e("XXX2", "do the work")
            }
        }

    }*/

    private fun attemptSendingSurvey() {
        Log.d(TAG, "attemptLogin")


        general_opinion_title.error = null
        theorical_class_level_title.error = null
        practical_class_level_title.error = null
        course_dificulty_title.error = null
        tp_dificulty_title.error = null
        interesting_toppics_title.error = null
        updated_toppics_title.error = null

        var cancel = false
        var focusView: View? = null

        if (general_opinion_l1.getCheckedRadioButtonId() == -1 && general_opinion_l2.getCheckedRadioButtonId() == -1) {
            general_opinion_title.error = getString(R.string.error_radio)
            focusView = general_opinion_title
            cancel = true
        }
        if (theorical_class_level_l1.getCheckedRadioButtonId() == -1 && theorical_class_level_l2.getCheckedRadioButtonId() == -1) {
            theorical_class_level_title.error = getString(R.string.error_radio)
            if (focusView == null)
                focusView = theorical_class_level_title
            cancel = true
        }
        if (practical_class_level_l1.getCheckedRadioButtonId() == -1 && practical_class_level_l2.getCheckedRadioButtonId() == -1) {
            practical_class_level_title.error = getString(R.string.error_radio)
            if (focusView == null)
                focusView = practical_class_level_title
            cancel = true
        }
        if (course_dificulty_l1.getCheckedRadioButtonId() == -1 && course_dificulty_l2.getCheckedRadioButtonId() == -1) {
            course_dificulty_title.error = getString(R.string.error_radio)
            if (focusView == null)
                focusView = course_dificulty_title
            cancel = true
        }
        if (tp_dificulty_l1.getCheckedRadioButtonId() == -1 && tp_dificulty_l2.getCheckedRadioButtonId() == -1) {
            tp_dificulty_title.error = getString(R.string.error_radio)
            if (focusView == null)
                focusView = tp_dificulty_title
            cancel = true
        }
        if (interesting_toppics_l1.getCheckedRadioButtonId() == -1 && interesting_toppics_l2.getCheckedRadioButtonId() == -1) {
            interesting_toppics_title.error = getString(R.string.error_radio)
            if (focusView == null)
                focusView = interesting_toppics_title
            cancel = true
        }
        if (updated_toppics_l1.getCheckedRadioButtonId() == -1 && updated_toppics_l2.getCheckedRadioButtonId() == -1) {
            updated_toppics_title.error = getString(R.string.error_radio)
            if (focusView == null)
                focusView = updated_toppics_title
            cancel = true
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView?.requestFocus()
        } else {

            getValuesToSend()

            val service = ServiceVolley()
            val apiController = APIController(service)

            val path = "api/poll"
            val params = JSONObject()
            params.put("course", course)
            params.put("student", student)
            params.put("q1", generalOpinionText)
            params.put("q2", theoricalClassLevelText)
            params.put("q3", practicalClassLevelText)
            params.put("q4", courseDificultyText)
            params.put("q5", tpDificultyText)
            params.put("q6", interestingToppicsText)
            params.put("q7", updatedToppicsText)
            params.put("q7", updatedToppicsText)
            params.put("passed", true)
            params.put("feedback", comentariesText)

            Log.d(TAG, "params: $params")

            apiController.post(path, token, params) { response ->
                Log.d(TAG, "response: ${response.toString()}")
                if (response != null) {
                    Log.d(TAG, "startActivity: SurveySubjectsActivity")
                    Toast.makeText(this, "Se envió la encuesta correctamente", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, SurveySubjectsActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    this.startActivity(intent)

                } else {
                    Toast.makeText(this, "Error al enviar los datos", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun getValuesToSend() {
        Log.d(TAG, "getValuesToSend")
        val preferences: SharedPreferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
        token = preferences.getString("token", "")
        Log.d(TAG, "token: $token")
        student = preferences.getString("username", "")
        Log.d(TAG, "student: $student")
        course = preferences.getInt("subject_course", 0)
        Log.d(TAG, "course: $course")

        var selectedId_l1 = general_opinion_l1.getCheckedRadioButtonId()
        var selectedId_l2 = general_opinion_l2.getCheckedRadioButtonId()
        val generalOpinionSelection: RadioButton = findViewById(if (selectedId_l1 == -1) selectedId_l2 else selectedId_l1)!!
        generalOpinionText = Integer.parseInt(generalOpinionSelection.getText().toString())
        Log.d(TAG, "generalOpinionText: $generalOpinionText")

        selectedId_l1 = theorical_class_level_l1.getCheckedRadioButtonId()
        selectedId_l2 = theorical_class_level_l2.getCheckedRadioButtonId()
        val theoricalClassLevelSelection: RadioButton = findViewById(if (selectedId_l1 == -1) selectedId_l2 else selectedId_l1)!!
        theoricalClassLevelText = Integer.parseInt(theoricalClassLevelSelection.getText().toString())
        Log.d(TAG, "theoricalClassLevelText: $theoricalClassLevelText")

        selectedId_l1 = practical_class_level_l1.getCheckedRadioButtonId()
        selectedId_l2 = practical_class_level_l2.getCheckedRadioButtonId()
        val practicalClassLevelSelection: RadioButton = findViewById(if (selectedId_l1 == -1) selectedId_l2 else selectedId_l1)!!
        practicalClassLevelText = Integer.parseInt(practicalClassLevelSelection.getText().toString())
        Log.d(TAG, "practicalClassLevelText: $practicalClassLevelText")

        selectedId_l1 = course_dificulty_l1.getCheckedRadioButtonId()
        selectedId_l2 = course_dificulty_l2.getCheckedRadioButtonId()
        val courseDificultySelection: RadioButton = findViewById(if (selectedId_l1 == -1) selectedId_l2 else selectedId_l1)!!
        courseDificultyText = Integer.parseInt(courseDificultySelection.getText().toString())
        Log.d(TAG, "courseDificultyText: $courseDificultyText")

        selectedId_l1 = tp_dificulty_l1.getCheckedRadioButtonId()
        selectedId_l2 = tp_dificulty_l2.getCheckedRadioButtonId()
        val tpDificultySelection: RadioButton = findViewById(if (selectedId_l1 == -1) selectedId_l2 else selectedId_l1)!!
        tpDificultyText = Integer.parseInt(tpDificultySelection.getText().toString())
        Log.d(TAG, "tpDificultyText: $tpDificultyText")

        selectedId_l1 = interesting_toppics_l1.getCheckedRadioButtonId()
        selectedId_l2 = interesting_toppics_l2.getCheckedRadioButtonId()
        val interestingToppicsSelection: RadioButton = findViewById(if (selectedId_l1 == -1) selectedId_l2 else selectedId_l1)!!
        interestingToppicsText = Integer.parseInt(interestingToppicsSelection.getText().toString())
        Log.d(TAG, "interestingToppicsText: $interestingToppicsText")

        selectedId_l1 = updated_toppics_l1.getCheckedRadioButtonId()
        selectedId_l2 = updated_toppics_l2.getCheckedRadioButtonId()
        val updatedToppicsSelection: RadioButton = findViewById(if (selectedId_l1 == -1) selectedId_l2 else selectedId_l1)!!
        updatedToppicsText = Integer.parseInt(updatedToppicsSelection.getText().toString())
        Log.d(TAG, "updatedToppicsText: $updatedToppicsText")

        val comentaries: EditText = findViewById(R.id.comentaries)
        comentariesText = comentaries.text.toString()
        Log.d(TAG, "comentariesText: $comentariesText")
    }

}